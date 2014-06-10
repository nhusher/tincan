(ns tincan.core
  (require [clojure.string :refer [split join capitalize]]))

;;
;; Lifted nearly-verbatim from Om's dom macros
;;

(def ^:private canvas-functions
  '[ arc
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
     translate])

(def ^:private canvas-properties
  '[ fill-style
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
     text-baseline ])

(defn ^:private camel-caseify [s]
  (let [[f & r] (split (name s) #"-")]
    (apply str f (map capitalize r))))

(defn ^:private gen-canvas-fn [cv-fn]
  `(defn ~(symbol cv-fn) [ ctx# & args# ]
     (.apply (. ctx# ~(symbol (str "-" (camel-caseify (name cv-fn))))) ctx# (to-array args#))
     ctx#))

(defn ^:private gen-canvas-prop [cv-prop]
  `(defn ~(symbol cv-prop) [ ctx# val# ]
     (set! (. ctx# ~(symbol (str "-" (camel-caseify (name cv-prop)))))
           val#)
     ctx#))

(defmacro ^:private gen-canvas-fns []
  `(do ~@(map gen-canvas-fn canvas-functions)))

(defmacro ^:private gen-canvas-props []
  `(do ~@(map gen-canvas-prop canvas-properties)))

