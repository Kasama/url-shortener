(ns url-shortener.router-test
  (:require [clojure.test :refer :all]
            [url-shortener.router :refer :all]
            [ring.mock.request :as mock]
            [clojure.pprint :as pp]
            [url-shortener.test-fixtures :refer [url_b expected_b_6 default_alias_length]]
            [clojure.data.json :as json]
            )
  )

(defn app [] (app-routes {:alias_length default_alias_length}))

;(use-fixtures :each app_fixture)

(defn read-json
  [string]
  (json/read-str string :key-fn clojure.core/keyword)
  )

(deftest shorten_test

  (testing "Test post create alias"
    (let [payload {:url url_b}
          expected_response {:url url_b, :alias expected_b_6}
          response ((app) (-> (mock/request :post "/")
                              (mock/content-type "application/json")
                              (mock/body (json/write-str payload))
                              )
                    )
          body     (json/read-str (:body response) :key-fn clojure.core/keyword)
          ]
      (is (= (:status response) 201))
      (is (= body expected_response))
      )
    )
  )

(deftest fetch_test

  (testing "Test get non existant alias"
    (let [expected_response {:error "Alias not found"}
          response ((app) (mock/request :get (str "/" expected_b_6)))
          body     (json/read-str (:body response) :key-fn clojure.core/keyword)
          ]
      (is (= (:status response) 404))
      (is (= body expected_response))
      )
    )

  (testing "Test get alias"
    (let [application           (app)
          payload               {:url url_b}
          {creation_body :body} (application (-> (mock/request :post "/")
                                                 (mock/content-type "application/json")
                                                 (mock/body (json/write-str payload))
                                                 )
                                             )
          {alias :alias}        (read-json creation_body)
          expected_response     {:url url_b :alias alias}
          response              (application (mock/request :get (str "/" alias)))
          body                  (read-json (:body response))
          ]
      (is (= (:status response) 200))
      (is (= body expected_response))
      )
    )
  )
