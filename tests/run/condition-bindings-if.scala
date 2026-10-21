import scala.language.experimental.conditionBindings

def classify(s: String): String =
  if
    val n = s.toIntOption
    n.isDefined
  then s"number ${n.get}"
  else s"not a number: ${s.length} chars, n = $n"

def firstWord(s: String): String =
  if { val i = s.indexOf(' '); i >= 0 } then s.substring(0, i) else s

// the condition of an else-if can have its own bindings
def sign(x: Int): String =
  if { val neg = x < 0; neg } then "negative"
  else if { val pos = x > 0; pos } then s"positive (pos = $pos)"
  else "zero"

// definitions are evaluated exactly once
var evaluations = 0
def counted(): Int = { evaluations += 1; evaluations }

inline def inlineIf: String =
  inline if { val two = 2; true } then "inline" else "never"

@main def Test =
  println(classify("42"))
  println(classify("foo"))
  println(firstWord("hello world"))
  println(firstWord("single"))
  println(sign(-1))
  println(sign(1))
  println(sign(0))
  val r = if { val c = counted(); c > 0 } then c else -c
  println(s"$r $evaluations")
  println(inlineIf)
