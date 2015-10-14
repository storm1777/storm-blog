(ns storm-blog.omps
  (:require [om.core :as om :include-macros true]
            [datascript.core :as d]
            [garden.core :refer [css]]
            [storm-blog.db :as db]
            [storm-blog.css :as style]
            [storm-blog.util :as u]
            [sablono.core :as html :refer-macros [html]]
            [cljs.core.async :as async :refer [<! >! put! take!]])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]]))

(def actions {:add-par      (fn [_ eid events]
                              (go (>! events [{:db/id -1 :widget/type :par
                                     :par/content "New Paragraph" :widget/owner 1}])))
              :add-section  (fn [_ eid events]
                              (go (>! events [{:db/id -1 :widget/type :section
                                     :section/content "New Section" :widget/owner 1}])))
              :section->par (fn [db eid events]
                              (go (>! events [{:db/id eid :widget/type :section
                                              :section/content (db/g db :par/content eid)}])))
              :par->section (fn [db eid events]
                              (go (>! events [{:db/id eid :widget/type :section
                                              :par/content (db/g db :section/content eid)}])))})

(defn handle-change [e data edit-key owner]
  (om/transact! data edit-key (fn [_] (.. e -target -value))))

(defmulti widgets
  (fn [[eid db] _]
     (db/g db :widget/type eid)))

(defmulti edit
  (fn [[eid db] _]
    (db/g db :widget/type eid)))

(defmethod edit :article [[eid db] owner]
  (reify
    om/IRender
    (render [this]
      (html
       [:.panel.panel-default
        [:.panel-heading
         [:label {:for "example"} "Example"]
         [:.input-group
          [:.input-group-addon "<header/>"]
          [:input#example.form-control
           {:type "header" :placeholder "Example"}]
          [:.input-group-addon "00"]]]
        [:.panel-body
         [:ul.list-group
          [:li.list-group-item (om/build-all edit (sort-by first (map conj (db/eav db :widget/owner eid)
                                                                      (repeat db))))]]]]))))

(defn dropdown-btn [btn-title & menuitem-pairs]
  [:span
   [:button.btn.btn-default.dropdown-toggle
    {:data-toggle "dropdown"
     :type "button"} btn-title
    [:span.caret]]
   [:ul.dropdown-menu {:role "menu"}
    (map (fn [menu args]
           [:li {:role "presentation" :tabIndex -1}
            [:a args menu]])
         menuitem-pairs)]])

(defmethod edit :par [[eid db] owner]
  (reify
    om/IRender
    (render [_]
      (let [events (:events (om/get-shared owner))]
        (html
          [:.input-group
           [:.input-group-addon
            (map (fn [name] [:button.btn.btn-default {:type "button"
                                                      :onClick #((:add-par actions) db eid events)}
                             name]) ["+" "B" "I"])
            (dropdown-btn "Par" ["par" {:onClick #((:add-par actions) db eid events)}])]
           [:textarea.form-control {:rows 3
                                    :value (:par/content (d/pull db [:par/content] eid))
                                    :onChange #((:add-par actions) db eid events)}]])))))

(defmethod edit :section [[eid db] owner {:keys [edit-key]}]
  (reify
    om/IRender
    (render [_]
      (let [events (:events (om/get-shared owner))]
        (html
         [:.input-group
          [:.input-group-btn
           [:button.btn.btn-default {:type "button"
                                     :onClick #((:add-section actions) db eid events)} "+"]
           (dropdown-btn "Section" ["par" {:onClick #((:add-par actions) db eid events)}])]
           [:input.form-control {:type "textarea" :rows 3
                                 :value (db/g db :section/content eid)
                                 :onChange #(let [new-value (-> % .-target .-value)]
                               (go (>! events [(db/section-template eid new-value)])))}]])))))

(defmethod edit :default [[eid _] _]
  (reify
    om/IRender
    (render [this]
      (html [:div  "Edit component"]))))

(defmethod widgets :section [[eid db] owner]
  (reify
    om/IRender
      (render [_]
        (html [:h3 (db/g db :section/content eid)]))))

(defmethod widgets :par [[eid db] owner]
  (reify
    om/IRender
    (render [_]
        (html [:p (db/g db :par/content eid)]))))

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
           [:span.icon-bar]
           [:span.icon-bar]
           [:span.icon-bar]]
          [:a.navbar-brand {:href "/"} "Storm Blog"]]
         [:nav.collapse.navbar-collapse {:role "navigation"}
          [:ul.nav.navbar-nav
           [:li [:a {:href "#"} "Get Started"]]
           [:li [:a {:href "#"} "Edit"]]
           [:li [:a {:href "#"} "Visualize"]]
           [:li [:a {:href "#"} "Prototype"]]]]]]))))

(defmethod widgets :comment [[eid db] owner]
  (reify
    om/IRender
    (render [this]
      (let [events (:events (om/get-shared owner))]
        (html
         [:.panel.panel-default
          [:.panel-heading (db/g db :comment/owner eid)]
          [:.panel-body (db/g db :comment/content eid)]])))))

(defmethod widgets :article [[eid db] owner]
  (reify
    om/IRender
    (render [this]
      (let [events (:events (om/get-shared owner))]
        (html
         [:.row
          [:.col-md-1]
          [:.col-md-5 (om/build-all edit [[eid db]])]
          [:.col-md-5
           [:.panel.panel-default
            [:.panel-heading "Plitvice Lakes"]
            [:ul.list-group.panel-body
             [:li.list-group-item
              (om/build-all widgets (sort-by first (map conj (db/eav db :widget/owner eid) (repeat db))))]
             [:li.list-group-item (om/build-all widgets [[2 db] [3 db]])]
             [:li.list-group-item (om/build-all widgets [[25 db]])]]]]])))))

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
          (om/build-all widgets [[8 db] [1 db] [6 db] [2 db]])
          (om/build-all widgets [[2 db] [3 db]])])))))
