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
    classpath(
      "com.ncorti.ktfmt.gradle:com.ncorti.ktfmt.gradle.gradle.plugin:${libs.versions.ktfmt.get()}"
    )
  }
}

plugins {
  alias(libs.plugins.ktfmt)
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.composeCompiler) apply false
  alias(libs.plugins.vanniktech.publish) apply false
}

allprojects {
  apply(plugin = "com.ncorti.ktfmt.gradle")

  group = extra["GROUP"]!!
  version = extra["VERSION_NAME"]!!

  repositories {
    mavenCentral()
    google()
  }

  ktfmt {
    googleStyle()
    tasks.findByName("lint")?.dependsOn("ktfmtCheck")
  }

  tasks.withType<Test> { reports { html.required.set(true) } }
}

tasks.register("clean", Delete::class) { delete(rootProject.layout.buildDirectory) }
