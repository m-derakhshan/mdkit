package media.hiway.mdkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.theoplayer.android.api.source.SourceDescription
import com.theoplayer.android.ui.UIController
import com.theoplayer.android.ui.rememberPlayer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import media.hiway.mdkit.translator.domain.model.TranslationLanguage
import media.hiway.mdkit.translator.domain.use_case.Translator
import media.hiway.mdkit.translator.presentation.composable.Text
import media.hiway.mdkit.translator.presentation.composable.translate
import media.hiway.mdkit.ui.theme.MDKitTheme
import media.hiwaymdkit.floating_view.FloatingView
import media.hiwaymdkit.floating_view.FloatingViewStatus
import media.hiwaymdkit.floating_view.rememberFloatingViewState
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var translator: Translator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MDKitTheme {

                val floatingViewState = rememberFloatingViewState()
                val screenStatus by floatingViewState.currentStatus
                val progress by animateFloatAsState(if (screenStatus == FloatingViewStatus.Minimized) 0.65f else 1f)
                val casts =
                    listOf(R.mipmap.index_1, R.mipmap.index_2, R.mipmap.index_3, R.mipmap.index_4)


                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(15.dp),
                        painter = painterResource(R.mipmap.image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )

                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            modifier = Modifier.width(250.dp),
                            painter = painterResource(R.mipmap.image),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth
                        )

                        Button(
                            modifier = Modifier.padding(top = 16.dp),
                            onClick = { floatingViewState.open() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF8B900),
                            )
                        ) {
                            Text(
                                text = "Play",
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp),
                                color = Color.Black
                            )
                        }
                    }

                    FloatingView(state = floatingViewState) {

                        val player = rememberPlayer()
                        LaunchedEffect(true) {
                            player.source =
                                SourceDescription.Builder("https://cdn.theoplayer.com/video/tears_of_steel/index.m3u8")
                                    .build()
                            player.play()
                        }
                        Column(
                            modifier = Modifier
                                .background(color = Color.Black)
                                .then(
                                    if (screenStatus == FloatingViewStatus.Minimized)
                                        Modifier.wrapContentSize()
                                    else
                                        Modifier.fillMaxSize()
                                )

                        ) {
                            Box {
                                Column {
                                    UIController(
                                        modifier = Modifier
                                            .fillMaxWidth(fraction = progress)
                                            .aspectRatio(16 / 9f),
                                        player = player
                                    )
                                    if (screenStatus == FloatingViewStatus.Opened){
                                        Text(
                                            modifier = Modifier.padding(16.dp),
                                            text = "In the future, a team of scientists and warriors gather at the Oude Kerk, an old building in Amsterdam, to stage an event from the past to save the world from destructive robots.",
                                            color = Color.LightGray,
                                            textAlign = TextAlign.Justify
                                        )
                                        Text(
                                            modifier = Modifier.padding(16.dp),
                                            text = "Main cast:",
                                            color = Color(0xFFF8B900),
                                        )
                                        Row(
                                            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Absolute.SpaceBetween
                                        ) {
                                            casts.fastForEach { cast ->
                                                Image(
                                                    modifier = Modifier
                                                        .width(85.dp),
                                                    painter = painterResource(cast),
                                                    contentDescription = "cast",
                                                    contentScale = ContentScale.FillWidth
                                                )
                                            }
                                        }

                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .pointerInput(Unit) {
                                            detectTapGestures(onTap = { floatingViewState.open() })
                                        })
                                IconButton(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .align(Alignment.TopEnd),
                                    onClick = { floatingViewState.close() }) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "close",
                                        tint = Color.White
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}
