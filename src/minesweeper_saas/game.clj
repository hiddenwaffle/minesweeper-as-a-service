(ns minesweeper-saas.game)

(def board-height 8)
(def board-width 8)
(def berries 10)

(defn reset []
  {:reset "as-json"})

(defn pick [old-state]
  {:pick "as-json"})
