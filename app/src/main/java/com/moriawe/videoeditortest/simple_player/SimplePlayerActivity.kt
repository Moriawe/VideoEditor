package com.moriawe.videoeditortest.simple_player

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.ui.PlayerView
import com.moriawe.videoeditortest.ui.theme.VideoEditorTestTheme
import com.moriawe.videoeditortest.video_editor.VideoEditorActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SimplePlayerActivity : ComponentActivity() {

    private val TAG = "SimplePlayerActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoEditorTestTheme {
                val context = LocalContext.current
                val viewModel = hiltViewModel<SimplePlayerViewModel>()
                val videoItems by viewModel.videoItems.collectAsState()
                val selectVideoLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = { uri ->
                        uri?.let(viewModel::addVideoUri)
                    }
                )
                var lifecycle by remember {
                    mutableStateOf(Lifecycle.Event.ON_CREATE)
                }
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        lifecycle = event
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)

                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    AndroidView(
                        factory = { context ->
                            PlayerView(context).also {
                                it.player = viewModel.player
                            }
                        },
                        update = {
                            when (lifecycle) {
                                Lifecycle.Event.ON_PAUSE -> {
                                    it.onPause()
                                    it.player?.pause()
                                }
                                Lifecycle.Event.ON_RESUME -> {
                                    it.onResume()
                                }
                                else -> Unit
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16 / 9f) //Change this to screen size
                    )
                    // progressbar + controls
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        IconButton(onClick = {
                            selectVideoLauncher.launch("video/mp4") // Will only show videofiles of type mp3
                        }) {
                            Icon(
                                imageVector = Icons.Default.FileOpen,
                                contentDescription = "Select Video"
                            )
                        }
                        IconButton( onClick = {
                            //viewModel.currentUri()
                            //val myIntent = Intent(context, VideoEditorActivity::class.java)
                                //.setType("video/*")
                            //    .putExtra("video", viewModel.currentUri())
                                //.putExtra(Intent.ACTION_VIEW, viewModel.currentUri())
                            //startActivity(myIntent)
                                viewModel.trimVideo()

                            }) {
                            Icon(
                                imageVector = Icons.Default.ContentCut,
                                contentDescription = "Edit Video"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(videoItems) { item ->
                            Text(
                                text = item.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.playVideo(item.contentUri)
                                    }
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
