plugins {
  id("com.android.library")
  kotlin("android")
  `maven-publish`
}

group = extra["GROUP"]!!
version = extra["VERSION_NAME"]!!

android {
  namespace = "com.ryanmoelter.magellanx.test"
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
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  kotlinOptions {
    jvmTarget = "1.8"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
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
      listOf(
        "-Xexplicit-api=strict",
        "-opt-in=kotlin.RequiresOptIn",
      ),
    )
    allWarningsAsErrors = true
  }
}

dependencies {
  implementation(project(":magellanx-compose"))

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

  testImplementation(libs.junit)
  testImplementation(libs.kotest.assertions.core)
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
