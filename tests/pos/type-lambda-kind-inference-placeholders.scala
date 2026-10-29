//> using options -Xkind-projector:underscores
import scala.language.experimental.typeLambdaKindInference

case class Kleisli[F[_], A, B](run: A => F[B])
trait FunctorK[T[_[_]]]
trait BiK[T[_[_], _]]

def kleisli(x: FunctorK[Kleisli[_, Int, String]]) = ()
def mixed(x: BiK[Kleisli[_, _, String]]) = ()
