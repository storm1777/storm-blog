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

(defn carousel-item [[eid db] & [{active :active :or {active false}}]]
  [(if active :.item.active :.item)   
   [:img {:src (db/vea db eid :img/src)}]
   [:.carousel-caption [:h3 (db/vea db eid :img/caption)]]])

(defn carousel [[eid db]]
  [:.carousel.slide {:data-ride "carousel" :id "car"
                     :data-interval "false"}
   [:ol.carousel-indicators
    [:li.active {:data-target "#car" :data-slide-to "0"}]
    [:li        {:data-target "#car" :data-slide-to "1"}]
    [:li        {:data-target "#car" :data-slide-to "2"}]]
   [:.carousel-inner {:role "listbox"}
    (carousel-item [32 db] {:active true})
    (carousel-item [32 db])
    (carousel-item [32 db])]
   [:.left.carousel-control {:href "#car" :role "button"
                             :data-slide "prev"}
    [:span.glyphicon.glyphicon-chevron-left {:aria-hidden "true"}]
    [:span.sr-only "Previous"]]
   [:.right.carousel-control {:href "#car" :role "button"
                             :data-slide "next"}
    [:span.glyphicon.glyphicon-chevron-right {:aria-hidden "true"}]
    [:span.sr-only "Next"]]])

(defn dropdown-btn [btn-title & menuitem-pairs]
  [:span
   [:button.btn.btn-primary.dropdown-toggle
    {:data-toggle "dropdown"
     :type "button"} btn-title
    [:span.caret]]
   [:ul.dropdown-menu {:role "menu"}
    (map (fn [[title args]]
           [:li [:a args title]]) menuitem-pairs)]])

(defmethod widgets :section [[eid db] owner]
  (reify
    om/IInitState
    (init-state [_]
      {:edit false
       :show-dropdown false})
    om/IRenderState
    (render-state [this state]
      (html
       (if (not (:show-dropdown state))
         [:h3 {:on-click #(om/set-state! owner :show-dropdown true)
               :on-mouse-leave #(om/set-state! owner :show-dropdown false)}
          (db/g db :widget/content eid)]
         (let [events (:events (om/get-shared owner))]
           [:.input-group {:on-mouse-enter #(om/set-state! owner :show-dropdown true)
                           :on-mouse-leave #(om/set-state! owner :show-dropdown false)}
            [:.input-group-addon.btn.btn-default {:onClick #(a/add-par db eid events)} "+"]
            [:.input-group-addon.btn.btn-default {:onClick #(a/retract db eid events)} "-"]
            [:input.form-control {:type "textarea" :rows 3
                                  :value (db/g db :widget/content eid)
                                  :onChange #(let [new-value (-> % .-target .-value)]
                                               (go (>! events [(db/section-template eid new-value)])))}]
            (when (:show-dropdown state)
                 [:.input-group-addon.btn.btn-default {:onClick #(a/retract db eid events)} "-"])]))))))

(defmethod widgets :par [[eid db] owner]
  (reify
    om/IInitState
    (init-state [_]
      {:edit false
       :show-dropdown false})
    om/IRenderState
    (render-state [this state]
      (html
       (if (not (:show-dropdown state))
         [:p {:on-click #(om/set-state! owner :show-dropdown true)
              :on-mouse-leave #(om/set-state! owner :show-dropdown false)}
          (db/g db :widget/content eid)]
         (let [events (:events (om/get-shared owner))]
           [:.input-group {:on-mouse-enter #(om/set-state! owner :show-dropdown true)
                           :on-mouse-leave #(om/set-state! owner :show-dropdown false)}
            [:.input-group-addon.btn.btn-default {:onClick #(a/add-par db eid events)} "+"]
            [:.input-group-addon.btn.btn-default {:onClick #(a/retract db eid events)} "-"]
            [:textarea.form-control {:rows 4
                                     :value (db/g db :widget/content eid)
                                     :onChange #(let [new-value (-> % .-target .-value)]
                                                  (go (>! events [(db/par-template eid new-value)])))}]
            (when (:show-dropdown state)
              [:p [:.input-group-addon.btn.btn-default {:onClick #(a/retract db eid events)} "-"]
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
      (let [events (:events (om/get-shared owner))]
        (html
         [:.panel.panel-default
          [:.panel-heading
           [:.panel-title
            [:img {:src (str "http://gravatar.com/avatar/" (md5/md5 (db/g db :comment/email eid)))}]
            [:a {:data-toggle "collapse" :href (str "#collapse6" eid)}
             (db/g db :comment/owner eid)]]]
          [:.panel-body.panel-collapse.collapse.in {:id (str "collapse6" eid)}
           (db/g db :widget/content eid)]])))))

(defn facet [db {:keys [type att title]}]
  (let [ref (str "collapse-" type)]
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
                db att))]]))
 
(defmethod widgets :article [[eid db] owner]
  (reify
    om/IRender
    (render [this]
      (let [events (:events (om/get-shared owner))]
        (html
         [:.page
          [:.row
           [:.col-md-1]
           [:.col-md-2
            (facet db {:type "location" :att :article/country :title "Locations"})
            (facet db {:type "category" :att :article/category :title "Categories"})
            (facet db {:type "archive" :att :article/date :title "Archive"})]
           [:.col-md-5
            [:.panel.panel-default
             [:.panel-heading
              [:.panel-title
               [:a {:data-toggle "collapse" :href "#collapse4"} "Plitvice Lakes"]]]
             [:ul#collapse4.list-group.panel-collapse.collapse.in
              [:li.list-group-item
               (carousel [eid db])
               (om/build-all 
                widgets (sort-by first (map conj (db/eav db :widget/owner eid) (repeat db))))]
              [:li.list-group-item (om/build-all widgets [[2 db] [3 db]])]
              [:li.list-group-item (om/build-all widgets [[25 db]])]]]]
           [:.col-md-3
            [:.panel.panel-default
             [:.panel-heading 
              [:.panel-title [:a {:data-toggle "collapse" :href "#collapse5"} "About Us"]]]
             [:ul#collapse5.list-group.panel-collapse.collapse.in
              [:p (map (partial str "blab blah") (range 100))]]]]]])))))

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
      (html
       (let [db @conn]
         [:div
          (om/build-all 
           widgets [[8 db] 
                    [(db/get-att 
                      db :ui/article)
                     db]])])))))
