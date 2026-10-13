---
layout: doc-page
title: "Companion Placeholders"
nightlyOf: https://docs.scala-lang.org/scala3/reference/experimental/companion-placeholders.html
---

Companion placeholders are enabled by the language import
```scala
import scala.language.experimental.companionPlaceholders
```
With this import, `#` can be used in an expression as a placeholder for the
companion object of the expected type of that expression:
```scala
val xs: Vector[Int] = #(1, 2, 3)       // Vector(1, 2, 3)
val ys: List[Int] = #.empty            // List.empty
val c: Color = #.Red                   // Color.Red
```

## Scoping

A `#` placeholder is bound exactly like an underscore `_` in an
[anonymous function placeholder](https://scala-lang.org/files/archive/spec/3.4/06-expressions.html#placeholder-syntax-for-anonymous-functions):
it refers to the companion of the expected type of the smallest enclosing
expression that properly contains it. In other words, if
```scala
val x: T.type => T = e
```
compiles, where `e` contains an underscore placeholder, then
```scala
val x: T = e'
```
compiles as well, where `e'` is `e` with the underscore replaced by `#`. For instance:
```scala
val v: Vector[Int] = List(1, 2, 3).to(#)   // List(1, 2, 3).to(Vector)
val w: List[Int] = #.from(xs).map(_ + 1)   // List.from(xs).map(_ + 1)
```
Each argument of a method call is an expression of its own, so placeholders
in nested arguments refer to the companions of the respective parameter types:
```scala
val xss: Seq[Seq[Int]] = #(#(1), #(2))     // Seq(Seq(1), Seq(2))
```
If `_` and `#` placeholders occur in the same expression, `#` refers to the
companion of the result type of the anonymous function:
```scala
val f: Int => List[Int] = #(_)             // x => List(x)
```

## Type Inference

The expected type does not need to be fully known. For instance, in
```scala
def f[A](s: Seq[A]): A = s.head
val x = f(#(1, 2, 3))
```
the expected type of the argument is `Seq[A]` for some yet unknown `A`, which
is enough to determine that `#` refers to `Seq`. Typing `Seq(1, 2, 3)` then
determines that `A` is `Int`. If the expected type is an uninstantiated type
variable, its upper bound is used to determine the companion. If that does not
determine a companion, the type variable is instantiated, in the same way as for
the parameter types of anonymous functions.

When a method is overloaded, a `#` placeholder argument can be typed if all
alternatives agree on the companion that the placeholder refers to.

## Companions

The companion of the expected type is determined as follows:

 - Type aliases are dealiased.
 - Classes and opaque type aliases refer to their companion object. For Java classes,
   this is the object containing the static members of the class.
 - Prefixes are kept: if the expected type is `p.C`, `#` refers to `p.C`'s companion
   as seen from `p`.
 - Type parameters and abstract types, union and intersection types, as well as
   `Any`, `AnyVal` and classes without a companion object are rejected. The only
   exception are unions of the form `T | Null`, which are treated like `T`.
