(ns btc-lisp.utils
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn])
  (:import [java.io PushbackReader]))

(def edn-resource
  (comp edn/read
        #(PushbackReader. %)
        io/reader
        io/resource))


