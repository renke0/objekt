package com.objekt

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime

open class ObjektRandom(builder: ObjektRandomConfigBuilder = ObjektRandomConfigBuilder()) {
  private var config: ObjektConfig = builder.build()

  companion object : ObjektRandom()

  constructor(
      builder: ObjektRandomConfigBuilder.() -> Unit
  ) : this(ObjektRandomConfigBuilder().apply(builder))

  fun string(
      length: PoolBuilder<Int>.() -> Unit = {},
      chars: PoolBuilder<Char>.() -> Unit = {}
  ): String {
    val lengthPool = PoolBuilder<Int>().apply(length).pool() ?: config.stringLength
    val charsPool = PoolBuilder<Char>().apply(chars).pool() ?: config.chars
    return (1..lengthPool.random()).map { charsPool.random() }.joinToString("")
  }

  fun integer(
      pool: PoolBuilder<Int>.() -> Unit = {},
  ): Int {
    val intPool = PoolBuilder<Int>().apply(pool).pool() ?: config.integers
    return intPool.random()
  }

  fun long(
      pool: PoolBuilder<Long>.() -> Unit = {},
  ): Long {
    val longPool = PoolBuilder<Long>().apply(pool).pool() ?: config.longs
    return longPool.random()
  }

  fun float(
      pool: PoolBuilder<Float>.() -> Unit = {},
  ): Float {
    val floatPool = PoolBuilder<Float>().apply(pool).pool() ?: config.floats
    return floatPool.random()
  }

  fun double(
      pool: PoolBuilder<Double>.() -> Unit = {},
  ): Double {
    val doublePool = PoolBuilder<Double>().apply(pool).pool() ?: config.doubles
    return doublePool.random()
  }

  fun boolean(
      pool: PoolBuilder<Boolean>.() -> Unit = {},
  ): Boolean {
    val booleanPool = PoolBuilder<Boolean>().apply(pool).pool() ?: config.booleans
    return booleanPool.random()
  }

  fun localTime(
      pool: PoolBuilder<LocalTime>.() -> Unit = {},
  ): LocalTime {
    val localTimePool = PoolBuilder<LocalTime>().apply(pool).pool() ?: config.times
    return localTimePool.random()
  }

  fun localDate(
      pool: PoolBuilder<LocalDate>.() -> Unit = {},
  ): LocalDate {
    val localDatePool = PoolBuilder<LocalDate>().apply(pool).pool() ?: config.dates
    return localDatePool.random()
  }

  fun localDateTime(
      pool: PoolBuilder<LocalDateTime>.() -> Unit = {},
  ): LocalDateTime {
    val localDateTimePool = PoolBuilder<LocalDateTime>().apply(pool).pool() ?: config.localDateTimes
    return localDateTimePool.random()
  }

  fun instant(
      pool: PoolBuilder<Instant>.() -> Unit = {},
  ): Instant {
    val instantPool = PoolBuilder<Instant>().apply(pool).pool() ?: config.instants
    return instantPool.random()
  }

  fun zonedDateTime(
      pool: PoolBuilder<ZonedDateTime>.() -> Unit = {},
  ): ZonedDateTime {
    val zonedDateTimePool = PoolBuilder<ZonedDateTime>().apply(pool).pool() ?: config.zonedDateTimes
    return zonedDateTimePool.random()
  }
}

class ObjektRandomConfigBuilder {
  private var stringLength: Pool<Int>? = null
  private var chars: Pool<Char>? = null
  private var integers: Pool<Int>? = null
  private var longs: Pool<Long>? = null
  private var floats: Pool<Float>? = null
  private var doubles: Pool<Double>? = null
  private var booleans: Pool<Boolean>? = null
  private var times: Pool<LocalTime>? = null
  private var dates: Pool<LocalDate>? = null
  private var localDateTimes: Pool<LocalDateTime>? = null
  private var instants: Pool<Instant>? = null
  private var zonedDateTimes: Pool<ZonedDateTime>? = null

  fun build(): ObjektConfig {
    val defaults = ObjektConfig()
    return ObjektConfig(
        stringLength = stringLength ?: defaults.stringLength,
        chars = chars ?: defaults.chars,
        integers = integers ?: defaults.integers,
        longs = longs ?: defaults.longs,
        floats = floats ?: defaults.floats,
        doubles = doubles ?: defaults.doubles,
        booleans = booleans ?: defaults.booleans,
        times = times ?: defaults.times,
        dates = dates ?: defaults.dates,
        localDateTimes = localDateTimes ?: defaults.localDateTimes,
        instants = instants ?: defaults.instants,
        zonedDateTimes = zonedDateTimes ?: defaults.zonedDateTimes,
    )
  }

