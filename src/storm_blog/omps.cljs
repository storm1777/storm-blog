(ns storm-blog.omps
  (:require [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [datascript.core :as d]
            [garden.core :refer [css]]
            [storm-blog.db :as db]
            [storm-blog.css :as style]
            [storm-blog.util :as u]
            [om-bootstrap.button :as b]
            [om-bootstrap.grid :as g]
            [om-bootstrap.input :as i]
            [om-bootstrap.mixins :as m]
            [om-bootstrap.modal :as md]
            [om-bootstrap.nav :as n]
            [om-bootstrap.pagination :as pg]
            [om-bootstrap.panel :as p]
            [om-bootstrap.progress-bar :as pb]
            [om-bootstrap.random :as r]
            [om-bootstrap.table :refer [table]]
            [sablono.core :as html :refer-macros [html]]
            [cljs.core.async :as async :refer [<! >! put! take!]])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]]))

(defn handle-change [e data edit-key owner]
  (om/transact! data edit-key (fn [_] (.. e -target -value))))

(defmulti widgets
  (fn [[eid db] _]
     (:widget/type (d/pull db [:widget/type] eid))))

(defmulti edit
  (fn [[eid db] _]
    (:widget/type (d/pull db [:widget/type] eid))))

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

(defmethod edit :par [[eid db] owner]
  (reify
    om/IRender
    (render [_]
      (let [events (:events (om/get-shared owner))]
        (html
          [:.input-group
           [:.input-group-addon
            [:button.btn.btn-default {:type "button"} "+"]
            [:button.btn.btn-default {:type "button"} "B"]
            [:button.btn.btn-default {:type "button"} "I"]
            [:button.btn.btn-default.dropdown-toggle
             {:data-toggle "dropdown"
              :type "button"}
             "P"
             [:span.caret]]
            [:ul.dropdown-menu {:role "menu"}
             [:li {:role "presentation"}
              [:a {:onClick #(go (>! events [{:db/id eid :widget/type :section
                                              :section/content (db/g db :par/content eid)}]))} "Section"]]
             #_ [:li {:role "presentation" :tabIndex -1}
              [:a {:onClick #(go (>! events [{:db/id eid :widget/type :par
                                              :par/content (db/g db :section/content eid)}]))} "Paragraph"]]
             [:li {:role "presentation" :tabIndex -1}
              [:a {:href "#":role "menuitem"} "Image"]]
             [:li {:role "presentation" :tabIndex -1}
              [:a {:href "#" :role "menuitem"} "null"]]]]
           [:textarea.form-control {:rows 3
                                    :value (:par/content (d/pull db [:par/content] eid))
                                    :onChange #(let [new-value (-> % .-target .-value)]
                                              (go (>! events [(db/par-template eid new-value)])))}]])))))

(defmethod edit :section [[eid db] owner {:keys [edit-key]}]
  (reify
    om/IRender
    (render [_]
      (let [events (:events (om/get-shared owner))]
        (html
         [:.input-group
          [:.input-group-btn
           [:button.btn.btn-default {:type "button"
                                     :onClick #(go (>! events [{:db/id -1 :widget/type :section
                                     :section/content "New Section" :widget/owner 1}]))} "+"]
           [:button.btn.btn-default.dropdown-toggle {:data-toggle "dropdown"
                                                     :type "button"}
            "Section"
            [:span.caret]]
           [:ul.dropdown-menu {:role "menu"}
            #_ [:li {:role "presentation"}
              [:a {:onClick #(go (>! events [{:db/id eid :widget/type :section
                                              :section/content (db/g db :par/content eid)}]))} "Section"]]
            [:li {:role "presentation" :tabIndex -1}
              [:a {:onClick #(go (>! events [{:db/id eid :widget/type :par
                                              :par/content (db/g db :section/content eid)}]))} "Paragraph"]]
            [:li {:role "presentation" :tabIndex -1}
             [:a {:href "#":role "menuitem"} "Image"]]
            [:li {:role "presentation" :tabIndex -1}
             [:a {:href "#" :role "menuitem"} "null"]]]]
           [:input.form-control {:type "textarea" :rows 3
                                 :value (db/g db :section/content eid)
                                 :onChange #(let [new-value (-> % .-target .-value)]
                               (go (>! events [(db/section-template eid new-value)])))}]])))))

(defmethod edit :default [[eid _] _]
  (reify
    om/IRender
    (render [this]
      (dom/div "Edit component"))))

(defmethod widgets :section [[eid db] owner]
  (reify
    om/IRender
      (render [_]
        (dom/h3 (:section/content (d/pull db [:section/content] eid))))))

(defmethod widgets :par [[eid db] owner]
  (reify
    om/IRender
    (render [_]
        (dom/p (:par/content (d/pull db [:par/content] eid))))))

(defmethod widgets :comment-form [[_ db] owner]
  (reify
    om/IRender
    (render [this]
            (dom/form {:class "form-horizontal"} "Add comment"
              (i/input {:label "Input Wrapper"}
                (g/row {}
                  (g/col {:xs 4 :xs-offset 2} (i/input {:type "text" :class "form-control"
                                           :placeholder "Name*"}))
                  (g/col {:xs 1})
                  (g/col {:xs 4} (i/input {:type "text" :class "form-control"
                                           :placeholder "Email"}))))
                  (i/input {:type "textarea"
                            :label-classname "col-xs-2"
                            :wrapper-classname "col-xs-10"
                            :placeholder "Comment"})
              (i/input {:type "checkbox" :label "Checkbox"
                                :label-classname "col-xs-2"
                                :wrapper-classname "col-xs-offset-2 col-xs-10"
                                :help "Offset is applied to the wrapper."})))))

#_(defmethod widgets :header [[eid db] owner]
  (reify
    om/IRender
    (render [this]
      (html
       [:.page-header (db/g db :header/title eid)
        [:small "Subtitle for header"]]))))

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
