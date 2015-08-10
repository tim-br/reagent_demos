(ns betting-game.todo
  (:require [reagent.core :as reagent :refer [atom]]))

(def list-of-items
  (atom {:items [{:item "program in clojure"} {:item "finish web app"} {:item "solve euler problems"}]}))

#_(def upcoming-item (atom {:item "google me"}))

(def upcoming-item (atom "google me"))


(defn display-item [i]
  [:li
   [:span i]])


(defn display-all []
  [:div
   [:ul
    (for [ i (:items @list-of-items)]
      (display-item  (:item  i)))]])

(defn insert-to-list []
  ;;(swap! {:items @list-of-items} :items conj (:item @upcoming-item))
  #_(swap! list-of-items conj {:item @upcoming-item})
  #_(swap! list-of-items update :items conj {:item @upcoming-item})
  (swap! list-of-items update :items conj {:item @upcoming-item})
  (js/alert @list-of-items))

(defn new-item-form []
  [ :div
   [:form
    [:div.form-group
     [:br]
     [:label "What do you need to put on your to-do list?" [:br]]
     [:input
      {:type :text
       :on-change #( do
                     (reset! upcoming-item (-> % .-target .-value))
                     (js/console.log @upcoming-item)
                     #_(js/console.log (-> % .-target .-value)))}]

     [:button.btn.btn-primary {:on-click (fn [event] (do
                                                      (.preventDefault event)
                                                      (js/console.log @upcoming-item)
                                                      (insert-to-list)))}
      "submit your item"]]]])
