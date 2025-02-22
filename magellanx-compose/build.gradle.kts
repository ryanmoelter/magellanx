plugins {
  id("com.android.library")
  kotlin("android")
  alias(libs.plugins.kotest)
  alias(libs.plugins.composeCompiler)
  `maven-publish`
}

android {
  namespace = "com.ryanmoelter.magellanx.compose"
  compileSdk =
    libs.versions.compileSdk
      .get()
      .toInt()

  defaultConfig {
    minSdk =
      libs.versions.minSdk
        .get()
        .toInt()

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
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

  kotlinOptions {
    jvmTarget = "1.8"
  }

  buildFeatures {
    compose = true
  }

  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
  }

  publishing {
    singleVariant("release") {
      withJavadocJar()
      withSourcesJar()
    }
  }
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll(
      "-Xexplicit-api=strict",
      "-opt-in=kotlin.RequiresOptIn",
    )
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

dependencies {
  api(project(":magellanx-core"))

  val composeBom = platform(libs.compose.bom)
  implementation(composeBom)
  androidTestImplementation(composeBom)

  // Kotlin
  implementation(libs.coroutines.core)
  implementation(libs.coroutines.android)
  testImplementation(libs.coroutines.test)

  // Android
  implementation(libs.appcompat)
  implementation(libs.constraintlayout)
  implementation(libs.ktx.core)

  // Compose
  implementation(libs.compose.ui)
  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.foundation)
  implementation(libs.compose.material3)
  implementation(libs.compose.material.icons.core)
  implementation(libs.compose.material.icons.extended)
  implementation(libs.activity.compose)
  androidTestImplementation(libs.compose.ui.test)
  implementation(libs.accompanist.systemuicontroller)
  implementation(libs.accompanist.drawablepainter)

  // Testing
  testImplementation(libs.kotest.runner.junit5)
  testImplementation(libs.kotest.assertions.core)
  testImplementation(libs.kotest.extensions.robolectric)
  testImplementation(libs.robolectric)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.androidx.test.core.ktx)
  androidTestImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.espresso.core)
}

publishing {
  publications {
    register<MavenPublication>("release") {
      groupId = extra["GROUP"] as String
      artifactId = extra["POM_ARTIFACT_ID"] as String
      version = extra["VERSION_NAME"] as String

      pom {
        name.set(extra["POM_NAME"] as String)
        packaging = extra["POM_PACKAGING"] as String
        description.set(extra["POM_DESCRIPTION"] as String)
        url.set(extra["POM_URL"] as String)

        scm {
          url.set(extra["POM_SCM_URL"] as String)
          connection.set(extra["POM_SCM_CONNECTION"] as String)
          developerConnection.set(extra["POM_SCM_DEV_CONNECTION"] as String)
        }

        licenses {
          license {
            name.set(extra["POM_LICENCE_NAME"] as String)
            url.set(extra["POM_LICENCE_URL"] as String)
            distribution.set(extra["POM_LICENCE_DIST"] as String)
          }
        }

        developers {
          developer {
            id.set(extra["POM_DEVELOPER_ID"] as String)
            name.set(extra["POM_DEVELOPER_NAME"] as String)
          }
        }
      }

      afterEvaluate {
        from(components["release"])
      }
    }
  }
}
