(ns minesweeper-saas.game)

(def blank-tile "" #{"hidden" 0})

(defn increment-number [current]
  (case current
    0 1
    1 2
    2 3
    3 4
    4 5
    5 6
    6 7
    7 8
    "<error>"))

(defn tile-number [tile]
  (cond (contains? tile 0) 0
        (contains? tile 1) 1
        (contains? tile 2) 2
        (contains? tile 3) 3
        (contains? tile 4) 4
        (contains? tile 5) 5
        (contains? tile 6) 6
        (contains? tile 7) 7
        (contains? tile 8) 8
        :else "<error>"))

(defn add-tag [tile tag]
  (conj (set tile) tag))

(defn remove-tag [tile tag]
  (disj (set tile) tag))

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
      (recur (update tiles index #(conj % "mine"))
             (rest indexes)
             (first indexes))
      tiles)))

(defn index->xy [index width]
  (let [x (mod index width)
        y (/ (- index x) width)]
    {:x x, :y y}))

(defn xy->index [x y width]
  (+ (* y width) x))

(defn relative-index
  "Returns the index relative to the given index, offset by dx and dy,
  or :out-of-bounds if the offset not within the range."
  [index width height dx dy]
  (let [{:keys [x y]} (index->xy index width)
        dest-x (+ x dx)
        dest-y (+ y dy)
        dest-index (xy->index dest-x dest-y width)]
    (if (and (>= dest-x 0) (< dest-x width) (>= dest-y 0) (< dest-y height))
      dest-index
      :out-of-bounds)))

(defn update-relative-tile
  "Applies f to the tile relative to the given position
  or nil if out of bounds"
  [tiles index width height dx dy f]
  (let [dest-index (relative-index index width height dx dy)]
    (if (= dest-index :out-of-bounds)
      tiles
      (update tiles dest-index f))))

(defn increment-tile
  [tile]
  (let [current-number (tile-number tile)
        new-number (increment-number current-number)]
    (-> tile
        (remove-tag current-number)
        (add-tag new-number))))

(defn increment-surrounding
  "Takes an index and increments its surrounding 8 tiles'
  counts, if they are not themselves mines."
  [tiles index width height]
  (-> tiles
      (update-relative-tile index width height -1 -1 increment-tile)   ;; up left
      (update-relative-tile index width height  0 -1 increment-tile)   ;; up
      (update-relative-tile index width height  1 -1 increment-tile)   ;; up right
      (update-relative-tile index width height -1  0 increment-tile)   ;; left
      (update-relative-tile index width height  1  0 increment-tile)   ;; right
      (update-relative-tile index width height -1  1 increment-tile)   ;; down left
      (update-relative-tile index width height  0  1 increment-tile)   ;; down
      (update-relative-tile index width height  1  1 increment-tile))) ;; down right

(defn assign-numbers
  "Increment the tiles that are surrounding miles"
  [tiles width height]
  (let [tile-count (count tiles)]
    (loop [tiles tiles
           index 0]
      (if (>= index tile-count)
        tiles
        (let [tile (set (get tiles index))]
          (if (contains? tile "mine")
            (recur (increment-surrounding tiles index width height)
                   (inc index))
            (recur tiles
                   (inc index))))))))

(defn generate-tiles
  "Create a starting minefield for the given parameters"
  [mine-count width height]
  (let [blank-tiles (vec (repeat (* width height) blank-tile))]
    (-> blank-tiles
        (place-mines mine-count)
        (assign-numbers width height))))

(defn reset
  "Create a starting minefield with predefined parameters"
  []
  (let [mine-count 10
        height     8
        width      8]
    {:tiles  (generate-tiles mine-count width height)
     :height height
     :width  width}))

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
