import Versions.archVersion
import Versions.butterKnifeVersion
import Versions.daggerVersion
import Versions.espressoVersion
import Versions.extVersion
import Versions.jacksonVersion
import Versions.javaInjectVersion
import Versions.junitVersion
import Versions.kotlinVersion
import Versions.kotlinterVersion
import Versions.lifecycleVersion
import Versions.materialVersion
import Versions.mockitoVersion
import Versions.okhttpVersion
import Versions.retrofitVersion
import Versions.robolectricVersion
import Versions.rxandroidVersion
import Versions.rxjava2Version
import Versions.rxjavaAdapterVersion
import Versions.rxjavaVersion
import Versions.supportLibVersion
import Versions.testCoreVersion
import Versions.testRunnerVersion
import Versions.truthVersion
import Versions.uiAutomatorVersion

object Dependencies {

  const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
  const val appCompat = "androidx.appcompat:appcompat:$supportLibVersion"
  const val lifecycle = "androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion"

  const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
  const val kotlinterGradle = "org.jmailen.gradle:kotlinter-gradle:$kotlinterVersion"

  const val material = "com.google.android.material:material:$materialVersion"
  const val junit = "junit:junit:$junitVersion"
  const val truth = "com.google.truth:truth:$truthVersion"
  const val mockito = "org.mockito:mockito-core:$mockitoVersion"
  const val archTesting = "androidx.arch.core:core-testing:$archVersion"
  const val robolectric = "org.robolectric:robolectric:$robolectricVersion"
  const val butterknife = "com.jakewharton:butterknife:$butterKnifeVersion"
  const val butterknifeCompiler = "com.jakewharton:butterknife-compiler:$butterKnifeVersion"
  const val dagger = "com.google.dagger:dagger:$daggerVersion"
  const val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"
  const val inject = "javax.inject:javax.inject:$javaInjectVersion"

  const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
  const val rxjava = "io.reactivex:rxjava:$rxjavaVersion"
  const val rxjava2 = "io.reactivex.rxjava2:rxjava:$rxjava2Version"
  const val rxjavaAdapter = "com.squareup.retrofit2:adapter-rxjava:$rxjavaAdapterVersion"
  const val rxandroid = "io.reactivex:rxandroid:$rxandroidVersion"
  const val jackson = "com.squareup.retrofit2:converter-jackson:$jacksonVersion"
  const val okhttp = "com.squareup.okhttp3:logging-interceptor:$okhttpVersion"

  const val testCore = "androidx.test:core:$testCoreVersion"
  const val testRunner = "com.android.support.test:runner:$testRunnerVersion"
  const val testRules = "com.android.support.test:rules:$testRunnerVersion"
  const val uiAutomator ="androidx.test.uiautomator:uiautomator:$uiAutomatorVersion"
  const val extJunit = "androidx.test.ext:junit:$extVersion"
  const val espressoCore = "com.android.support.test.espresso:espresso-core:$espressoVersion"
}

