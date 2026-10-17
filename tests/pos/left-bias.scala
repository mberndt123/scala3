//> using options -Yexplicit-nulls
import language.experimental.errorHandling


// Higher-kinded type inference treats `Maybe` as if its type parameters were
// in reverse order: matching `F[_]` against `T ? E` infers `F := [X] =>> X ? E`,
// not `[X] =>> T ? X`, and matching `F[_, _]` against `T ? E` infers
// `F := [X, Y] =>> Y ? X`, so that the error type comes first, as for `Either`.

def foo[F[_]](x: F[Int]): F[Boolean] = ???

def bar(x: Int ? String) =
  val y = foo(x)
  val _: Boolean ? String = y

// Optional types `T?`, i.e. `T ? Null`
def optional(x: Int?) =
  val y = foo(x)
  val _: Boolean? = y

// A fixed error type that is itself a type application
def nestedError(x: Int ? List[String]) =
  val _: Boolean ? List[String] = foo(x)

// The value type can be a type application too
def nestedValue(x: List[Int] ? String) =
  def baz[F[_], A](x: F[List[A]]): F[A] = ???
  val _: Int ? String = baz(x)

// Nested maybe types: the outer `Maybe` is abstracted
def nestedMaybe(x: (Int ? String) ? Boolean) =
  def baz[F[_], A](x: F[A]): F[List[A]] = ???
  val _: List[Int ? String] ? Boolean = baz(x)

// Covariant type constructor parameter
def fooCov[F[+_]](x: F[Int]): F[Boolean] = ???

def cov(x: Int ? String) =
  val _: Boolean ? String = fooCov(x)

// Type constructor parameter with an upper bound on its argument
def fooBounded[F[_ <: AnyVal]](x: F[Int]): F[Boolean] = ???

def bounded(x: Int ? String) =
  val _: Boolean ? String = fooBounded(x)

// Result type is inferred from the argument's element type
def extract[F[_], A](x: F[A]): A = ???

def extracted(x: Int ? String) =
  val _: Int = extract(x)

// Several arguments sharing the same inferred type constructor
def zip[F[_], A, B](x: F[A], y: F[B]): F[(A, B)] = ???

def zipped(x: Int ? String, y: Boolean ? String) =
  val _: (Int, Boolean) ? String = zip(x, y)

// Type classes over the value type
trait Functor[F[_]]:
  extension [A](x: F[A]) def fmap[B](f: A => B): F[B]

given maybeFunctor: [E] => Functor[[X] =>> X ? E]:
  extension [A](x: A ? E) def fmap[B](f: A => B): B ? E = x.map(f)

def fmapTwice[F[_]: Functor, A](x: F[A])(f: A => A): F[A] =
  x.fmap(f).fmap(f)

def functor(x: Int ? String, y: Int?) =
  val _: Int ? String = fmapTwice(x)(_ + 1)
  val _: Int? = fmapTwice(y)(_ + 1)
  val _: String ? String = x.fmap(_.toString)

trait Monad[F[_]] extends Functor[F]:
  def pure[A](x: A): F[A]
  extension [A](x: F[A]) def bind[B](f: A => F[B]): F[B]
  extension [A](x: F[A]) def fmap[B](f: A => B): F[B] = x.bind(a => pure(f(a)))

given maybeMonad: [E] => Monad[[X] =>> X ? E]:
  def pure[A](x: A): A ? E = Ok(x)
  extension [A](x: A ? E) def bind[B](f: A => B ? E): B ? E = x.flatMap(f)

def sequence[F[_], A](xs: List[F[A]])(using m: Monad[F]): F[List[A]] =
  xs.foldRight(m.pure(Nil: List[A])): (x, acc) =>
    x.bind(a => acc.bind(as => m.pure(a :: as)))

def sequenced(xs: List[Int ? String]) =
  val _: List[Int] ? String = sequence(xs)

// An alias for a maybe type with a fixed error type
type Result[+T] = T ? String

def alias(x: Result[Int]) =
  val _: Result[Boolean] = foo(x)
  val _: Boolean ? String = foo(x)

// Explicit type arguments can still abstract over the error type
def explicitRight(x: String ? Int) =
  val _: String ? Boolean = foo[[X] =>> String ? X](x)

// A two-parameter type constructor sees the error type first
def foo2[F[_, _]](x: F[String, Int]): F[String, Boolean] = ???

def twoParams(x: Int ? String) =
  val _: Boolean ? String = foo2(x)

// Type classes over both the error and the value type
trait Bifunctor[F[_, _]]:
  extension [A, B](x: F[A, B]) def bimap[C, D](f: A => C, g: B => D): F[C, D]

given maybeBifunctor: Bifunctor[[E, T] =>> T ? E]:
  extension [A, B](x: B ? A) def bimap[C, D](f: A => C, g: B => D): D ? C =
    x.map(g).mapErr(f)

given eitherBifunctor: Bifunctor[Either]:
  extension [A, B](x: Either[A, B]) def bimap[C, D](f: A => C, g: B => D): Either[C, D] =
    x.left.map(f).map(g)

def bimapTwice[F[_, _]: Bifunctor, A, B](x: F[A, B])(f: A => A, g: B => B): F[A, B] =
  x.bimap(f, g).bimap(f, g)

def bifunctor(x: Int ? String, y: Either[String, Int], z: Int?) =
  val _: Int ? String = bimapTwice(x)(_ ++ "!", _ + 1)
  val _: Either[String, Int] = bimapTwice(y)(_ ++ "!", _ + 1)
  val _: Int? = bimapTwice(z)(identity, _ + 1)
  val _: Boolean ? Int = x.bimap(_.length, _ > 0)

// A functor over the value type, derived from a two-parameter type constructor
def rightMap[F[_, _], E, A, B](x: F[E, A])(f: A => B)(using Functor[[X] =>> F[E, X]]): F[E, B] =
  x.fmap(f)

def derived(x: Int ? String) =
  val _: Boolean ? String = rightMap(x)(_ > 0)

// Aliases keep their declared parameter order
type Res[+E, +T] = T ? E

def errorFirstAlias(x: Res[String, Int]) =
  val _: Res[String, Int] = bimapTwice(x)(_ ++ "!", _ + 1)

// Other type constructors keep right bias
def either(x: Either[String, Int]) =
  val _: Either[String, Boolean] = foo(x)

def map(x: Map[String, Int]) =
  val _: Map[String, Boolean] = foo(x)
