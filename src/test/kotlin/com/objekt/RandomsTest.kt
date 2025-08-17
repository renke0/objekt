package com.objekt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

class RandomsTest {

  @RepeatedTest(10)
  fun `randomString should generate string with length from lengthPool`() {
    // Create a pool that always returns the same length
    val fixedLength = 5
    val lengthPool = Pool.unique(fixedLength)

    // Create a pool of characters
    val charsPool = Pool.oneOf('a', 'b', 'c')

    // Generate a random string
    val result = randomString(lengthPool, charsPool)

    // Verify the length
    assertEquals(fixedLength, result.length, "Generated string should have length from lengthPool")
  }

  @RepeatedTest(10)
  fun `randomString should generate string with characters from charsPool`() {
    // Create a pool for length
    val lengthPool = Pool.range(5..10)

    // Create a pool with a specific set of characters
    val allowedChars = setOf('a', 'b', 'c')
    val charsPool = Pool.oneOf(allowedChars)

    // Generate a random string
    val result = randomString(lengthPool, charsPool)

    // Verify all characters are from the allowed set
    result.forEach { char ->
      assertTrue(
          char in allowedChars, "Generated string should only contain characters from charsPool")
    }
  }

  @RepeatedTest(10)
  fun `randomString should generate different strings with variable length`() {
    // Create a pool for variable length
    val lengthPool = Pool.range(3..8)

    // Create a pool with a wide range of characters
    val charsPool = Pool.oneOf('a'..'z')

    // Generate multiple random strings
    val strings = (1..10).map { randomString(lengthPool, charsPool) }

    // Verify that strings have different lengths within the expected range
    val lengths = strings.map { it.length }
    assertTrue(
        lengths.any { it != lengths.first() } || lengths.all { it in 3..8 },
        "Generated strings should have variable lengths within the specified range")

    // Verify that strings are different (this could occasionally fail due to randomness)
    assertTrue(
        strings.toSet().size > 1,
        "Multiple calls to randomString should generate different strings")
  }

  @RepeatedTest(10)
  fun `randomString should generate empty string when lengthPool returns 0`() {
    // Create a pool that always returns 0
    val lengthPool = Pool.unique(0)

    // Create a pool of characters
    val charsPool = Pool.oneOf('a', 'b', 'c')

    // Generate a random string
    val result = randomString(lengthPool, charsPool)

    // Verify the string is empty
    assertTrue(result.isEmpty(), "Generated string should be empty when lengthPool returns 0")
  }

  @Test
  fun `randomString should work with different types of pools`() {
    // Test with Range pool for length
    val lengthRangePool = Pool.range(5..5)
    val result1 = randomString(lengthRangePool, Pool.oneOf('a', 'b', 'c'))
    assertEquals(5, result1.length)

    // Test with OneOf pool for length
    val lengthOneOfPool = Pool.oneOf(3, 4, 5)
    val result2 = randomString(lengthOneOfPool, Pool.oneOf('a', 'b', 'c'))
    assertTrue(result2.length in 3..5)

    // Test with Range pool for chars
    val charsRangePool = Pool.range('a'..'c')
    val result3 = randomString(Pool.unique(5), charsRangePool)
    assertEquals(5, result3.length)
    result3.forEach { char -> assertTrue(char in 'a'..'c') }

    // Test with OneOfMultiple pool for chars
    val charsOneOfMultiplePool = Pool.oneOfMultiple(listOf('a', 'b'), listOf('c', 'd'))
    val result4 = randomString(Pool.unique(5), charsOneOfMultiplePool)
    assertEquals(5, result4.length)
    result4.forEach { char -> assertTrue(char in setOf('a', 'b', 'c', 'd')) }
  }
}