  fun stringLength(block: PoolBuilder<Int>.() -> Unit) {
    stringLength = getPool(block)
  }

  fun char(block: PoolBuilder<Char>.() -> Unit) {
    chars = getPool(block)
  }

  fun number(block: PoolBuilder<Int>.() -> Unit) {
    integers = getPool(block)
  }

  fun long(block: PoolBuilder<Long>.() -> Unit) {
    longs = getPool(block)
  }

  fun float(block: PoolBuilder<Float>.() -> Unit) {
    floats = getPool(block)
  }

  fun double(block: PoolBuilder<Double>.() -> Unit) {
    doubles = getPool(block)
  }

  fun boolean(block: PoolBuilder<Boolean>.() -> Unit) {
    booleans = getPool(block)
  }

  fun localTime(block: PoolBuilder<LocalTime>.() -> Unit) {
    times = getPool(block)
  }

  fun localDate(block: PoolBuilder<LocalDate>.() -> Unit) {
    dates = getPool(block)
  }

  fun localDateTime(block: PoolBuilder<LocalDateTime>.() -> Unit) {
    localDateTimes = getPool(block)
  }

  fun instant(block: PoolBuilder<Instant>.() -> Unit) {
    instants = getPool(block)
  }

  fun zonedDateTime(block: PoolBuilder<ZonedDateTime>.() -> Unit) {
    zonedDateTimes = getPool(block)
  }

  private fun <T : Comparable<T>> getPool(
      block: PoolBuilder<T>.() -> Unit,
  ): Pool<T>? = PoolBuilder<T>().apply(block).pool()
}

data class ObjektConfig(
    val stringLength: Pool<Int> = Pool.range(1..10),
    val chars: Pool<Char> = Pool.oneOfMultiple('a'..'z', 'A'..'Z', '0'..'9'),
    val integers: Pool<Int> = Pool.range(0..100),
    val longs: Pool<Long> = Pool.range(0L..100L),
    val floats: Pool<Float> = Pool.range(0.0f..100.0f),
    val doubles: Pool<Double> = Pool.range(0.0..100.0),
    val booleans: Pool<Boolean> = Pool.oneOf(true, false),
    val times: Pool<LocalTime> = Pool.range(TIME_RANGE),
    val dates: Pool<LocalDate> = Pool.range(DATE_RANGE),
    val localDateTimes: Pool<LocalDateTime> = Pool.range(DATE_TIME_RANGE),
    val instants: Pool<Instant> = Pool.range(INSTANT_RANGE),
    val zonedDateTimes: Pool<ZonedDateTime> =
        Pool.range(
            ZonedDateTime.now(UTC).minusYears(50)..ZonedDateTime.now(UTC).plusYears(50),
        ),
)

data class PoolBuilder<T : Comparable<T>>(
    var exactly: T? = null,
    var between: ClosedRange<T>? = null,
    var oneOf: Iterable<T>? = null,
    var iterables: List<Iterable<T>>? = null,
) {
  fun pool(): Pool<T>? =
      when {
        exactly != null -> createUnique()
        between != null -> createRange()
        oneOf != null -> createIterable()
        iterables != null -> createIterables()
        else -> null
      }

  private fun createUnique(): Pool<T> {
    requireAllNull(between, oneOf, iterables) {
      "Cannot specify 'between', 'fromIterable', or 'fromIterables' with 'exactly'"
    }
    return Pool.unique(exactly!!)
  }

  private fun createRange(): Pool<T> {
    requireAllNull(exactly, oneOf, iterables) {
      "Cannot specify 'exactly', 'fromIterable', or 'fromIterables' with 'between'"
    }
    return Pool.range(between!!)
  }

  private fun createIterable(): Pool<T> {
    requireAllNull(exactly, between, iterables) {
      "Cannot specify 'exactly', 'between', or 'fromIterables' with 'fromIterable'"
    }
    return Pool.oneOf(oneOf!!)
  }

  private fun createIterables(): Pool<T> {
    requireAllNull(exactly, between, oneOf) {
      "Cannot specify 'exactly', 'between', or 'fromIterable' with 'fromIterables'"
    }
    return Pool.oneOfMultiple(iterables!!)
  }

  private fun requireAllNull(vararg values: Any?, function: () -> String) {
    require(values.all { it == null }) { function() }
  }
}

fun main() {
  repeat(20) {
    println(
        ObjektRandom {
              stringLength { between = 2..3 }
              char { oneOf = listOf('a', 'b', 'c', 'd', 'e') }
            }
            .string(),
    )
  }
}
