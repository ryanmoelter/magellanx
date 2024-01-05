// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
  repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
  }
  dependencies {
    classpath(libs.kotlin.gradle)
    classpath(libs.android.gradle)
    classpath("org.jmailen.gradle:kotlinter-gradle:${libs.versions.kotlinter.get()}")
  }
}

plugins {
  alias(libs.plugins.kotlinter)
}

allprojects {
  apply(plugin = "org.jmailen.kotlinter")

  group = extra["GROUP"]!!
  version = extra["VERSION_NAME"]!!

  repositories {
    mavenCentral()
    google()
  }

  kotlinter {
    tasks.findByName("lint")?.dependsOn("lintKotlin")
  }

  tasks.withType<Test> {
    reports {
      html.required.set(true)
    }
  }
}

tasks.register("clean", Delete::class) {
  delete(rootProject.layout.buildDirectory)
}
