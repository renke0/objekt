package com.objekt

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.ZoneId
import java.util.UUID

class ObjektRandomTest {

    @Test
    fun `string should generate random strings with specified length constraints and character pool`() {
        val minLength = 5
        val maxLength = 10
        val charPool = listOf('a', 'b', 'c', 'd', 'e')

        // Generate 10 random strings
        val strings = (1..10).map { ObjektRandom.string(minLength, maxLength, charPool) }

        // Verify that all strings have lengths within the specified constraints
        strings.forEach { string ->
            assertTrue(string.length in minLength..maxLength)
            // Verify that all characters in the string are from the specified character pool
            string.forEach { char ->
                assertTrue(char in charPool)
            }
        }
    }

    @Test
    fun `string with String charPool should generate random strings with specified length constraints and character pool`() {
        val minLength = 5
        val maxLength = 10
        val charPool = "abcde"

        // Generate 10 random strings
        val strings = (1..10).map { ObjektRandom.string(minLength, maxLength, charPool) }

        // Verify that all strings have lengths within the specified constraints
        strings.forEach { string ->
            assertTrue(string.length in minLength..maxLength)
            // Verify that all characters in the string are from the specified character pool
            string.forEach { char ->
                assertTrue(char in charPool)
            }
        }
    }

    @Test
    fun `alphabeticString should generate random strings with only alphabetic characters`() {
        val minLength = 3
        val maxLength = 8

        // Generate 10 random alphabetic strings
        val strings = (1..10).map { ObjektRandom.alphabeticString(minLength, maxLength) }

        // Verify that all strings have lengths within the specified constraints
        strings.forEach { string ->
            assertTrue(string.length in minLength..maxLength)
            // Verify that all characters in the string are alphabetic
            string.forEach { char ->
                assertTrue(char in ('a'..'z') || char in ('A'..'Z'))
            }
        }
    }

    @Test
    fun `alphanumericString should generate random strings with alphanumeric characters`() {
        val minLength = 3
        val maxLength = 8

        // Generate 10 random alphanumeric strings
        val strings = (1..10).map { ObjektRandom.alphanumericString(minLength, maxLength) }

        // Verify that all strings have lengths within the specified constraints
        strings.forEach { string ->
            assertTrue(string.length in minLength..maxLength)
            // Verify that all characters in the string are alphanumeric
            string.forEach { char ->
                assertTrue(char in ('a'..'z') || char in ('A'..'Z') || char in ('0'..'9'))
            }
        }
    }

    @Test
    fun `numericString should generate random strings with only numeric characters`() {
        val minLength = 3
        val maxLength = 8

        // Generate 10 random numeric strings
        val strings = (1..10).map { ObjektRandom.numericString(minLength, maxLength) }

        // Verify that all strings have lengths within the specified constraints
        strings.forEach { string ->
            assertTrue(string.length in minLength..maxLength)
            // Verify that all characters in the string are numeric
            string.forEach { char ->
                assertTrue(char in ('0'..'9'))
            }
        }
    }

    @Test
    fun `uuid should generate valid UUID strings`() {
        // Generate 10 random UUIDs
        val uuids = (1..10).map { ObjektRandom.uuid() }

        // Verify that all UUIDs are unique
        assertEquals(uuids.size, uuids.toSet().size)

        // Verify that all UUIDs are valid
        uuids.forEach { uuidString ->
            assertDoesNotThrow { UUID.fromString(uuidString) }
        }
    }

    @Test
    fun `int should generate random integers within the specified range`() {
        val min = 10
        val max = 20

        // Generate 100 random integers
        val integers = (1..100).map { ObjektRandom.int(min, max) }

        // Verify that all integers are within the specified range
        integers.forEach { integer ->
            assertTrue(integer in min..max)
        }

        // Verify that we get a good distribution of values
        assertTrue(integers.distinct().size > 1)
    }

    @Test
    fun `long should generate random longs within the specified range`() {
        val min = 10L
        val max = 20L

        // Generate 100 random longs
        val longs = (1..100).map { ObjektRandom.long(min, max) }

        // Verify that all longs are within the specified range
        longs.forEach { long ->
            assertTrue(long in min..max)
        }

        // Verify that we get a good distribution of values
        assertTrue(longs.distinct().size > 1)
    }

    @Test
    fun `double should generate random doubles within the specified range and precision`() {
        val min = 0.0
        val max = 1.0
        val precision = 2

        // Generate 100 random doubles
        val doubles = (1..100).map { ObjektRandom.double(min, max, precision) }

        // Verify that all doubles are within the specified range
        doubles.forEach { double ->
            assertTrue(double >= min && double <= max)
        }

        // Verify that all doubles have the specified precision
        doubles.forEach { double ->
            val decimalPlaces = double.toString().split(".").getOrNull(1)?.length ?: 0
            assertTrue(decimalPlaces <= precision)
        }
    }

    @Test
    fun `boolean should generate random booleans with the specified probability`() {
        // Generate 1000 random booleans with 100% probability of true
        val allTrue = (1..1000).map { ObjektRandom.boolean(1.0) }
        assertTrue(allTrue.all { it })

        // Generate 1000 random booleans with 0% probability of true
        val allFalse = (1..1000).map { ObjektRandom.boolean(0.0) }
        assertTrue(allFalse.none { it })

        // Generate 1000 random booleans with 50% probability of true
        val halfTrue = (1..1000).map { ObjektRandom.boolean(0.5) }
        val trueCount = halfTrue.count { it }
        // Allow for some statistical variation
        assertTrue(trueCount in 400..600)
    }

