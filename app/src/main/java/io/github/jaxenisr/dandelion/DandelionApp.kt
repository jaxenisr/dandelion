package io.github.jaxenisr.dandelion

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.jaxenisr.dandelion.ui.Ink
import io.github.jaxenisr.dandelion.ui.Motion
import io.github.jaxenisr.dandelion.ui.nav.Destination
import io.github.jaxenisr.dandelion.ui.screens.EmulationScreen
import io.github.jaxenisr.dandelion.ui.screens.GameDetailsScreen
import io.github.jaxenisr.dandelion.ui.screens.GamesScreen
import io.github.jaxenisr.dandelion.ui.screens.SettingsScreen

private enum class ScreenChange { Push, Pop, Crossfade }

/** Which of the three moves a change of destination gets, following the iOS navigation model. */
private fun changeBetween(from: Destination, to: Destination): ScreenChange = when {
    from == Destination.Emulation -> ScreenChange.Pop
    from is Destination.Details && to == Destination.Games -> ScreenChange.Pop
    to is Destination.Details || to == Destination.Emulation -> ScreenChange.Push
    else -> ScreenChange.Crossfade
}

/** iOS push: the new page comes in from the right, the old one gives way and dims. */
private fun pushIncoming(): EnterTransition =
    slideInHorizontally(Motion.gentleOffset()) { width -> width } + fadeIn(Motion.Fade)

private fun pushOutgoing(): ExitTransition =
    slideOutHorizontally(Motion.gentleOffset()) { width -> -(width * 0.3f).toInt() } +
        fadeOut(Motion.Fade, targetAlpha = 0.85f)

/** Back: the same move reversed. */
private fun popIncoming(): EnterTransition =
    slideInHorizontally(Motion.gentleOffset()) { width -> -(width * 0.3f).toInt() } +
        fadeIn(Motion.Fade, initialAlpha = 0.85f)

private fun popOutgoing(): ExitTransition =
    slideOutHorizontally(Motion.gentleOffset()) { width -> width } + fadeOut(Motion.Fade)

@Composable
fun DandelionApp(vm: DandelionViewModel = viewModel()) {
    val view = LocalView.current
    val destination = vm.destination
    val emulating = destination is Destination.Emulation
    val drift = with(LocalDensity.current) { 12.dp.roundToPx() }

    // Black screen means light status bar icons, everything else dark icons, like iOS.
    LaunchedEffect(emulating) {
        val window = (view.context as? Activity)?.window ?: return@LaunchedEffect
        val controller = WindowCompat.getInsetsController(window, view)
        controller.isAppearanceLightStatusBars = !emulating
        controller.isAppearanceLightNavigationBars = !emulating
    }

    Box(Modifier.fillMaxSize().background(if (emulating) Color.Black else Ink.Background)) {
        AnimatedContent(
            targetState = destination,
            transitionSpec = {
                when (changeBetween(initialState, targetState)) {
                    ScreenChange.Push -> pushIncoming() togetherWith pushOutgoing()
                    ScreenChange.Pop -> popIncoming() togetherWith popOutgoing()
                    ScreenChange.Crossfade ->
                        (fadeIn(Motion.Crossfade) + slideInVertically(Motion.screenOffset()) { drift }) togetherWith
                            (fadeOut(Motion.Crossfade) + slideOutVertically(Motion.screenOffset()) { -drift })
                }
            },
            label = "screen",
        ) { dest ->
            // Every screen carries its own background, so nothing flashes between them.
            Box(
                Modifier
                    .fillMaxSize()
                    .background(if (dest is Destination.Emulation) Color.Black else Ink.Background),
            ) {
                when (dest) {
                    Destination.Games -> GamesScreen(vm)
                    Destination.Emulation -> EmulationScreen(vm)
                    Destination.Settings -> SettingsScreen(vm)
                    is Destination.Details -> GameDetailsScreen(vm)
                }
            }
        }
    }

    BackHandler(enabled = destination != Destination.Games) { vm.back() }
}
