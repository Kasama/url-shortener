(ns url-shortener.service-test
  (:require [clojure.test :refer :all]
            [url-shortener.repository :refer [write]]
            [url-shortener.service :refer :all]
            [url-shortener.test-fixtures :refer [url_a
                                                 url_b
                                                 url_b_hash
                                                 expected_a_4
                                                 expected_b_6
                                                 default_alias_length
                                                 ]
             ]
            )
  )

(deftest hash_url_test

  (testing "Create hash with length 4"
    (is (= (hash_url url_a 4) expected_a_4))
    )

  (testing "Create hash with default length"
    (is (= (hash_url url_b default_alias_length) expected_b_6))
    )

  (testing "Create hash with too big length"
    (is (= (hash_url url_b 999999999) url_b_hash))
    )

  )

(deftest shorten_url_test

  (testing "Shorten URL calls hash_url and saves the result"
    (with-redefs-fn {#'hash_url (fn
                                  [url len]
                                  (is (= url url_a))
                                  (is (= len default_alias_length))
                                  expected_b_6
                                  )
                     #'write (fn
                               [alias url]
                               (is (= alias expected_b_6))
                               (is (= url url_a))
                               alias
                               )
                     }
      #(let [result (shorten_url url_a default_alias_length)]
        (is (= result expected_b_6))
        )
      )
    )
  )

(deftest fetch_alias_test

  (testing "Fetch just created hash"
    (let [result (shorten_url url_a default_alias_length)]
      (is (= (fetch_alias result) url_a))
      )
    )
  )
