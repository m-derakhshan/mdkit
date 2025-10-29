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
       implementation("com.github.m-derakhshan.mdkit:translator:$LATEST_VERSION")
}
```

---


## ⚙️ Configuration

You **must** provide a `TranslationConfig` via Hilt:

```kotlin
@InstallIn(SingletonComponent::class)
object TranslationConfigModule {
    @Provides
    @Singleton
    fun provideTranslationConfig(): TranslationConfig {
        return object : TranslationConfig {
            override val baseUrl: String
                get() ="BASE URL"
            override val translationFilePath: String
                get() = "PATH"
            override val syncPeriod: Duration
                get() = 1.days
            override val initLanguage: TranslationLanguage
                get() = TranslationLanguage.Spanish
            override val useLegacy: Boolean
                get() = true
        }
    }
}
```

### 📝 TranslationConfig Parameters

- `baseUrl`: Base URL for the translation service.

- `translationFilePath`: Path to the translation file, used in Retrofit GET requests.

- `syncPeriod`: Duration after which the translation should be synchronized.

- `initLanguage`: The initial language used for translations.

- `useLegacy`: Indicates whether to use the legacy API for fetching translation files.  
  Default is `false`, meaning the new API will be used.  
  _Note: This parameter is temporary and will be removed in future versions._


---

## 🛠️ Usage

After setting up the config, the library will handle translation syncing and caching. You can use the provided functions and view helpers in your project directly.

Either use it normaly as Text:
```kotlin
import media.hiway.mdkit.translator.presentation.composable.Text
Text(text = "404-BUTTON".uppercase(), keepCase = true)
```
Or use it as `String` extention function inside of a `composable`:
```kotlin
"word".translate()
```
