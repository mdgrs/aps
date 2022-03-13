(ns mdgrs.graphs
  (:require [oz.core :as oz]
            [aps :as aaps]))

(oz/start-server!)
(defn play-data [& names]
  (for [n names
        i (range 20)]
    {:time i :item n :quantity (+ (Math/pow (* i (count n)) 0.8) (rand-int (count n)))}))
(def line-plot
  {:data {:values (play-data "monkey" "slipper" "broom")}
   :encoding {:x {:field "time" :type "quantitative"}
              :y {:field "quantity" :type "quantitative"}
              :color {:field "item" :type "nominal"}}
   :mark "line"})

;; Render the plot
(oz/view! line-plot)

(aaps/data)
(def test-plot
  {:data {:values aaps/data}
   :encoding {:x {:field "dateString" :type "temporal"}
              :y {:field "sgv" :type "quantitative"}}
   :mark "line"})
(oz/view! test-plot)