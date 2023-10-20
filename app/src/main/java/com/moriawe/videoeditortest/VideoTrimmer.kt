package com.moriawe.videoeditortest

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.transformer.TransformationRequest
import java.io.File

interface VideoTrimmer {
    fun trimVideo(uri: Uri, startTimeInMs: Long, endTimeInMs: Long): MediaItem
}

class VideoTrimmerImpl(): VideoTrimmer {
    override fun trimVideo(uri: Uri, startTimeInMs: Long, endTimeInMs: Long): MediaItem {

        val inputMediaItem = MediaItem.Builder()
            .setUri(uri)
            .setClippingConfiguration(
                MediaItem.ClippingConfiguration.Builder()
                    .setStartPositionMs(startTimeInMs)
                    .setEndPositionMs(endTimeInMs)
                    .build())
            .build()
        return inputMediaItem
    }
}