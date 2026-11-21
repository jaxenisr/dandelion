package io.github.jaxenisr.dandelion.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.jaxenisr.dandelion.ui.Ink
import io.github.jaxenisr.dandelion.ui.Motion
import io.github.jaxenisr.dandelion.ui.Type
import kotlin.math.abs
import kotlin.math.roundToInt

/** Press alpha and scale, shared by every tappable surface. */
private fun Modifier.pressFeedback(
    pressedAlpha: Float,
    pressedScale: Float,
    onClick: () -> Unit,
): Modifier = composed {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val strength by animateFloatAsState(if (pressed) 1f else 0f, Motion.Snappy, label = "press")
    graphicsLayer {
        alpha = 1f - (1f - pressedAlpha) * strength
        val scale = 1f + (pressedScale - 1f) * strength
        scaleX = scale
        scaleY = scale
    }.clickable(interactionSource = interaction, indication = null, onClick = onClick)
}

/**
 * iOS style press feedback for full width rows: the row dims, it does not scale, and there is no
 * ripple.
 */
fun Modifier.tapDim(onClick: () -> Unit): Modifier =
    pressFeedback(pressedAlpha = 0.6f, pressedScale = 1f, onClick = onClick)

/** Dim and scale for compact controls. Cells lift instead of pressing in, so they pass scale 1.02. */
fun Modifier.tapScale(
    onClick: () -> Unit,
    scale: Float = 0.97f,
    alpha: Float = 0.6f,
): Modifier = pressFeedback(pressedAlpha = alpha, pressedScale = scale, onClick = onClick)

/** Swallows taps without any visual feedback. */
fun Modifier.tapOnly(onClick: () -> Unit): Modifier = composed {
    val interaction = remember { MutableInteractionSource() }
    this.clickable(interactionSource = interaction, indication = null, onClick = onClick)
}

@Composable
fun Label(
    text: String,
    style: TextStyle,
    color: Color = Ink.Text,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    align: TextAlign? = null,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = style.copy(color = color, textAlign = align ?: style.textAlign),
        maxLines = maxLines,
    )
}

@Composable
fun Hairline(modifier: Modifier = Modifier, color: Color = Ink.Hairline) {
    Box(modifier.fillMaxWidth().height(0.5.dp).background(color))
}

@Composable
fun RoundSymbolButton(
    icon: IconKind,
    description: String,
    modifier: Modifier = Modifier,
    dark: Boolean = false,
    tint: Color = if (dark) Color.White else Ink.Text,
    diameter: Dp = 34.dp,
    iconSize: Dp = 13.dp,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(diameter)
            .tapScale(onClick)
            .clip(CircleShape)
            .background(if (dark) Ink.EmulatorControl else Color.Transparent)
            .border(0.5.dp, if (dark) Ink.EmulatorControlStroke else Ink.Hairline, CircleShape)
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        DandelionIcon(icon, Modifier.size(iconSize), tint, if (dark) 1.6.dp else 1.8.dp)
    }
}

@Composable
fun Toggle(checked: Boolean, onChange: (Boolean) -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    val travel by animateDpAsState(
        targetValue = if (checked) 20.dp else 0.dp,
        animationSpec = Motion.snappyDp(),
        label = "knobTravel",
    )
    val knobWidth by animateDpAsState(
        targetValue = if (pressed) 32.dp else 27.dp,
        animationSpec = Motion.snappyDp(),
        label = "knobWidth",
    )
    val track by animateColorAsState(
        targetValue = if (checked) Ink.Green else Ink.ToggleOff,
        animationSpec = Motion.FadeColor,
        label = "track",
    )
    val control by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = Motion.Snappy,
        label = "togglePress",
    )

    // While the knob is stretched it grows away from the edge it is parked against.
    val stretch = knobWidth - 27.dp
    val knobOffset = travel - if (checked) stretch else 0.dp

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = control
                scaleY = control
            }
            .size(51.dp, 31.dp)
            .clip(CircleShape)
            .background(track)
            .clickable(interactionSource = interaction, indication = null) { onChange(!checked) }
            .padding(2.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            Modifier
                .offset(x = knobOffset)
                .width(knobWidth)
                .height(27.dp)
                .shadow(3.dp, CircleShape, ambientColor = KnobShadow, spotColor = KnobShadow)
                .clip(CircleShape)
                .background(Color.White),
        )
    }
}

private val KnobShadow = Color.Black.copy(alpha = 0.12f)

