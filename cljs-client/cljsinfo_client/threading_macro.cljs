(ns cljsinfo-client.threading-macro
  (:require-macros [hiccups.core :as hiccups])
  (:require
    hiccups.runtime
    [cljsinfo-client.dom :refer [by-id set-html! show-el! hide-el!]]
    [cljsinfo-client.util :refer [js-log log uuid]]))

(def $ js/jQuery)

(defn- half [x]
  (/ x 2))

(defn- twice [x]
  (* 2 x))

(def reg-x-unit 40)
(def reg-y-unit (twice reg-x-unit))

(def small-x-unit 10)
(def small-y-unit (twice small-x-unit))

;;------------------------------------------------------------------------------
;; Helpers
;;------------------------------------------------------------------------------

;; TODO: I'm sure this could be combined with the functions below
(defn- max-x-in-frame [f]
  (->> f
       vals
       (remove nil?)
       (map first)
       (apply max)))

(defn- max-y-in-frame [f]
  (->> f
       vals
       (remove nil?)
       (map second)
       (apply max)))

(defn- max-x-value-in-frames [frames]
  (->> frames
       (map vals)
       (apply concat)
       (remove nil?)
       (map first)
       (apply max)))

(defn- max-y-value-in-frames [frames]
  (->> frames
       (map vals)
       (apply concat)
       (remove nil?)
       (map second)
       (apply max)))

(defn- coords->style [x y small?]
  (let [x-unit (if small? small-x-unit reg-x-unit)
        y-unit (if small? small-y-unit reg-y-unit)]
    (str "left: " (* x-unit (dec x)) "px; "
         "top: " (* y-unit (dec y)) "px; "
         "height: " y-unit "px; "
         "width: " x-unit "px; ")))

(defn- grid
  ([x y]
    (grid x y false))
  ([x y small?]
    {:style (coords->style x y small?)}))

;;------------------------------------------------------------------------------
;; Thread First 1
;;------------------------------------------------------------------------------

