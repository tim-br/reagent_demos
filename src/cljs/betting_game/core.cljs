(ns betting-game.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType])
    (:import goog.History))

(def total-money (atom 100))
(def current-guess (atom 0))
(def current-bet (atom 0))

;; -------------------------
;; Views

(defn check-bet [bet-amount]
  (let [result (rand-int 5)]
    (js/console.log result)
    (js/console.log  bet-amount)
    (if (= result (int bet-amount))
      (do
        (js/alert "you win")
        (reset! total-money (+ (int @total-money)  (int @current-bet))))
      (do
        (reset! total-money (- (int @total-money) (int @current-bet)))
        (js/alert "you lose")))))

(defn bet-form []
  [ :div
   [:form
    [:div.form-group
     [:label "Enter how much money you would like to bet here" [:br]]
     [:input
      {:type :text
       :on-change #( do
                     (reset! current-bet (-> % .-target .-value))
                     (if (< ( int @current-bet) 0)
                       (js/alert "must be greater than 0")))}]
     [:br]
     [:label "Enter a guess here (choose a number between 1 and 5)" [:br]]
     [:input
      {:type :text
       :on-change #( do
                     (reset! current-guess (-> % .-target .-value))
                     (js/console.log @current-guess)
                     (js/console.log (-> % .-target .-value)))}]

     [:button.btn.btn-primary {:on-click (fn [event] (do
                                                      (.preventDefault event)
                                                      (check-bet @current-guess)))}
      "place a bet"]]]])

(defn home-page []
  [:div [:h2 "Welcome to the betting game"]
   [:div [:h4 "You have a total of " @total-money " dollars"]]
   [bet-form]
   #_(js/alert (check-bet 30))
   [:div [:a {:href "#/about"} "go to about page"]]])

(defn about-page []
  [:div [:h2 "About betting_game"]
   [:p "Contact me at t.williams.im@gmail.com"]
   [:div [:a {:href "#/"} "go to the home page"]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (mount-root))
