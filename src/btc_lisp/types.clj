(ns btc-lisp.types
  (:require [btc-lisp.protocols :as p]))

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

(comment
    
  (def type-lookup
    (let [raw-edn 
          (->> "primitive-types.edn"
               (clojure.java.io/resource)
               (clojure.java.io/reader)
               (java.io.PushbackReader.)
               (clojure.edn/read)
               (mapcat (fn [{:keys [type values]}]
                         (map #(hash-map % type) values)))
               (apply merge-with
                      #(throw (ex-info "type conflict!")
                              {:type1 %1
                               :type2 %2})))]
      (fn [to-lookup]
        (if-let [result (get raw-edn to-lookup)]
          result
          (throw (ex-info (format "No type found for %s" to-lookup)
                          {:original-item to-lookup}))))))

  (btc-lisp.types/type-check (btc-lisp.types/type-infer type-lookup '(+ 1 2)))
  )
