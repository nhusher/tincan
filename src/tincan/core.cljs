(ns tincan.core
  (:require-macros [tincan.core :as tin]))

(tin/gen-canvas-fns)
(tin/gen-canvas-getters)
(tin/gen-canvas-setters)

(defn canvas? [cvx]
  (= (type cvx) js/HTMLCanvasElement))

(defn- get-canvas [cvx]
  (if (canvas? cvx) cvx (.-canvas cvx)))

(defn get-context [canvas]
  (.getContext canvas "2d"))

(defn set-height! [cvx h]
  (set! (.-height (get-canvas cvx)) h))

(defn set-width! [cvx w]
  (set! (.-width (get-canvas cvx)) w))

(defn set-dimensions! [cvx w h]
  (doto cvx
    (set-height! h)
    (set-width! w)))

(defn get-data [cvx encoding]
  (.toDataUrl (get-canvas cvx) encoding))

(defn canvas [w h]
  (doto (js/document.createElement "canvas")
    (set-dimensions! w h)))
