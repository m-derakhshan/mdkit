package media.hiway.mdkit.translator.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import media.hiway.mdkit.translator.data.data_source.local.data_store.provider.TranslationDataStore
import media.hiway.mdkit.translator.data.data_source.local.data_store.serializer.TranslationSerializer
import media.hiway.mdkit.translator.data.data_source.local.entity.TranslationEntity
import media.hiway.mdkit.translator.data.data_source.remote.api.TranslationAPI
import media.hiway.mdkit.translator.data.repository.TranslationRepositoryImpl
import media.hiway.mdkit.translator.domain.model.TranslationConfig
import media.hiway.mdkit.translator.domain.repository.TranslationRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TranslationDataModule {

    @Provides
    @Singleton
    fun provideTranslationDataStore(
        @ApplicationContext context: Context,
    ): DataStore<TranslationEntity> {
        return DataStoreFactory.create(
            serializer = TranslationSerializer,
            produceFile = {
                File(
                    context.filesDir,
                    "${context.packageName}_translation_data_store.pb"
                )
            }
        )
    }


    @Provides
    @Singleton
    fun provideTranslationAPI(
        config: TranslationConfig
    ): TranslationAPI {
        return Retrofit
            .Builder()
            .baseUrl(config.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslationAPI::class.java)
    }


    @Provides
    @Singleton
    fun provideTranslationRepository(
        translationDataStore: TranslationDataStore,
        translationAPI: TranslationAPI,
        config: TranslationConfig,
    ): TranslationRepository {
        return TranslationRepositoryImpl(
            translationDataStore = translationDataStore,
            translationAPI = translationAPI,
            config = config
        )
    }

}