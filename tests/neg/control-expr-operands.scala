// without the language import, control expressions are not allowed as infix operands
def test(c: Boolean) =
  val x = 1 + if c then 1 else 2 // error
  x
