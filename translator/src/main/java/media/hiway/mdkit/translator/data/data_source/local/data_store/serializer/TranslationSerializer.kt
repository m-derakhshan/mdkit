package media.hiway.mdkit.translator.data.data_source.local.data_store.serializer

import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import media.hiway.mdkit.translator.data.data_source.local.entity.TranslationEntity
import java.io.InputStream
import java.io.OutputStream


@Suppress("BlockingMethodInNonBlockingContext")
object TranslationSerializer : Serializer<TranslationEntity> {
    override val defaultValue: TranslationEntity
        get() = TranslationEntity()

    override suspend fun readFrom(input: InputStream): TranslationEntity {
        return try {
            Json.decodeFromString(
                deserializer = TranslationEntity.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: TranslationEntity, output: OutputStream) {
        output.write(
            Json.encodeToString(serializer = TranslationEntity.serializer(), value = t).encodeToByteArray()
        )
    }
}