(hiccups/defhtml thread-first-1-html []
  [:div#line1.line-num (grid -1 1) "1"]
  [:div#line2.line-num (grid -1 2) "2"]
  [:div#line3.line-num (grid -1 3) "3"]

  [:div#openParen1 "("]

  [:div#arrow1.arrow "-"]
  [:div#arrow2.arrow ">"]
  [:div#symbolA "a"]

  [:div#openParen2 "("]
  [:div#symbolB "b"]
  [:div#symbolC "c"]
  [:div#symbolD "d"]
  [:div#closeParen2 ")"]

  [:div#openParen3 "("]
  [:div#symbolX "x"]
  [:div#symbolY "y"]
  [:div#symbolZ "z"]
  [:div#closeParen3 ")"]

  [:div#closeParen1 ")"])

(def thread-first-1 {
  :chars {
    :op1  "("
    :arr1 "-"
    :arr2 ">"
    :a    "a"

    :op2 "("
    :b "b"
    :c "c"
    :d "d"
    :cp2 ")"

    :op3 "("
    :x   "x"
    :y   "y"
    :z   "z"
    :cp3 ")"

    :cp1 ")"
  }
  :frames [
    {
    :op1  [1 1]
    :arr1 [2 1]
    :arr2 [3 1]
    :a    [5 1]

    :op2 [5 2]
    :b   [6 2]
    :c   [8 2]
    :d   [10 2]
    :cp2 [11 2]

    :op3 [5 3]
    :x   [6 3]
    :y   [8 3]
    :z   [10 3]
    :cp3 [11 3]

    :cp1 [12 3]
    }
    {
    :op1  [1 1]
    :arr1 [2 1]
    :arr2 [3 1]
    :a    [5 1]

    :op2 [5 2]
    :b   [6 2]
    :c   [10 2]
    :d   [12 2]
    :cp2 [13 2]

    :op3 [5 3]
    :x   [6 3]
    :y   [8 3]
    :z   [10 3]
    :cp3 [11 3]

    :cp1 [12 3]
    }
    {
    :op1  [1 1]
    :arr1 [2 1]
    :arr2 [3 1]

    :op2 [5 2]
    :b   [6 2]
    :a   [8 2]
    :c   [10 2]
    :d   [12 2]
    :cp2 [13 2]

    :op3 [5 3]
    :x   [6 3]
    :y   [8 3]
    :z   [10 3]
    :cp3 [11 3]

    :cp1 [12 3]
    }
    {
    :op1 [1 1]
    :arr1 [2 1]
    :arr2 [3 1]

    :op2 [5 1]
    :b [6 1]
    :a [8 1]
    :c [10 1]
    :d [12 1]
    :cp2 [13 1]

    :op3 [5 2]
    :x [6 2]
    :y [8 2]
    :z [10 2]
    :cp3 [11 2]

    :cp1 [12 2]
    }
    {
    :op1 [1 1]
    :arr1 [2 1]
    :arr2 [3 1]

    :op2 [5 1]
    :b [6 1]
    :a [8 1]
    :c [10 1]
    :d [12 1]
    :cp2 [13 1]

    :op3 [5 2]
    :x [6 2]
    :y [18 2]
    :z [20 2]
    :cp3 [21 2]

    :cp1 [22 2]
    }
    {
    :op1 [1 1]
    :arr1 [2 1]
    :arr2 [3 1]

    :op2 [8 2]
    :b [9 2]
    :a [11 2]
    :c [13 2]
    :d [15 2]
    :cp2 [16 2]

    :op3 [5 2]
    :x [6 2]
    :y [18 2]
    :z [20 2]
    :cp3 [21 2]

    :cp1 [22 2]
    }
    {
    :op1 nil
    :arr1 nil
    :arr2 nil

    :op2 [8 2]
    :b [9 2]
    :a [11 2]
    :c [13 2]
    :d [15 2]
    :cp2 [16 2]

    :op3 [5 2]
    :x [6 2]
    :y [18 2]
    :z [20 2]
    :cp3 [21 2]

    :cp1 nil
    }
    {
    :op1 nil
    :arr1 nil
    :arr2 nil

    :op3 [1 1]
    :x [2 1]
    :op2 [4 1]
    :b [5 1]
    :a [7 1]
    :c [9 1]
    :d [11 1]
    :cp2 [12 1]
    :y [14 1]
    :z [16 1]
    :cp3 [17 1]

    :cp1 nil
    }
  ]})

;;------------------------------------------------------------------------------
;; Example 2
;;------------------------------------------------------------------------------

(hiccups/defhtml ex2-html []
  [:div#line1.line-num (grid -1 1) "1"]
  [:div#line2.line-num (grid -1 2) "2"]
  [:div#line3.line-num (grid -1 3) "3"]

  [:div#openParen1 "("]

  [:div#arrow1.arrow "-"]
  [:div#arrow2.arrow ">"]

  [:div#symbolM "m"]

  [:div#kwdA1 ":"]
  [:div#kwdA2 "a"]

  [:div#kwdB1 ":"]
  [:div#kwdB2 "b"]

  [:div#kwdC1 ":"]
  [:div#kwdC2 "c"]

  [:div#openParen2 "("]
  [:div#closeParen2 ")"]

  [:div#openParen3 "("]
  [:div#closeParen3 ")"]

  [:div#closeParen1  ")"]
  )

(def ex2-pos1 {
  :op1 [1 1]
  :arr1 [2 1]
  :arr2 [3 1]

  :m [5 1]

  :kwdA1 [7 1]
  :kwdA2 [8 1]

  :kwdB1 [9 1]
  :kwdB2 [10 1]

  :kwdC1 [12 1]
  :kwdC2 [13 1]

  :op2 nil
  :cp2 nil

  :op3 nil
  :cp3 nil

  :cp1 [14 1]
  })

;;------------------------------------------------------------------------------
;; Small Multiples
;;------------------------------------------------------------------------------

(hiccups/defhtml single-char [[k v]]
  [:div.big-char-6b108 {:id (name k)} v])

(hiccups/defhtml big-line-number [n]
  [:div.big-num-c7cf6 (grid -1 n) n])

(hiccups/defhtml big-screen-chars [a]
  (let [frames (:frames a)
        max-y (max-y-value-in-frames frames)
        chars (:chars a)]
    (list
      (map big-line-number (range 1 (inc max-y)))
      (map single-char chars))))

(hiccups/defhtml small-multiple-char [chars [k v]]
  (when v
    [:div.small-char-3142f (grid (first v) (second v) true)
      (k chars)]))

(hiccups/defhtml small-line-number [n]
  [:div.small-num-59b3c (grid -1 n true) n])

(hiccups/defhtml small-multiple [chars frame-idx frame]
  (let [max-x (max-x-in-frame frame)
        width (* max-x small-x-unit)
        max-y (max-y-in-frame frame)
        height (* max-y small-y-unit)]
    [:div.small-ac2ae
      {:id (str "smallMultiple-" frame-idx)
       :style (str "height: " height "px; width: " width "px")}
      [:span.step-e483d (str "Step " (inc frame-idx))]
      (map (partial small-multiple-char chars) frame)
      (map small-line-number (range 1 (inc max-y)))]))

(hiccups/defhtml small-multiples [animation]
  (let [chars (:chars animation)
        frames (:frames animation)
        max-x (max-x-value-in-frames frames)
        width (* max-x reg-x-unit)
        max-y (max-y-value-in-frames frames)
        height (* max-y small-y-unit)]
    (map-indexed (partial small-multiple chars) frames)))

;;------------------------------------------------------------------------------
;; Bubble Links
;;------------------------------------------------------------------------------

(hiccups/defhtml bubble-link [idx frame]
  [:i.fa.fa-circle.bubble-0374c
    {:data-frame-index idx
     :id (str "bubbleLink-" idx)}])

(hiccups/defhtml bubble-links [a]
  (map-indexed bubble-link (:frames a)))

;;------------------------------------------------------------------------------
;; Animation
;;------------------------------------------------------------------------------

(def animation-duration 800)

(defn- animate-single! [[k v]]
  (let [fade-out? (nil? v)
        sel (str "#" (name k))
        $el ($ sel)]
    (.velocity $el
      (if fade-out?
        (js-obj "opacity" 0)
        (js-obj "left" (* reg-x-unit (dec (first v)))
                "opacity" 1
                "top" (* reg-y-unit (dec (second v)))))
      (js-obj "duration" animation-duration))))

(defn- animate-to-position! [pos]
  (doall (map animate-single! pos)))

(defn- set-single! [[k v]]
  (let [fade-out? (nil? v)
        sel (str "#" (name k))
        $el ($ sel)]
    (.css $el
      (if fade-out?
        (js-obj "opacity" 0)
        (js-obj
          "left" (* reg-x-unit (dec (first v)))
          "opacity" 1
          "top" (* reg-y-unit (dec (second v))))))))

(defn- set-position-instant! [pos]
  (doall (map set-single! pos)))

;;------------------------------------------------------------------------------
;; Current Animation
;;------------------------------------------------------------------------------

(def current-animation (atom nil))

(defn- load-animation! [a]
  (set-html! "smallMultiples" (small-multiples a))
  (set-html! "bubblesContainer" (bubble-links a))
  (set-html! "bigScreen" (big-screen-chars a))
  (let [max-y (max-y-value-in-frames (:frames a))
        big-screen-height (+ (* max-y reg-y-unit) (half reg-y-unit))]
    (.height ($ "#bigScreen") big-screen-height)))

(defn- on-change-animation [_kwd _atom old-a new-a]
  (load-animation! new-a))

(add-watch current-animation :change on-change-animation)

;;------------------------------------------------------------------------------
;; Current Frame
;;------------------------------------------------------------------------------

(def current-frame-index (atom 0))
(def active-bubble-class "active-51cf5")
(def small-multiple-animation-speed 100)

(defn- small-code-left [idx frames]
  (let [$el ($ (str "#smallMultiple-" idx))
        js-pos (.position $el)
        left (int (aget js-pos "left"))]
    (+ (* -1 left) 500)))

(defn- position-small-frame! [idx frames]
  (.velocity ($ (str "#smallMultiples"))
    (js-obj "left" (small-code-left idx frames))
    (js-obj "duration" small-multiple-animation-speed)))

(defn- on-change-frame [_kwd _atom old-idx new-idx]
  (let [animation @current-animation
        frames (:frames animation)
        new-frame (nth frames new-idx)]
    ;; toggle bubble classes
    (.removeClass ($ (str "#bubbleLink-" old-idx)) active-bubble-class)
    (.addClass    ($ (str "#bubbleLink-" new-idx)) active-bubble-class)

    ;; position the small multiple frame
    (position-small-frame! new-idx frames)

    ;; animate if the frames are adjacent, else set position instantly
    (animate-to-position! new-frame)

    ))

(add-watch current-frame-index :change on-change-frame)

;;------------------------------------------------------------------------------
;; Events
;;------------------------------------------------------------------------------

(defn- click-bubble [js-evt]
  (let [$current-target ($ (aget js-evt "currentTarget"))
        frame-index (int (.attr $current-target "data-frame-index"))]
    ;; TODO: verify that frame-index is valid here
    (when-not (= frame-index @current-frame-index)
      (reset! current-frame-index frame-index))))

(def events-added? (atom false))

;; NOTE: this is a "run once" function
(defn- add-events! []
  (when-not @events-added?
    (.on ($ "body") "click" ".bubble-0374c" click-bubble)
    (reset! events-added? true)))

;;------------------------------------------------------------------------------
;; Init
;;------------------------------------------------------------------------------

(def time-between 1200)

(defn- round [n]
  (* n (+ animation-duration time-between)))

(defn- animate! []
  ; (set-position-instant! ex1-pos1)
  ; (js/setTimeout #(animate-to-position ex1-pos2) (round 1))
  ; (js/setTimeout #(animate-to-position ex1-pos3) (round 2))
  ; (js/setTimeout #(animate-to-position ex1-pos4) (round 3))
  ; (js/setTimeout #(animate-to-position ex1-pos5) (round 4))
  ; (js/setTimeout #(animate-to-position ex1-pos6) (round 5))
  ; (js/setTimeout #(animate-to-position ex1-pos7) (round 6))
  ; (js/setTimeout #(animate-to-position ex1-pos8) (round 7))

  ; (set-position-instant! ex1-pos6)
  ; (js/setTimeout #(animate-to-position ex1-pos5) (round 2))
  ; (js/setTimeout #(animate-to-position ex1-pos4) (round 3))
  ; (js/setTimeout #(animate-to-position ex1-pos3) (round 4))
  ; (js/setTimeout #(animate-to-position ex1-pos2) (round 5))
  ; (js/setTimeout #(animate-to-position ex1-pos1) (round 6))
  )

(defn init!
  "Initialize the threading macro page."
  []
  (add-events!)
  (reset! current-animation thread-first-1)
  (reset! current-frame-index 0))
