(ns storm-blog.db
  (:require [datascript.core :as d]
            [storm-blog.mock-data :as m]))

(defn create-db []
  (d/create-conn m/schema))

(defn populate-db! [conn]
  (d/transact! conn m/fixtures))

(defn pea [db att]
  (d/q '[:find (pull ?e [*])
         :in $ ?a
         :where [?e ?a]] db att))

(defn pvea [db eid att]
  (d/q '[:find (pull ?v [*])
         :in $ ?e ?a
         :where [?e ?a ?v]] db eid att))

(defn vea [db eid att]
  (d/q '[:find ?v .
         :in $ ?e ?a
         :where [?e ?a ?v]]
       db eid att))

(defn eav [db att v]
  (d/q '[:find ?e
         :in $ ?a ?v
         :where [?e ?a ?v]]
       db att v))

(defn g [db att eid]
  (att (d/pull db [att] eid)))

(defn gv [db atts eid]
  (map (fn [att] (g db att eid)) atts))

(defn children 
  ([db eid]
   (map conj (eav db :widget/owner eid) (repeat db)))
  ([db vals eid]))

(defn ordered-children [db eid]
  (apply map vector
         [(->> (d/pull db [{:widget/_owner [:db/id :widget/order]}] eid)
                :widget/_owner
                (sort-by :widget/order)
                (map :db/id))
          (repeat db)]))

(defn get-widgets [db type]
  (map conj (d/q '[:find ?e
                   :in $ ?v
                   :where [?e :widget/type ?v]]
                 db type)
       (repeat db))) 

(defn get-ui-att [db att]
  (g db att 0))

(defn section-template [eid content]
  {:db/id eid
   :widget/owner 1
   :widget/type :section
   :widget/content content})

(defn par-template [eid content]
  {:db/id eid
   :widget/owner 1
   :widget/type :par
   :widget/content content})

(defn get-att [db att]
  (d/q '[:find ?v .
         :in $ ?a
         :where [_ ?a ?v]]
       db att))
