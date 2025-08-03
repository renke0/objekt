package com.objekt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertNotEquals

// Sample data class for testing
data class User(
    var id: String,
    var name: String,
    var age: Int,
    var email: String
)

class ObjektTest {
    @Test
    fun `should create fixture provider without customization`() {
        // Create a fixture provider for User
        val aUser = objekt<User>()

        // Create two instances using the provider
        val user1 = aUser(null)
        val user2 = aUser(null)

        // Verify that instances are created with random values
        assertNotNull(user1.id)
        assertNotNull(user1.name)
        assertTrue(user1.age >= 0)
        assertNotNull(user1.email)

        // Verify that different instances have different values
        assertNotEquals(user1, user2)
    }

    @Test
    fun `should create fixture provider with customization`() {
        // Create a fixture provider for User with custom values
        val aUser = objekt<User> {
            // Override some properties with custom values
            id = randomUuid()
            name = randomString(minLength = 4, maxLength = 12)
            age = 25
        }

        // Create an instance using the provider
        val user = aUser(null)

        // Verify that the custom values are applied
        assertNotNull(user.id)
        assertTrue(user.name.length in 4..12)
        assertEquals(25, user.age)
        assertNotNull(user.email) // This should still be random
    }

    @Test
    fun `should generate random strings with specified length constraints`() {
        val minLength = 3
        val maxLength = 8

        // Generate 10 random strings
        val strings = (1..10).map { randomString(minLength, maxLength) }

        // Verify that all strings have lengths within the specified constraints
        strings.forEach { string ->
            assertTrue(string.length in minLength..maxLength)
        }
    }

    @Test
    fun `should generate different UUIDs`() {
        // Generate 10 random UUIDs
        val uuids = (1..10).map { randomUuid() }

        // Verify that all UUIDs are unique
        assertEquals(uuids.size, uuids.toSet().size)
    }

    @Test
    fun `should allow customization at creation time`() {
        // Create a fixture provider for User with default values
        val aUser = objekt<User> {
            // Set default values
            age = 25
            name = "Default Name"
        }

        // Create an instance using the provider with creation-time customization
        val user = aUser {
            // Override some properties at creation time
            age = 30
            email = "custom@example.com"
        }

        // Verify that both default and creation-time customizations are applied
        assertEquals(30, user.age) // Creation-time value overrides default
        assertEquals("Default Name", user.name) // Default value is preserved
        assertEquals("custom@example.com", user.email) // Creation-time value is applied
        assertNotNull(user.id) // Random value is generated
    }
}
