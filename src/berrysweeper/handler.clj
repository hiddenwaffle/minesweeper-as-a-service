(ns berrysweeper.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]))

(defn reset-handler [request]
  (let [body (berrysweeper.game/reset)]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body body}))

(defn pick-handler [request]
  (let [body (berrysweeper.game/pick (:body request))]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body body}))

(defroutes app-routes
  (GET "/" [] "")
  (GET "/reset" [] reset-handler)
  (POST "/pick" [] pick-handler)
  (route/not-found ""))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true})
      (wrap-json-response)
      (wrap-defaults api-defaults)))
