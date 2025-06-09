package media.hiway.mdkit.translator.data.data_source.local.data_store.provider

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import media.hiway.mdkit.translator.data.data_source.local.entity.TranslationEntity
import media.hiway.mdkit.translator.domain.model.DataStoreProvider
import javax.inject.Inject


class TranslationDataStore @Inject constructor(private val dataStore: DataStore<TranslationEntity>) :
    DataStoreProvider<TranslationEntity> {
    override suspend fun updateData(transform: suspend (TranslationEntity) -> TranslationEntity): TranslationEntity {
        return dataStore.updateData(transform)
    }

    override fun data(): Flow<TranslationEntity> {
        return dataStore.data
    }
}