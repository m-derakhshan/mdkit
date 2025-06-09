package media.hiway.mdkit.translator.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import media.hiway.mdkit.translator.domain.repository.TranslationRepository
import media.hiway.mdkit.translator.domain.use_case.GetLanguages
import media.hiway.mdkit.translator.domain.use_case.GetTranslation
import media.hiway.mdkit.translator.domain.use_case.Translator
import media.hiway.mdkit.translator.domain.use_case.UpdateCurrentLanguage
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TranslationDomainModule {

    @Provides
    @Singleton
    fun provideGetTranslationUseCase(
        repository: TranslationRepository
    ): GetTranslation {
        return GetTranslation(repository = repository)
    }


    @Provides
    @Singleton
    fun provideTranslatorUseCases(
        repository: TranslationRepository
    ): Translator {
        return Translator(
            getTranslation = GetTranslation(repository),
            getLanguages = GetLanguages(repository),
            updateCurrentLanguage = UpdateCurrentLanguage(repository)
        )
    }
}