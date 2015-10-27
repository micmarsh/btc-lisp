(ns btc-lisp.compiler-test
  (:require [clojure.test :refer :all]
            [btc-lisp.compiler :refer [compile*]]
            [btc-lisp.test.helpers
             [script-eval :as s]
             [clojurify :as c]]
            [btc-lisp.test.helpers.gen.code :refer :all]
            [clojure.test.check
             [clojure-test :refer [defspec]]
             [properties :as prop]]))

(defn output= [clj-out bl-out]
  (let [top-stack (first (last bl-out))]
    (== clj-out
        (long (cond (nil? top-stack) 0
                    (neg? top-stack) (- (+ 128 top-stack))
                    :else top-stack)))))

(defn -compare [btc-lisp-code]
  {:clojure (eval (c/clojurify btc-lisp-code))
   :btc-lisp (s/eval-opcodes (compile* (str btc-lisp-code)))})

(defspec test-compiler-valid-output
  {:num-tests 50 :seed 1445808966733}
  (prop/for-all
   [btc-lisp-code gen-primitive-expr]
   (output= (eval (c/clojurify btc-lisp-code))
            (s/eval-opcodes (compile* (str btc-lisp-code))))))
(comment
  (compile* (str '(= (not 7) (= 7 1))))
  (s/eval-opcodes '(OP_1 OP_7 OP_EQUAL))
  (s/eval-opcodes '(OP_7 OP_NOT))
  ;; TODO
  ;; as the above indicates, there might be some issue w/ num typing?
  ;; may have to look into different between num and equal, since the
  ;; actual byte structure of the true and false is clearly subtley
  ;; diff, which ur not accounting for in clj-mock.

  ;; Have to decide if this is even a problem, too
  
  )
