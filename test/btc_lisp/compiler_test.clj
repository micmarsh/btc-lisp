(ns btc-lisp.compiler-test
  (:require [clojure.test :refer :all]
            [btc-lisp.compiler :refer [compile*]]
            [btc-lisp.test.helpers
             [script-eval :as s]
             [clojurify :as c]]
            [btc-lisp.test.helpers.gen.code
             :refer [gen-primitive-expr]]
            [clojure.test.check
             [clojure-test :refer [defspec]]
             [properties :as prop]]))

(defn output= [clj-out bl-out]
  (let [top-stack (first (last bl-out))]
    (== clj-out
        (long (cond (nil? top-stack) 0
                    (neg? top-stack) (- (+ 128 top-stack))
                    :else top-stack)))))

(defspec test-compiler-valid-output 20
  (prop/for-all
   [btc-lisp-code gen-primitive-expr]
   (output= (eval (c/clojurify btc-lisp-code))
            (s/eval-opcodes (compile* (str btc-lisp-code))))))
