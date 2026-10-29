import scala.language.experimental.typeLambdaKindInference

case class Kleisli[F[_], A, B](run: A => F[B])
trait FunctorK[T[_[_]]]
trait Simple[T[_]]

// No expected type: parameters keep the simple kind
type K = [F] =>> Kleisli[F, Int, String] // error

// Arity mismatch: no inference
def arity(x: FunctorK[[F, A] =>> Kleisli[F, A, String]]) = () // error

// Simple kind expected: no inference
def simple(x: Simple[[F] =>> Kleisli[F, Int, String]]) = () // error

// Inferred kind does not fit the body
def wrong(x: FunctorK[[F] =>> F]) = () // error
