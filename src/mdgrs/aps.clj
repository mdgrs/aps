(ns mdgrs.aps
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [oz.core :as oz]
            [mdgrs.fetcher :as fetch]))

;; sample data
(def today-string "2022-03-10")
(def data-sgv (fetch/get-sgv-since today-string 50))
(def data-treatment (fetch/get-treatments-since today-string 50))
(def profile (fetch/get-current-profile))
(def basals (fetch/get-temp-basal data-treatment))


(prn (first data-sgv))

;; plot stuff
(def test-plot
  {:data {:values data-sgv}
   :encoding {:x {:field "dateString"}
              :y {:field "sgv"}}
   :mark "line"})
(def test-plot2
  {:data {:values basals}
   :encoding {:x {:field "created_at"}
              :y {:field "rate"}}
   :mark "line"})


(oz/start-server!)
(oz/view! test-plot)
(oz/view! test-plot2)