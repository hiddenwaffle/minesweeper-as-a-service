(ns minesweeper-saas.production
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [minesweeper-saas.handler :as handler])
  (:gen-class))

(defn -main [& args]
  (run-jetty handler/app {:port (Integer/valueOf (or (System/getenv "PORT") "3000"))}))
