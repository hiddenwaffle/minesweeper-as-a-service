(ns minesweeper-saas.game)

(defn cartesian [xs ys]
  (#(for [x %1 y %2] [x y]) xs ys))

(defn generate-mines []
  (let [board-height 8
        board-width 8
        starting-mines-count 10
        board-x-positions (range 0 board-width)
        board-y-positions (range 0 board-height)]
    (take starting-mines-count
          (shuffle (cartesian board-x-positions
                              board-y-positions)))))

(defn reset []
  {:mines (generate-mines)})

(defn pick [old-state]
  {:pick "as-json"})
