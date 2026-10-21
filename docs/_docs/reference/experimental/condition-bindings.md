---
layout: doc-page
title: "Condition Bindings"
nightlyOf: https://docs.scala-lang.org/scala3/reference/experimental/condition-bindings.html
---

Definitions in the condition block of an `if` expression or a `while` loop can be made
visible in the branches of the `if` or the body of the loop.
This is enabled with the language import

```scala
import scala.language.experimental.conditionBindings
```

or with the compiler flag `-language:experimental.conditionBindings`.

## Motivation

Many loops need to compute something before they can decide whether to continue, and
then use the computed value in the body. This is known as the _loop-and-a-half_ problem.
Scala 3 already allows arbitrary statements in the condition of a `while` loop, but
definitions made there are not visible in the body, so the usual workaround is a `var`
declared outside the loop:

```scala
var line = in.readLine()
while line != null do
  process(line)
  line = in.readLine()
```

With `conditionBindings`, the loop can be written without the duplicated read and without a `var`:

```scala
while
  val line = in.readLine()
  line != null
do
  process(line)
```

The same applies to `if` expressions, where a value is computed only to be tested and then used:

```scala
if
  val i = s.indexOf(' ')
  i >= 0
then s.substring(0, i)
else s
```

## Rules

If the condition of an `if` expression or a `while` loop is a block that contains
definitions (including pattern definitions and imports), these definitions are in scope
in both branches of the `if`, or in the body of the loop, respectively.

This only applies to the syntax with `then` or `do`. In the old syntax with
parentheses, such as `while ({ val x = f(); x > 0 }) g(x)`, the scope of the
definitions does not extend beyond the closing parenthesis.

An `if` expression

```scala
if { stats; cond } then thenp else elsep
```

is translated to

```scala
{ stats; if cond then thenp else elsep }
```

A `while` loop

```scala
while { stats; cond } do body
```

is translated to

```scala
while { stats; if cond then { body; true } else false } do ()
```

Consequently, the definitions in a loop condition are re-evaluated in every iteration.
Flow typing applies as well: in the example above, under `-Yexplicit-nulls`, `line`
has type `String` in the body if `readLine` returns `String | Null`.

Note that a definition in the condition block shadows an outer definition of the same
name in the branches or the body.
