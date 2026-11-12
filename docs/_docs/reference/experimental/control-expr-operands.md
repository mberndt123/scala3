---
layout: doc-page
title: "Control Expressions as Infix Operands"
nightlyOf: https://docs.scala-lang.org/scala3/reference/experimental/control-expr-operands.html
---

This experimental feature allows control expressions (`if`, `while`, `for`, `try` and `throw`)
to be used as the last operand of an infix operation without wrapping them in parentheses.
It is enabled by

```scala
import scala.language.experimental.controlExprOperands
```

or by setting the command line option `-language:experimental.controlExprOperands`.

## Single-line usage

```scala
val x = 1 + if c then a else b
val y = opt getOrElse throw IllegalStateException("empty")
val zs = xs ++ for x <- ys yield x * 2
```

A control expression extends as far to the right as possible. Therefore it can only be the last
operand of an infix operation, and any operators following it belong to its last part:

```scala
1 + if c then a else b + 2    // means: 1 + (if c then a else (b + 2))
```

Use parentheses if a different grouping is intended:

```scala
(1 + if c then a else b) + 2
```

## Indented branches

If a branch of an `if` is an indented block, the branch ends where the indentation ends.
An infix operator that follows on a less indented line applies to the whole conditional,
not just to its last branch:

```scala
val x =
  if c then
    a
  else
    b
  + 2              // means: (if c then a else b) + 2

val y = 1 +
  if c then
    a
  else
    b
  + 2              // means: (1 + (if c then a else b)) + 2
```

Without the language import, `+ 2` in the first example is parsed as part of the `else` branch.

## Syntax changes

```ebnf
InfixExpr  ::=  PrefixExpr
             |  InfixExpr id [nl] InfixExpr
             |  InfixExpr id [nl] ControlExpr
             |  InfixExpr id ColonArgument
             |  InfixExpr MatchClause
ControlExpr ::= [‘inline’] ‘if’ ...  |  ‘while’ ...  |  ‘try’ ...  |  ‘throw’ Expr  |  ForExpr
```

where `ControlExpr` comprises the corresponding alternatives of `Expr1`.
