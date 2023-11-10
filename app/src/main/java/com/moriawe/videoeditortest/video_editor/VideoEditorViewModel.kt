package com.moriawe.videoeditortest.video_editor

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.media3.common.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VideoEditorViewModel @Inject constructor(
    val player: Player
): ViewModel() {

    init {
        player.prepare()
        //player.setMediaItem(MediaItem.fromUri(uri!!))
    }

    fun updateUri(uri: Uri) {
        //uri = uri
    }



    override fun onCleared() {
        super.onCleared()
        player.release()
    }

}