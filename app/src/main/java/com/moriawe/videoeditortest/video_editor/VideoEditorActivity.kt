package com.moriawe.videoeditortest.video_editor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.moriawe.videoeditortest.simple_player.SimplePlayerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // Nore sure about this one
class VideoEditorActivity: ComponentActivity() {

    private val TAG = "VideoEditorActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = hiltViewModel<SimplePlayerViewModel>()
            VideoEditor()
        }
    }

}

@Composable
fun VideoEditor() {
    val context = LocalContext.current


}