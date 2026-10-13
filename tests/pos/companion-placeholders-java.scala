import scala.language.experimental.companionPlaceholders

// `#` can refer to the static members of a Java class
def test =
  val d: java.time.DayOfWeek = #.valueOf("MONDAY")
  val t: java.util.concurrent.TimeUnit = #.SECONDS
