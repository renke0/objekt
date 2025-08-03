# objekt

A Kotlin library for creating test fixtures with ease.

## Overview

The `objekt` library provides a simple and flexible way to create test fixtures for your Kotlin classes. It uses the [EasyRandom](https://github.com/j-easy/easy-random) library to generate random values for your class properties, and allows you to customize the default values as needed.

## Usage

### Basic Usage

```kotlin
// Create a fixture provider for User
val aUser = objekt<User>()

// Create a new User instance with random values
val testUser = aUser(null)
```

### Customizing Default Values

You can customize the default values for your fixtures by providing a lambda function:

```kotlin
// Create a fixture provider for User with custom values
val aUser = objekt<User> {
    id = randomUuid()
    name = randomString(minLength = 4, maxLength = 12)
    age = 25
}

// Create a new User instance with the custom values
val testUser = aUser(null)
```

### Customizing Objects at Creation Time

You can also customize objects at creation time by providing a lambda function to the fixture provider:

```kotlin
// Create a fixture provider for User with default values
val aUser = objekt<User> {
    age = 25 // makes all users to be created with age equals 25 by default
}

// Create a new User instance with custom values that override the defaults
val user = aUser {
    age = 30 // allow to change the age value on creation
}
```
