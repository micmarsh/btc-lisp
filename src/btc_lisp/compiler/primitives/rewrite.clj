(ns btc-lisp.compiler.primitives.rewrite
  (:require [clojure.core.match :refer [match]]))

(defn rewrite [expr]
  (if (list? expr) ;; this is complected move out eventually   
    (match
     [(vec expr)]
     [['- a b]] (list '- b a)
     :else expr)
    expr))
