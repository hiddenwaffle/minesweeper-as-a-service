(defproject minesweeper-as-a-service "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.4.0"]
                 [ring-cors "0.1.12"]]
  :plugins [[lein-ring "0.12.4"]]
  :ring {:handler minesweeper-saas.handler/app}
  :main minesweeper-saas.production
  :uberjar-name "minesweeper-saas-standalone.jar"
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.3.2"]]}
             :uberjar {:aot :all
                       :dependencies [[ring/ring-jetty-adapter "1.7.0"]]}})
