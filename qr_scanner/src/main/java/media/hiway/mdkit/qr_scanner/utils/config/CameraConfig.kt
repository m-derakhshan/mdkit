package media.hiway.mdkit.qr_scanner.utils.config

import android.content.Context
import java.io.File
import java.lang.ref.WeakReference


class CameraConfig {
    private var controller: WeakReference<CameraController>? = null

    internal fun setController(controller: CameraController) {
        this.controller = WeakReference(controller)
    }

    suspend fun takeImage(context: Context) = controller?.get()?.takeImage(context = context)


    fun saveImage(context: Context, image: File, imageName: String? = null) =
        controller?.get()?.saveImage(context = context, image = image, imageName = imageName)

}


internal interface CameraController {
    suspend fun takeImage(context: Context): File?
    fun saveImage(context: Context, image: File, imageName: String?): Boolean
}
