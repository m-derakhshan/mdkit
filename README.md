[![](https://jitpack.io/v/m-derakhshan/mdkit.svg)](https://jitpack.io/#m-derakhshan/mdkit)

# MDKit

`MDKit` is a Kotlin Android library that provides the core functionality for Hiway Media applications. It consists of multiple modules, and not all modules may be suitable for every scenario. For example, the `translator` module might not fit all projects. 

However, there are some generic modules that are useful in any project, such as the runtime `permission` handler. Therefore, we recommend installing only the modules you need rather than the entire library at once. Installation instructions are provided within each module.




---

## 📦 Installation

Add JitPack to your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
````

Then include the library in your `build.gradle.kts`:

```kotlin
dependencies {
       implementation("com.github.m-derakhshan:mdkit:$LATEST_VERSION")
}
```

---


## ⚙️ Configuration
Please check each module's folder to see how to set it up.


## 📁 Modules

This is part of the larger `mdkit` monorepo:

* ✅ [`mdkit-translator`](https://github.com/m-derakhshan/mdkit/tree/main/translator) – Application language translator.
* ✅ [`mdkit-floating view`](https://github.com/m-derakhshan/mdkit/tree/main/floating_view) – A Minimizable view.
* ✅ [`mdkit-permission`](https://github.com/m-derakhshan/mdkit/tree/main/permission) – An easy, ready to go, jetpack compose run time permission handler.
* ✅ [`mdkit-qr_scanner`](https://github.com/m-derakhshan/mdkit/tree/main/qr_scanner) – An easy, ready to go, jetpack compose QR Code Scanner which allows you a full modification and controll over the camera layout.
* ⏳ More coming soon...

---
