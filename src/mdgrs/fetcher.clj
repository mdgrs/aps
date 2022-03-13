(ns mdgrs.fetcher  (:require [clj-http.client :as client]
                             [clojure.data.json :as json]
                             [tick.core :as t]))


(def root "https://cgm-mdgrs.herokuapp.com/api/v1/")
(def treatment-types ["Sensor Change" "Correction Bolus" "Temp Basal" "Note" "Meal Bolus" "Carb Correction" "Bolus Wizard" "Temporary Target" "Site Change" "Insulin Change"])

(defn get-profiles []
  (let [profiles
        (json/read-json (:body (client/get (str root "profile"))))]
    (->> profiles
         (sort-by :startDate)
         (reverse))))

(defn get-current-profile []
  (let [profile (first (get-profiles))]
    (as-> profile $
      (:store $)
      ((keyword (:defaultProfile profile)) $))))

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

(defn get-sgv-last-days [days count]
  (let [mydate (t/<< (t/now) (t/new-period days :days))]
    (get-sgv-since (str (t/date mydate)) count)))


(defn get-treatments-since
  ([date] (get-treatments-since date 5))
  ([date count]
   (let [url (str root "treatments.json?find[created_at][$gte]=" date "&count=" count)]
     (json/read-json (:body (client/get url))))))

(defn in? [seq elm]
  (some #(= elm %) seq))

(defn get-carbs [data-treatment]
  (filter #(in? ["Meal Bolus" "Bolus Wizard" "Bolus Correction"] (:eventType %)) data-treatment))

(defn get-temp-basal [data-treatment]
  (->> data-treatment
       (filter #(= "Temp Basal" (:eventType %)))
       (filter #(contains? % :duration))))