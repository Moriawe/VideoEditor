package com.moriawe.videoeditortest.video_editor

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.moriawe.videoeditortest.ui.theme.VideoEditorTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // Not sure about this one
class VideoEditorActivity: ComponentActivity() {

    private val TAG = "VideoEditorActivity"
    val uri = intent.getStringExtra("video") as Uri?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoEditorTestTheme {
                val viewModel = hiltViewModel<VideoEditorViewModel>()
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$uri"
                    )
                }
                VideoEditor()
            }
        }
    }

}

@Composable
fun VideoEditor() {
    val context = LocalContext.current


}