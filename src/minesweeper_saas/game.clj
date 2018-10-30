(ns minesweeper-saas.game)

(def blank-tile "" #{:hidden})

(defn random-indexes
  "Generate some random numbers, non-repeating, in a range from 0 to n"
  [how-many n]
  (take how-many (shuffle (range 0 n))))

(defn place-mines
  "Place mines randomly around a tile vector"
  [tiles mine-count]
  (loop [tiles   tiles
         indexes (random-indexes mine-count (count tiles))
         index   (first indexes)]
    (if index
      (recur (update tiles index #(conj % :mine))
             (rest indexes)
             (first indexes))
      tiles)))

(defn assign-numbers
  "Increment each tile's number by surrounding mines"
  [tiles]
  tiles)

(defn generate-tiles
  "Create a starting minefield for the given parameters"
  [mine-count tile-count]
  (let [blank-tiles (vec (repeat tile-count blank-tile))]
    (-> blank-tiles
        (place-mines mine-count)
        assign-numbers)))

(defn reset
  "Create a starting minefield with predefined parameters"
  []
  (let [mine-count 10
        height     8
        width      8]
    {:tiles  (generate-tiles mine-count (* width height))
     :height height
     :width  width}))

(defn add-tag [tile tag]
  (conj (set tile) tag))

(defn remove-tag [tile tag]
  (disj (set tile) tag))

(defn game-over [state]
  (println "do game over")
  state)

(defn clear-fill [index state]
  (println "do clear-fill")
  state)

(defn clear
  "Attempt to either clear tile successfully, or hit a mine.
  Tile must be a hidden tile"
  [index state]
  (let [path-to-tile [:tiles index]
        tile (set (get-in state path-to-tile))]
    (cond
      (contains? tile "mine") (game-over state)
      (contains? tile "hidden") (clear-fill index state)
      :else state)))

(defn flag
  "Toggle the flag of a tile. Tile must be a hidden tile."
  [index state]
  (let [path-to-tile [:tiles index]
        tile (set (get-in state path-to-tile))]
    (cond
      (contains? tile "flag") (update-in state
                                         path-to-tile
                                         #(-> %
                                              (remove-tag "flag")
                                              (add-tag "hidden")))
      (contains? tile "hidden") (update-in state
                                           path-to-tile
                                           #(-> %
                                                (remove-tag "hidden")
                                                (add-tag "flag")))
      :else state)))

(defn apply-pick
  "Determine and carry out the pick of a tile"
  [type index state]
  (case type
    :clear (clear index state)
    :flag (flag index state)
    state))

(defn pick [{:keys [pick-grid-index] :as prev-state}
            type]
  (apply-pick type
              pick-grid-index
              prev-state))
