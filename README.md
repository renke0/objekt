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

### Utility Functions

The library provides several utility functions for generating random values:

- `randomUuid()`: Generates a random UUID string
- `randomString(minLength, maxLength)`: Generates a random string with a length between minLength and maxLength

## Building the Project

This project uses [Gradle](https://gradle.org/). To build and run the application, use the *Gradle* tool window by
clicking the Gradle icon in the right-hand toolbar, or run it directly from the terminal:

* Run `./gradlew build` to build the application.
* Run `./gradlew check` to run all checks, including tests.
* Run `./gradlew clean` to clean all build outputs.

Note the usage of the Gradle Wrapper (`./gradlew`). This is the suggested way to use Gradle in production projects.

[Learn more about the Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html).

[Learn more about Gradle tasks](https://docs.gradle.org/current/userguide/command_line_interface.html#common_tasks).

## Project Structure

This project follows a multi-module setup and consists of the `core` and `utils` subprojects. The shared
build logic was extracted to a convention plugin located in `buildSrc`.

This project uses a version catalog (see `gradle/libs.versions.toml`) to declare and version dependencies and both a
build cache and a configuration cache (see `gradle.properties`).
