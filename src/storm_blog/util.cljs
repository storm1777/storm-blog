(ns storm-blog.util
  (:require
   [datascript.core :as d]
   [datascript.transit :as dt]
   [cljs.core.async :as async]
   [goog.string]
   [goog.string.format])
  (:import
    [goog.net XhrIo]))

(defn- format-time [date]
  (goog.string/format "%02d:%02d" (.getHours date) (.getMinutes date)))

(defn- should-scroll? [node]
  (<=
   (- (.-scrollHeight node) (.-scrollTop node) (.-offsetHeight node))
   0))

(defn ajax [url callback]
  (.send goog.net.XhrIo url
    (fn [reply]
      (-> (.-target reply)
          (.getResponseText)
          (cljs.reader/read-string)
          (callback)))))

(defn load-articles! [conn]
  (doseq [[id url title] (d/q '[:find ?id ?src
                                :where [?id :article/source ?src]] @conn)]
    (ajax url
          (fn [ps]
            (d/transact! conn [{:db/id id
                                :article/content ps}])))))

(defn get-ui [db]
  (d/pull db '[*] 0))

;; transit serialization
(defn db->string [db]
  (dt/write-transit-str db))

(defn string->db [s]
  (dt/read-transit-str s))

;; persisting DB between page reloads
(defn persist [db]
  (js/localStorage.setItem "storm-blog/DB" (db->string db)))

(def event-bus (async/chan))
(def event-bus-pub (async/pub event-bus first))

(defn reset-conn! [db conn]
  (reset! conn db)
  #_ #_ (render db)
  (persist db))



