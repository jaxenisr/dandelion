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
 * these specs, so the whole interface has one feel and no screen hides a magic number.
 */
object Motion {
    private const val SnappyDamping = 0.82f
    private const val SnappyStiffness = 900f
    private const val GentleDamping = 0.9f
    private const val GentleStiffness = 380f
    private const val ScreenDamping = 0.95f
    private const val ScreenStiffness = 420f
    private const val FadeMillis = 180

    /** Controls, toggles, buttons: quick, barely bouncy. */
    val Snappy = spring<Float>(dampingRatio = SnappyDamping, stiffness = SnappyStiffness)

    /** Menu and sheets: slower, still settled. */
    val Gentle = spring<Float>(dampingRatio = GentleDamping, stiffness = GentleStiffness)

    /** Whole screens moving sideways. */
    val Screen = spring<Float>(dampingRatio = ScreenDamping, stiffness = ScreenStiffness)

    /** Colours and small fades. */
    val Fade = tween<Float>(durationMillis = FadeMillis, easing = FastOutSlowInEasing)

    /** Closing the title menu: short, no spring. */
    val Close = tween<Float>(durationMillis = 160, easing = FastOutSlowInEasing)

    /** Swapping one page for another where there is no push. */
    val Crossfade = tween<Float>(durationMillis = 200, easing = FastOutSlowInEasing)

    val FadeColor = tween<Color>(durationMillis = FadeMillis, easing = FastOutSlowInEasing)

    // The same curves for the value types Compose asks for elsewhere: dp offsets inside a
    // control, and pixel offsets inside a screen transition.
    fun snappyDp(): SpringSpec<Dp> = spring(dampingRatio = SnappyDamping, stiffness = SnappyStiffness)

    fun gentleOffset(): SpringSpec<IntOffset> =
        spring(dampingRatio = GentleDamping, stiffness = GentleStiffness)

    fun screenOffset(): SpringSpec<IntOffset> =
        spring(dampingRatio = ScreenDamping, stiffness = ScreenStiffness)
}
