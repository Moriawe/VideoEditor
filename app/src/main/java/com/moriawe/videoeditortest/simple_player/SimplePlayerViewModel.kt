package com.moriawe.videoeditortest.simple_player

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Util
import com.moriawe.videoeditortest.MetaDataReader
import com.moriawe.videoeditortest.VideoItem
import com.moriawe.videoeditortest.VideoTrimmer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SimplePlayerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val player: Player,
    private val metaDataReader: MetaDataReader,
    private val videoTrimmer: VideoTrimmer
): ViewModel() {

    private val TAG = "SimplePlayerViewModel"
    private val videoUris = savedStateHandle.getStateFlow("videoUris", emptyList<Uri>())

    val videoItems = videoUris.map { uris ->
        uris.map {uri ->
            VideoItem(
                contentUri = uri,
                mediaItem = MediaItem.fromUri(uri),
                name = metaDataReader.getMetaDataFromUri(uri)?.fileName ?: "No name"
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        player.prepare()
    }

    fun addVideoUri(uri: Uri) {
        savedStateHandle["videoUris"] = videoUris.value + uri
        player.addMediaItem(MediaItem.fromUri(uri))
    }

    fun playVideo(uri: Uri) {
        player.setMediaItem(
            videoItems.value.find { it.contentUri == uri}?.mediaItem ?: return
        )
    }

    fun trimVideo() {
        val uri = player.currentMediaItem?.mediaId?.toUri()
        if (uri != null) {
            player.setMediaItem(
                videoTrimmer.trimVideo(uri, 1000, 10000)
            )
            Log.d(TAG,"trimVideo: video is cut")
        } else {
            Log.d(TAG,"trimVideo: No video found")
            println("You need to provide a video")
        }
        //Internal storage
        val file = File(filesDir)
        val outputFile = File(applicationContext, filesDir, "temp_video.mp4")
        val newVideo = VideoItem(
            uri,
            videoTrimmer.trimVideo(uri, 1000, 10000),
            "temporaryVideo.mp4"
        )
        //createTempFile(String prefix, String suffix)
        setDestinationPath("")
        setVideoUri(Uri.parse("path"))

        val context: Context = ApplicationProvider.getApplicationContext<Context>()
        val outputPath = Util.createTempFile(context, "TransformerTest").path

    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}