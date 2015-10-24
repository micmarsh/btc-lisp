(ns btc-lisp.compiler.syntax
  (:require [instaparse.core :as insta]
            [clojure.java.io :refer [resource]]))

(def parser
  (let [grammar (resource "btc-lisp.bnf")
        grammar-parser (insta/parser grammar)]
    (fn [string]
      (let [result (grammar-parser string)]
        (when (instance? instaparse.gll.Failure result)
          (throw (ex-info (pr-str result) {:failure result})))))))
