(ns tincan.core
  (require [clojure.string :as s :refer [split join capitalize]]
           [clojure.walk :refer [postwalk]]))

(def ^:private canvas-functions
  '#{arc
     arc-to
     begin-path
     bezier-curve-to
     clear-rect
     clip
     close-path
     create-linear-gradient
     create-pattern
     create-radial-gradient
     draw-image
     draw-custom-focus-ring
     draw-system-focus-ring
     fill
     fill-rect
     get-image-data
     get-line-dash
     is-point-in-path
     is-point-in-stroke
     line-to
     measure-text
     move-to
     put-image-data
     quadratic-curve-to
     rect
     restore
     rotate
     save
     scale
     scroll-path-into-view
     set-line-dash
     set-transform
     stroke
     stroke-rect
     stroke-text
     transform
     translate})

(def ^:private canvas-properties
  '#{fill-style
     font
     global-alpha
     global-composite-operation
     line-cap
     line-dash-offset
     line-join
     line-width
     miter-limit
     shadow-blur
     shadow-offset-x
     shadow-offset-y
     stroke-style
     text-align
     text-baseline })

;;
;; TODO: these functions are really gross.
;;
(defn- setter? [s] (and
                    (contains? canvas-properties (symbol (s/replace (join "-" (rest (split (name s) #"-"))) "!" "")))
                    (= "set" (first (split (name s) #"-")))
                    (= \! (last (name s)))))

(defn- getter? [s] (and
                    (contains? canvas-properties (symbol (join "-" (rest (split (name s) #"-")))))
                    (= "get" (first (split (name s) #"-")))))

(defn- prop-fn? [s] (or (getter? s) (setter? s)))

(defn- to-js-name
  "Translates a clojure-style function name into the relevant JS property
   name. It strips 'get-', 'set-' and '!' from the name before translation.

   e.g. 'get-fill-style' tranlates to '-fillStyle' or 'set-stroke-style!' to
   '-strokeStyle'"

  [s] (let [[f & r] (split (name s) #"-")]
        (if (prop-fn? s)
          (to-js-name (s/replace (join "-" r) "!" ""))
          (apply str f (map capitalize r)))))

(defn- prop-name [s] (symbol (str "-" (to-js-name (name s)))))

(defn- gen-canvas-fn [cv-fn]
  `(defn ~(symbol cv-fn) [ ctx# & args# ]
     (.apply (. ctx# ~(prop-name cv-fn)) ctx# (to-array args#))
     ctx#))

(defn- gen-canvas-setter [cv-prop]
  `(defn ~(symbol (str "set-" cv-prop "!")) [ ctx# val# ]
     (set! (. ctx# ~(prop-name cv-prop)) val#)
     ctx#))

(defn- gen-canvas-getter [cv-prop]
  `(defn ~(symbol (str "get-" cv-prop)) [ ctx# ]
     (. ctx# ~(prop-name cv-prop))))

;;
;; TODO: I might be able to mash these together a little more nicely.
;;
(defmacro ^:private gen-canvas-fns []
  `(do ~@(map gen-canvas-fn canvas-functions)))

(defmacro ^:private gen-canvas-setters []
  `(do ~@(map gen-canvas-setter canvas-properties)))

(defmacro ^:private gen-canvas-getters []
  `(do ~@(map gen-canvas-getter canvas-properties)))

(defn- translate-fn [ctx form]
  (if (seq? form)
    (let [[f & args] form
          fs (prop-name f)]
      (cond
       (setter? f) `(set! (. ~ctx ~fs) ~(first args))
       (getter? f) `(. ~ctx ~fs)
       (contains? canvas-functions f) `(.call (. ~ctx ~fs) ~ctx ~@args)
       :else form))

    form))

(defmacro draw [ ctx# & forms# ]
  `(do ~@(map #(postwalk (partial translate-fn ctx#) %) forms#)))
