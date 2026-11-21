package io.github.jaxenisr.dandelion.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset

/**
 * The app's motion, named the way SwiftUI names it. Every animation in the app takes one of
 * these specs, so the feel is the same everywhere and nothing hides a magic number.
 */
object Motion {
    /** Controls, toggles, buttons: quick, barely bouncy. */
    val Snappy = spring<Float>(dampingRatio = 0.82f, stiffness = 900f)

    /** Menu and sheets: slower, still settled. */
    val Gentle = spring<Float>(dampingRatio = 0.9f, stiffness = 380f)

    /** Whole screens moving sideways. */
    val Screen = spring<Float>(dampingRatio = 0.95f, stiffness = 420f)

    /** Colours and small fades. */
    val Fade = tween<Float>(durationMillis = 180, easing = FastOutSlowInEasing)

    /** Closing the title menu, and the crossfade between two pages. */
    val Close = tween<Float>(durationMillis = 160, easing = FastOutSlowInEasing)
    val Crossfade = tween<Float>(durationMillis = 200, easing = FastOutSlowInEasing)

    val FadeColor = tween<Color>(durationMillis = 180, easing = FastOutSlowInEasing)

    // The same curves for the value types Compose asks for elsewhere: dp offsets in a control,
    // and pixel offsets in a screen transition.
    fun snappyDp(): SpringSpec<Dp> = spring(dampingRatio = 0.82f, stiffness = 900f)

    fun gentleDp(): SpringSpec<Dp> = spring(dampingRatio = 0.9f, stiffness = 380f)

    fun gentleOffset(): SpringSpec<IntOffset> = spring(dampingRatio = 0.9f, stiffness = 380f)

    fun screenOffset(): SpringSpec<IntOffset> = spring(dampingRatio = 0.95f, stiffness = 420f)
}
