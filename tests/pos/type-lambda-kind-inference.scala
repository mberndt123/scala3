import scala.language.experimental.typeLambdaKindInference

case class Kleisli[F[_], A, B](run: A => F[B])
trait FunctorK[T[_[_]]]
trait BiK[T[_[_], _]]
trait Nested[T[_[_[_]], _]]
trait Poly[T <: AnyKind]
trait Bounded[T[_[X <: Int]]]

def kleisli(x: FunctorK[[F] =>> Kleisli[F, Int, String]]) = ()
def mixed(x: BiK[[F, A] =>> Kleisli[F, A, String]]) = ()
def nested(x: Nested[[G, A] =>> G[List]]) = ()
def applied(x: FunctorK[[F] =>> F[Int]]) = ()

// Explicit kinds are still accepted
def explicit(x: FunctorK[[F[_]] =>> Kleisli[F, Int, String]]) = ()

// Bounds of inner parameters are propagated along with the kind
def bounded(x: Bounded[[F] =>> F[Int]]) = ()

// No inference against AnyKind: the parameter keeps the simple kind
def poly(x: Poly[[A] =>> List[A]]) = ()