    @Test
    fun `localDate should generate random dates within the specified range`() {
        val minDate = LocalDate.of(2020, 1, 1)
        val maxDate = LocalDate.of(2020, 12, 31)

        // Generate 100 random dates
        val dates = (1..100).map { ObjektRandom.localDate(minDate, maxDate) }

        // Verify that all dates are within the specified range
        dates.forEach { date ->
            assertTrue(date >= minDate && date <= maxDate)
        }

        // Verify that we get a good distribution of values
        assertTrue(dates.distinct().size > 1)
    }

    @Test
    fun `localTime should generate random times within the specified range`() {
        val minHour = 9
        val maxHour = 17

        // Generate 100 random times
        val times = (1..100).map { ObjektRandom.localTime(minHour, maxHour) }

        // Verify that all times have hours within the specified range
        times.forEach { time ->
            assertTrue(time.hour in minHour..maxHour)
        }

        // Verify that we get a good distribution of values
        assertTrue(times.distinct().size > 1)
    }

    @Test
    fun `localDateTime should generate random date-times within the specified range`() {
        val minDateTime = LocalDateTime.of(2020, 1, 1, 0, 0)
        val maxDateTime = LocalDateTime.of(2020, 12, 31, 23, 59)

        // Generate 100 random date-times
        val dateTimes = (1..100).map { ObjektRandom.localDateTime(minDateTime, maxDateTime) }

        // Verify that all date-times are within the specified range
        dateTimes.forEach { dateTime ->
            assertTrue(dateTime >= minDateTime && dateTime <= maxDateTime)
        }

        // Verify that we get a good distribution of values
        assertTrue(dateTimes.distinct().size > 1)
    }

    @Test
    fun `zonedDateTime should generate random zoned date-times within the specified range`() {
        val zone = ZoneId.systemDefault()
        val minDateTime = ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0), zone)
        val maxDateTime = ZonedDateTime.of(LocalDateTime.of(2020, 12, 31, 23, 59), zone)

        // Generate 100 random zoned date-times
        val dateTimes = (1..100).map { ObjektRandom.zonedDateTime(minDateTime, maxDateTime, zone) }

        // Verify that all zoned date-times are within the specified range
        dateTimes.forEach { dateTime ->
            assertTrue(dateTime >= minDateTime && dateTime <= maxDateTime)
            assertEquals(zone, dateTime.zone)
        }

        // Verify that we get a good distribution of values
        assertTrue(dateTimes.distinct().size > 1)
    }

    @Test
    fun `list should generate random lists with the specified size constraints`() {
        val minSize = 3
        val maxSize = 8
        val generator = { ObjektRandom.int(1, 100) }

        // Generate 10 random lists
        val lists = (1..10).map { ObjektRandom.list(minSize, maxSize, generator) }

        // Verify that all lists have sizes within the specified constraints
        lists.forEach { list ->
            assertTrue(list.size in minSize..maxSize)
        }

        // Verify that the lists contain the expected type of elements
        lists.forEach { list ->
            list.forEach { element ->
                assertTrue(element is Int)
                assertTrue(element in 1..100)
            }
        }
    }

    @Test
    fun `set should generate random sets with the specified size constraints`() {
        val minSize = 3
        val maxSize = 8
        val generator = { ObjektRandom.int(1, 100) }

        // Generate 10 random sets
        val sets = (1..10).map { ObjektRandom.set(minSize, maxSize, generator) }

        // Verify that all sets have sizes within the specified constraints
        sets.forEach { set ->
            assertTrue(set.size in minSize..maxSize)
        }

        // Verify that the sets contain the expected type of elements
        sets.forEach { set ->
            set.forEach { element ->
                assertTrue(element is Int)
                assertTrue(element in 1..100)
            }
        }
    }

    @Test
    fun `map should generate random maps with the specified size constraints`() {
        val minSize = 3
        val maxSize = 8
        val keyGenerator = { ObjektRandom.string(5, 10) }
        val valueGenerator = { ObjektRandom.int(1, 100) }

        // Generate 10 random maps
        val maps = (1..10).map { ObjektRandom.map(minSize, maxSize, keyGenerator, valueGenerator) }

        // Verify that all maps have sizes within the specified constraints
        maps.forEach { map ->
            assertTrue(map.size in minSize..maxSize)
        }

        // Verify that the maps contain the expected types of keys and values
        maps.forEach { map ->
            map.forEach { (key, value) ->
                assertTrue(key is String)
                assertTrue(key.length in 5..10)
                assertTrue(value is Int)
                assertTrue(value in 1..100)
            }
        }
    }

    @Test
    fun `oneOf should pick a random element from the given collection`() {
        val collection = listOf("a", "b", "c", "d", "e")

        // Pick 100 random elements
        val elements = (1..100).map { ObjektRandom.oneOf(collection) }

        // Verify that all elements are from the collection
        elements.forEach { element ->
            assertTrue(element in collection)
        }

        // Verify that we get a good distribution of values
        assertTrue(elements.distinct().size > 1)
    }

    @Test
    fun `oneOf with varargs should pick a random element from the given array`() {
        // Pick 100 random elements
        val elements = (1..100).map { ObjektRandom.oneOf("a", "b", "c", "d", "e") }

        // Verify that all elements are from the array
        elements.forEach { element ->
            assertTrue(element in listOf("a", "b", "c", "d", "e"))
        }

        // Verify that we get a good distribution of values
        assertTrue(elements.distinct().size > 1)
    }
}
