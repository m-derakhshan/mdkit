package media.hiway.mdkit.permission.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import media.hiway.mdkit.permission.data.data_source.local.PermissionDataStore
import media.hiway.mdkit.permission.data.data_source.local.PermissionDataStoreSerializer
import java.io.File
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object PermissionDataSingletonModule {

    @Provides
    @Singleton
    fun providePermissionDataStore(@ApplicationContext context: Context): DataStore<PermissionDataStore> {
        return DataStoreFactory.create(
            serializer = PermissionDataStoreSerializer,
            produceFile = { File(context.filesDir, "permission_data_store.pb") }
        )
    }


}