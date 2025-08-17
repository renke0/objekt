import io.gitlab.arturbosch.detekt.Detekt

plugins {
  kotlin("jvm") version "2.1.10"
  id("com.diffplug.spotless") version "7.2.1"
  id("io.gitlab.arturbosch.detekt") version "1.23.5"
  id("jacoco")
}

group = "com.renke0"

version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies {
  implementation("org.jeasy:easy-random-core:5.0.0")
  testImplementation(kotlin("test"))

  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
}

tasks.test { useJUnitPlatform() }

kotlin { jvmToolchain(21) }

spotless {
  kotlin { ktfmt("0.56") }
  kotlinGradle { ktfmt("0.56") }
  format("yaml") {
    target("**/*.yml", "**/*.yaml")
    prettier("3.0.3")
  }
  format("markdown") {
    target("**/*.md")
    prettier("3.0.3")
  }
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

tasks.test { finalizedBy(tasks.jacocoTestReport) }

tasks.jacocoTestReport {
  dependsOn(tasks.test)
  reports {
    xml.required.set(true)
    html.required.set(true)
  }
}

tasks.jacocoTestCoverageVerification {
  dependsOn(tasks.jacocoTestReport)
  violationRules { rule { limit { minimum = "0.75".toBigDecimal() } } }
}

// Task to install git hooks
tasks.register("installGitHooks") {
  doLast {
    val hooksDir = File("${rootProject.rootDir}/.githooks")
    val gitHooksDir = File("${rootProject.rootDir}/.git/hooks")

    if (gitHooksDir.exists()) {
      hooksDir.listFiles()?.forEach { hookFile ->
        val targetFile = File(gitHooksDir, hookFile.name)
        hookFile.copyTo(targetFile, overwrite = true)
        targetFile.setExecutable(true)
        println("Installed git hook: ${hookFile.name}")
      }
    } else {
      println("Git hooks directory not found. Make sure this is a git repository.")
    }
  }
}

// Run installGitHooks task during project setup
tasks.named("build") { dependsOn("installGitHooks") }
