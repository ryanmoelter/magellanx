[![Latest release on Jitpack](https://jitpack.io/v/com.ryanmoelter/magellanx.svg)](https://jitpack.io/#com.ryanmoelter/magellanx)
[![Test results](https://github.com/ryanmoelter/magellanx/actions/workflows/runTests.yml/badge.svg?branch=main)](https://github.com/ryanmoelter/magellanx/actions/workflows/runTests.yml)

# Magellan X

A simple, flexible, and practical navigation framework for Android using Jetpack Compose.

## Why would I use Magellan?

- **Simple**: Intuitive abstractions and encapsulation make it easy to reason through code.
- **Flexible**: The infinitely-nestable structure allows for many different styles of structuring an
  app and navigating between pages.
- **Practical**: We pay special attention to simplifying common patterns and removing day-to-day
  boilerplate.
- **Testable**: Plain objects that are easy to instantiate and control make testing simple.

## Download

Add jitpack to your `repositories` (in your root `build.gradle.kts` file):

```kotlin
allprojects {
  repositories {
    // ...
    maven("https://jitpack.io")
  }
}
```

### Add to Gradle version catalog

To use in [gradle's version catalogs](https://docs.gradle.org/current/userguide/platforms.html),
add the following to your `libs.versions.toml`:

[![Latest release on Jitpack](https://jitpack.io/v/com.ryanmoelter/magellanx.svg)](https://jitpack.io/#com.ryanmoelter/magellanx)

```toml
[versions]
magellanx = "0.5.2"
# ...

[libraries]
magellanx-compose = { module = "com.ryanmoelter.magellanx:magellanx-compose", version.ref = "magellanx" }
magellanx-test = { module = "com.ryanmoelter.magellanx:magellanx-test", version.ref = "magellanx" }
```

Alternatively, if you only want the core library without the Compose implementation, you can use:

```toml
magellanx-core = { module = "com.ryanmoelter.magellanx:magellanx-core", version.ref = "magellanx" }
```

> Note: `magellanx-core` is included in and exposed by `magellanx-compose`, and `magellan-test` only
> applies to `magellanx-compose`.

And don't forget to use them in your `dependencies` block:

```kotlin
dependencies {
  implementation(libs.magellan.compose)
  testImplementation(libs.magellan.test)
  // Alternatively:
  // implementation(libs.magellan.core)

  // ...
}
```

### Add to `dependencies` block

<details>
  <summary>Add to <code>dependencies</code> block instead</summary>

If you don't want to use version catalogs, you can add the dependencies you need directly in your
`dependencies` block:

[![Latest release on Jitpack](https://jitpack.io/v/com.ryanmoelter/magellanx.svg)](https://jitpack.io/#com.ryanmoelter/magellanx)

```kotlin
val magellanxVersion = "0.5.2"
implementation("com.ryanmoelter.magellanx:magellanx-compose:${magellanxVersion}")
testImplementation("com.ryanmoelter.magellanx:magellanx-test:${magellanxVersion}")
```

Alternatively, if you only want the core library without the Compose implementation, you can use:

```kotlin
implementation("com.github.ryanmoelter.magellanx:magellanx-core:0.5.2")
```

> Note: `magellanx-core` is included in and exposed by `magellanx-compose`, and `magellan-test` only
> applies to `magellanx-compose`.

</details>

### Dependency versions

<details>
  <summary>Dependencies' versions for different Magellan X versions</summary>

Magellan X uses the following dependencies, and since `0.2.0` is using
[the compose bill of materials (BOM)](https://developer.android.com/jetpack/compose/setup#using-the-bom).

| Magellan X version | Kotlin version | Compose compiler version | Compose BOM  | Tested compatible compose versions |
|--------------------|----------------|--------------------------|--------------|------------------------------------|
| `0.5.2`            | `1.9.23`       | `1.5.11`                 | `2024.03.00` | `1.6.4`                            |
| `0.5.1`            | `1.9.22`       | `1.5.10`                 | `2024.02.02` | `1.6.3`                            |
| `0.5.0`            | `1.9.22`       | `1.5.10`                 | `2024.02.01` | `1.6.2`                            |
| `0.4.0` - `0.4.1`  | `1.9.22`       | `1.5.8`                  | `2024.01.00` | `1.6.0`                            |
| `0.3.0` - `0.3.1`  | `1.9.21`       | `1.5.7`                  | `2023.10.01` | `1.5.4`                            |
| `0.2.0`            | `1.7.20`       | `1.3.2`                  | `2022.11.00` | `1.3.*`                            |
| `0.1.2`            | `1.6.10`       | -                        | -            | `1.2.0-alpha05`                    |

</details>

## Quickstart

To start, create a `RootStep` that just says "Hello world":

```kotlin
class RootStep : ComposeStep() {
  @Composable
  override fun Content() {
    Text("Hello world!")
  }
}
```

Then attach it in your `MainActivity`:

```kotlin
class MainActivity : ComponentActivity() {
  private val rootStep = /* Get singleton from dependency injection */

  override fun onCreate(savedInstanceState: Bundle?) {
    // ...
    setContentNavigable(rootStep)  // Add rootStep to the lifecycle and UI
    // ...
  }
}
```

And run the app! You should see your "Hello world!" message.

If you want to navigate, make the `RootStep` a `Journey` and create a child step:

```kotlin
class RootStep : ComposeJourney() {
  override fun onCreate() {
    // Navigating in onCreate (before the UI is shown) sets the initial `Step` with no transition
    navigator.goTo(
      ChildStep(
        goToNext = { navigator.goTo(MessageStep("Hello Magellan!")) }
      )
    )
  }

  // No need to override Content() in a Journey, unless you want to add decoration around the child Steps
}

class ChildStep(
  // Navigation is passed into child Steps as parameters
  // Steps don't do their own navigation, instead they delegate to their parent Journey
  val goToNext: () -> Unit
) : ComposeStep() {
  @Composable
  override fun Content() {
    Button(onClick = { goToNext() }) {
      Text("Go to next page")
    }
  }
}

class SecondChildStep(
  // Step parameters are typesafe
  val message: String
): ComposeStep() {
  @Composable
  override fun Content() {
    Text("Navigation is fun!")
  }
}
```

And congrats, you've now created your first two pages! 🥳

These are simple concepts, but they're powerful layers of abstraction. Some more advanced patterns include:

- `Journey`s are `Step`s, meaning you can nest `Journey`s inside other `Journey`s to encapsulate your navigation.
- You can nest a `Step` inside another `Step` using `val nestedChild by attachFieldToLifecycle(MyStep())`, and it will receive all lifecycle events. This is especially useful to break up complex pages into smaller, more maintainable parts.
- If you want to reuse a piece of lifecycle-aware logic, make an object that implements the `LifecycleAware` interface, and attach it with `attachFieldToLifecycle` just like a step.
- In addition to the usual Android lifecycle events (which work as expected), Magellan also has `Shown` (`onShow()`/`onHide()`), which is like `Started` but not called when the activity is simply recreated (e.g. on rotation or another configuration change).

Eventually, I'll get around to adding a proper wiki, but feel free to [create an issue](https://github.com/ryanmoelter/magellanx/issues) or [a discussion](https://github.com/ryanmoelter/magellanx/discussions) if you have any questions!

## Learning

> Note: This library is a fork of [wealthfront/magellan](https://github.com/wealthfront/magellan),
> and is in the process of getting published. In the meantime, you can refer to the original's wiki
> pages below.

For an explanation of the core concepts of Magellan, see the source
repo's [wiki](https://github.com/wealthfront/magellan/wiki), starting
with [Thinking in Magellan](https://github.com/wealthfront/magellan/wiki/Thinking-in-Magellan).

If you're eager to start, check out the source
repo's [Quickstart wiki page](https://github.com/wealthfront/magellan/wiki/Quickstart).

## License

```
Copyright 2022 Ryan Moelter

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

Based on code from [wealthfront/magellan](https://github.com/wealthfront/magellan) licensed by:

```
Copyright 2017 Wealthfront, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
