(ns storm-blog.actions
  (:require
   [storm-blog.db :as db]
   [cljs.core.async :as async :refer [<! >! put! take!]])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))


(defn add-par [_ eid events]
  (go (>! events [{:db/id -1 :widget/type :par
                   :widget/content "New Paragraph" :widget/owner 1}])))

(defn add-section [_ eid events]
  (go (>! events [{:db/id -1 :widget/type :section
                   :widget/content "New Section" :widget/owner 1}])))

(defn ->par [db eid events]
  (go (>! events [{:db/id eid :widget/type :par
                   :widget/content (db/g db :widget/content eid)}])))

(defn ->section [db eid events]
  (go (>! events [{:db/id eid :widget/type :section
                   :widget/content (db/g db :widget/content eid)}])))

(defn retract [_ eid events]
  (go (>! events [[:db.fn/retractEntity eid]])))
