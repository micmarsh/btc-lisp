(ns btc-lisp.core
  (:require [instaparse.core :as insta]))

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

(declare valid-syntax? valid-types? all-primitives? lisp->script)

(defn compile
  [btc-lisp-str]
  (valid-syntax? btc-lisp-str)
  (valid-types? btc-lisp-str)
  (loop [lisp-code (as-lisp btc-lisp-str)]
    (if (all-primitives? lisp-code)
      (lisp->script lisp-code)
      (throw (ex-info "Only primitives allowed!"
                      {:string btc-lisp-str
                       :code lisp-code}))
      #_(recur and stuff))))

(comment
  (require '[instaparse.core :as insta])
  (def r clojure.java.io/resource)
  (def p clojure.pprint/pprint)
  (defn bnf [] (r "btc-lisp.bnf"))
  ((instaparse.core/parser (bnf)) "(+ 1(+ (neg 1)2))")

  (defn unwrap [unwrap-types [type body :as token]]
    (if (contains? unwrap-types type)
      body
      token))

  (def ^:const superfluous-types #{:expression :item :sexpr-item})

  (def unwrap-all
    (partial clojure.walk/prewalk
             (fn [token]
               (if (vector? token)
                 (unwrap superfluous-types token)
                 token))))

 )
