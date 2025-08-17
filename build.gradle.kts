import io.gitlab.arturbosch.detekt.Detekt

plugins {
  kotlin("jvm") version "2.1.10"
  id("com.diffplug.spotless") version "7.2.1"
  id("io.gitlab.arturbosch.detekt") version "1.23.5"
}

group = "com.renke0"

version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
  implementation("org.jeasy:easy-random-core:5.0.0")
  testImplementation(kotlin("test"))

  // Detekt dependencies
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(21) }

spotless {
  kotlin { ktfmt("0.56") }
  kotlinGradle { ktfmt("0.56") }
}

detekt {
  buildUponDefaultConfig = true
  allRules = false
  config.setFrom("$projectDir/detekt.yml")
}

tasks.withType<Detekt>().configureEach {
  reports {
    html.required.set(true)
    xml.required.set(true)
    txt.required.set(true)
    sarif.required.set(true)
  }
}
