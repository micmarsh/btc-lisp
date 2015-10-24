(ns btc-lisp.compiler.syntax
  (:require [instaparse.core :as insta]
            [clojure.java.io :refer [resource]]))

(def parser
  (let [grammar (resource "btc-lisp.bnf")
        grammar-parser (insta/parser grammar)]
    grammar-parser))