@Composable
private fun RowShell(
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val base = Modifier.fillMaxWidth().height(46.dp)
    Row(
        modifier = (if (onClick != null) base.tapDim(onClick) else base).padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Composable
fun TextRow(
    title: String,
    value: String? = null,
    chevrons: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    RowShell(onClick = onClick) {
        Label(title, Type.Body, maxLines = 1)
        Spacer(Modifier.weight(1f))
        if (value != null) {
            Crossfade(targetState = value, animationSpec = Motion.Fade, label = "value") { text ->
                Label(text, Type.Value, Ink.Secondary, maxLines = 1)
            }
            if (chevrons) {
                Spacer(Modifier.width(6.dp))
                DandelionIcon(IconKind.UpDown, Modifier.size(13.dp), Ink.Secondary, 1.5.dp)
            }
        }
    }
}

@Composable
fun ToggleRow(title: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    RowShell {
        Label(title, Type.Body, maxLines = 1)
        Spacer(Modifier.weight(1f))
        Toggle(checked, onChange)
    }
}

@Composable
fun SegmentedRow(
    title: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
) {
    RowShell {
        Label(title, Type.Body, maxLines = 1)
        Spacer(Modifier.weight(1f))
        SegmentedControl(options, selected, onSelect)
    }
}

/** iOS segmented control: a grey track with a white pill that slides under the labels. */
@Composable
private fun SegmentedControl(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
) {
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val widest = remember(options) {
        options.maxOf { measurer.measure(AnnotatedString(it), Type.TabSelected).size.width }
    }
    val segmentWidth = with(density) { widest.toDp() } + 16.dp

    val index = options.indexOf(selected).coerceAtLeast(0)
    val position by animateFloatAsState(index.toFloat(), Motion.Snappy, label = "segmentPosition")
    val sources = remember(options.size) { List(options.size) { MutableInteractionSource() } }
    val pressedStates = sources.map { it.collectIsPressedAsState() }
    val pressed = pressedStates.any { it.value }
    val press by animateFloatAsState(if (pressed) 1f else 0f, Motion.Snappy, label = "segmentPress")
    val pillOffset by animateDpAsState(segmentWidth * position, Motion.snappyDp(), label = "pill")
    val travelling = abs(position - position.roundToInt())

    Box(
        Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Ink.ToggleOff)
            .padding(2.dp),
    ) {
        Box(
            Modifier
                .offset(x = pillOffset)
                .width(segmentWidth)
                .fillMaxHeight()
                .graphicsLayer {
                    scaleX = 1f + 0.04f * press + 0.06f * travelling
                    scaleY = 1f
                }
                .shadow(2.dp, RoundedCornerShape(6.dp), ambientColor = KnobShadow, spotColor = KnobShadow)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White),
        )

        Row(Modifier.matchParentSize()) {
            options.forEachIndexed { i, option ->
                val active = i == index
                val color by animateColorAsState(
                    targetValue = if (active) Ink.Text else Ink.Secondary,
                    animationSpec = Motion.FadeColor,
                    label = "segmentLabel",
                )
                Box(
                    modifier = Modifier
                        .width(segmentWidth)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = sources[i],
                            indication = null,
                        ) { onSelect(option) },
                    contentAlignment = Alignment.Center,
                ) {
                    Label(
                        text = option,
                        style = if (active) Type.TabSelected else Type.TabIdle,
                        color = color,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, subtitle: String) {
    Column(Modifier.padding(horizontal = 24.dp)) {
        Label(title, Type.SectionTitle)
        Spacer(Modifier.height(6.dp))
        Label(subtitle, Type.Caption, Ink.Secondary)
    }
}

@Composable
fun FilledButton(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    height: Dp = 50.dp,
    shape: Shape = RoundedCornerShape(12.dp),
    style: TextStyle = Type.Button,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .tapScale(onClick)
            .clip(shape)
            .background(color),
        contentAlignment = Alignment.Center,
    ) {
        Label(text, style, Color.White)
    }
}

@Composable
fun ProgressBar(progress: Float, modifier: Modifier = Modifier, height: Dp = 4.dp) {
    val fill by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = Motion.Gentle,
        label = "progress",
    )
    Box(modifier.height(height).clip(CircleShape).background(Ink.Track)) {
        Box(
            Modifier
                .fillMaxWidth(fill)
                .height(height)
                .clip(CircleShape)
                .background(Ink.Purple),
        )
    }
}

@Composable
fun CirclePad(modifier: Modifier = Modifier) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.94f else 1f, Motion.Snappy, label = "pad")

    Box(
        modifier = modifier
            .size(70.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(CircleShape)
            .border(1.dp, Color(0xFF2A2A2C), CircleShape)
            .clickable(interactionSource = interaction, indication = null) {},
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFF17171A))
                .border(0.5.dp, Color(0xFF3A3A3E), CircleShape),
        )
    }
}

@Composable
fun FaceButton(label: String, modifier: Modifier = Modifier) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.94f else 1f, Motion.Snappy, label = "face")

    Box(
        modifier = modifier
            .size(36.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(CircleShape)
            .background(Ink.EmulatorControl)
            .border(0.5.dp, Color(0xFF3A3A3E), CircleShape)
            .clickable(interactionSource = interaction, indication = null) {},
        contentAlignment = Alignment.Center,
    ) {
        Label(label, Type.FaceButton, Color.White)
    }
}
