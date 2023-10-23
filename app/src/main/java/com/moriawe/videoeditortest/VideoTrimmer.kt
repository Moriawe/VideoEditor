package com.moriawe.videoeditortest

import android.app.Application
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Util
import androidx.media3.transformer.Transformer

interface VideoTrimmer {
    fun trimVideo(uri: Uri, startTimeInMs: Long, endTimeInMs: Long): MediaItem
}

class VideoTrimmerImpl(
    private val app: Application
): VideoTrimmer {

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    override fun trimVideo(uri: Uri, startTimeInMs: Long, endTimeInMs: Long): MediaItem {

        val outputPath = Util.createTempFile(app.applicationContext, "TransformerTest").path

        val inputMediaItem = MediaItem.Builder()
            .setUri(uri)
            .setClippingConfiguration(
                MediaItem.ClippingConfiguration.Builder()
                    .setStartPositionMs(startTimeInMs)
                    .setEndPositionMs(endTimeInMs)
                    .build())
            .build()
        /*
        val transformer = Transformer.Builder(app.applicationContext)
            .build()
        transformer.start(inputMediaItem, outputPath)

         */
        return inputMediaItem
    }
}

/*
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
 */