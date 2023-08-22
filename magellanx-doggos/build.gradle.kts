plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "com.ryanmoelter.magellanx.doggos"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "com.ryanmoelter.magellanx.doggos"
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.compileSdk.get().toInt()
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
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
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {
  project(":magellanx-compose")

  implementation(libs.bundles.android.app)

  implementation(libs.coroutines.core)
  implementation(libs.coroutines.android)
  testImplementation(libs.coroutines.test)

  implementation(libs.ktx.core)
  implementation(libs.lifecycle.runtime.ktx)

  implementation(libs.activity.compose)
  implementation(platform(libs.compose.bom))
  implementation(libs.compose.ui)
  implementation(libs.ui.graphics)
  implementation(libs.ui.tooling.preview)
  implementation(libs.compose.material3)
  androidTestImplementation(platform(libs.compose.bom))
  androidTestImplementation(libs.compose.ui.test)
  debugImplementation(libs.compose.ui.tooling)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.espresso.core)
  debugImplementation(libs.ui.test.manifest)
}
