package com.objekt

import java.util.Random
import java.util.UUID
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer

/**
 * Creates a fixture provider for the specified type.
 *
 * @param T The type for which to create fixtures
 * @param defaultCustomizer Optional lambda to customize the default values of the fixture
 * @return A function that creates new instances of T with the specified defaults and optional
 *   creation-time customizations
 */
inline fun <reified T : Any> objekt(
    noinline defaultCustomizer: (T.() -> Unit)? = null
): ((T.() -> Unit)?) -> T {
  // Configure EasyRandom with parameters
  val parameters =
      EasyRandomParameters()
          .seed(Random().nextLong())
          .collectionSizeRange(1, 5) // Limit collection sizes
          .stringLengthRange(5, 20) // Set reasonable string lengths
          .randomizationDepth(3) // Limit object graph depth

  // Add a randomizer for integers to ensure they're positive
  parameters.randomize(Int::class.java, IntegerRangeRandomizer(0, 100))

  val easyRandom = EasyRandom(parameters)

  return { customizer ->
    // Create a new instance using EasyRandom
    val instance = easyRandom.nextObject(T::class.java)

    // Apply default customizations if provided
    defaultCustomizer?.invoke(instance)

    // Apply creation-time customizations if provided
    customizer?.invoke(instance)

    instance
  }
}

/** Utility function to generate a random UUID string */
fun randomUuid(): String = UUID.randomUUID().toString()

/** Utility function to generate a random string with specified length constraints */
fun randomString(minLength: Int = 5, maxLength: Int = 10): String {
  val length = (minLength..maxLength).random()
  val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
  return (1..length).map { allowedChars.random() }.joinToString("")
}
