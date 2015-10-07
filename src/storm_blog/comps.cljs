(ns storm-blog.comps
  (:require [rum.core :as rum]
            [datascript.core :as d]
            [garden.core :refer [css]]
            [storm-blog.db :as db]
            [storm-blog.css :as style]))

(defn- send-msg [chan text]
  (async/put! chan [:send-msg text]))

(rum/defc comment-section [db {own :comment/owner ema :comment/email
                               com :comment/content web :comment/website eid :db/id}]
    [:.comment
     [:.owner own] [:.ema ema]
     [:.web web] [:.com com]])

(defn extract-comment [eid]

(rum/defc comment-form [db {article-eid :db/id :or {article-eid 1}}]
  [:form {:on-submit (fn [_] (add-comment) false)}
   [:input.add-owner {:type "text" :placeholder "Name"}]
   [:input.add-email {:type "text" :placeholder "Name"}]
   [:input.add-website {:type "text" :placeholder "Name"}]
   [:input.add-comment {:type "text" :placeholder "Name"}]])

(rum/defc demo-card [db {title :card/title words :card/words buttons :card/buttons eid :db/id :as state}]
    [:.demo-card-square.mdl-card.mdl-shadow--2dp
     [:.mdl-card__title.mdl-card--expand
      [:h2.mdl-card__title-text title]]
     [:.mdl-card__supporting-text words]
     [:.mdl-card__actions.mdl-card--border
      (map #(vector :a.mdl-button.mdl-button--colored.mdl-js-button.mdl-js-ripple-effect %) buttons)]])

(rum/defc article [db {title :article/title a-list :article/list
                       content :article/content category :article/category eid :db/id :as state}]
    [:.mdl-grid
     [:.article-spacer.mdl-cell.mdl-cell--2-col.mdl-cell--hide-table.mdl-cell--hide-phone]
     [:article.demo-content.mdl-color--white.mdl-shadow--4dp.content.mdl-color-text--grey-800.mdl-cell.mdl-cell--8-col
      [:.article-header
       [:.article-banner] [:.article-category category] [:.article-title title]
       [:ul.article-list (map #(vector :li.article-list-item %) a-list)]]
      [:section (map #(vector :p %) content)]
      [:.mdl-layout-spacer]
       [:comment-section (map #(comment-section db (first %)) (db/pvea db eid :article/comment))
        (comment-form db state)]]])

(rum/defc grid [db]
  [:.mdl-grid.demo-main
   #_ [:.article-spacer.mdl-cell.mdl-cell--2-col.mdl-cell--hide-table.mdl-cell--hide-phone]
   (map #(article db (first %)) (db/pea db :article/title))
   (map #(vector :.mdl-cell.mdl-cell--4-col
                 (demo-card db (first %))) (db/pea db :card/title))])

(rum/defc content [db]
  [:main.mdl-layout__content [:.page-content (grid db)]])

(rum/defc nav [db {links :nav/links}]
  [:nav.mdl-navigation (map #(vector :a.mdl-navigation__link {:href ""} %) links)])

(rum/defc header [db]
  [:.header.mdl-layout__header [:.mdl-layout__header-row (nav db (ffirst (db/pea db :nav/links)))]])

(rum/defc drawer [db]
  [:.mdl-layout__drawer [:span.mdl-layout-title "Title" ] (nav db (ffirst (db/pea db :nav/links)))])

(rum/defc page < rum/reactive [conn]
  (let [db (rum/react conn)]
  [:.mdl-layout.mdl-js-layout.mdl-layout--fixed-header.canvas
   style/page-css
   (header db)
   (drawer db)
   (content db)]))
