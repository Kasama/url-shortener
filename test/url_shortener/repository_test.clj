(ns url-shortener.repository-test
  (:require [clojure.test :refer :all]
            [url-shortener.repository :refer :all]))

(def test_key "Some key")
(def value "Some value")
(def new_value "Some new value")

(defn test_fixture
  [t]
  (swap! database (fn [_] {}))
  (t)
  )

(use-fixtures :each test_fixture)

(deftest write_test

  (testing "Write to the database are actually saved"
    (is (= (get (deref database) test_key) nil))
    (write test_key value)
    (is (= (get (deref database) test_key) value))
    )

  (testing "Write to existing key overrides it"
    (write test_key value)
    (is (= (get (deref database) test_key) value))
    (write test_key new_value)
    (is (= (get (deref database) test_key) new_value))
    )
  )

(deftest read_test

  (testing "Read missing content should give nil"
    (is (= (fetch test_key) nil))
    )

  (testing "Read contents that were just written"
    (write test_key value)
    (is (= (fetch test_key) value))
    )

  )
