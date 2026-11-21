package io.github.jaxenisr.dandelion.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.jaxenisr.dandelion.ui.Ink
import io.github.jaxenisr.dandelion.ui.Type

/**
 * iOS style press feedback: the surface dims while the finger is down and there is no ripple.
 */
fun Modifier.tapDim(onClick: () -> Unit): Modifier = composed {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    this
        .graphicsLayer { alpha = if (pressed) 0.55f else 1f }
        .clickable(interactionSource = interaction, indication = null, onClick = onClick)
}

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
            .tapDim(onClick)
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
    Box(
        modifier = Modifier
            .size(51.dp, 31.dp)
            .tapDim { onChange(!checked) }
            .clip(CircleShape)
            .background(if (checked) Ink.Green else Ink.ToggleOff)
            .padding(2.dp),
        contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        Box(Modifier.size(27.dp).clip(CircleShape).background(Color.White))
    }
}

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
            Label(value, Type.Value, Ink.Secondary, maxLines = 1)
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
        Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
            options.forEach { option ->
                val active = option == selected
                Label(
                    text = option,
                    style = if (active) Type.TabSelected else Type.TabIdle,
                    color = if (active) Ink.Text else Ink.Secondary,
                    modifier = Modifier.tapDim { onSelect(option) },
                )
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
            .tapDim(onClick)
            .clip(shape)
            .background(color),
        contentAlignment = Alignment.Center,
    ) {
        Label(text, style, Color.White)
    }
}

@Composable
fun ProgressBar(progress: Float, modifier: Modifier = Modifier, height: Dp = 4.dp) {
    Box(modifier.height(height).clip(CircleShape).background(Ink.Track)) {
        Box(
            Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(height)
                .clip(CircleShape)
                .background(Ink.Purple),
        )
    }
}

@Composable
fun CirclePad(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(70.dp)
            .clip(CircleShape)
            .border(1.dp, Color(0xFF2A2A2C), CircleShape),
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
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Ink.EmulatorControl)
            .border(0.5.dp, Color(0xFF3A3A3E), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Label(label, Type.FaceButton, Color.White)
    }
}
