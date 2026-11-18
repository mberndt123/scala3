// Under -source:future-migration -rewrite, leading infix operators after a block
// are moved into the block, so that they keep their meaning under -source:future.

def c = false

def indented =
  if c then
    1
  else
    2
  + 100

def indentedChain =
  if c then
    1
  else
    2
  + 100
  * 3
  - foo(
      4)

def foo(x: Int) = x

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

def valRhs =
  val x =
    1
  + 100
  x

@main def Test =
  println(List(indented, indentedChain, braces, bracesChain, valRhs))
