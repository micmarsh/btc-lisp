(ns btc-lisp.compiler
  (:require [instaparse.core :as insta]
            [btc-lisp.compiler
             [primitives :as pr]
             [protocols :as p]
             [syntax :refer [parser]]
             [types :as t]]))

(defn compile
  [{:keys [valid-syntax? valid-types?
           all-primitives? lisp->script]}
   btc-lisp-str]
  (valid-syntax? btc-lisp-str)
  (valid-types? btc-lisp-str)
  (loop [lisp-code (p/as-lisp btc-lisp-str)]
    (if (all-primitives? lisp-code)
      (lisp->script lisp-code)
      (throw (ex-info "Only primitives allowed!"
                      {:string btc-lisp-str
                       :code lisp-code}))
      #_(recur and stuff))))

(def defaults
  {:valid-syntax? (comp not empty? parser)
   :valid-types? (comp t/type-check
                       (partial t/type-infer t/type-lookup)
                       p/as-lisp)
   :all-primitives? (comp (partial every? pr/primitive?) flatten)
   :lisp->script (comp (partial map pr/->opcode) reverse flatten)})

(def compile* (partial compile defaults))

(comment
  (require '[btc-lisp.core :refer :all]
           '[btc-lisp.syntax :refer :all])

  )
