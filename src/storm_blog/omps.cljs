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


(comment (defn comment-form [db {article-eid :db/id :or {article-eid 1}}]
  [:form {:on-submit (fn [_] (js/alert (extract-comment article-eid)) false)}
   [:input.add-owner   {:type :text :placeholder :Owner}]
   [:input.add-email   {:type :text :placeholder :Email}]
   [:input.add-website {:type :text :placeholder :Website}]
   [:input.add-comment {:type :text :placeholder :Comment}]
   [:input.add-submit  {:type "submit" :value "Add comment"}]]))

(defn com [eid owner]
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

(defn article [eid owner]
  (reify
    om/IInitState
    (init-state [_]
      {:eid eid
       :pullq [:article/title]})
    om/IRenderState
    (render-state [this {:keys [pullq]}]
      (let [db (:db (om/get-shared owner))]
        (g/grid {}
          (g/row {:class "show-grid"}
            (g/col {:xs 8 :xs-offset 2}
              (p/panel {:header (:article/title (d/pull db [:article/title] eid))
                :list-group
                (dom/ul {:class "list group"}
                  (dom/li {:class "list-group-item"} (:article/content (d/pull db [:article/content] eid)))
                  (dom/li {:class "list-group-item"} (om/build-all com [2 3]))
                  (dom/li {:class "list-group-item"} "Item 3"))}
               nil))))))))

(defn widget [_ owner]
  (reify
    om/IRender
    (render [this]
            (p/panel
            (om/build-all article [1])))))
