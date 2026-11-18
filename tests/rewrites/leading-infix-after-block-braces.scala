// Under -no-indent -source:future-migration -rewrite, a leading infix operator
// after a closing brace is moved to the line of the closing brace, so that it keeps
// its meaning under -source:future.

object Test {
  def c = false

  def braces =
    if c then {
      1
    } else {
      2
    }
    + 100

  def bracesChain =
    if c then {
      1
    } else {
      2
    }
    + 100
    + 1000

  def valRhs = {
    val x = {
      1
    }
    + 100
    x
  }

  def main(args: Array[String]): Unit =
    println(List(braces, bracesChain, valRhs))
}
