package com.objekt

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.random.Random

/**
 * Utility class containing functions for generating random values of various types.
 * All functions are configurable with parameters to control the range, length, or
 * other characteristics of the generated values.
 */
object ObjektRandom {

    // String generation

    /**
     * Generates a random string with the specified length constraints and character pool.
     *
     * @param minLength The minimum length of the generated string (inclusive)
     * @param maxLength The maximum length of the generated string (inclusive)
     * @param charPool The pool of characters to use for generating the string
     * @return A random string with length between minLength and maxLength
     */
    fun string(
        minLength: Int = 5,
        maxLength: Int = 20,
        charPool: List<Char> = ('a'..'z').toList() + ('A'..'Z').toList() + ('0'..'9').toList()
    ): String {
        val length = Random.nextInt(minLength, maxLength + 1)
        return (1..length)
            .map { charPool.random() }
            .joinToString("")
    }

    /**
     * Generates a random string with the specified length constraints and character pool.
     *
     * @param minLength The minimum length of the generated string (inclusive)
     * @param maxLength The maximum length of the generated string (inclusive)
     * @param charPool The pool of characters as a string to use for generating the string
     * @return A random string with length between minLength and maxLength
     */
    fun string(
        minLength: Int = 5,
        maxLength: Int = 20,
        charPool: String
    ): String {
        return string(minLength, maxLength, charPool.toList())
    }

    /**
     * Generates a random alphabetic string (a-z, A-Z).
     *
     * @param minLength The minimum length of the generated string (inclusive)
     * @param maxLength The maximum length of the generated string (inclusive)
     * @return A random alphabetic string
     */
    fun alphabeticString(minLength: Int = 5, maxLength: Int = 20): String {
        return string(minLength, maxLength, ('a'..'z').toList() + ('A'..'Z').toList())
    }

    /**
     * Generates a random alphanumeric string (a-z, A-Z, 0-9).
     *
     * @param minLength The minimum length of the generated string (inclusive)
     * @param maxLength The maximum length of the generated string (inclusive)
     * @return A random alphanumeric string
     */
    fun alphanumericString(minLength: Int = 5, maxLength: Int = 20): String {
        return string(minLength, maxLength, ('a'..'z').toList() + ('A'..'Z').toList() + ('0'..'9').toList())
    }

    /**
     * Generates a random numeric string (0-9).
     *
     * @param minLength The minimum length of the generated string (inclusive)
     * @param maxLength The maximum length of the generated string (inclusive)
     * @return A random numeric string
     */
    fun numericString(minLength: Int = 5, maxLength: Int = 10): String {
        return string(minLength, maxLength, ('0'..'9').toList())
    }

    /**
     * Generates a random UUID string.
     *
     * @return A random UUID string
     */
    fun uuid(): String = UUID.randomUUID().toString()

    // Number generation

    /**
     * Generates a random integer within the specified range.
     *
     * @param min The minimum value (inclusive)
     * @param max The maximum value (inclusive)
     * @return A random integer between min and max
     */
    fun int(min: Int = 0, max: Int = 100): Int {
        return Random.nextInt(min, max + 1)
    }

    /**
     * Generates a random long within the specified range.
     *
     * @param min The minimum value (inclusive)
     * @param max The maximum value (inclusive)
     * @return A random long between min and max
     */
    fun long(min: Long = 0L, max: Long = 100L): Long {
        return Random.nextLong(min, max + 1)
    }

    /**
     * Generates a random double within the specified range.
     *
     * @param min The minimum value (inclusive)
     * @param max The maximum value (exclusive)
     * @param precision The number of decimal places to round to
     * @return A random double between min and max
     */
    fun double(min: Double = 0.0, max: Double = 1.0, precision: Int = 2): Double {
        val value = min + (max - min) * Random.nextDouble()
        val factor = Math.pow(10.0, precision.toDouble())
        return Math.round(value * factor) / factor
    }

    /**
     * Generates a random boolean.
     *
     * @param trueProbability The probability of generating true (0.0 to 1.0)
     * @return A random boolean
     */
    fun boolean(trueProbability: Double = 0.5): Boolean {
        return Random.nextDouble() < trueProbability
    }

    // Date and time generation

    /**
     * Generates a random LocalDate within the specified range.
     *
     * @param minDate The minimum date (inclusive)
     * @param maxDate The maximum date (inclusive)
     * @return A random LocalDate between minDate and maxDate
     */
    fun localDate(
        minDate: LocalDate = LocalDate.now().minusYears(1),
        maxDate: LocalDate = LocalDate.now().plusYears(1)
    ): LocalDate {
        val minDay = minDate.toEpochDay()
        val maxDay = maxDate.toEpochDay()
        val randomDay = Random.nextLong(minDay, maxDay + 1)
        return LocalDate.ofEpochDay(randomDay)
    }

