import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id("com.android.library")
  kotlin("android")
  alias(libs.plugins.vanniktech.publish)
}

android {
  namespace = "com.ryanmoelter.magellanx.test"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  buildTypes { release { isMinifyEnabled = false } }
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll(listOf("-Xexplicit-api=strict", "-opt-in=kotlin.RequiresOptIn"))
    allWarningsAsErrors = true
    jvmTarget = JvmTarget.JVM_1_8
  }
}

dependencies {
  implementation(project(":magellanx-compose"))

  val composeBom = platform(libs.compose.bom)
  implementation(composeBom)
  androidTestImplementation(composeBom)

  // Compose
  implementation(libs.compose.ui)
  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.foundation)
  implementation(libs.compose.material3)
  implementation(libs.compose.material.icons.core)
  implementation(libs.compose.material.icons.extended)
  implementation(libs.activity.compose)
  androidTestImplementation(libs.compose.ui.test)

  testImplementation(libs.junit)
  testImplementation(libs.kotest.assertions.core)
}
