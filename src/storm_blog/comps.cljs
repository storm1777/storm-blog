(ns storm-blog.comps
  (:require [om.next :as om :refer-macros [defui]]
            [goog.dom :as gdom]
            [om.dom :as dom]
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


;om/dispatch returns the key for the read
(defmulti read om/dispatch)  

;[env key params]
;env is a hashmap containing any context necessary to accomplish reads
;key is the key that is being requested to be read
;params is a hash map of parameters that can be used to customize the read
;read functions must return a hash map containing a :value entry
(defmethod read :app/counter
  [{:keys [state selector]} _ _]
  {:value (d/q '[:find [(pull ?e ?selector) ...]
                 :in $ ?selector
                 :where [?e :app/title]]
               (d/db state) selector)})

(defmethod read :article [{:keys [state selector]} _ _]
  {:value (d/q '[:find [(pull ?e ?selector) ...]
                 :in $ ?selector
                 :where [?e :widget/type :article]]
               (d/db state) selector)})

(defmulti mutate om/dispatch)

;[env key params]
; returns a hashmap that has both :value and :action keys
(defmethod mutate 'app/increment
  [{:keys [state]} _ entity]
  {:value [:app/counter]
   :action (fn [] (d/transact! state
                               [(update-in entity [:app/count] inc)]))})

(defui Article
  static om/IQuery
  (query [this]
    [{:article[:db/id :article/title
               :article/country :article/content 
               :article/category]}])
  Object
  (render [this]
    (let [{:keys [article/title article/country 
                  article/content article/category] :as entity}
          (get-in (om/props this) [:article 0])]
      (html
       [:.panel.panel-default
        [:.panel-heading
         [:.panel-title title]]
        [:ul.list-group.panel-collapse.collapse.in
         country content category]]))))


;query should return a query expression, a la pull syntax
(defui Counter
  static om/IQuery
  (query [this]
    [{:app/counter [:db/id :app/title :app/count]}])
  Object
  (render [this]
    (let [{:keys [app/title app/count] :as entity}
          (get-in (om/props this) [:app/counter 0])]
      (dom/div nil
        (dom/h2 nil title)
        (dom/span nil (str "Count: " count))
        (dom/button
         #js {:onClick
              (fn [e]
                (om/transact! this
                              `[(app/increment ~entity)]))}
         "Click me!")))))


;reconciler takes a parser, and populates the env parameter with all the necessary
; context needed to make decisions about reads and mutations including whatever :state
; parameter was provided to the reconciler.
#_(def reconciler
  (om/reconciler
   {:state conn
    :parser (om/parser {:read read :mutate mutate})}))

#_(om/add-root! reconciler
   Counter (gdom/getElement "app"))

