(ns url-shortener.router
  (:require
            [compojure.core :refer [defroutes, GET, POST]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [url-shortener.service :refer [shorten_url fetch_alias]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-params]]
            [clojure.data.json :as json]
            )
  (:gen-class)
  )

(def CREATED 201)
(def OK 200)
(def NOT_FOUND 404)

(defn wrap-response
  [status resp]
  {:status status
   :headers {"Content-Type" "application/json"}
   :body (json/write-str resp :escape-slash false)
   }
  )

(defn shorten-handler
  [ shortened_length {params :json-params} ]
  (let [url (get params "url")]
    (wrap-response
      CREATED
      {:url url, :alias (shorten_url url shortened_length)}
      )
    )
  )

(defn fetch-handler
  [ {{alias :alias} :params} ]
  (let [url (fetch_alias alias)]
    (if (nil? url)
      (wrap-response
        NOT_FOUND
        {:error "Alias not found"}
        )
      (wrap-response
        OK
        {:url (fetch_alias alias), :alias alias}
        )
      )
    )
  )

(defn app-routes
  [ {alias_length :alias_length} ]
  (defroutes routes
    (GET "/:alias" [] fetch-handler)
    (POST "/" [] (-> (partial shorten-handler alias_length)
                     wrap-keyword-params
                     wrap-json-params
                     )
          )
    (route/not-found (json/write-str {:error "Not Found"}))
    )
  routes
  )

