//> using options -Yexplicit-nulls -Xkind-projector:underscores
import language.experimental.errorHandling

// Under -Xkind-projector:underscores, placeholders in maybe types are numbered in the order
// in which `Maybe` is applied, i.e. error type first. So `_ ? _` is the
// `Maybe` type constructor itself and `_ ? E` is `[T] =>> T ? E`.

trait Functor[F[_]]:
  extension [A](x: F[A]) def fmap[B](f: A => B): F[B]

trait Bifunctor[F[_, _]]:
  extension [A, B](x: F[A, B]) def bimap[C, D](f: A => C, g: B => D): F[C, D]

given maybeBifunctor: Bifunctor[_ ? _]:
  extension [A, B](x: B ? A) def bimap[C, D](f: A => C, g: B => D): D ? C =
    x.map(g).mapErr(f)

given maybeFunctor: [E] => Functor[_ ? E]:
  extension [A](x: A ? E) def fmap[B](f: A => B): B ? E = x.map(f)

given optionalFunctor: Functor[_?]:
  extension [A](x: A?) def fmap[B](f: A => B): B? = x.map(f)

def fmapTwice[F[_]: Functor, A](x: F[A])(f: A => A): F[A] =
  x.fmap(f).fmap(f)

def bimapTwice[F[_, _]: Bifunctor, A, B](x: F[A, B])(f: A => A, g: B => B): F[A, B] =
  x.bimap(f, g).bimap(f, g)

def test(x: Int ? String, y: Int?) =
  val _: Int ? String = bimapTwice(x)(_ ++ "!", _ + 1)
  val _: Int ? String = fmapTwice(x)(_ + 1)
  val _: Int? = fmapTwice(y)(_ + 1)
  val _: Bifunctor[compiletime.Maybe] = maybeBifunctor
  val _: Functor[[T] =>> T ? String] = maybeFunctor[String]
  val _: Functor[[T] =>> T?] = optionalFunctor

// A placeholder on the error side only
def errorSide(x: Int ? String) =
  def keep[F[_]](y: F[String]): F[String] = y
  val _: Int ? String = keep[Int ? _](x)
