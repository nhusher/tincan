(ns tincan.demo
  (:require-macros [tincan.core :refer [draw]])
  (:require [tincan.core :as tin]))

(enable-console-print!)


(def c (js/document.getElementById "c"))

(def ctx (tin/get-context c))

;; (-> ctx
;;     (tin/fill-style "#ccc")
;;     (tin/fill-rect 10 10 10 10)
;;     (tin/fill-rect 20 20 10 10)
;;     (tin/fill-rect 30 30 10 10))

(draw ctx
      (fill-style "#ccc")
      (fill-rect 10 10 10 10))
