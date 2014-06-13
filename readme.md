# Tincan

A simple ClojureScript library that makes interacting with the (2d) HTML canvas API a little nicer.

### Transliteration

Canvas properties and functions have been translated to their [kebab-case](https://github.com/qerub/camel-snake-kebab) equivalents. For example, the canvas function `createRadialGradient` can be called with `create-radial-gradient`. Canvas context properties (e.g. `fillStyle`) are accessible with getter and setter forms (e.g. `get-fill-style` and `set-fill-style!`). 

### Macro

The easiest way is to use the `draw` macro to get started. Function and property names are described above.

```clojure
(ns tincan.demo
  (:require-macros [tincan.core :refer [draw]]))

(def c (js/document.getElementById "canvas"))
(def ctx (.getContext c "2d"))

(draw ctx
      (set-fill-style! "#ccc")        ;; set the fill style to a light gray
      (fill-rect 10 10 10 10)         ;; draw a rectangle
      (if (= (get-fill-style) "#ccc") ;; do a comparison
        (fill-rect 10 20 10 10)))
```

### Functions

For higher-order usage, it might be worthwhile to use the function wrappers instead. There are also a few helper functions for setting various canvas features as well.

The following is equivalent to the above:

```clojure
(ns tincan.demo
  (:require [tincan.core :as tin]))

(def c (js/document.getElementById "canvas"))
(def ctx (tin/get-context c))

(do
    (tin/set-fill-style! ctx "#ccc")
    (tin/fill-rect ctx 10 10 10 10)
    (if (= (tin/get-fill-style ctx) "#ccc")
      (fill-rect 10 20 10 10)))
  
```

### Todo:

- [ ] Publish to clojars
- [ ] Live example, more/better examples
- [ ] Unit tests
- [ ] Nail down a better get-image-data API
