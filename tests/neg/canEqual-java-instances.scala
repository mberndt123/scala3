//> using options -language:strictEquality

import java.time.*

def test(instant: Instant, localDate: LocalDate, dayOfWeek: DayOfWeek, month: Month, uuid: java.util.UUID) =
  instant == localDate // error
  dayOfWeek == month // error
  uuid == "uuid" // error
  scala.util.Try(1) == scala.util.Try("a") // error
