(ns berrysweeper.production
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [berrysweeper.handler :as handler])
  (:gen-class))

(defn -main [& args]
  (run-jetty handler/app {:port (Integer/valueOf (or (System/getenv "PORT") "3000"))}))
