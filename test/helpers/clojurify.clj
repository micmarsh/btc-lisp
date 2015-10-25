(ns btc-lisp.test.helpers.clojurify
  (:require [clojure.walk :refer [postwalk]]))

(def primitives->clj
  '{neg -
    abs Math/abs
    num= (comp #(if % 1 0) =)
    = (comp #(if % 1 0) =)
    not #(cond (= 0 %) 1 % 0 :else 1)})

(def clojurify
  (partial postwalk #(get primitives->clj % %)))
