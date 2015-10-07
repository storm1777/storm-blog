(ns storm-blog.core
  (:require
   [clojure.set :as set]
   [clojure.string :as str]
   [datascript.core :as d]
   [datascript.transit :as dt]))

(defmacro profile [k & body]
  `(let [k# ~k]
     (.time js/console k#)
     (let [res# (do ~@body)]
       (.timeEnd js/console k#)
       res#)))

(comment

(def schema {:article/title    {}
             :article/content  {:db.cardinality :db.cardinality/many}
             :article/category {}
             :article/list     {}})

(def fixtures
  [{:db/id -1
    :article/title "Article 1"
    :article/content "Article Content"
    :article/category "Location Feature"
    :article/list ["1" "2" "3" "4"]
    :article/source "/edn/article.edn"}
   {:db/id -2
    :card/title "Plitvice Lakes, Croatia"
    :card/words "Let me just start by giving it to you straight: this is one of the most magnificent places I have ever visited in my entire life"
    :card/buttons ["view" "comment" "share" "like"]}
   {:db/id -
    :card/title "Jeremy and Kate"
    :card/words "Let me just start by giving it to you straight: this is one of the most magnificent places I have ever visited in my entire life"
    :card/buttons ["view" "comment" "share" "like"]}])

(def conn (d/create-conn schema))
(d/transact! conn fixtures)


(d/pull @conn [:article/title :article/content] 1)


(d/q '[ :find  ?e
          :where [?e :aka "Maks Otto von Stirlitz"]]@conn)
(d/pull @conn [:age] (ffirst (d/q '[ :find  ?e
          :where [?e :aka "Maks Otto von Stirlitz"]]@conn)))

(def a (let [schema {:aka {:db/cardinality :db.cardinality/many}}
      conn   (d/create-conn schema)]
  (d/transact! conn [ { :db/id -1
                        :name  "Maksim"
                        :age   45
                        :aka   ["Maks Otto von Stirlitz", "Jack Ryan"] } ])
  (d/q '[ :find  ?n ?a
          :where [?e :aka "Maks Otto von Stirlitz"]
                 [?e :name ?n]
                 [?e :age  ?a] ]
       @conn))))
