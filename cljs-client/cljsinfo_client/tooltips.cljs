  (ns cljsinfo-client.tooltips
  (:require
    [clojure.set :refer [difference]]
    [cljsinfo-client.dom :refer [by-id set-html! show-el! hide-el!]]
    [cljsinfo-client.util :refer [js-log log uuid]]))

(def $ js/jQuery)

(def has-touch-events? (aget js/window "hasTouchEvents"))

;;------------------------------------------------------------------------------
;; Helper
;;------------------------------------------------------------------------------

(defn- evt->tt-num
  "Returns a tooltip-id from a JS event or nil if the tooltip was not found."
  [js-evt]
  (let [current-target (aget js-evt "currentTarget")
        tooltip-num (.attr ($ current-target) "data-tooltip-id")
        tooltip-id (str "tooltip-" tooltip-num)
        tooltip-el (by-id tooltip-id)]
    (if tooltip-el
      tooltip-id)))

(defn- pinned?
  "Returns whether or not a tooltip is pinned."
  [tt-el]
  (= "true" (.attr ($ tt-el) "data-pinned")))

(defn- pin-down! [tooltip-id]
  ;; TODO: write me
  )

(defn- remove-pin! [tooltip-id]
  ;; TODO: write me
  )

;;------------------------------------------------------------------------------
;; Events
;;------------------------------------------------------------------------------

(defn- on-click [js-evt]
  (if-let [tooltip-id (evt->tt-num js-evt)]
    (if (pinned? (by-id tooltip-id))
      (remove-pin! tooltip-id)
      (pin-down! tooltip-id))))

;; NOTE: this value needs to stay in sync with the .tooltip-53ddee class
;; in /less/main.less
(def max-tooltip-width 360)

(defn- on-mouseenter [js-evt]
  (when-let [tooltip-id (evt->tt-num js-evt)]
    (let [$target-el ($ (aget js-evt "currentTarget"))
          target-height (.height $target-el)
          target-width (.width $target-el)
          target-coords (.offset $target-el)
          target-x (+ (aget target-coords "left") (/ target-width 2))
          target-y (+ (aget target-coords "top") (/ target-height 2))
          browser-width (.width ($ js/window))
          $tooltip-el ($ (str "#" tooltip-id))
          tooltip-width (.width $tooltip-el)
          flip? (> (+ target-x max-tooltip-width 50) browser-width)]
      (.removeClass $tooltip-el "left-arr-42ea1 right-arr-d3345")
      (if flip?
        (.addClass $tooltip-el "right-arr-d3345")
        (.addClass $tooltip-el "left-arr-42ea1"))
      (.css $tooltip-el (js-obj
        "display" ""
        "left" target-x
        "marginLeft" (if flip? (- 0 tooltip-width 30) 18)
        "top" target-y )))))

;; TODO: IE fires the mouseleave event while the mouse cursor is still inside
;; the tooltip icon, causing a very distracting flicker effect.
;; Need to find a fix. Maybe capture the x/y of everything on mouseenter and
;; add a "listenOnce" event on the body for the exit?
;; https://github.com/oakmac/clojurescript.info/issues/11
(defn- on-mouseleave [js-evt]
  (if-let [tooltip-id (evt->tt-num js-evt)]
    (hide-el! tooltip-id)))

(defn- on-touchstart [js-evt]
  nil
  )

;;------------------------------------------------------------------------------
;; Init and Events
;;------------------------------------------------------------------------------

(def tt-link-sel ".tooltip-link-0e91b")

(defn- add-touch-events! []
  (doto ($ "body")
    (.on "touchstart" tt-link-sel on-touchstart)
    ))

(defn init!
  "Initialize tooltip events."
  []
  (doto ($ "body")
    (.on "click" tt-link-sel on-click)
    (.on "mouseenter" tt-link-sel on-mouseenter)
    (.on "mouseleave" tt-link-sel on-mouseleave))
  ; (when has-touch-events?
  ;   (add-touch-events!))
  )