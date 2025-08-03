plugins {
  kotlin("jvm") version "2.1.10"
  id("com.diffplug.spotless") version "7.2.1"
}

group = "com.renke0"

version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
  implementation("org.jeasy:easy-random-core:5.0.0")
  testImplementation(kotlin("test"))
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(21) }

spotless {
  kotlin { ktfmt("0.56") }
  kotlinGradle { ktfmt("0.56") }
}
