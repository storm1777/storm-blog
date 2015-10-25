(ns storm-blog.omps
  (:require [om.core :as om :include-macros true]
            [datascript.core :as d]
            [garden.core :refer [css]]
            [storm-blog.actions :as a]
            [storm-blog.db :as db]
            [storm-blog.css :as style]
            [storm-blog.util :as u]
            [storm-blog.md5 :as md5]
            [sablono.core :as html :refer-macros [html]]
            [cljs.core.async :as async :refer [<! >! put! take!]])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]]))

(defmulti widgets
  (fn [[eid db] _]
     (db/g db :widget/type eid)))

(def make (partial om/build-all widgets))

(defmethod widgets :carousel-item [[eid db] owner]
  (reify
    om/IRender
    (render [this]
      (let [atts [:carousel-item/active
                  :carousel-item/img
                  :carousel-item/caption]
            [active img caption] (db/gv db atts eid)]
        (html
         [(if active :.item.active :.item)
          [:img {:src img}]
          [:.carousel-caption caption]])))))
                                                                                                                         
(defmethod widgets :carousel [[eid db] owner]
  (reify
    om/IRender
    (render [this]
      (let [carousel-slides (db/children db eid)]
        (html
         [:.carousel.slide {:data-ride "carousel" :id "car"
                            :data-interval "false"}
          [:ol.carousel-indicators
           (map (fn [n]
                  [:li {:data-target "#car" :data-slide-to n}])
                (range (count carousel-slides)))]
          [:.carousel-inner {:role "listbox"} (make carousel-slides)]
          [:.left.carousel-control {:href "#car" :role "button"
                                    :data-slide "prev"}
           [:span.glyphicon.glyphicon-chevron-right {:aria-hidden "true"}]
           [:span.sr-only "Previous"]]
          [:right.carousel-control {:href "#car" :role "button"
                                    :data-slide "next"}
           [:span.glyphicon.glyphicon-chevron-right {:aria-hidden "true"}]
           [:span.sr-only "Next"]]])))))

(defn dropdown-btn [btn-title & menuitem-pairs]
  [:span
   [:button.btn.btn-primary.dropdown-toggle
    {:data-toggle "dropdown"
     :type "button"} btn-title
    [:span.caret]]
   [:ul.dropdown-menu {:role "menu"}
    (map (fn [[title args]]
           [:li [:a args title]]) menuitem-pairs)]])

(defn addon-button [f val]
  [:.input-group-addon.btn.btn-default {:on-click f} val])

