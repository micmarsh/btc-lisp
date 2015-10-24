(ns btc-lisp.compiler.primitives.types
  (:require [btc-lisp.utils :refer [edn-resource]]))

(def primitive-types
  (->> "primitive-types.edn"
       (edn-resource)
       (mapcat
        (fn [{:keys [type values]}]
          (map #(hash-map % type) values)))
       (apply merge-with
              #(throw (ex-info "type conflict!")
                      {:type1 %1
                       :type2 %2}))))
