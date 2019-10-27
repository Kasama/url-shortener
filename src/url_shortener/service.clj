(ns url-shortener.service
  (:require [pandect.algo.sha256 :refer [sha256]]
            [url-shortener.repository :refer [fetch write]]))

(defn hash_url
  "Return the first `length` characters of the sha256 hash of the `url`"
  [url length]
  (let [url_hash (sha256 url)]
    (if (< length (count url_hash))
      (subs url_hash 0 length)
      url_hash
      )
    )
  )

(defn shorten_url
  "Return an `alias` of `length` that can be used to fetch the `url` with `fetch_alias`"
  [url length]
  (let [alias (hash_url url length)]
    (write alias url)
    alias
    )
  )

(defn fetch_alias
  "Return the `url` associated with the `alias` or `nil` if none exists"
  [alias]
  (fetch alias)
  )
