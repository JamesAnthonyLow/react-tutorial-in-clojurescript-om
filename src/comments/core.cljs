(ns ^:figwheel-always comments.core
  (:require[om.core :as om :include-macros true]
                    [markdown.core :refer [md->html]]
                    [cljs.core.async :refer [put! chan <!]]
                    [om.dom :as dom :include-macros true]))

(enable-console-print!)

(println "Hocus pocus I'm the dopest")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state 
  (atom {:comments [{:author "Pete Hunt" :children "This is one comment"}
                    {:author "John Thomas" :children "**THIS IS A STRONG COMMENT**"}
                    {:author "YO DOOD" :children ">Hey look another comment"}
                    {:author "Jordan Walke" :children "This is *another* comment"}]}))

(defn comment-component [data]
  (reify om/IRender
    (render [_]
      (dom/div #js {:className "comment"} 
               (dom/h2 #js {:className "commentAuthor" } (:author data) )
               (dom/p #js {:dangerouslySetInnerHTML #js {:__html (md->html (:children data))}})))))

(defn comment-list [data]
  (reify om/IRender
    (render [_]
      (apply dom/div #js {:className "commentList"}
             (om/build-all comment-component (:comments data))))))


(defn comment-form [_]
  (reify om/IRender
    (render [_]
      (dom/div #js {:className "commentForm"} 
               "Hello, world! I am a CommentForm."))))

(defn comment-box [data owner]
  (reify
    om/IInitState
    (init-state [_] 
      {:data (chan)})
    om/IWillMount
    (will-mount [_]
     (let [data (om/get-state owner :data)]))
    om/IRenderState
    (render-state [this state]
      (dom/div #js {:className "commentBox"}
               (dom/h1 nil "Comments")
               (om/build comment-list data)
               (om/build comment-form nil)))))

(om/root comment-box app-state
         {:target (. js/document (getElementById "app"))})

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )

