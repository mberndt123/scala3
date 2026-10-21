import scala.language.experimental.conditionBindings

@main def Test =
  // the classic loop-and-a-half: read until a sentinel
  val input = Iterator("a", "b", "c", "", "d")
  while
    val line = input.next()
    line.nonEmpty
  do
    println(s"got $line")

  // pattern definitions, multiple definitions and nested defs
  val pairs = Iterator((1, "one"), (2, "two"), (0, "stop"))
  var count = 0
  while
    val (n, name) = pairs.next()
    def describe = s"$n is $name"
    n != 0
  do
    count += 1
    println(describe)
  println(count)

  // the body is executed after the condition is evaluated, each iteration anew
  var i = 0
  while { val j = i * i; i += 1; j < 10 } do println(j)

  // a body of `()` is left alone (old do-while idiom)
  var k = 0
  while { val next = k + 1; k = next; k < 3 } do ()
  println(k)

  // the condition block without definitions is unaffected
  var m = 0
  while { m += 1; m < 3 } do println(m)
