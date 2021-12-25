(ns mdgrs.aps
  (:require [clj-http.client :as client]
            [clojure.data.json :as json])
  (:gen-class))

(defn greet
  "Callable entry point to the application."
  [data]
  (println (str "Hello, " (or (:name data) "World") "!")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (greet {:name (first args)}))
(+ 1 1)

(def root "https://cgm-mdgrs.herokuapp.com/api/v1/")

(def date "2021-12-20")

(defn get-sgv-since
  ([date] (get-sgv-since date 5))
  ([date count]
   (let [url (str root "entries.json?find[created_at][$gte]=" date "&count=" count)]
     (json/read-json (:body (client/get url))))))
(defn get-sgv-date
  ([date] (get-sgv-date date 5))
  ([date count]
   (let [url (str root "times/" date ".json?count=" count)]
     (json/read-json (:body (client/get url))))))

(defn get-treatments-since
  ([date] (get-treatments-since date 5))
  ([date count]
   (let [url (str root "treatments.json?find[created_at][$gte]=" date "&count=" count)]
     (json/read-json (:body (client/get url))))))
(get-treatments-since "2021-12-20")
(get-sgv-date "2021-12-20")
(def data (get-sgv-since "2021-12-20" 500))
(def data-treatment (get-treatments-since "2021-12-20" 500))
