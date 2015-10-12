(ns ^:figwheel-always storm-blog.core
  (:require [om.core :as om :include-macros true]
            [storm-blog.util :as u]
            [storm-blog.omps :as c]
            [storm-blog.db :as db]
            [cljs.core.async :as async :refer [<! >! put! take!]]
            [datascript.core :as d]
            [om.dom :as dom]
            [goog.dom :as gdom])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]]))

(enable-console-print!)

(defn on-js-reload []
  "All good")

(def conn (db/create-db))
(defonce testa (db/populate-db! conn))

(defn main []
  (let [events (async/chan 10)]
    ;; server loop
    (go
     (while true
       (d/transact! conn (<! events))))
    (om/root c/widget conn
             {:shared {:events events}
              :target (. js/document (getElementById "app"))})))

(main)

