package media.hiway.mdkit.translator.domain.model

import kotlinx.coroutines.flow.Flow

internal interface DataStoreProvider<T> {
    suspend fun updateData(transform: suspend (t: T) -> T): T
    fun data(): Flow<T>
}