package media.hiway.mdkit.translator.presentation.utils

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import media.hiway.mdkit.translator.domain.use_case.Translator


@EntryPoint
@InstallIn(SingletonComponent::class)
interface TranslationEntryPoint {
    fun getTranslator(): Translator
}