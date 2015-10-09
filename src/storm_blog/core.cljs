(ns ^:figwheel-always storm-blog.core
  (:require [om.core :as om :include-macros true]
            [storm-blog.util :as u]
            [storm-blog.omps :as c]
            [storm-blog.db :as db]
            [cljs.core.async :as async]
            [datascript.core :as d]
            [clojure.browser.repl :as repl]
            [om.next.protocols :as p]
          #_[om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [goog.dom :as gdom]
            [goog.object :as gobj]
            [cljs.pprint :refer [pprint]]
            [sablono.core :as html :refer-macros [html]]))

(enable-console-print!)

(def event-bus (async/chan))
(def event-bus-pub (async/pub event-bus first))

(defn on-js-reload []
  "All good")

(defonce conn (db/create-db))

(defn init []
  (db/populate-db! conn)
  #_ (rum/mount (c/page conn) js/document.body)
  (u/load-articles! conn))

(defonce state (init))

(om/root c/article 1
  {:shared {:db @conn}
   :target (. js/document (getElementById "app"))})



