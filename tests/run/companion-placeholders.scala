import scala.language.experimental.companionPlaceholders
import scala.annotation.targetName

enum Color:
  case Red, Green

opaque type Meters = Double
object Meters:
  def apply(d: Double): Meters = d

class Outer:
  class Inner(val x: Int)
  object Inner:
    def make: Inner = new Inner(1)

def f[A](s: Seq[A]): A = s.head
def nested[A](s: Seq[Seq[A]]): A = s.head.head
def g[A](a: A, b: A): A = b
def k[A](z: A)(y: A): A = y

def h(x: List[Int]): String = "h1"
def h(x: List[Int], y: Int): String = "h2"
def h2(x: List[Int]): String = "h2 Int"
@targetName("h2s") def h2(x: List[String]): String = "h2 String"

@main def Test =
  // the expected type is propagated inwards
  val a: Seq[Int] = #(1, 2, 3)
  assert(a == Seq(1, 2, 3))
  val b: Seq[Seq[Int]] = #(#(1), #(2))
  assert(b == Seq(Seq(1), Seq(2)))
  val c: Option[List[Int]] = #(#.empty)
  assert(c == Some(Nil))

  // types are propagated outwards
  val d = f(#(1, 2, 3))
  val d1: Int = d
  assert(d1 == 1)
  val e = nested(#(#("x")))
  val e1: String = e
  assert(e1 == "x")
  val l1: List[Int] = g(List(1), #(2))
  assert(l1 == List(2))
  val l2: List[Int] = k(List(1))(#.empty)
  assert(l2 == Nil)

  // `#` binds like `_`: to the smallest enclosing expression that properly contains it
  val v: Vector[Int] = List(1, 2, 3).to(#)
  assert(v == Vector(1, 2, 3))
  val s: Set[Int] = List(1).to(#) ++ #(2)
  assert(s == Set(1, 2))
  val j: List[Int] = #.from(Vector(1)).map(_ + 1)
  assert(j == List(2))
  val fn1: Int => List[Int] = #(_)
  assert(fn1(1) == List(1))
  val fn2: Int => List[Int] = _ :: #.empty
  assert(fn2(2) == List(2))
  val fn3: Int => List[Int] = x => #(x)
  assert(fn3(3) == List(3))

  // different kinds of companions
  val col: Color = #.Red
  assert(col == Color.Red)
  val m: Meters = #(3.0)
  assert(m == Meters(3.0))
  val o = Outer()
  val i: o.Inner = #.make
  assert(i.x == 1)

  // layout
  val bl: List[Int] =
    println()
    #(1)
  assert(bl == List(1))
  val cond: List[Int] = if bl.isEmpty then #(1) else #.empty
  assert(cond == Nil)

  // overloading
  assert(h(#(1)) == "h1")
  assert(h2(#(1)) == "h2 Int")
  assert(h2(#("a")) == "h2 String")
