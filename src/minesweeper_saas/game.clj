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

(defn generate-tiles
  "Create a starting minefield for the given parameters"
  [mine-count tile-count]
  (let [blank-tiles (vec (repeat tile-count blank-tile))]
    (-> blank-tiles
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

(defn clear [index state] state)

(defn flag [index
            {:keys [tiles] :as state}]
  (println state)
  (let [tile (set (tiles index))]
    (println tile)
    (if (contains? tile "hidden")
      (let [updated-tile (-> tile
                             (disj "hidden")
                             (conj "flag"))]
        (assoc state
               :tiles
               (assoc tiles index updated-tile)))
      state)))

(defn apply-pick
  "Determine and carry out the pick of a tile"
  [type index state]
  (case type
    :clear (clear index state)
    :flag (flag index state)
    state))

(defn pick [{:keys [pick-grid-index] :as prev-state}
            type]
  (apply-pick type pick-grid-index prev-state))
