(ns btc-lisp.test.helpers.script-eval
  (:import [org.bitcoinj.script
            Script ScriptBuilder ScriptOpCodes]))

(defn opcode-enum [opcode-sym]
  (->> opcode-sym
       (name)
       (drop (count "OP_"))
       (apply str)
       (ScriptOpCodes/getOpCode)))

(defn jscript [num-opcodes]
  (.build (reduce
           (fn [^ScriptBuilder builder opcode]
             (.op builder opcode))
           (ScriptBuilder.)
           num-opcodes)))

(defn jscript-execute [script]
  (let [stack (java.util.LinkedList.)]
    (Script/executeScript nil 0 script stack false)
    stack))

(defn eval-opcodes
  "Given input of the form '(OP_1 OP_2 ...), return a vector
   of representing the final state of the script's stack."
  [opcode-syms]
  (->> opcode-syms
       (map opcode-enum)
       (jscript)
       (jscript-execute)
       (mapv seq)))
