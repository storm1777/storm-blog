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
             :comment/email    {}
             :widget/owner     {:db.type :db.type/ref}
             :ui/article       {:db.type :db.type/ref}})

(def fixtures
  [{:db/id -1
    :widget/type :article
    :article/title "Plitvice Lakes, Croatia"
    :article/country "Croatia"
    :widget/content "Article Content"
    :article/category "Location Feature"
    :article/list ["1" "2" "3" "4"]
    :article/source "/edn/article-1.edn"
    :article/comment [-2 -3]
    :article/date "October 14th, 2015"}
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
    :widget/content "Article Content"
    :article/country "Ireland"
    :article/category "Location Feature"
    :article/date "October 15th, 2015"
    :article/list ["1" "2" "3" "4"]
    :article/source "/edn/article-2.edn"}
   {:db/id -7
    :widget/type :nav
    :nav/links ["Link1" "Link2" "Link3" "Link4"]}
   {:db/id -2
    :widget/type :comment
    :widget/content "Hello World"
    :comment/owner "The Commenter"
    :comment/email "storm.jeremy@gmail.com"
    :comment/website "email.com"}
   {:db/id -3
    :widget/type :comment
    :widget/content "Hello World Again"
    :comment/owner "The Commenter"
    :comment/email "email@email.com"
    :comment/website "email.com"}
   {:db/id -8
    :widget/type :header
    :header/title "All Ports for the Storms "}
   {:db/id -9
    :widget/type :text
    :widget/tag :h3
    :widget/owner -1
    :widget/order 1
    :widget/content "The Textk"}
   {:db/id -10
    :widget/type :text
    :widget/tag :p
    :widget/owner -1
    :widget/order 2
    :widget/content "Let me just start by giving it to you straight: this is one of the most magnificent places I have ever visited in my entire life, and if you have the chance: go. Seriously, go. You will not regret it."}
   {:db/id -11
    :widget/type :text
    :widget/tag :p
    :widget/owner -1
    :widget/order 3
    :widget/content "Plitvice Lakes National Textk has been a UNESCO World Heritage Site since 1979. This incredible place is made up of 16 interconnected lakes that are an indescribable color blue. They vary from crisp, clear blue, to turquoise, to an almost green color throughout the day and with light changes--but they are always magnificent. The photos I have posted here are not photoshopped or altered in anyway--any change at all is a disservice to this level of beauty."}
   {:db/id -12
    :widget/type :text
    :widget/tag :p
    :widget/owner -1
    :widget/order 4
    :widget/content "We spent 2 full days in the area, and truly could have used a third in order to explore some of the things outside of the national textk itself. Though day trips here are popular from the Croatian coastal towns, I strongly recommend a full side trip if at all possible-- one day is simply not enough. We spent 7 hours each day hiking. For obvious reasons, be sure to bring plenty of water, as well as food. There are snacks and a small grill available in the textk, but the quality is about what you would expect from a small restaurant in the middle of nowhere with no competition."}
   {:db/id -13
    :widget/type :text
    :widget/tag :p
    :widget/owner -1
    :widget/order 5
    :widget/content "The 14 hours of hiking gave us the opportunity to walk around all 16 lakes, almost in their entirety. With very limited exception, we did not take any of the trails that led away from the lakes, though I’m sure they are also magnificent in their way."}
   {:db/id -14
    :widget/type :text
    :widget/tag :p
    :widget/owner -1
    :widget/order 6
    :widget/content "The textk is divided into two texts: the Upper Lakes and the Lower Lakes. On day one, we bought our tickets and were ferryed onto a boat that took us over to the Upper Lakes. They suggested that we walk up until we got to their station, and then catch the bus back down."}
   {:db/id -15
    :widget/type :text
    :widget/tag :p
    :widget/owner -1
    :widget/order 7
    :widget/content "Being mavericks, we were not remotely finished when we reached the place to catch the bus, and so continued on. We made our way back down the lakes ourselves, which was a delightful adventure. There were trails the entire way, but some were very steep in places. I definitely don’t recommend walking back if you’re not comfortable on reasonably rugged trails. Beware the distance, as well: though we greatly enjoyed the experience, what we thought would be a straightforward walk back was actually much, much (read: a couple hours) longer once we factored in all of the inlets we had to curve around. Worth it, though! Except for 2-3 small groups, we were completely alone with the lakes during this leg of our trip."}
   {:db/id -16
    :widget/type :text
    :widget/tag :p
    :widget/owner -1
    :widget/order 8
    :widget/content "In general, the Upper Lakes are less busy than the Lower Lakes, and have just as much to offer. If you’re a serious photographer of any kind, you’ll likely have better luck in this text finding the space and time to set up a human-free shot."}
   {:db/id -17
    :widget/type :text
    :widget/tag :p
    :widget/owner -1
    :widget/order 9
    :widget/content "On day two, we hit the Lower Lakes, which include the Big Waterfall (it’s aptly named), an awesome cave to climb through, and some beautiful overviews of the lakes. Downside, it’s a bit more crowded and more “on the beaten path”. Absolutely worth visiting, but a distinct environment from the more tranquil Upper Lakes."}
   {:db/id -18
    :widget/type :text
    :widget/tag :p
    :widget/owner -1
    :widget/order 10
    :widget/content "We closed out day two by renting a small rowboat on one of the larger lakes--for a whopping 7 USD, we had an hour on the lake, which gave us the opportunity to get up close and personal with some of the smaller falls and picturesque scenes that were too far off any trail to get a good view from land."}
   {:db/id -19
    :widget/type :text
    :widget/tag :h3
    :widget/owner -1
    :widget/order 11
    :widget/content "Food, Lodging & Transportation"}
   {:db/id -20
    :widget/type :text
    :widget/tag :p
    :widget/owner -1
    :widget/order 12
    :widget/content "The area that the textks are located in is fairly rural--there’s a couple of small grocery stores in town, and a handful of restaurants and inns. Beware the limited hours, especially for the grocery stores. We didn’t eat any of our meals out while here, instead opting to spend 42.52 USD on groceries (3 breakfasts, 2 lunches, and 2 dinners for 2 adults). We cooked simple hot meals for breakfast and dinner--nothing complex, but we were definitely starving due to the level of activity."}
   {:db/id -21
    :widget/type :text
    :widget/tag :p
    :widget/owner -1
    :widget/order 13
    :widget/content "That being said, there was one exception to our eating-at-home rule: Ledo ice cream in the textk. Ledo is a Croatian brand of ice cream that is absolutely delicious--try some whenever you get the chance. Our Airbnb host in Zadar recommended the brand to us later on, stating Croatia was (rightfully) very proud of it--we had already tried it at that point and were hooked."}
   {:db/id -22
    :widget/type :text
    :widget/tag :p
    :widget/owner -1
    :widget/order 14
    :widget/content "Lodging is available in the area, of course, due to the draw of the textk--but options are limited. There are a few hotels in textk, a few outside, and several hostels in the surrounding area. We went with a private room in an Airbnb house. The top floor of the home was dedicated to Airbnb--we had a private room and bath, and a kitchen that we were sharing with one other family (though we never saw them, and never had an issue with room in the kitchen)."}
   {:db/id -23
    :widget/type :text
    :widget/tag :p
    :widget/owner -1
    :widget/order 14
    :widget/content "Regarding transportation: there are buses that run through the area, but they are not plentiful. I was very glad we had the rental car, and would recommend getting one if at all possible. To properly enjoy the area, it simply doesn’t lend itself to public transportation--it is too rural, too sprawling, with too many nooks and crannies to explore, both in and out of the textk."}
   {:db/id -24
    :widget/type :text
    :widget/tag :h3
    :widget/owner -1
    :widget/order 15
    :widget/content "Overall"}
   {:db/id -25
    :widget/type :comment-form}
   {:db/id -26
    :widget/type :article-creator}
   {:db/id -27
    :widget/type :img
    :widget/owner -1
    :widget/order 16
    :img/src "/img/img0.jpg"}
   {:db/id 0
    :ui/article -1}
   {:db/id -28
    :widget/type :text
    :widget/tag :h3
    :widget/owner -6
    :widget/content "Text example"}
   {:db/id -29
    :widget/type :text
    :widget/tag :p
    :widget/owner -6
    :widget/order 1
    :widget/content "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque vel lacinia enim, vitae consequat erat. Donec massa dolor, euismod nec auctor sed, luctus sit amet massa. Vivamus consectetur fermentum quam, in dapibus metus consequat non. Nunc id felis vel lorem pharetra euismod. Maecenas ultrices dignissim mattis. Integer facilisis tristique tempus. Vestibulum ut vestibulum ligula. Cum sociis natoque penatibus et magnis dis textturient montes, nascetur ridiculus mus. Aliquam sodales sem sed libero posuere, et iaculis metus mollis. Nulla finibus purus eu condimentum molestie. In pellentesque augue at libero bibendum consectetur. Phasellus quis felis efficitur nunc egestas fringilla."}
   {:db/id -30
    :widget/type :text
    :widget/tag :p
    :widget/owner -6
    :widget/order 2
    :widget/content "Aliquam venenatis, augue sit amet congue bibendum, risus mauris pretium felis, ac tempus arcu diam nec erat. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi nec erat interdum, convallis mi sed, condimentum felis. Quisque a lectus non ex feugiat sodales. Donec lobortis sagittis ullamcorper. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum porttitor ex ac nulla porttitor, ac faucibus nisl finibus. Quisque quis nibh ullamcorper, varius mi et, sollicitudin dui. Donec eu quam dolor. Suspendisse tincidunt enim ligula, eu egestas purus feugiat ac. Cras iaculis finibus enim vel imperdiet. Nunc nec ligula sit amet quam tempor viverra. Donec vel libero diam."}
   {:db/id -31
    :widget/type :text
    :widget/tag :p
    :widget/owner -6 
    :widget/order 3
    :widget/content "Curabitur elementum justo nec ante tristique, ac dapibus dolor euismod. Donec lacus massa, feugiat nec cursus ac, ultrices id diam. Etiam pretium ipsum auctor nulla sagittis, nec ultricies diam porttitor. Morbi molestie nisl dolor, id vestibulum felis lacinia nec. Vivamus fermentum ornare neque, vel convallis mi efficitur id. Nunc ultricies auctor aliquet. Donec vel nisl vel erat fermentum sagittis sit amet et lectus. Etiam ut orci vel lorem eleifend ullamcorper ac sit amet diam. Quisque commodo at quam sed feugiat. Fusce euismod eros ipsum, vitae posuere mauris iaculis non. Morbi condimentum neque a metus ultricies, eu tristique dolor convallis. Fusce vel porta sem, vel viverra mi."}
   {:db/id -32
    :widget/type :img
    :widget/owner -1
    :img/src "/img/img0.jpg"
    :img/caption "Textliament Caption"}
   {:db/id -33
    :widget/type :img
    :widget/owner -1
    :img/src "/img/img1.jpg"
    :img/caption "banner caption"}
   {:db/id -34
    :widget/type :img
    :widget/owner -1
    :img/src "/img/img2.jpg"
    :img/caption "Parliament 1 picture"}
   {:db/id -35
    :widget/type :facet
    :facet/att :article/country
    :facet/type "location"
    :facet/title "Locations"}
   {:db/id -36
    :widget/type :facet
    :facet/att :article/category
    :facet/type "category"
    :facet/title "Category"}
   {:db/id -37
    :widget/type :facet
    :facet/att :article/date
    :facet/type "archive"
    :facet/title "Archive"}
   {:db/id -38
    :widget/type :carousel
    :widget/order 30
    :widget/owner -1}
   {:db/id -39
    :carousel-item/active true
    :carousel-item/img "/img/img0.jpg"
    :carousel-item/caption "Image 0"
    :widget/type :carousel-item
    :widget/owner -38}
   {:db/id -40
    :carousel-item/active false
    :carousel-item/img "/img/img1.jpg"
    :carousel-item/caption "Image 1"
    :widget/type :carousel-item
    :widget/owner -38}
   {:db/id -41
    :carousel-item/active false
    :carousel-item/img "/img/img2.jpg"
    :carousel-item/caption "Image 2"
    :widget/type :carousel-item
    :widget/owner -38}
   {:db/id -42
    :widget/type :page}
   {:db/id -43
    :app/title "Hello, Datascript!"
    :app/count 0}])
