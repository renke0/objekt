package com.objekt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import java.time.LocalTime
import java.time.ZoneId

class RandomPlusTest {

  @RepeatedTest(10)
  fun `nextLong should return value within range`() {
    val range = 10L..20L
    val result = RandomPlus.nextLong(range)
    assertTrue(result in range, "Generated long should be within the specified range")
  }

  @RepeatedTest(10)
  fun `nextFloat with until should return value less than until`() {
    val until = 10.0f
    val result = RandomPlus.nextFloat(until)
    assertTrue(result >= 0.0f && result < until, "Generated float should be between 0 and until")
  }

  @RepeatedTest(10)
  fun `nextFloat with from and until should return value within range`() {
    val from = 5.0f
    val until = 10.0f
    val result = RandomPlus.nextFloat(from, until)
    assertTrue(result >= from && result < until, "Generated float should be between from and until")
  }

  @RepeatedTest(10)
  fun `nextFloat with range should return value within range`() {
    val range = 5.0f..10.0f
    val result = RandomPlus.nextFloat(range)
    assertTrue(result in range, "Generated float should be within the specified range")
  }

  @RepeatedTest(10)
  fun `nextBoolean with default probability should return both true and false`() {
    // This test might occasionally fail due to randomness, but with 10 repetitions it's unlikely
    val results = (1..100).map { RandomPlus.nextBoolean() }
    assertTrue(
        results.contains(true) && results.contains(false),
        "Generated booleans should include both true and false")
  }

  @Test
  fun `nextBoolean with probability 1 should always return true`() {
    val results = (1..10).map { RandomPlus.nextBoolean(1.0) }
    assertTrue(results.all { it }, "All generated booleans should be true with probability 1.0")
  }

  @Test
  fun `nextBoolean with probability 0 should always return false`() {
    val results = (1..10).map { RandomPlus.nextBoolean(0.0) }
    assertTrue(results.none { it }, "All generated booleans should be false with probability 0.0")
  }

  @Test
  fun `nextBoolean should throw exception for invalid probability`() {
    assertThrows(IllegalArgumentException::class.java) { RandomPlus.nextBoolean(-0.1) }
    assertThrows(IllegalArgumentException::class.java) { RandomPlus.nextBoolean(1.1) }
  }

  @RepeatedTest(10)
  fun `nextLocalTime should return value within default range`() {
    val result = RandomPlus.nextLocalTime()
    assertTrue(result in TIME_RANGE, "Generated LocalTime should be within the default range")
  }

  @RepeatedTest(10)
  fun `nextLocalTime with until should return value within range`() {
    val until = LocalTime.of(12, 0)
    val result = RandomPlus.nextLocalTime(until)
    assertTrue(
        result >= TIME_RANGE.start && result <= until,
        "Generated LocalTime should be between start of day and specified until time")
  }

  @RepeatedTest(10)
  fun `nextLocalTime with from and until should return value within range`() {
    val from = LocalTime.of(10, 0)
    val until = LocalTime.of(12, 0)
    val result = RandomPlus.nextLocalTime(from, until)
    assertTrue(
        result in from..until,
        "Generated LocalTime should be between specified from and until times")
  }

  @RepeatedTest(10)
  fun `nextInstant should return value within default range`() {
    val result = RandomPlus.nextInstant()
    assertTrue(result in INSTANT_RANGE, "Generated Instant should be within the default range")
  }

  @RepeatedTest(10)
  fun `nextLocalDate should return value within default range`() {
    val result = RandomPlus.nextLocalDate()
    assertTrue(result in DATE_RANGE, "Generated LocalDate should be within the default range")
  }

  @RepeatedTest(10)
  fun `nextLocalDateTime should return value within default range`() {
    val result = RandomPlus.nextLocalDateTime()
    assertTrue(
        result in DATE_TIME_RANGE, "Generated LocalDateTime should be within the default range")
  }

  @RepeatedTest(10)
  fun `nextZonedDateTime should return value with specified zone`() {
    val zone = ZoneId.of("Europe/Paris")
    val result = RandomPlus.nextZonedDateTime(zone)
    assertEquals(zone, result.zone, "Generated ZonedDateTime should have the specified zone")
  }
}
