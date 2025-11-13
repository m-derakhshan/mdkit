package media.hiway.mdkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import dagger.hilt.android.AndroidEntryPoint
import media.hiway.mdkit.examples.floating_view.FloatingViewExample
import media.hiway.mdkit.examples.permission.PermissionExample
import media.hiway.mdkit.examples.qr_scanner.QRScannerExample
import media.hiway.mdkit.examples.translator.TranslatorExample
import media.hiway.mdkit.translator.domain.use_case.Translator
import media.hiway.mdkit.ui.theme.MDKitTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var translator: Translator

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MDKitTheme {

                //!------------Use each example at once. comment out the others-----------//
//                TranslatorExample(translator = translator)
//                FloatingViewExample()
//                PermissionExample()
                QRScannerExample()
                //!------------Use each example at once. comment out the others-----------//

            }
        }
    }
}
