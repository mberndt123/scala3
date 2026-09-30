package scala

import scala.runtime.{MaybeCase, Valid}
import scala.compiletime.Maybe
import annotation.experimental

@experimental
object Ok:
  inline def apply[T](x: T): Maybe[Nothing, T] = {
    if x == null || x.isInstanceOf[MaybeCase] then new Valid(x)
    else x
  }.asInstanceOf[Maybe[Nothing, T]]

  def unapply(x: Maybe[Any, Any]): x.type = x
