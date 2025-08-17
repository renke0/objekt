package com.objekt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

class ObjektRandomTest {

  @RepeatedTest(10)
  fun `string should generate random string with default configuration`() {
    // Use the companion object
    val result = ObjektRandom.string()

    // Verify the string is not empty and has expected length
    assertNotNull(result)
    assertTrue(result.length in 1..10, "Default string length should be between 1 and 10")

    // Verify the string contains only expected characters
    val allowedChars = ('a'..'z').toSet() + ('A'..'Z').toSet() + ('0'..'9').toSet()
    result.forEach { char ->
      assertTrue(
          char in allowedChars,
          "Generated string should only contain characters from default char pool")
    }
  }

  @RepeatedTest(10)
  fun `string should use custom length pool`() {
    // Create a custom length pool
    val fixedLength = 15
    val result = ObjektRandom.string(length = { exactly = fixedLength })

    // Verify the string has the expected length
    assertEquals(
        fixedLength, result.length, "Generated string should have length from custom length pool")
  }

  @RepeatedTest(10)
  fun `string should use custom char pool`() {
    // Create a custom char pool
    val allowedChars = setOf('x', 'y', 'z')
    val result = ObjektRandom.string(chars = { oneOf = allowedChars })

    // Verify the string contains only characters from the custom pool
    result.forEach { char ->
      assertTrue(
          char in allowedChars,
          "Generated string should only contain characters from custom char pool")
    }
  }

  @Test
  fun `ObjektRandom constructor should accept configuration builder`() {
    // Create a custom ObjektRandom with configuration
    val objektRandom = ObjektRandom {
      stringLength { exactly = 20 }
      char { oneOf = setOf('a', 'b', 'c') }
    }

    // Generate a string using the custom configuration
    val result = objektRandom.string()

    // Verify the string has the expected length
    assertEquals(20, result.length, "Generated string should have length from custom configuration")

    // Verify the string contains only characters from the custom pool
    result.forEach { char ->
      assertTrue(
          char in setOf('a', 'b', 'c'),
          "Generated string should only contain characters from custom configuration")
    }
  }

  @Test
  fun `PoolBuilder should create unique pool when exactly is set`() {
    val builder = PoolBuilder<Int>().apply { exactly = 5 }
    val pool = builder.pool()

    assertNotNull(pool)
    repeat(10) {
      assertEquals(5, pool!!.random(), "Unique pool should always return the same value")
    }
  }

  @Test
  fun `PoolBuilder should create range pool when between is set`() {
    val range = 1..10
    val builder = PoolBuilder<Int>().apply { between = range }
    val pool = builder.pool()

    assertNotNull(pool)
    repeat(10) {
      val result = pool!!.random()
      assertTrue(result in range, "Range pool should return a value within the specified range")
    }
  }

  @Test
  fun `PoolBuilder should create oneOf pool when oneOf is set`() {
    val values = listOf("a", "b", "c")
    val builder = PoolBuilder<String>().apply { oneOf = values }
    val pool = builder.pool()

    assertNotNull(pool)
    repeat(10) {
      val result = pool!!.random()
      assertTrue(result in values, "OneOf pool should return one of the specified values")
    }
  }

  @Test
  fun `PoolBuilder should create oneOfMultiple pool when iterables is set`() {
    val iterables = listOf(listOf("a", "b"), listOf("c", "d"))
    val builder = PoolBuilder<String>().apply { this.iterables = iterables }
    val pool = builder.pool()

    assertNotNull(pool)
    repeat(10) {
      val result = pool!!.random()
      assertTrue(
          result in listOf("a", "b", "c", "d"),
          "OneOfMultiple pool should return one of the values from any of the iterables")
    }
  }

  @Test
  fun `PoolBuilder should throw exception when multiple properties are set`() {
    // Test exactly + between
    val builder1 =
        PoolBuilder<Int>().apply {
          exactly = 5
          between = 1..10
        }
    assertThrows(IllegalArgumentException::class.java) { builder1.pool() }

    // Test exactly + oneOf
    val builder2 =
        PoolBuilder<String>().apply {
          exactly = "a"
          oneOf = listOf("a", "b", "c")
        }
    assertThrows(IllegalArgumentException::class.java) { builder2.pool() }

    // Test between + iterables
    val builder3 =
        PoolBuilder<Int>().apply {
          between = 1..10
          iterables = listOf(listOf(1, 2), listOf(3, 4))
        }
    assertThrows(IllegalArgumentException::class.java) { builder3.pool() }
  }

  @Test
  fun `ObjektRandomConfigBuilder should build config with defaults when nothing is specified`() {
    val builder = ObjektRandomConfigBuilder()
    val config = builder.build()

    // Verify the default values
    assertNotNull(config.stringLength)
    assertNotNull(config.chars)

    // Test the default string length pool
    val lengths = (1..10).map { config.stringLength.random() }
    lengths.forEach { length ->
      assertTrue(length in 1..10, "Default string length should be between 1 and 10")
    }

    // Test the default char pool
    val allowedChars = ('a'..'z').toSet() + ('A'..'Z').toSet() + ('0'..'9').toSet()
    val chars = (1..10).map { config.chars.random() }
    chars.forEach { char ->
      assertTrue(char in allowedChars, "Default char pool should include letters and digits")
    }
  }

  @Test
  fun `ObjektRandomConfigBuilder should build config with custom values`() {
    val builder = ObjektRandomConfigBuilder()

    // Configure custom values
    builder.stringLength { exactly = 15 }
    builder.char { oneOf = setOf('x', 'y', 'z') }

    val config = builder.build()

    // Test the custom string length pool
    repeat(10) {
      assertEquals(15, config.stringLength.random(), "Custom string length should be exactly 15")
    }

    // Test the custom char pool
    val allowedChars = setOf('x', 'y', 'z')
    repeat(10) {
      val char = config.chars.random()
      assertTrue(char in allowedChars, "Custom char pool should only include specified characters")
    }
  }
}
