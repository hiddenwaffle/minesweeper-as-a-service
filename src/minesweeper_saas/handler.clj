(ns minesweeper-saas.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.cors :refer [wrap-cors]]
            [minesweeper-saas.game :as game]))

(defn reset-handler [request]
  (let [body (game/reset)]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body body}))

(defn pick-handler [request type]
  (let [body (game/pick (:body request) type)]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body body}))

(defroutes app-routes
  (GET "/" [] "")
  (GET "/reset" [] reset-handler)
  (POST "/clear" [] #(pick-handler % :clear))
  (POST "/flag" [] #(pick-handler % :flag))
  (route/not-found ""))

(def app
  (-> app-routes
      (wrap-cors :access-control-allow-origin [#"https://unremarkableSCM.github.io"
                                               #"http://localhost:8000"]
                 :access-control-allow-methods [:get :post])
      (wrap-json-body {:keywords? true})
      (wrap-json-response)
      (wrap-defaults api-defaults)))
