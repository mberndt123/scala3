import scala.language.experimental.controlExprOperands

@main def Test =
  // `if` as the last operand of an infix operation
  assert((1 + if true then 10 else 20) == 11)
  assert((1 + if false then 10 else 20) == 21)
  assert((2 * 3 + if false then 10 else 20) == 26)

  // the `else` branch extends as far to the right as possible
  assert((1 + if false then 10 else 20 + 300) == 321)
  assert((1 + if true then 10 else 20 + 300) == 11)

  // old-style syntax
  assert((1 + if (false) 10 else 20) == 21)

  // as argument
  def f(x: Int, y: Int) = x - y
  assert(f(1 + if true then 1 else 2, 1) == 1)

  // other control expressions
  val opt: Option[Int] = None
  val e = try opt getOrElse throw IllegalStateException("empty") catch case ex: IllegalStateException => ex.getMessage
  assert(e == "empty")
  assert((List(1) ++ for x <- List(2, 3) yield x * 10) == List(1, 20, 30))
  assert((1 + try 2 finally ()) == 3)

  // operator at the end of the line
  val a =
    1 +
      if false then 10 else 20
  assert(a == 21)

  // leading infix operator
  val b =
    1
    + if false then 10 else 20
  assert(b == 21)

  // an indented branch ends with its outdent, so a following leading
  // infix operator applies to the whole conditional
  val c =
    if true then
      10
    else
      1
    + 100
  assert(c == 110)

  val d =
    if false then
      10
    else
      1
    + 100
  assert(d == 101)

  val g = 1 +
    if true then
      10
    else
      20
    + 300
  assert(g == 311)

  // operators following the outdent are subject to the usual precedence rules
  val k = 2 *
    if true then
      10
    else
      20
    + 300
  assert(k == 320)

  // `+ 100` is indented relative to the conditional, so it still belongs to the branch
  val h =
    if true then
      10
    else
      1
      + 100
  assert(h == 10)
