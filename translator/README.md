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

Either use it normaly as Text:
```kotlin
import media.hiway.mdkit.translator.presentation.composable.Text
Text(text = "404-BUTTON".uppercase(), keepCase = true)
```
Or use it as `String` extention function inside of a `composable`:
```kotlin
"word".translate()
```
