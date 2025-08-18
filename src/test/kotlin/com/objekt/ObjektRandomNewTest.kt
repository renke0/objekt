package com.objekt

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ObjektRandomNewTest {

  @Test
  fun `boolean should generate random boolean`() {
    val result = ObjektRandom.boolean()
    assertNotNull(result)
  }

  @Test
  fun `long should generate random long`() {
    val result = ObjektRandom.long()
    assertNotNull(result)
    assertTrue(result in 0L..100L, "Default long should be between 0 and 100")
  }

  @Test
  fun `float should generate random float`() {
    val result = ObjektRandom.float()
    assertNotNull(result)
    assertTrue(result in 0.0f..100.0f, "Default float should be between 0.0 and 100.0")
  }

  @Test
  fun `double should generate random double`() {
    val result = ObjektRandom.double()
    assertNotNull(result)
    assertTrue(result in 0.0..100.0, "Default double should be between 0.0 and 100.0")
  }

  @Test
  fun `localTime should generate random LocalTime`() {
    val result = ObjektRandom.localTime()
    assertNotNull(result)
  }

  @Test
  fun `localDate should generate random LocalDate`() {
    val result = ObjektRandom.localDate()
    assertNotNull(result)
  }

  @Test
  fun `localDateTime should generate random LocalDateTime`() {
    val result = ObjektRandom.localDateTime()
    assertNotNull(result)
  }

  @Test
  fun `instant should generate random Instant`() {
    val result = ObjektRandom.instant()
    assertNotNull(result)
  }

  @Test
  fun `zonedDateTime should generate random ZonedDateTime`() {
    val result = ObjektRandom.zonedDateTime()
    assertNotNull(result)
  }
}
