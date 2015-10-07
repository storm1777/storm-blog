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
  (d/q '[:find ?v
         :in $ ?e ?a
         :where [?e ?a ?v]]
       db eid att))


