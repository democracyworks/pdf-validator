(defproject pdf-validator "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ch.qos.logback/logback-classic "1.2.2"]
                 [org.apache.pdfbox/pdfbox "2.0.12"]
                 [org.apache.pdfbox/preflight "2.0.12"]]
  :main ^:skip-aot pdf-validator.core)
