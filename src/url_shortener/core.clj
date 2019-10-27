(ns url-shortener.core
  (:require [org.httpkit.server :refer [run-server]]
            [url-shortener.router :refer [app-routes]]
  )
  (:gen-class)
  )

(defn get-port []
  (Integer/parseInt (or (System/getenv "PORT") "8080"))
  )

(defn alias_length []
  (Integer/parseInt (or (System/getenv "SHORTENED_LENGTH") "6"))
  )

(def configured_routes (app-routes {:alias_length (alias_length)}))

(defn -main
  [& _]
  (let [port (get-port)]
    (run-server #'configured_routes {:port port})
    (println (str "Server started on port " port))
    )
  )
