package scala

import language.experimental.captureChecking

import java.time.*

/** `CanEqual` instances for `java.time` types, inherited by the [[CanEqual$ `CanEqual`]] companion.
 *
 *  They live in a separate trait because `java.time` is not available in the Scala.js javalib,
 *  so the Scala.js version of this trait is empty. The instances are `inline` so that they leave
 *  no trace in the bytecode or IR: code compiled against this version (e.g. on Scala.js with a
 *  `java.time` polyfill) never refers to them at run time, and therefore links against the
 *  empty Scala.js version as well.
 */
private[scala] trait CanEqualJavaTimeInstances {
  inline given canEqualInstant: CanEqual[Instant, Instant] = CanEqual.derived
  inline given canEqualLocalDate: CanEqual[LocalDate, LocalDate] = CanEqual.derived
  inline given canEqualLocalTime: CanEqual[LocalTime, LocalTime] = CanEqual.derived
  inline given canEqualLocalDateTime: CanEqual[LocalDateTime, LocalDateTime] = CanEqual.derived
  inline given canEqualOffsetTime: CanEqual[OffsetTime, OffsetTime] = CanEqual.derived
  inline given canEqualOffsetDateTime: CanEqual[OffsetDateTime, OffsetDateTime] = CanEqual.derived
  inline given canEqualZonedDateTime: CanEqual[ZonedDateTime, ZonedDateTime] = CanEqual.derived
  inline given canEqualYear: CanEqual[Year, Year] = CanEqual.derived
  inline given canEqualYearMonth: CanEqual[YearMonth, YearMonth] = CanEqual.derived
  inline given canEqualMonthDay: CanEqual[MonthDay, MonthDay] = CanEqual.derived
  inline given canEqualJavaDuration: CanEqual[Duration, Duration] = CanEqual.derived
  inline given canEqualPeriod: CanEqual[Period, Period] = CanEqual.derived
  inline given canEqualZoneId: CanEqual[ZoneId, ZoneId] = CanEqual.derived
}
