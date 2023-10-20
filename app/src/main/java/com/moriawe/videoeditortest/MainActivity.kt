package com.moriawe.videoeditortest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.moriawe.videoeditortest.simple_player.SimplePlayerActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ButtonToSimplePlayer()
        }
    }
}

@Composable
fun ButtonToSimplePlayer() {

    val context = LocalContext.current

    Button(onClick = {
        context.startActivity(Intent(context, SimplePlayerActivity::class.java))
    }) {
    Text(text = "Simple Player")
    }
}