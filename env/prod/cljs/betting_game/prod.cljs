(ns betting-game.prod
  (:require [betting-game.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
