package com.objekt

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

class PoolTest {

  @Test
  fun `unique pool should always return the same value`() {
    val value = "test"
    val pool = Pool.unique(value)

    // Test multiple calls to random() return the same value
    repeat(10) {
      assertEquals(value, pool.random(), "Unique pool should always return the same value")
    }
  }

  @RepeatedTest(10)
  fun `range pool with IntRange should return value within range`() {
    val range = 1..10
    val pool = Pool.range(range)

    val result = pool.random()
    assertTrue(result in range, "Range pool should return a value within the specified range")
  }

  @RepeatedTest(10)
  fun `range pool with LongRange should return value within range`() {
    val range = 1L..10L
    val pool = Pool.range(range)

    val result = pool.random()
    assertTrue(result in range, "Range pool should return a value within the specified range")
  }

  @RepeatedTest(10)
  fun `range pool with CharRange should return value within range`() {
    val range = 'a'..'z'
    val pool = Pool.range(range)

    val result = pool.random()
    assertTrue(result in range, "Range pool should return a value within the specified range")
  }

  @RepeatedTest(10)
  fun `range pool with DoubleRange should return value within range`() {
    val range = 1.0..10.0
    val pool = Pool.range(range)

    val result = pool.random()
    assertTrue(result in range, "Range pool should return a value within the specified range")
  }

  @RepeatedTest(10)
  fun `range pool with FloatRange should return value within range`() {
    val range = 1.0f..10.0f
    val pool = Pool.range(range)

    val result = pool.random()
    assertTrue(result in range, "Range pool should return a value within the specified range")
  }

  @RepeatedTest(10)
  fun `range pool with LocalTimeRange should return value within range`() {
    val range = LocalTime.of(10, 0)..LocalTime.of(12, 0)
    val pool = Pool.range(range)

    val result = pool.random()
    assertTrue(result in range, "Range pool should return a value within the specified range")
  }

  @RepeatedTest(10)
  fun `range pool with InstantRange should return value within range`() {
    val range = Instant.parse("2020-01-01T00:00:00Z")..Instant.parse("2020-12-31T23:59:59Z")
    val pool = Pool.range(range)

    val result = pool.random()
    assertTrue(result in range, "Range pool should return a value within the specified range")
  }

  @RepeatedTest(10)
  fun `range pool with LocalDateRange should return value within range`() {
    val range = LocalDate.of(2020, 1, 1)..LocalDate.of(2020, 12, 31)
    val pool = Pool.range(range)

    val result = pool.random()
    assertTrue(result in range, "Range pool should return a value within the specified range")
  }

  @RepeatedTest(10)
  fun `range pool with LocalDateTimeRange should return value within range`() {
    val range = LocalDateTime.of(2020, 1, 1, 0, 0)..LocalDateTime.of(2020, 12, 31, 23, 59)
    val pool = Pool.range(range)

    val result = pool.random()
    assertTrue(result in range, "Range pool should return a value within the specified range")
  }

  @RepeatedTest(10)
  fun `range pool with ZonedDateTimeRange should return value within range`() {
    val zone = ZoneId.of("UTC")
    val range =
        ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, zone)..ZonedDateTime.of(
                2020, 12, 31, 23, 59, 59, 0, zone)
    val pool = Pool.range(range)

    val result = pool.random()
    assertTrue(result in range, "Range pool should return a value within the specified range")
  }

  @Test
  fun `range pool should throw exception for invalid range`() {
    assertThrows(IllegalArgumentException::class.java) {
      @Suppress("EmptyRange")
      Pool.range(10..1) // End is less than start
    }
  }

  @RepeatedTest(10)
  fun `oneOf pool should return one of the specified values`() {
    val values = listOf("a", "b", "c")
    val pool = Pool.oneOf(values)

    val result = pool.random()
    assertTrue(result in values, "OneOf pool should return one of the specified values")
  }

  @RepeatedTest(10)
  fun `oneOf pool with varargs should return one of the specified values`() {
    val pool = Pool.oneOf("a", "b", "c")

    val result = pool.random()
    assertTrue(
        result in listOf("a", "b", "c"), "OneOf pool should return one of the specified values")
  }

  @Test
  fun `oneOf pool should throw exception for empty collection`() {
    assertThrows(IllegalArgumentException::class.java) { Pool.oneOf(emptyList<String>()) }
  }

  @RepeatedTest(10)
  fun `oneOfMultiple pool should return one of the values from any of the iterables`() {
    val iterables = listOf(listOf("a", "b"), listOf("c", "d"))
    val pool = Pool.oneOfMultiple(iterables)

    val result = pool.random()
    assertTrue(
        result in listOf("a", "b", "c", "d"),
        "OneOfMultiple pool should return one of the values from any of the iterables")
  }

  @RepeatedTest(10)
  fun `oneOfMultiple pool with varargs should return one of the values from any of the iterables`() {
    val pool = Pool.oneOfMultiple(listOf("a", "b"), listOf("c", "d"))

    val result = pool.random()
    assertTrue(
        result in listOf("a", "b", "c", "d"),
        "OneOfMultiple pool should return one of the values from any of the iterables")
  }

  @Test
  fun `oneOfMultiple pool should throw exception for empty collection`() {
    assertThrows(IllegalArgumentException::class.java) {
      Pool.oneOfMultiple(emptyList<List<String>>())
    }
  }
}
