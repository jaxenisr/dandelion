package io.github.jaxenisr.dandelion

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.jaxenisr.dandelion.ui.Ink
import io.github.jaxenisr.dandelion.ui.nav.Destination
import io.github.jaxenisr.dandelion.ui.screens.EmulationScreen
import io.github.jaxenisr.dandelion.ui.screens.GameDetailsScreen
import io.github.jaxenisr.dandelion.ui.screens.GamesScreen
import io.github.jaxenisr.dandelion.ui.screens.SettingsScreen

@Composable
fun DandelionApp(vm: DandelionViewModel = viewModel()) {
    val view = LocalView.current
    val destination = vm.destination
    val emulating = destination is Destination.Emulation

    // Black screen means light status bar icons, everything else dark icons, like iOS.
    LaunchedEffect(emulating) {
        val window = (view.context as? Activity)?.window ?: return@LaunchedEffect
        val controller = WindowCompat.getInsetsController(window, view)
        controller.isAppearanceLightStatusBars = !emulating
        controller.isAppearanceLightNavigationBars = !emulating
    }

    Box(Modifier.fillMaxSize().background(if (emulating) Color.Black else Ink.Background)) {
        when (destination) {
            Destination.Games -> GamesScreen(vm)
            Destination.Emulation -> EmulationScreen(vm)
            Destination.Settings -> SettingsScreen(vm)
            is Destination.Details -> GameDetailsScreen(vm)
        }
    }

    BackHandler(enabled = destination != Destination.Games) { vm.back() }
}
