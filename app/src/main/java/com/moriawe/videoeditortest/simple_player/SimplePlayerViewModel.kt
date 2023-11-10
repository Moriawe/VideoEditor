package com.moriawe.videoeditortest.simple_player

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.moriawe.videoeditortest.MetaDataReader
import com.moriawe.videoeditortest.VideoItem
import com.moriawe.videoeditortest.VideoTrimmer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    fun currentUri(): Uri? {
        Log.d(TAG, "${player.currentMediaItem?.localConfiguration?.uri}")
        return player.currentMediaItem?.localConfiguration?.uri
    }

    fun trimVideo() {
        val uri = currentUri()
        if (uri != null) {
            player.setMediaItem(
                videoTrimmer.trimVideo(uri, 1000, 10000)
            )
            Log.d(TAG,"trimVideo: video is cut")
        } else {
            Log.d(TAG,"trimVideo: No video found")
            println("You need to provide a video")
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}