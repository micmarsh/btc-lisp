(ns btc-lisp.primitives
  (:require [clojure.java.io :as io]))

(def raw-edn
  (-> "primitive-items.edn"
      (io/resource)
      (io/reader)
      (java.io.PushbackReader.)
      (clojure.edn/read)))

(def primitive?
  (into #{ }
        (comp (mapcat val)
              (map key))
        raw-edn))

(def ->opcode
  (into { } (mapcat val) raw-edn))
