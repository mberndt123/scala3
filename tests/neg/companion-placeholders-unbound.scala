import scala.language.experimental.companionPlaceholders

def test =
  val a: List[Int] = #                          // error: unbound placeholder
