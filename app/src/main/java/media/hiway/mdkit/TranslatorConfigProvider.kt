package media.hiway.mdkit

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import media.hiway.mdkit.translator.domain.model.TranslationConfig
import media.hiway.mdkit.translator.domain.model.TranslationLanguage
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


@Module
@InstallIn(SingletonComponent::class)
object TranslationConfigModule {
    @Provides
    @Singleton
    fun provideTranslationConfig(): TranslationConfig {
        return object : TranslationConfig {
            override val baseUrl: String
                get() = "https://tv.sevillafc.es/"
            override val syncPeriod: Duration
                get() = 20.seconds
            override val initLanguage: TranslationLanguage
                get() = TranslationLanguage.Spanish
        }
    }
}