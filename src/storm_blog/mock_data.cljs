(ns storm-blog.mock-data)

(def schema {:article/title    {}
             :article/content  {:db.cardinality :db.cardinality/many}
             :article/category {}
             :article/list     {}
             :article/source   {}
             :article/comment  {:db.type :db.type/ref :db.cardinality :db.cardinality/many}
             :card/title       {}
             :card/words       {}
             :card/buttons     {}
             :nav/links        {}
             :comment/content  {}
             :comment/owner    {}
             :comment/website  {}
             :comment/email    {}})

(def fixtures
  [{:db/id -1
    :widget/type :article
    :article/title "Plitvice Lakes, Croatia"
    :article/content "Article Content"
    :article/category "Location Feature"
    :article/list ["1" "2" "3" "4"]
    :article/source "/edn/article-1.edn"
    :article/comment [-2 -3]}
   {:db/id -4
    :widget/type :card
    :card/title "Plitvice Lakes, Croatia"
    :card/words "Let me just start by giving it to you straight: this is one of the most magnificent places I have ever visited in my entire life"
    :card/buttons ["view" "comment" "share" "like"]}
   {:db/id -5
    :widget/type :card
    :card/title "Jeremy and Kate"
    :card/words "Blah blah blah"
    :card/buttons ["view" "comment" "share" "like"]}
   {:db/id -6
    :widget/type :article
    :article/title "Article 2"
    :article/content "Article Content"
    :article/category "Location Feature"
    :article/list ["1" "2" "3" "4"]
    :article/source "/edn/article-2.edn"}
   {:db/id -7
    :widget/type :nav
    :nav/links ["Link1" "Link2" "Link3" "Link4"]}
   {:db/id -2
    :widget/type :comment
    :comment/content "Hello World"
    :comment/owner "The Commenter"
    :comment/email "email@email.com"
    :comment/website "email.com"}
   {:db/id -3
    :widget/type :comment
    :comment/content "Hello World Again"
    :comment/owner "The Commenter"
    :comment/email "email@email.com"
    :comment/website "email.com"}
   {:db/id -8
    :widget/type :header
    :header/title "All Ports for the Storms "}
   {:db/id -9
    :widget/type :section
    :widget/owner 1
    :widget/order 1
    :section/content "The Park"}
   {:db/id -10
    :widget/type :par
    :widget/owner 1
    :widget/order 2
    :par/content "Let me just start by giving it to you straight: this is one of the most magnificent places I have ever visited in my entire life, and if you have the chance: go. Seriously, go. You will not regret it."}
   {:db/id -11
    :widget/type :par
    :widget/owner 1
    :widget/order 3
    :par/content "Plitvice Lakes National Park has been a UNESCO World Heritage Site since 1979. This incredible place is made up of 16 interconnected lakes that are an indescribable color blue. They vary from crisp, clear blue, to turquoise, to an almost green color throughout the day and with light changes--but they are always magnificent. The photos I have posted here are not photoshopped or altered in anyway--any change at all is a disservice to this level of beauty."}
   {:db/id -12
    :widget/type :par
    :widget/owner 1
    :widget/order 4
    :par/content "We spent 2 full days in the area, and truly could have used a third in order to explore some of the things outside of the national park itself. Though day trips here are popular from the Croatian coastal towns, I strongly recommend a full side trip if at all possible-- one day is simply not enough. We spent 7 hours each day hiking. For obvious reasons, be sure to bring plenty of water, as well as food. There are snacks and a small grill available in the park, but the quality is about what you would expect from a small restaurant in the middle of nowhere with no competition."}
   {:db/id -13
    :widget/type :par
    :widget/owner 1
    :widget/order 5
    :par/content "The 14 hours of hiking gave us the opportunity to walk around all 16 lakes, almost in their entirety. With very limited exception, we did not take any of the trails that led away from the lakes, though I’m sure they are also magnificent in their way."}
   {:db/id -14
    :widget/type :par
    :widget/owner 1
    :widget/order 6
    :par/content "The park is divided into two sections: the Upper Lakes and the Lower Lakes. On day one, we bought our tickets and were ferryed onto a boat that took us over to the Upper Lakes. They suggested that we walk up until we got to their station, and then catch the bus back down."}
   {:db/id -15
    :widget/type :par
    :widget/owner 1
    :widget/order 7
    :par/content "Being mavericks, we were not remotely finished when we reached the place to catch the bus, and so continued on. We made our way back down the lakes ourselves, which was a delightful adventure. There were trails the entire way, but some were very steep in places. I definitely don’t recommend walking back if you’re not comfortable on reasonably rugged trails. Beware the distance, as well: though we greatly enjoyed the experience, what we thought would be a straightforward walk back was actually much, much (read: a couple hours) longer once we factored in all of the inlets we had to curve around. Worth it, though! Except for 2-3 small groups, we were completely alone with the lakes during this leg of our trip."}
   {:db/id -16
    :widget/type :par
    :widget/owner 1
    :widget/order 8
    :par/content "In general, the Upper Lakes are less busy than the Lower Lakes, and have just as much to offer. If you’re a serious photographer of any kind, you’ll likely have better luck in this section finding the space and time to set up a human-free shot."}
   {:db/id -17
    :widget/type :par
    :widget/owner 1
    :widget/order 9
    :par/content "On day two, we hit the Lower Lakes, which include the Big Waterfall (it’s aptly named), an awesome cave to climb through, and some beautiful overviews of the lakes. Downside, it’s a bit more crowded and more “on the beaten path”. Absolutely worth visiting, but a distinct environment from the more tranquil Upper Lakes."}
   {:db/id -18
    :widget/type :par
    :widget/owner 1
    :widget/order 10
    :par/content "We closed out day two by renting a small rowboat on one of the larger lakes--for a whopping 7 USD, we had an hour on the lake, which gave us the opportunity to get up close and personal with some of the smaller falls and picturesque scenes that were too far off any trail to get a good view from land."}
   {:db/id -19
    :widget/type :section
    :widget/owner 1
    :widget/order 11
    :section/content "Food, Lodging & Transportation"}
   {:db/id -20
    :widget/type :par
    :widget/owner 1
    :widget/order 12
    :par/content "The area that the parks are located in is fairly rural--there’s a couple of small grocery stores in town, and a handful of restaurants and inns. Beware the limited hours, especially for the grocery stores. We didn’t eat any of our meals out while here, instead opting to spend 42.52 USD on groceries (3 breakfasts, 2 lunches, and 2 dinners for 2 adults). We cooked simple hot meals for breakfast and dinner--nothing complex, but we were definitely starving due to the level of activity."}
   {:db/id -21
    :widget/type :par
    :widget/owner 1
    :widget/order 13
    :par/content "That being said, there was one exception to our eating-at-home rule: Ledo ice cream in the park. Ledo is a Croatian brand of ice cream that is absolutely delicious--try some whenever you get the chance. Our Airbnb host in Zadar recommended the brand to us later on, stating Croatia was (rightfully) very proud of it--we had already tried it at that point and were hooked."}
   {:db/id -22
    :widget/type :par
    :widget/owner 1
    :widget/order 14
    :par/content "Lodging is available in the area, of course, due to the draw of the park--but options are limited. There are a few hotels in park, a few outside, and several hostels in the surrounding area. We went with a private room in an Airbnb house. The top floor of the home was dedicated to Airbnb--we had a private room and bath, and a kitchen that we were sharing with one other family (though we never saw them, and never had an issue with room in the kitchen)."}
   {:db/id -23
    :widget/type :par
    :widget/owner 1
    :widget/order 14
    :par/content "Regarding transportation: there are buses that run through the area, but they are not plentiful. I was very glad we had the rental car, and would recommend getting one if at all possible. To properly enjoy the area, it simply doesn’t lend itself to public transportation--it is too rural, too sprawling, with too many nooks and crannies to explore, both in and out of the park."}
   {:db/id -24
    :widget/type :section
    :widget/owner 1
    :widget/order 15
    :section/content "Overall"}
   {:db/id -25
    :widget/type :comment-form}
   {:db/id -26
    :widget/type :article-creator}])
