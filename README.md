# BTC Lisp

Compile lisp to Bitcoin Script.

# Very General Roadmap

## Low-Level Concerns

* testing
 * ~~using bitcoinj to get result values for scripts~~
 * ~~write a generator for all sorts of valid code~~
 
* actually "running" these transactions
 * don't much much about this, but probably most important
 * start to consider size limit and how best to incorporate into language
 * also consider simple contracts and actually write some

* if expressions
 * will almost certainly include generics/templates

* crypto primitives + operations
 * primitive stuff will be easy
 * will probably extend generics from above to =
 * how to encode these keys into some form bitcoinj can use

## Real Language Stuff

* symbol binding
 * scoping oh boy
 * work into type checker

* function invocation 
 * size limit considerations from above become very important
 * hindley-milner for typing???
 * closures

* non-primitive value types
 * start w/ cons
 * foldl is probably other requirement

## License

Copyright © 2015 Michael Marsh

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
