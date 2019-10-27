(ns url-shortener.repository)

(def database (atom {}))

(defn fetch
  "Fetches a value of a key from the database"
  [key]
  (get (deref database) key)
  )

(defn write
  "Associates the value `value` with the key `key` in the database, returns the inserted key"
  [key value]
  (swap! database assoc key value)
  key
  )
