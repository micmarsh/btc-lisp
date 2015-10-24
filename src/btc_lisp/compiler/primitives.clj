(ns btc-lisp.compiler.primitives
  (:require [btc-lisp.utils :refer [edn-resource]]
            [btc-lisp.compiler.primitives.types :as t]))

(def raw-edn (edn-resource "primitive-items.edn"))

(def primitive?
  (into #{ }
        (comp (mapcat val)
              (map key))
        raw-edn))

(def ->opcode
  (into { } (mapcat val) raw-edn))

(doseq [prim-sym primitive?]
  (when-not (get t/primitive-types prim-sym)
    (throw (ex-info (format "No type declared for primitive item %s" prim-sym)
                    {:primitive prim-sym}))))
