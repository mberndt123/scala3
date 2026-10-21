def withoutImport =
  var i = 0
  while
    val j = i
    i += 1
    j < 10
  do
    println(j) // error

  if { val k = i; k > 0 } then k else 0 // error

def parensDoNotExtendScope =
  import scala.language.experimental.conditionBindings
  var i = 0
  while ({ val j = i; i += 1; j < 10 }) println(j) // error
  if ({ val k = i; k > 0 }) k else 0 // error
