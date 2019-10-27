(defproject url-shortener "1.0.0"
  :description "A simple in-memory URL Shortener"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.5.0"]
                 [ring/ring-mock "0.4.0"]
                 [org.clojure/data.json "0.2.6"]
                 [http-kit "2.2.0"]
                 [pandect "0.6.1"]
                 ]
  :main ^:skip-aot url-shortener.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  )
