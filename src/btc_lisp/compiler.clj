(ns btc-lisp.compiler
  (:require [instaparse.core :as insta]
            [btc-lisp.compiler.protocols :as p]))

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

(comment
  (require '[btc-lisp.core :refer :all]
           '[btc-lisp.syntax :refer :all])

  (def defaults
    {:valid-syntax? identity
     :valid-types? identity
     :all-primitives? identity
     :lisp->script (comp reverse flatten)})

  )
