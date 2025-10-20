package media.hiway.mdkit.permission.data.data_source.local


import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
@Suppress("BlockingMethodInNonBlockingContext")
internal object PermissionDataStoreSerializer : Serializer<PermissionDataStore> {
    override val defaultValue: PermissionDataStore
        get() = PermissionDataStore()

    override suspend fun readFrom(input: InputStream): PermissionDataStore {
        return try {
            Json.decodeFromString(
                deserializer = PermissionDataStore.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: PermissionDataStore, output: OutputStream) {
        output.write(
            Json.encodeToString(serializer = PermissionDataStore.serializer(), value = t).encodeToByteArray()
        )
    }
}