(defmethod widgets :section [[eid db] owner]
  (reify
    om/IInitState
    (init-state [_]
      {:edit false
       :show-dropdown false})
    om/IRenderState
    (render-state [this state]
      (let [events (:events (om/get-shared owner))
            content (db/g db :widget/content eid)]
        (html
         (if (not (:show-dropdown state))
           [:h3 (a/not-active owner) content]
           [:.input-group (a/active owner)
            (addon-button #(a/add-section db eid events) "+")
            (addon-button #(a/retract db eid events) "-")
            [:input.form-control {:type "textarea" :rows 3
                                  :value content
                                  :onChange #(let [new-value (-> % .-target .-value)]
                                               (a/transact! events (db/section-template eid new-value)))}]
            (when (:show-dropdown state)
              (addon-button #(a/retract db eid events) "-"))]))))))

(defmethod widgets :par [[eid db] owner]
  (reify
    om/IInitState
    (init-state [_]
      {:edit false
       :show-dropdown false})
    om/IRenderState
    (render-state [this state]
      (let [events (:events (om/get-shared owner))
            content (db/g db :widget/content eid)]
        (html
         (if (not (:show-dropdown state))
           [:p (a/not-active owner)
            (db/g db :widget/content eid)]
           [:.input-group (a/active owner)
            (addon-button #(a/add-par db eid events) "+")
            (addon-button #(a/retract db eid events) "-")
            [:textarea.form-control {:rows 4
                                     :value (db/g db :widget/content eid)
                                     :onChange #(let [new-value (-> % .-target .-value)]
                                                  (a/transact! events (db/par-template eid new-value)))}]
            (when (:show-dropdown state)
              [:p (addon-button #(a/retract db eid events) "-")
               [:.input-group-addon.btn.btn-default
                (dropdown-btn "Par"
                              ["par" {:onClick #(a/->par db eid events)}]
                              ["Section" {:onClick #(a/->section db eid events)}])]])]))))))

(defmethod widgets :header [[eid db] owner]
  (reify
    om/IRender
    (render [this]
      (html
       [:header.navbar.navbar-default.navbar-static-top {:role "banner"}
        [:.container
         [:.navbar-header
          [:button.navbar-toggle {:type "button" :data-toggle "collapse"
                                  :data-target ".navbar-collapse"}
           [:span.sr-only "Toggle Navigation"]
           (map (fn [] [:span.icon-bar]) (range 3))]
          [:a.navbar-brand {:href "#/article/1"} "Storm Blog"]]
         [:nav.collapse.navbar-collapse {:role "navigation"}
          [:ul.nav.navbar-nav
           (map (fn [title] [:li [:a {:href "#/article/1"} title]])
                ["Get Started" "Edit" "Visualize" "Prototype"])]]]]))))

(defmethod widgets :comment [[eid db] owner]
  (reify
    om/IRender
    (render [this]
      (let [events (:events (om/get-shared owner))
            [email cowner content] (db/gv db [:comment/email :comment/owner :widget/content] eid)]
        (html
         [:.panel.panel-default
          [:.panel-heading
           [:.panel-title
            [:img {:src (str "http://gravatar.com/avatar/" (md5/md5 email))}]
            [:a {:data-toggle "collapse" :href (str "#collapse6" eid)} cowner]]]
          [:.panel-body.panel-collapse.collapse.in {:id (str "collapse6" eid)} content]])))))

(defmethod widgets :facet [[eid db] owner]
  (reify
    om/IRender
    (render [this]
      (let [ref (str "collapse-" "type")
            [title att type] (db/gv db [:facet/title :facet/att :facet/type] eid)]
        (html
         [:.panel.panel-default
          [:.panel-heading
           [:.panel-title
            [:a {:data-toggle "collapse" :href (str "#" ref)} title]]]
          [:ul.list-group.panel-collapse.collapse.in
           {:id (keyword ref)}
           (map (fn [[x eid n]]
                  [:li.list-group-item
                   [:a {:href (str "#/" type "/" eid)} x]
                   [:span.badge n]])
                (d/q '[:find ?v ?e (count ?e)
                       :in $ ?a
                       :where [?e ?a ?v]]
                     db att))]])))))
 
(defmethod widgets :page [[eid db] owner]
  (reify
    om/IRender
    (render [this]
      (let [events (:events (om/get-shared owner))
            facets (db/get-widgets db :facet)
            articles (db/get-widgets db :article)
            about-us [[10 db]]]
        (html
         [:page
          [:.row
           [:.col-md-1]
           [:.col-md-2 (make facets)]
           [:.col-md-5 (make articles)]
           [:.col-md-3 (make about-us)]]])))))

(defmethod widgets :article [[eid db] owner]
  (reify
    om/IRender
    (render [this]
      (let [events (:events (om/get-shared owner))
            content (sort-by first (db/children db eid))
            comments (db/get-widgets db :comment)
            comment-form (db/get-widgets db :comment-form)]
        (html
         [:.panel.panel-default
          [:.panel-heading
           [:.panel-title
            [:a {:data-toggle "collapse" :href "#collapse4"} "Plitvice Lakes"]]]
          [:ul#collapse4.list-group.panel-collapse.collapse.in
           (map (fn [els] 
                   [:li.list-group-item (make els)])
                 [content comments comment-form])]])))))

(defmethod widgets :default [[eid db] owner]
  (reify
    om/IRender
    (render [this]
      (html
       [:p (db/g db :widget/type eid)]))))

(defn widget [conn owner]
  (reify
    om/IRender
    (render [_]
      (let [db @conn
            header [8 db]
            article [(db/get-att db :ui/article) db]
            page [42 db]]
        (html
         [:div (make [header #_ article page])])))))
