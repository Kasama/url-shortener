(ns url-shortener.core
  (:require [org.httpkit.server :refer [run-server]]
            [compojure.core :refer [defroutes, GET]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            ; [clojure.pprint :as pp]
            [clojure.data.json :as json]
  )
  (:gen-class)
  )

(defn get-port []
  (Integer/parseInt (or (System/getenv "PORT") "8080"))
  )

(defn wrap-response
  [status resp]
  {:status status
   :headers {"Content-Type" "application/json"}
   :body resp
   }
  )

(defn root
  [_]
  (wrap-response 200 "{\"hello\": \"world\"}")
  )

(defroutes app-routes
  (GET "/" [] root)
  (route/not-found "{\"Error\": \"Not Found\"}")
  )

(batata "hello")

(defn -main
  [& _]
  (let [port (get-port)]
    (run-server #'app-routes {:port port})
    (println (str "Server started on port " port))
    )
  )
