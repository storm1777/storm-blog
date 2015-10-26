(ns storm-blog.actions
  (:require
   [storm-blog.db :as db]
   [cljs.core.async :as async :refer [<! >! put! take!]]
   [om.core :as om])
  
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(defn transact! [events & data]
  (go (>! events data)))

(defn add-par [_ eid events owner order]
  (go (>! events [{:db/id -1 :widget/type :par
                   :widget/content "New Paragraph" 
                   :widget/owner owner
                   :widget/order order}])))

(defn add-section [_ eid events owner order]
  (go (>! events [{:db/id -1 :widget/type :section
                   :widget/content "New Section" 
                   :widget/owner owner
                   :widget/order order}])))

(defn ->par [db eid events]
  (go (>! events [{:db/id eid :widget/type :par
                   :widget/content (db/g db :widget/content eid)}])))

(defn ->section [db eid events]
  (go (>! events [{:db/id eid :widget/type :section
                   :widget/content (db/g db :widget/content eid)}])))

(defn retract [_ eid events]
  (go (>! events [[:db.fn/retractEntity eid]])))

(defn not-active [owner]
  {:on-click       #(om/set-state! owner :show-dropdown true)
   :on-mouse-leave #(om/set-state! owner :show-dropdown false)})

(defn active [owner]
  {:on-mouse-enter #(om/set-state! owner :show-dropdown true)
   :on-mouse-leave #(om/set-state! owner :show-dropdown false)})
