package com.objekt

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
    val lengthBuilder = PoolBuilder<Int>().apply(length)
    val charsBuilder = PoolBuilder<Char>().apply(chars)
    return randomString(
        lengthBuilder.pool() ?: config.stringLength, charsBuilder.pool() ?: config.chars)
  }
}

class ObjektRandomConfigBuilder {
  private var stringLength: Pool<Int>? = null
  private var chars: Pool<Char>? = null

  fun build(): ObjektConfig {
    val defaults = ObjektConfig()
    return ObjektConfig(
        stringLength = stringLength ?: defaults.stringLength,
        chars = chars ?: defaults.chars,
    )
  }

  fun stringLength(block: PoolBuilder<Int>.() -> Unit) {
    stringLength = getPool(block)
  }

  fun char(block: PoolBuilder<Char>.() -> Unit) {
    chars = getPool(block)
  }

  private fun <T : Comparable<T>> getPool(
      block: PoolBuilder<T>.() -> Unit,
  ): Pool<T>? = PoolBuilder<T>().apply(block).pool()
}

data class ObjektConfig(
    val stringLength: Pool<Int> = Pool.range(1..10),
    val chars: Pool<Char> = Pool.oneOfMultiple('a'..'z', 'A'..'Z', '0'..'9'),
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
