package io.github.jaxenisr.dandelion.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class IconKind {
    Plus,
    Ellipsis,
    ChevronUp,
    ChevronDown,
    ChevronLeft,
    UpDown,
    Check,
    Pause,
    Grid,
    Gamepad,
    Sliders,
}

/** Every icon in the app is drawn here with Canvas, so the app ships no icon set. */
@Composable
fun DandelionIcon(
    kind: IconKind,
    modifier: Modifier = Modifier,
    tint: Color = Color.Black,
    strokeWidth: Dp = 1.8.dp,
) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)

        fun line(x1: Float, y1: Float, x2: Float, y2: Float) {
            drawLine(tint, Offset(w * x1, h * y1), Offset(w * x2, h * y2), strokeWidth.toPx(), StrokeCap.Round)
        }

        when (kind) {
            IconKind.Plus -> {
                line(0.5f, 0.1f, 0.5f, 0.9f)
                line(0.1f, 0.5f, 0.9f, 0.5f)
            }

            IconKind.Ellipsis -> listOf(0.16f, 0.5f, 0.84f).forEach { x ->
                drawCircle(tint, radius = w * 0.088f, center = Offset(w * x, h * 0.5f))
            }

            IconKind.ChevronDown -> drawPath(
                Path().apply {
                    moveTo(w * 0.18f, h * 0.36f)
                    lineTo(w * 0.5f, h * 0.66f)
                    lineTo(w * 0.82f, h * 0.36f)
                },
                tint,
                style = stroke,
            )

            IconKind.ChevronUp -> drawPath(
                Path().apply {
                    moveTo(w * 0.18f, h * 0.64f)
                    lineTo(w * 0.5f, h * 0.34f)
                    lineTo(w * 0.82f, h * 0.64f)
                },
                tint,
                style = stroke,
            )

            IconKind.ChevronLeft -> drawPath(
                Path().apply {
                    moveTo(w * 0.62f, h * 0.14f)
                    lineTo(w * 0.32f, h * 0.5f)
                    lineTo(w * 0.62f, h * 0.86f)
                },
                tint,
                style = stroke,
            )

            IconKind.UpDown -> {
                drawPath(
                    Path().apply {
                        moveTo(w * 0.2f, h * 0.44f)
                        lineTo(w * 0.5f, h * 0.14f)
                        lineTo(w * 0.8f, h * 0.44f)
                    },
                    tint,
                    style = stroke,
                )
                drawPath(
                    Path().apply {
                        moveTo(w * 0.2f, h * 0.56f)
                        lineTo(w * 0.5f, h * 0.86f)
                        lineTo(w * 0.8f, h * 0.56f)
                    },
                    tint,
                    style = stroke,
                )
            }

            IconKind.Check -> drawPath(
                Path().apply {
                    moveTo(w * 0.12f, h * 0.52f)
                    lineTo(w * 0.4f, h * 0.8f)
                    lineTo(w * 0.9f, h * 0.2f)
                },
                tint,
                style = stroke,
            )

            IconKind.Pause -> {
                drawRoundRect(
                    tint,
                    topLeft = Offset(w * 0.28f, h * 0.18f),
                    size = Size(w * 0.14f, h * 0.64f),
                    cornerRadius = CornerRadius(w * 0.07f),
                )
                drawRoundRect(
                    tint,
                    topLeft = Offset(w * 0.58f, h * 0.18f),
                    size = Size(w * 0.14f, h * 0.64f),
                    cornerRadius = CornerRadius(w * 0.07f),
                )
            }

            IconKind.Grid -> {
                val gap = w * 0.1f
                val cell = (w - gap) / 2f
                for (i in 0..1) {
                    for (j in 0..1) {
                        drawRoundRect(
                            tint,
                            topLeft = Offset(gap / 2f + i * cell, gap / 2f + j * cell),
                            size = Size(cell - gap, cell - gap),
                            cornerRadius = CornerRadius(w * 0.1f),
                            style = stroke,
                        )
                    }
                }
            }

            IconKind.Gamepad -> {
                drawRoundRect(
                    tint,
                    topLeft = Offset(w * 0.06f, h * 0.28f),
                    size = Size(w * 0.88f, h * 0.46f),
                    cornerRadius = CornerRadius(h * 0.23f),
                    style = stroke,
                )
                line(0.3f, 0.51f, 0.4f, 0.51f)
                line(0.35f, 0.46f, 0.35f, 0.56f)
                drawCircle(tint, radius = w * 0.045f, center = Offset(w * 0.66f, h * 0.46f))
                drawCircle(tint, radius = w * 0.045f, center = Offset(w * 0.72f, h * 0.56f))
            }

            IconKind.Sliders -> {
                line(0.1f, 0.28f, 0.9f, 0.28f)
                line(0.1f, 0.5f, 0.9f, 0.5f)
                line(0.1f, 0.72f, 0.9f, 0.72f)
                drawCircle(tint, radius = w * 0.09f, center = Offset(w * 0.34f, h * 0.28f))
                drawCircle(tint, radius = w * 0.09f, center = Offset(w * 0.66f, h * 0.5f))
                drawCircle(tint, radius = w * 0.09f, center = Offset(w * 0.44f, h * 0.72f))
            }
        }
    }
}
