(ns minesweeper-saas.game)

(def empty-tile "" "")
(def mine-tile  "" "mine")

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
      (recur (assoc tiles index mine-tile)
             (rest indexes)
             (first indexes))
      tiles)))

(defn generate-tiles
  "Create a starting minefield for the given parameters"
  [mine-count tile-count]
  (let [empty-tiles (vec (repeat tile-count empty-tile))]
    (-> empty-tiles
        (place-mines mine-count))))

(defn reset
  "Create a starting minefield with predefined parameters"
  []
  (let [mine-count 10
        height     8
        width      8]
    {:tiles  (generate-tiles mine-count (* width height))
     :height height
     :width  width}))

(defn pick [old-state]
  {:pick "as-json"})
