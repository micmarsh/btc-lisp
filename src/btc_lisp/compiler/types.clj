(ns btc-lisp.compiler.types
  (:require [btc-lisp.compiler.primitives.types :as t]))

(defmulti type-infer (fn [_ val] (type val)))

(def map' (comp (partial apply list) map))

(defmethod type-infer clojure.lang.PersistentList
  ;; this shit is gonna get wild with symbol binding, but you already
  ;; knew that
  [types lisp-code]
  (let [[f :as annotated] (map' (partial type-infer types) lisp-code)
        f-type (:type f)]
    {:value annotated
     :type (last f-type)}))

(defmethod type-infer java.lang.Long
  [type-lookup num]
  {:value num
   :type (type-lookup num)})

(defmethod type-infer clojure.lang.Symbol
  ;; this shit is also gonna get wild with symbol binding, sinc u
  ;; don't want to type-check something that hasn't been bound yet
  [type-lookup sym]
  {:value sym
   :type (type-lookup sym)})

(defmulti type-check (comp type :value))

(defn fn-arg-match? [{[f & args] :value}]
  (let [types (remove '#{->} (:type f))
        arg-types (drop-last types)
        arity (count arg-types)]
    (when-not (= arity (count args))
      (throw (ex-info "Arity execption"
                      {:expression (map' :value (list* f args))
                       :expected-arg-count (count arg-types)
                       :actual-arg-count (count args)
                       :type ::arity-exception})))
    (when-not (= arg-types (map :type args))
      (throw (ex-info "type error"
                      {:expression (map' :value (list* f args))
                       :expected-arg-types arg-types
                       :actual-arg-types (map :type args)
                       :type ::arity-exception})))
    true))

(defmethod type-check clojure.lang.PersistentList
  [lisp-code]
  (and (fn-arg-match? lisp-code)
       (every? true? (map' type-check (:value lisp-code)))))

(defmethod type-check clojure.lang.Symbol [_] true)
(defmethod type-check java.lang.Long [_] true)

(defn type-lookup [to-lookup]
  (if-let [prim-result (get t/primitive-types to-lookup)]
    prim-result
    (throw (ex-info (format "No type found for %s" to-lookup)
                    {:original-item to-lookup}))))
(comment
  (btc-lisp.types/type-check (btc-lisp.types/type-infer type-lookup '(+ 1 2)))
  )
