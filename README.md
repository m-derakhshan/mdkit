[![](https://jitpack.io/v/m-derakhshan/mdkit.svg)](https://jitpack.io/#m-derakhshan/mdkit)
# 🧠 MDKit

`MDKit` is a Kotlin Android library that provides the core functionality of the Hiway Media applications. It consists of different modules.



---

## 📦 Installation

Add JitPack to your `settings.gradle.kts` or `build.gradle.kts`:

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
       implementation("com.github.m-derakhshan:mdkit:1.1.0")
}
```

---

## ⚙️ Configuration

You **must** provide a `TranslationConfig` via Hilt:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object TranslationConfigModule {

    @Provides
    @Singleton
    fun provideTranslationConfig(): TranslationConfig {
        return object : TranslationConfig {
            override val baseUrl: String
                get() = "https://your-api-base-url.com"

            override val syncPeriod: Duration
                get() = 1.days

            override val initLanguage: TranslationLanguage
                get() = TranslationLanguage.English()
        }
    }
}
```

> 🧩 `baseUrl` should point to your translation backend.
>
> 🔁 `syncPeriod` controls how frequently translations sync.
>
> 🌐 `initLanguage` sets the default app language.

---

## 🛠️ Usage

After setting up the config, the library will handle translation syncing and caching. You can use the provided functions and view helpers in your project directly.

---

## 📁 Modules

This is part of the larger `mdkit` monorepo:

* ✅ `mdkit-translator` – Automatic language translation support. It's designed for easy plug-and-play use within your project using [Hilt](https://dagger.dev/hilt/) for configuration.

* ⏳ More coming soon...

---
