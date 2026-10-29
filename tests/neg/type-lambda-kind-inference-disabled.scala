case class Kleisli[F[_], A, B](run: A => F[B])
trait FunctorK[T[_[_]]]

def kleisli(x: FunctorK[[F] =>> Kleisli[F, Int, String]]) = () // error
