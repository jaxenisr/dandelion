package io.github.jaxenisr.dandelion.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import io.github.jaxenisr.dandelion.data.Game

/**
 * Placeholder cover: a small landscape painted from the game palette. No artwork is copied and
 * nothing is written on the cover; the name sits under it in the library.
 */
@Composable
fun CoverArt(game: Game, modifier: Modifier = Modifier) {
    Canvas(modifier.clip(RoundedCornerShape(12.dp))) {
        val w = size.width
        val h = size.height

        drawRect(Brush.verticalGradient(listOf(game.skyTop, game.skyBottom)))

        // Sun glow, low and soft over the horizon.
        val sun = Offset(w * 0.7f, h * 0.34f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.55f), Color.Transparent),
                center = sun,
                radius = h * 0.34f,
            ),
            radius = h * 0.34f,
            center = sun,
        )

        val far = Path().apply {
            moveTo(0f, h * 0.72f)
            cubicTo(w * 0.2f, h * 0.5f, w * 0.4f, h * 0.62f, w * 0.58f, h * 0.7f)
            cubicTo(w * 0.74f, h * 0.77f, w * 0.86f, h * 0.66f, w, h * 0.72f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(far, game.horizon.copy(alpha = 0.45f))

        val near = Path().apply {
            moveTo(0f, h * 0.88f)
            cubicTo(w * 0.24f, h * 0.66f, w * 0.6f, h * 0.96f, w, h * 0.78f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(near, game.horizon.copy(alpha = 0.8f))
    }
}

/** Static placeholder gameplay: a soft landscape painted from the game palette. */
@Composable
fun GameplayArt(game: Game, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height

        drawRect(Brush.verticalGradient(listOf(game.skyTop, game.skyBottom)))

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.55f), Color.Transparent),
                center = Offset(w * 0.74f, h * 0.16f),
                radius = h * 0.62f,
            ),
            radius = h * 0.62f,
            center = Offset(w * 0.74f, h * 0.16f),
        )

        val far = Path().apply {
            moveTo(0f, h * 0.74f)
            cubicTo(w * 0.18f, h * 0.48f, w * 0.33f, h * 0.66f, w * 0.5f, h * 0.56f)
            cubicTo(w * 0.67f, h * 0.46f, w * 0.82f, h * 0.62f, w, h * 0.58f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(far, game.horizon.copy(alpha = 0.32f))

        val near = Path().apply {
            moveTo(0f, h * 0.9f)
            cubicTo(w * 0.22f, h * 0.7f, w * 0.5f, h * 0.98f, w, h * 0.74f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(near, game.horizon.copy(alpha = 0.62f))

        val island = Path().apply {
            moveTo(w * 0.34f, h * 0.8f)
            lineTo(w * 0.66f, h * 0.8f)
            lineTo(w * 0.56f, h * 1.04f)
            lineTo(w * 0.44f, h * 1.04f)
            close()
        }
        drawPath(island, Color(0xFF2A2438).copy(alpha = 0.5f))

        val ledge = Path().apply {
            moveTo(w * 0.32f, h * 0.8f)
            cubicTo(w * 0.42f, h * 0.75f, w * 0.58f, h * 0.75f, w * 0.68f, h * 0.8f)
            cubicTo(w * 0.58f, h * 0.85f, w * 0.42f, h * 0.85f, w * 0.32f, h * 0.8f)
            close()
        }
        drawPath(ledge, game.horizon.copy(alpha = 0.9f))
    }
}
