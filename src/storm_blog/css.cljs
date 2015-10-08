(ns storm-blog.css
  (:require [garden.core :refer [css]]))

(def page-css
  [:style
   (css [[:.demo-card-square.mdl-card
          :.who-we-are-card
          {:width :300px :height :320px}]
         [:.demo-card-square :.who-we-are-card
          [:.mdl-card__title {:color :#fff
                              :background :#46B6AC}]]
         [:.first-article {:width :310px}]
         [:.article-banner {:background :#46B6AC}]
         [:.demo-content {:border-radius :2px :padding [[:80px :56px]]
                          :margin-bottom :80px}]
         [:.demo-main
          {:flex-shrink 0
           #_ #_ :background [[:#ffffff "url(/img/banner-background.jpg)" :no-repeat :fixed :top]]}]
         [:.article-banner
          {:background-image "url(/img/j-and-k-picture.JPG)"
           :background-size :cover
           :width :100%
           :height :400px}]
         [:body::before
          {:background-image "url(/img/boat.JPG)"
           :background-size :cover
           :background-attachment :fixed
           :content ""
           :will-change :transform
           :z-index -1
           :position :fixed
           :transform "rotate(90deg)"}]])])
