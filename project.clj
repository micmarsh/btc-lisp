(defproject btc-lisp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/core.match "0.3.0-alpha4"]
                 [org.clojure/clojure "1.7.0"]
                 [instaparse "1.4.1"]]

  :profiles {:dev {:dependencies [[org.clojure/test.check "0.8.2"]
                                  [com.gfredericks/test.chuck "0.2.0"]
                                  [org.bitcoinj/bitcoinj-core "0.14-SNAPSHOT"]]}})
;; to get the right bitcoinj:
;; clone git@github.com:bitcoinj/bitcoinj.git, make sure mvn is v3 (http://stackoverflow.com/q/15630055), mvn install
