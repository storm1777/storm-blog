(ns storm-blog.mock-data)

(def schema {:article/title    {}
             :article/content  {}
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
    :article/title "Article 1"
    :article/content "Article Content"
    :article/category "Location Feature"
    :article/list ["1" "2" "3" "4"]
    :article/source "/edn/article-1.edn"
    :article/comment [-6 -7]}
   {:db/id -2
    :card/title "Plitvice Lakes, Croatia"
    :card/words "Let me just start by giving it to you straight: this is one of the most magnificent places I have ever visited in my entire life"
    :card/buttons ["view" "comment" "share" "like"]}
   {:db/id -3
    :card/title "Jeremy and Kate"
    :card/words "Blah blah blah"
    :card/buttons ["view" "comment" "share" "like"]}
   {:db/id -4
    :article/title "Article 2"
    :article/content "Article Content"
    :article/category "Location Feature"
    :article/list ["1" "2" "3" "4"]
    :article/source "/edn/article-2.edn"}
   {:db/id -5
    :nav/links ["Link1" "Link2" "Link3" "Link4"]}
   {:db/id -6
    :comment/content "Hello World"
    :comment/owner "The Commenter"
    :comment/email "email@email.com"
    :comment/website "email.com"}
   {:db/id -7
    :comment/content "Hello World Again"
    :comment/owner "The Commenter"
    :comment/email "email@email.com"
    :comment/website "email.com"}
   {:db/id 0
    :-com-sec :.comment-section
    :-com-own :.comment-owner
    :-com-ema :.comment-email
    :-com-web :.comment-website
    :-com-con :.comment-content
    :-crds :.mdl-cell.mdl-cell--4-col
    :-crd :.demo-card-square.mdl-card.mdl-shadow--2dp
    :-crd-hdr :.mdl-card__title.mdl-card--expand
    :-crd-title :h2.mdl-card__title-text
    :-crd-txt :.mdl-card__supporting-text
    :-crd-bttns :.mdl-card__actions.mdl-card--border
    :-bttn :.a.mdl-button.mdl-button--colored.mdl-js-button.mdl-js-ripple-effect
    :-grd :.mdl-grid
    :-art-spacr :.article-spacer.mdl-cell.mdl-cell--2-col.mdl-cell--hide-table.mdl-cell--hide-phone
    :-art :article.demo-content.mdl-color--white.mdl-shadow--4dp.content.mdl-color-text--grey-800.mdl-cell.mdl-cell--8-col
    :-art-hdr :.article-header
    :-art-ban :.article-banner
    :-art-cat :.article-category
    :-art-title :.article-title
    :-art-list :ul.article-list
    :-art-list-i :.li.article-list-item
    :-sec :section
    :-spacr :.mdl-layout-spacer
    :-demo-grd :.mdl-grid.demo-main
    :-con :main.mdl-layout__content
    :-pg-con :.page-content
    :-nav :nav.mdl-navigation
    :-nav-lnk :a.mdl-navigation__link
    :-hdr :.header.mdl-layout__header
    :-hdr-r :.mdl-layout__header-row
    :-drwr :.mdl-layout__drawer
    :-title :span.mdl-layout-title
    :-lyt :.mdl-layout.mdl-js-layout.mdl-layout--fixed-header.canvas}])
