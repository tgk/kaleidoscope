# kaleidoscope

![Odili Donald Odita: Kaleidoscope](http://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/Odili_NB-A_%287704409802%29.jpg/800px-Odili_NB-A_%287704409802%29.jpg)
["Kaleidoscope" by Odili Donald Odita](http://commons.wikimedia.org/wiki/File:Odili_NB-A_(7704409802).jpg)

kaleidoscope is a library offering
[predicate dispatch](http://en.wikipedia.org/wiki/Predicate_dispatch)
operators to Clojure programmers.

kaleidoscope offers:

- Extensible operators, which is effectively a generalisation of
  multimethods.
- Recursion within the same operator matching predicate defined
  downstream.
- Run-time creation of generic operators.
- Unlike muiltimethods, kaleidoscope operators are not bound to
  namespaces.

The price is that predicate logic is stored as meta-data on the generic
operator. For a further discussion of the tradeoffs selected in this
implementation,
[please see this blog post](http://tgk.github.io/2013/06/generic-operator-predicate-dispatch-semantics-in-clojure.html).

## Usage

```clojure
(require '[kaleidoscope :refer (generic-operator assign-operation)])

(let [plus (generic-operator +)]
  (doto plus
    (assign-operation concat vector? vector?))
  [(plus 1 2)
   (plus [1 2 3] [4 5])])
;; => [3 (1 2 3 4 5)]
```

For a more advanced example, please see `example.clj`.

## License

Copyright © 2014 Thomas G. Kristensen

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
