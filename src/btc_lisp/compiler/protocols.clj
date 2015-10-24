(ns btc-lisp.compiler.protocols)

(defprotocol Lisp (as-lisp [this]))

(extend-protocol Lisp
  java.lang.String
  (as-lisp [str] (read-string str))
  clojure.lang.Symbol
  (as-lisp [symbol] symbol)
  clojure.lang.ISeq
  (as-lisp [items]
    (->> items
         (map as-lisp)
         (apply list)))

  java.lang.Long
  (as-lisp [num] num))
