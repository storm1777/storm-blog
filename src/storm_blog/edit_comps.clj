(ns storm-blog.edit-comps)

#_ (defmulti edit
  (fn [[eid db] _]
    (db/g db :widget/type eid)))

#_ (defmethod edit :article [[eid db] owner]
  (reify
    om/IRender
    (render [this]
      (html
       [:.panel.panel-default
        [:.panel-heading
         [:label {:for "example"} "Example"]
         [:.input-group
          [:.input-group-addon "<header/>"]
          [:input#example.form-control
           {:type "header" :placeholder "Example"}]
          [:.input-group-addon "00"]]]
        [:ul.list-group
         [:li.list-group-item
          (om/build-all edit
                        (sort-by first
                                 (map conj
                                      (db/eav db :widget/owner eid)
                                      (repeat db))))]]]))))

#_ (defmethod edit :par [[eid db] owner]
  (reify
    om/IInitState
    (init-state [_]
      {:show-dropdown false})
    om/IRenderState
    (render-state [this state]
      (let [events (:events (om/get-shared owner))]
        (html
          [:.input-group {:on-mouse-enter #(om/set-state! owner :show-dropdown true)
                          :on-mouse-leave #(om/set-state! owner :show-dropdown false)}
           [:.input-group-addon.btn.btn-default {:onClick #(a/add-par db eid events)} "+"]
           [:.input-group-addon.btn.btn-default {:onClick #(a/retract db eid events)} "-"]
           [:textarea.form-control {:rows 4
                                    :value (db/g db :widget/content eid)
                                    :onChange #(let [new-value (-> % .-target .-value)]
                                           (go (>! events [(db/par-template eid new-value)])))
                                    }]
           (when (:show-dropdown state)
             [:.input-group-addon.btn.btn-default {:onClick #(a/retract db eid events)} "-"]
             #_ (dropdown-btn "Par"
                          ["par" {:onClick #(a/->par db eid events)}]
                          ["Section" {:onClick #(a/->section db eid events)}]))])))))

#_ (defmethod edit :section [[eid db] owner]
  (reify
    om/IInitState
    (init-state [_]
      {:show-dropdown false})
    om/IRenderState
    (render-state [this state]
    (let [events (:events (om/get-shared owner))]
      (html
       [:.input-group {:on-mouse-enter #(om/set-state! owner :show-dropdown true)
                       :on-mouse-leave #(om/set-state! owner :show-dropdown false)}
        [:.input-group-addon.btn.btn-default {:onClick #(a/add-par db eid events)} "+"]
        [:.input-group-addon.btn.btn-default {:onClick #(a/retract db eid events)} "-"]
        [:input.form-control {:type "textarea" :rows 3
                              :value (db/g db :widget/content eid)
                              :onChange #(let [new-value (-> % .-target .-value)]
                                           (go (>! events [(db/section-template eid new-value)])))}]
        (when (:show-dropdown state)
             [:.input-group-addon.btn.btn-default {:onClick #(a/retract db eid events)} "-"])])))))

#_ (defmethod edit :default [[eid _] _]
  (reify
    om/IRender
    (render [this]
      (html [:div  "Edit component"]))))
