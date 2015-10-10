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
            [sablono.core :as html :refer-macros [html]]))

(defmulti widgets
  (fn [[eid conn] _]
    (:widget/type (d/pull conn [:widget/type] eid))))

(defmethod widgets :section [[eid db] _]
  (reify
    om/IRender
    (render [_]
      (dom/div (:section/content (d/pull db [:section/content] eid))))))

(defmethod widgets :par [[eid db] _]
  (reify
    om/IRender
    (render [_]
      (dom/div (:par/content (d/pull db [:par/content] eid))))))

(defmethod widgets :comment-form [[_ _] owner]
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

(defmethod widgets :header [[eid db] owner]
  (reify
    om/IRender
    (render [this]
      (let [db (:db (om/get-shared owner))]
        (r/page-header {} (:header/title (d/pull db [:header/title] eid))
                       (dom/small "Subtitle for header"))))))

(defmethod widgets :comment [[eid db] owner]
  (reify
    om/IInitState
    (init-state [_]
      {:eid eid
       :pullq '[*]})
    om/IRenderState
    (render-state [this {:keys [pullq eid]}]
      (let [db (:db (om/get-shared owner))]
        (p/panel {:header (:comment/owner (d/pull db pullq eid))}
                 (:comment/content (d/pull db pullq eid)))))))

(defmethod widgets :article [[eid db] owner]
  (reify
    om/IInitState
    (init-state [_]
      {:eid eid
       :pullq [:article/title]})
    om/IRender
    (render [this]
      (let [db (:db (om/get-shared owner))]
        (g/grid {}
          (g/row {:class "show-grid"}
            (g/col {:xs 12}
              (p/panel {:header (:article/title (d/pull db [:article/title] eid))
                :list-group
                (dom/ul {:class "list group"}
                  (dom/li {:class "list-group-item"} (om/build-all widgets
                    (sort-by first (map conj (db/eav db :widget/owner eid) (repeat db)))))
                  (dom/li {:class "list-group-item"} (om/build-all widgets [[2 db] [3 db]]))
                #_(dom/li {:class "list-group-item"} (om/build-all widgets [[6 db]])))}
               nil))))))))

(defmethod widgets :default [[eid db] owner]
  (reify
    om/IRender
    (render [this]
      (dom/div {} eid ": " (:widget/type (d/pull db [:widget/type] eid))))))

(defn widget [[_ conn] owner]
  (reify
    om/IRender
    (render [_]
      (let [db @conn]
        (dom/div
          (om/build-all widgets [[8 db] [1 db] [6 db] [2 db]])
          (om/build-all widgets [[2 db] [3 db]]))))))
