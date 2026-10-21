//> using options -Yexplicit-nulls
import scala.language.experimental.conditionBindings

def f(it: Iterator[String | Null]): Int =
  var total = 0
  while
    val line = it.next()
    line != null
  do
    total += line.length // `line` is known to be non-null here
  total
