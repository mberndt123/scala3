//> using options -language:strictEquality

import java.time.*
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.*
import scala.util.Try

def test(
  uuid: java.util.UUID,
  uri: java.net.URI,
  instant: Instant,
  localDate: LocalDate,
  localTime: LocalTime,
  localDateTime: LocalDateTime,
  offsetTime: OffsetTime,
  offsetDateTime: OffsetDateTime,
  zonedDateTime: ZonedDateTime,
  year: Year,
  yearMonth: YearMonth,
  monthDay: MonthDay,
  javaDuration: java.time.Duration,
  period: Period,
  zoneId: ZoneId,
  zoneOffset: ZoneOffset,
  dayOfWeek: DayOfWeek,
  timeUnit: TimeUnit,
  tryInt: Try[Int],
  duration: scala.concurrent.duration.Duration,
  finiteDuration: FiniteDuration,
) =
  uuid == uuid
  uri == uri
  instant == instant
  localDate == localDate
  localTime == localTime
  localDateTime == localDateTime
  offsetTime == offsetTime
  offsetDateTime == offsetDateTime
  zonedDateTime == zonedDateTime
  year == year
  yearMonth == yearMonth
  monthDay == monthDay
  javaDuration == javaDuration
  period == period
  zoneId == zoneOffset
  dayOfWeek == DayOfWeek.MONDAY
  timeUnit != TimeUnit.SECONDS
  dayOfWeek match
    case DayOfWeek.MONDAY => ()
    case _ => ()
  tryInt == Try(1)
  duration == finiteDuration
  duration == scala.concurrent.duration.Duration.Inf
