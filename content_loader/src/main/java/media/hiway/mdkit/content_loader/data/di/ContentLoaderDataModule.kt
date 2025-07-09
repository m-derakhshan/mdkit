package media.hiway.mdkit.content_loader.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import media.hiway.mdkit.content_loader.data.data_source.remote.ContentApi
import media.hiway.mdkit.content_loader.domain.model.ContentLoaderConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object ContentLoaderDataModule {


    @Provides
    @Singleton
    fun provideContentAPI(
        config: ContentLoaderConfig
    ): ContentApi {
        return Retrofit
            .Builder()
            .baseUrl(config.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ContentApi::class.java)
    }

}