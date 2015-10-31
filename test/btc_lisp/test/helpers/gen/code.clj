(ns btc-lisp.test.helpers.gen.code
  (:refer-clojure :exclude [for])
  (:require [clojure.test.check.generators :as gen]
            [btc-lisp.utils :refer [edn-resource]]
            [com.gfredericks.test.chuck.generators :refer [for]]
            [btc-lisp.compiler.types :as t]))

(def primitives (edn-resource "primitive-items.edn"))

(def gen-primitive-literal
  (gen/elements (sequence (comp (map key)
                                (remove #{0}))
                          ;; for some reason bitcoinj doesn't
                          ;; like OP_0's
                          (:literals primitives))))

(def gen-primitive-symbol
  (gen/elements (map key (:symbols primitives))))

(defn- type-check [code]
  (try
    (->> code
         (t/type-infer t/type-lookup)
         (t/type-check))
    true
    (catch clojure.lang.ExceptionInfo e
      false)))

(defn gen-primitive-args [arg-gen]
  (for [args (gen/tuple arg-gen arg-gen)
        rand-arity (gen/elements [1 2])]
    (take rand-arity args)))

(defn gen-primitive-sexp [arg-gen]
  (for [operator gen-primitive-symbol
        args (gen-primitive-args arg-gen)
        :let [sexp (apply list operator args)]
        :when ^{:max-tries 30} (type-check sexp)]
    sexp))

(def gen-primitive-expr
  (gen/recursive-gen gen-primitive-sexp gen-primitive-literal))
