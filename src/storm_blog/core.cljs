(ns ^:figwheel-always storm-blog.core
  (:require [om.core :as om :include-macros true]
            [storm-blog.actions :as a]
            [storm-blog.util :as u]
            [storm-blog.omps :as c]
            [storm-blog.db :as db]
            [storm-blog.md5 :as md5]
            [sablono.core :as html :refer-macros [html]]
            [cljs.core.async :as async :refer [<! >! put! take!]]
            [datascript.core :as d]
            [goog.events :as events]
            [goog.dom :as gdom]
            [secretary.core :as secretary :refer-macros [defroute]])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]])
  (:import goog.History))

(enable-console-print!)

(secretary/set-config! :prefix "#") 
 
(def events (async/chan 10))

(defroute users "/users/:eid" [eid]
  (a/transact! events {:db/eid eid :article/title "users"}))

(defroute article "/article/:eid" [eid]
  (a/transact! events {:db/id 0 :ui/article {:db/id (js/parseInt eid)}}))

(defroute location "/location/:eid" [eid]
  (a/transact! events {:db/id 0 :ui/article {:db/id (js/parseInt eid)}}))

(defroute categories "/category/:eid" [eid] 
  (a/transact! events {:db/id 0 :ui/article {:db/id (js/parseInt eid)}}))

(defroute archive "/archive/:eid" [eid]
  (a/transact! events {:db/id 0 :ui/article {:db/id (js/parseInt eid)}}))
 
(defn on-js-reload []
  "All good")

(def conn (db/create-db))
(def testa (db/populate-db! conn))

(defn main []
  (go
    (while true
      (d/transact! conn (<! events))))
  (let [history (History.)]
    (events/listen history "navigate"
                   (fn [event]
                     (secretary/dispatch! (.-token event))))
    (.setEnabled history true))
  (om/root c/widget conn
           {:shared {:events events}
            :target (. js/document (getElementById "app"))}))

(main)


