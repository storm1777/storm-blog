(ns ^:figwheel-always storm-blog.core
  (:require [rum.core :as rum]
            [storm-blog.util :as u]
            [storm-blog.comps :as c]
            [storm-blog.db :as db]
            [cljs.core.async :as async]
            [datascript.core :as d]))

(enable-console-print!)

(def event-bus (async/chan))
(def event-bus-pub (async/pub event-bus first))

(defn on-js-reload []
  "All good")

(defonce conn (db/create-db))

(defn init []
  (db/populate-db! conn)
  (rum/mount (c/page conn) js/document.body)
  (u/load-articles! conn))

(defonce state (init))
