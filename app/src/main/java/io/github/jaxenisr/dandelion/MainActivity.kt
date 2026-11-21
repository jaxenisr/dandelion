package io.github.jaxenisr.dandelion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // The screens draw their own backgrounds all the way up, so the window does not inset.
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent { DandelionApp() }
    }
}
