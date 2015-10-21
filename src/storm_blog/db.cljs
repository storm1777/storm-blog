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

(defn section-template [eid content]
  {:db/id eid
   :widget/owner 1
   :widget/type :section
   :widget/order 15
   :widget/content content})

(defn par-template [eid content]
  {:db/id eid
   :widget/owner 1
   :widget/type :par
   :widget/order 15
   :widget/content content})

(defn get-att [db att]
  (d/q '[:find ?v .
         :in $ ?a
         :where [_ ?a ?v]]
       db att))
