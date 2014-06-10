# Tincan

A simple library that makes interacting with the HTML canvas API a little nicer.

### Transliteration

Canvas properties and functions have been translated to their [kebab-case](https://github.com/qerub/camel-snake-kebab) equivalents. For example, the canvas function `createRadialGradient` can be called with `create-radial-gradient`. Properties have been translated to setter functions. Getter functions are on the way.

It's possible I will rewrite setter functions to use an bang (e.g. `fill-style!`) while getters use the non-exclaimed version. I might also go the prefix route (e.g. `set-fill-style`), or combine the two approaches. Unclear at this point.

### Macro

The easiest way is to use the `draw` macro to get started. Function and property names are described above.

````
(ns tincan.demo
  (:require-macros [tincan.core :refer [draw]]))

(def c (js/document.getElementById "canvas"))
(def ctx (.getContext c "2d"))

(draw ctx
      (fill-style "#ccc")      ;; set the fill style to a light gray
      (fill-rect 10 10 10 10)) ;; draw a rectangle
````

### Functions

For higher-order usage, it might be worthwhile to use the function wrappers instead. There are also a few helper functions for setting various canvas features as well.

The following is equivalent to the above:

````
(ns tincan.demo
  (:require [tincan.core :as tin]))

(def c (js/document.getElementById "canvas"))
(def ctx (tin/get-context c))
  
(-> ctx
    (tin/fill-style "#ccc")
    (tin/fill-rect 10 10 10 10))
````