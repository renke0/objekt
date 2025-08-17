package com.objekt

import com.objekt.RandomPlus.nextDouble
import com.objekt.RandomPlus.nextFloat
import com.objekt.RandomPlus.nextInstant
import com.objekt.RandomPlus.nextLocalDate
import com.objekt.RandomPlus.nextLocalDateTime
import com.objekt.RandomPlus.nextLocalTime
import com.objekt.RandomPlus.nextZonedDateTime
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import kotlin.reflect.KClass

interface Pool<T : Comparable<T>> {
  fun random(): T

  companion object {
    fun <T : Comparable<T>> unique(value: T): Pool<T> = Unique(value)

    fun <T : Comparable<T>> range(range: ClosedRange<T>): Pool<T> = Range(range)

    fun <T : Comparable<T>> oneOf(values: Iterable<T>): Pool<T> = FromIterable(values)

    fun <T : Comparable<T>> oneOf(vararg values: T): Pool<T> = FromIterable(values.asIterable())

    fun <T : Comparable<T>> oneOfMultiple(values: Iterable<Iterable<T>>): Pool<T> =
        FromIterable(values.flatten())

    fun <T : Comparable<T>> oneOfMultiple(vararg values: Iterable<T>): Pool<T> =
        FromIterable(values.flatMap { it }.asIterable())
  }
}

internal class Unique<T : Comparable<T>>(private val value: T) : Pool<T> {
  override fun random(): T = value
}

internal class Range<T : Comparable<T>>(private val range: ClosedRange<T>) : Pool<T> {
  init {
    require(range.start <= range.endInclusive) {
      "Invalid range: start (${range.start}) must be less than or equal to end (${range.endInclusive})"
    }
  }

  override fun random(): T {
    @Suppress("UNCHECKED_CAST")
    return when (range) {
      is CharRange -> range.random() as T
      is IntRange -> range.random() as T
      is LongRange -> range.random() as T
      is UIntRange -> range.random() as T
      is ULongRange -> range.random() as T
      else -> tryRandomComparableRange(range)
    }
  }

  private fun <T : Comparable<T>> tryRandomComparableRange(range: ClosedRange<T>): T {
    val kClass = commonParent(range.start::class, range.endInclusive::class)
    @Suppress("UNCHECKED_CAST")
    return when (kClass) {
      Double::class -> nextDouble(range.start as Double, range.endInclusive as Double) as T
      Float::class -> nextFloat(range.start as Float, range.endInclusive as Float) as T
      LocalTime::class ->
          nextLocalTime(range.start as LocalTime, range.endInclusive as LocalTime) as T
      Instant::class -> nextInstant(range.start as Instant, range.endInclusive as Instant) as T
      LocalDate::class ->
          nextLocalDate(range.start as LocalDate, range.endInclusive as LocalDate) as T
      LocalDateTime::class ->
          nextLocalDateTime(range.start as LocalDateTime, range.endInclusive as LocalDateTime) as T
      ZonedDateTime::class ->
          nextZonedDateTime(range.start as ZonedDateTime, range.endInclusive as ZonedDateTime) as T
      else -> error { "Unsupported range type: $kClass" }
    }
  }

  private fun commonParent(a: KClass<*>, b: KClass<*>): KClass<*> =
      when {
        a == b -> a
        a.java.isAssignableFrom(b.java) -> a
        b.java.isAssignableFrom(a.java) -> b
        else -> Any::class
      }
}

internal open class FromIterable<T : Comparable<T>>(values: Iterable<T>) : Pool<T> {
  private val list: List<T> = values.toList()

  init {
    require(list.isNotEmpty()) { "Pool of iterables must contain at least one value" }
  }

  override fun random(): T = list.random()
}
