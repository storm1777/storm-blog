(ns ^:figwheel-always storm-blog.core
  (:require [om.core :as om :include-macros true]
            [storm-blog.util :as u]
            [storm-blog.omps :as c]
            [storm-blog.db :as db]
            [cljs.core.async :as async]
            [datascript.core :as d]
            [om.dom :as dom]
            [goog.dom :as gdom]))

(enable-console-print!)

(def event-bus (async/chan))
(def event-bus-pub (async/pub event-bus first))

(defn on-js-reload []
  "All good")

(defonce conn (db/create-db))

(defn init []
  (db/populate-db! conn)
  (u/load-articles! conn))

(defonce state (init))

(om/root c/widget [2 conn]
  {:shared {:db @conn}
   :target (. js/document (getElementById "app"))})



