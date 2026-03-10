plugins {
  id("com.android.library")
  kotlin("android")
  alias(libs.plugins.vanniktech.publish)
}

android {
  namespace = "com.ryanmoelter.magellanx.core"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
    aarMetadata { minCompileSdk = libs.versions.compileSdk.get().toInt() }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  kotlinOptions { jvmTarget = "1.8" }

  testOptions { unitTests { isIncludeAndroidResources = true } }

}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xexplicit-api=strict", "-opt-in=kotlin.RequiresOptIn")
  }
}

dependencies {
  // Kotlin
  implementation(libs.coroutines.core)
  implementation(libs.coroutines.android)
  testImplementation(libs.coroutines.test)

  // Android
  implementation(libs.appcompat)
  implementation(libs.constraintlayout)
  implementation(libs.ktx.core)

  // Testing
  testImplementation(libs.kotest.assertions.core)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.androidx.test.core.ktx)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.espresso.core)
}
