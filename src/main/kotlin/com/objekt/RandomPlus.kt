package com.objekt

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime
import kotlin.random.Random

val INSTANT_RANGE: ClosedRange<Instant> =
    Instant.parse("1970-01-01T00:00:00Z")..Instant.parse("2100-12-31T23:59:59Z")
val DATE_TIME_RANGE: ClosedRange<LocalDateTime> =
    INSTANT_RANGE.map { it.atZone(UTC).toLocalDateTime() }
val DATE_RANGE: ClosedRange<LocalDate> = INSTANT_RANGE.map { it.atZone(UTC).toLocalDate() }

val TIME_RANGE = LocalTime.of(0, 0, 0)..LocalTime.of(23, 59, 59)

object RandomPlus : Random() {
  override fun nextBits(bitCount: Int): Int = Random.nextBits(bitCount)

  // Long
  fun nextLong(range: LongRange): Long = nextLong(range.first, range.last + 1)

  // Char
  fun nextChar(): Char = nextChar(Char.MIN_VALUE..Char.MAX_VALUE)

  fun nextChar(until: Char): Char = nextChar(Char.MIN_VALUE..until)

  fun nextChar(from: Char, until: Char): Char = nextChar(from..until)

  fun nextChar(range: ClosedRange<Char>): Char {
    val start = range.start.code
    val end = range.endInclusive.code
    return (nextInt(start, end + 1)).toChar()
  }

  // Float
  fun nextFloat(until: Float): Float = nextFloat() * until

  fun nextFloat(from: Float, until: Float): Float = nextFloat() * (until - from) + from

  fun nextFloat(range: ClosedRange<Float>): Float = nextFloat(range.start, range.endInclusive)

  // Boolean
  fun nextBoolean(probability: Double = 0.5): Boolean {
    require(probability in 0.0..1.0) { "Probability must be between 0.0 and 1.0" }
    return nextDouble() < probability
  }

  // LocalTime
  fun nextLocalTime(): LocalTime = nextLocalTime(TIME_RANGE)

  fun nextLocalTime(until: LocalTime): LocalTime = nextLocalTime(TIME_RANGE.start..until)

  fun nextLocalTime(from: LocalTime, until: LocalTime): LocalTime = nextLocalTime(from..until)

  fun nextLocalTime(range: ClosedRange<LocalTime>): LocalTime {
    val startSeconds = range.start.toSecondOfDay()
    val endSeconds = range.endInclusive.toSecondOfDay()
    return LocalTime.ofSecondOfDay(nextLong(startSeconds.toLong(), endSeconds.toLong() + 1))
  }

  // Instant
  fun nextInstant(): Instant = nextInstant(INSTANT_RANGE)

  fun nextInstant(until: Instant): Instant = nextInstant(from = INSTANT_RANGE.start, until = until)

  fun nextInstant(from: Instant, until: Instant): Instant = nextInstant(from..until)

  fun nextInstant(range: ClosedRange<Instant>): Instant {
    val startEpoch = range.start.epochSecond
    val endEpoch = range.endInclusive.epochSecond
    val randomEpoch = nextLong(startEpoch, endEpoch)
    return Instant.ofEpochSecond(randomEpoch)
  }

  // LocalDate
  fun nextLocalDate(): LocalDate = nextLocalDate(DATE_RANGE)

  fun nextLocalDate(until: LocalDate): LocalDate = nextLocalDate(DATE_RANGE.start..until)

  fun nextLocalDate(from: LocalDate, until: LocalDate): LocalDate = nextLocalDate(from..until)

  fun nextLocalDate(range: ClosedRange<LocalDate>): LocalDate {
    val startEpochDay = range.start.toEpochDay()
    val endEpochDay = range.endInclusive.toEpochDay()
    val randomEpochDay = nextLong(startEpochDay, endEpochDay + 1)
    return LocalDate.ofEpochDay(randomEpochDay)
  }

  // LocalDateTime
  fun nextLocalDateTime(): LocalDateTime = nextLocalDateTime(DATE_TIME_RANGE)

  fun nextLocalDateTime(until: LocalDateTime): LocalDateTime =
      nextLocalDateTime(DATE_TIME_RANGE.start..until)

  fun nextLocalDateTime(from: LocalDateTime, until: LocalDateTime): LocalDateTime =
      nextLocalDateTime(from..until)

  fun nextLocalDateTime(range: ClosedRange<LocalDateTime>): LocalDateTime =
      nextInstant(range.map { it.atZone(UTC).toInstant() }).atZone(UTC).toLocalDateTime()

  // ZonedDateTime
  fun nextZonedDateTime(zone: ZoneId = UTC): ZonedDateTime =
      nextZonedDateTime(DATE_TIME_RANGE.map { it.atZone(zone) }, zone)

  fun nextZonedDateTime(until: ZonedDateTime, zone: ZoneId = until.zone): ZonedDateTime =
      nextZonedDateTime(DATE_TIME_RANGE.start.atZone(zone)..until, zone)

  fun nextZonedDateTime(
      from: ZonedDateTime,
      until: ZonedDateTime,
      zone: ZoneId = from.zone
  ): ZonedDateTime = nextZonedDateTime(from..until, zone)

  fun nextZonedDateTime(
      range: ClosedRange<ZonedDateTime>,
      zone: ZoneId = range.start.zone
  ): ZonedDateTime = nextInstant(range.map { it.toInstant() }).atZone(zone)
}

private fun <T : Comparable<T>, R : Comparable<R>> ClosedRange<T>.map(
    fn: (T) -> R
): ClosedRange<R> = fn(start)..fn(endInclusive)
