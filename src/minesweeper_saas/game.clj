(ns minesweeper-saas.game)

(def board-height 8)

(def board-width 8)

(defn cartesian [xs ys]
  (#(for [x %1 y %2] [x y]) xs ys))

(defn generate-mines []
  (let [starting-mines-count 10
        board-x-positions (range 0 board-width)
        board-y-positions (range 0 board-height)]
    (take starting-mines-count
          (shuffle (cartesian board-x-positions
                              board-y-positions)))))

(defn reset []
  {:mines (generate-mines)
   :board-height board-height
   :board-width board-width})

(defn pick [old-state]
  {:pick "as-json"})
