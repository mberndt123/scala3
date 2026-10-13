import scala.language.experimental.companionPlaceholders

class NoCompanion
def g[A](a: A): A = a
def h(x: List[Int]): Int = 1
def h(x: Vector[Int]): Int = 2

def test[A] =
  val a = #(1)                                  // error: no expected type
  val b = g(#(1))                               // error: unconstrained type parameter
  val c: Int | String = #.MaxValue              // error: union
  val d: A = #.foo                              // error: type parameter
  val e: Any = #.foo                            // error
  val f: AnyVal = #.foo                         // error
  val i: NoCompanion = #.foo                    // error
  val j: java.time.DayOfWeek = identity(#)      // error: Java statics are not a value
  val k = h(#(1))                               // error: alternatives disagree on companion
  val l: AnyRef = #.foo                         // error: not a member of Object's statics