    /**
     * Generates a random LocalTime.
     *
     * @param minHour The minimum hour (0-23)
     * @param maxHour The maximum hour (0-23)
     * @param minMinute The minimum minute (0-59)
     * @param maxMinute The maximum minute (0-59)
     * @param minSecond The minimum second (0-59)
     * @param maxSecond The maximum second (0-59)
     * @return A random LocalTime
     */
    fun localTime(
        minHour: Int = 0,
        maxHour: Int = 23,
        minMinute: Int = 0,
        maxMinute: Int = 59,
        minSecond: Int = 0,
        maxSecond: Int = 59
    ): LocalTime {
        val hour = Random.nextInt(minHour, maxHour + 1)
        val minute = Random.nextInt(minMinute, maxMinute + 1)
        val second = Random.nextInt(minSecond, maxSecond + 1)
        val nano = Random.nextInt(0, 999999999)
        return LocalTime.of(hour, minute, second, nano)
    }

    /**
     * Generates a random LocalDateTime within the specified range.
     *
     * @param minDateTime The minimum date-time (inclusive)
     * @param maxDateTime The maximum date-time (inclusive)
     * @return A random LocalDateTime between minDateTime and maxDateTime
     */
    fun localDateTime(
        minDateTime: LocalDateTime = LocalDateTime.now().minusYears(1),
        maxDateTime: LocalDateTime = LocalDateTime.now().plusYears(1)
    ): LocalDateTime {
        val date = localDate(minDateTime.toLocalDate(), maxDateTime.toLocalDate())
        val time = localTime()
        return LocalDateTime.of(date, time)
    }

    /**
     * Generates a random ZonedDateTime within the specified range.
     *
     * @param minDateTime The minimum date-time (inclusive)
     * @param maxDateTime The maximum date-time (inclusive)
     * @param zone The time zone
     * @return A random ZonedDateTime between minDateTime and maxDateTime
     */
    fun zonedDateTime(
        minDateTime: ZonedDateTime = ZonedDateTime.now().minusYears(1),
        maxDateTime: ZonedDateTime = ZonedDateTime.now().plusYears(1),
        zone: ZoneId = ZoneId.systemDefault()
    ): ZonedDateTime {
        val minLocalDateTime = minDateTime.toLocalDateTime()
        val maxLocalDateTime = maxDateTime.toLocalDateTime()
        val localDateTime = localDateTime(minLocalDateTime, maxLocalDateTime)
        return ZonedDateTime.of(localDateTime, zone)
    }

    // Collection generation

    /**
     * Generates a random list of items.
     *
     * @param minSize The minimum size of the list (inclusive)
     * @param maxSize The maximum size of the list (inclusive)
     * @param generator A function that generates a single item
     * @return A random list of items
     */
    fun <T> list(
        minSize: Int = 1,
        maxSize: Int = 10,
        generator: () -> T
    ): List<T> {
        val size = Random.nextInt(minSize, maxSize + 1)
        return List(size) { generator() }
    }

    /**
     * Generates a random set of items.
     *
     * @param minSize The minimum size of the set (inclusive)
     * @param maxSize The maximum size of the set (inclusive)
     * @param generator A function that generates a single item
     * @return A random set of items
     */
    fun <T> set(
        minSize: Int = 1,
        maxSize: Int = 10,
        generator: () -> T
    ): Set<T> {
        val result = mutableSetOf<T>()
        val targetSize = Random.nextInt(minSize, maxSize + 1)

        // Keep generating items until we reach the target size or exceed a reasonable attempt limit
        var attempts = 0
        while (result.size < targetSize && attempts < targetSize * 3) {
            result.add(generator())
            attempts++
        }

        return result
    }

    /**
     * Generates a random map of key-value pairs.
     *
     * @param minSize The minimum size of the map (inclusive)
     * @param maxSize The maximum size of the map (inclusive)
     * @param keyGenerator A function that generates a single key
     * @param valueGenerator A function that generates a single value
     * @return A random map of key-value pairs
     */
    fun <K, V> map(
        minSize: Int = 1,
        maxSize: Int = 10,
        keyGenerator: () -> K,
        valueGenerator: () -> V
    ): Map<K, V> {
        val result = mutableMapOf<K, V>()
        val targetSize = Random.nextInt(minSize, maxSize + 1)

        // Keep generating key-value pairs until we reach the target size or exceed a reasonable attempt limit
        var attempts = 0
        while (result.size < targetSize && attempts < targetSize * 3) {
            result[keyGenerator()] = valueGenerator()
            attempts++
        }

        return result
    }

    /**
     * Picks a random element from the given collection.
     *
     * @param collection The collection to pick from
     * @return A random element from the collection, or null if the collection is empty
     */
    fun <T> oneOf(collection: Collection<T>): T? {
        if (collection.isEmpty()) return null
        return collection.random()
    }

    /**
     * Picks a random element from the given array.
     *
     * @param array The array to pick from
     * @return A random element from the array, or null if the array is empty
     */
    fun <T> oneOf(vararg array: T): T? {
        if (array.isEmpty()) return null
        return array.random()
    }
}
