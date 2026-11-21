package io.github.jaxenisr.dandelion.ui.components

import android.os.Build
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import io.github.jaxenisr.dandelion.ui.Ink
import io.github.jaxenisr.dandelion.ui.Motion
import io.github.jaxenisr.dandelion.ui.Type
import io.github.jaxenisr.dandelion.ui.nav.MenuEntry
import kotlin.math.roundToInt
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val HeaderTopPadding = 12.dp
private val HeaderButtons = 34.dp
private val HeaderTitleGap = 20.dp

private val MenuLeft = 12.dp
private val MenuTop = 48.dp
private val MenuWidth = 292.dp
private val MenuHeight = 222.5.dp
private val MenuCorner = 22.dp
private val MenuInset = 12.dp
private val MenuTitleBlock = 72.dp
private val MenuRowHeight = 46.dp

private val BlurRadius = 24.dp
private val ScrimAlpha = 0.06f
private val TitleDrift = 4.dp
private const val HeaderInMillis = 80L
private const val RowStaggerMillis = 25L
private const val BlurSteps = 24f

/**
 * Page header shared by Games and Settings: round buttons at the top right, the large title
 * with its chevron, and the glass menu that opens over the page.
 */
@Composable
fun TitleMenuPage(
    title: String,
    subtitle: String,
    menuOpen: Boolean,
    onMenuOpenChange: (Boolean) -> Unit,
    current: MenuEntry,
    onMenuSelect: (MenuEntry) -> Unit,
    topRight: @Composable RowScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(MenuCorner)

    // One clock for the whole overlay: it opens with Gentle and closes on a short tween, and the
    // content only leaves the composition once the close has finished.
    val open = remember { Animatable(0f) }
    var present by remember { mutableStateOf(false) }

    LaunchedEffect(menuOpen) {
        if (menuOpen) {
            present = true
            open.animateTo(1f, Motion.Gentle)
        } else if (present) {
            open.animateTo(0f, Motion.Close)
            present = false
        }
    }

    val scrim by animateFloatAsState(
        targetValue = if (menuOpen) 1f else 0f,
        animationSpec = Motion.Fade,
        label = "scrim",
    )

    // Taps dismiss only once the entrance is past half way, so an opening menu is never
    // dismissed by the finger that opened it.
    val dismissable by remember { derivedStateOf { menuOpen && open.value > 0.5f } }

    // Blur is applied in composition, so the radius is quantised to whole dp to keep the
    // duplicated page from recomposing on every frame.
    val blurRadius by remember {
        derivedStateOf { BlurRadius * (open.value * BlurSteps).roundToInt() / BlurSteps }
    }

    Box(Modifier.fillMaxSize().background(Ink.Background)) {
        BoxWithConstraints(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars)) {
            PageLayout(
                title = title,
                subtitle = subtitle,
                menuOpen = menuOpen,
                onTitleClick = { onMenuOpenChange(!menuOpen) },
                topRight = topRight,
                content = content,
            )

            Box(
                Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = ScrimAlpha * scrim))
                    .then(
                        if (dismissable) {
                            Modifier.tapOnly { onMenuOpenChange(false) }
                        } else {
                            Modifier
                        },
                    ),
            )

            if (present) {
                Box(
                    Modifier
                        .matchParentSize()
                        .graphicsLayer {
                            alpha = open.value.coerceIn(0f, 1f)
                            val scale = 0.96f + 0.04f * open.value
                            scaleX = scale
                            scaleY = scale
                            transformOrigin = TransformOrigin(0.12f, 0.1f)
                        },
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        // The page is painted a second time behind the menu, blurred and clipped to
                        // the menu shape, so the glass has something to show through.
                        Box(
                            Modifier
                                .matchParentSize()
                                .drawWithContent {
                                    val path = Path().apply {
                                        addRoundRect(
                                            RoundRect(
                                                rect = Rect(
                                                    Offset(MenuLeft.toPx(), MenuTop.toPx()),
                                                    Size(MenuWidth.toPx(), MenuHeight.toPx()),
                                                ),
                                                cornerRadius = CornerRadius(MenuCorner.toPx()),
                                            ),
                                        )
                                    }
                                    clipPath(path) { this@drawWithContent.drawContent() }
                                },
                        ) {
                            Box(Modifier.fillMaxSize().blur(blurRadius)) {
                                PageLayout(
                                    title = title,
                                    subtitle = subtitle,
                                    menuOpen = menuOpen,
                                    onTitleClick = {},
                                    topRight = topRight,
                                    content = content,
                                )
                            }
                        }
                    }

                    MenuSurface(
                        title = title,
                        current = current,
                        onSelect = onMenuSelect,
                        onClose = { onMenuOpenChange(false) },
                        shape = shape,
                        visible = menuOpen,
                    )
                }
            }
        }
    }
}

/** The translucent white panel itself: tint, border, shadow and the three entries. */
@Composable
private fun MenuSurface(
    title: String,
    current: MenuEntry,
    onSelect: (MenuEntry) -> Unit,
    onClose: () -> Unit,
    shape: RoundedCornerShape,
    visible: Boolean,
) {
    val header = remember { Animatable(0f) }
    val rows = remember { List(MenuEntry.entries.size) { Animatable(0f) } }

    // The title settles first, then the entries arrive one after another.
    LaunchedEffect(visible) {
        if (!visible) return@LaunchedEffect
        header.snapTo(0f)
        rows.forEach { it.snapTo(0f) }
        coroutineScope {
            launch { header.animateTo(1f, tween(HeaderInMillis.toInt(), easing = FastOutSlowInEasing)) }
            rows.forEachIndexed { index, row ->
                launch {
                    delay(HeaderInMillis + index * RowStaggerMillis)
                    row.animateTo(1f, Motion.Fade)
                }
            }
        }
    }

    Box(
        Modifier
            .padding(start = MenuLeft, top = MenuTop)
            .width(MenuWidth)
            .height(MenuHeight)
            .shadow(22.dp, shape, clip = false)
            .clip(shape)
            .border(0.5.dp, Color.White.copy(alpha = 0.9f), shape),
    ) {
        Box(
            Modifier
                .matchParentSize()
                .background(Color.White.copy(alpha = 0.66f))
                .tapOnly { },
        )

        Column(Modifier.matchParentSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MenuTitleBlock)
                    .padding(horizontal = MenuInset)
                    .graphicsLayer {
                        alpha = header.value
                        translationY = (1f - header.value) * TitleDrift.toPx()
                    }
                    .tapDim(onClose),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Label(title, Type.LargeTitle, maxLines = 1)
                Spacer(Modifier.width(9.dp))
                DandelionIcon(
                    kind = IconKind.ChevronDown,
                    modifier = Modifier
                        .size(16.dp)
                        .offset(y = 2.dp)
                        .graphicsLayer { rotationZ = 180f },
                    tint = Ink.Secondary,
                    strokeWidth = 2.dp,
                )
            }
            Hairline(Modifier.padding(horizontal = MenuInset), Color.Black.copy(alpha = 0.08f))
            MenuEntry.entries.forEachIndexed { index, entry ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(MenuRowHeight)
                        .graphicsLayer { alpha = rows[index].value }
                        .tapDim { onSelect(entry) },
                ) {
                    Row(
                        Modifier.fillMaxSize().padding(horizontal = MenuInset),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Label(entry.title, Type.MenuItem)
                        Spacer(Modifier.weight(1f))
                        if (entry == current) {
                            DandelionIcon(
                                kind = IconKind.Check,
                                modifier = Modifier.size(15.dp),
                                tint = Ink.Purple,
                                strokeWidth = 2.dp,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PageLayout(
    title: String,
    subtitle: String,
    menuOpen: Boolean,
    onTitleClick: () -> Unit,
    topRight: @Composable RowScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    // One chevron that turns over, never two icons swapping places.
    val chevron by animateFloatAsState(
        targetValue = if (menuOpen) 180f else 0f,
        animationSpec = Motion.Snappy,
        label = "chevron",
    )

    Column(Modifier.fillMaxSize().background(Ink.Background)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(HeaderTopPadding + HeaderButtons)
                .padding(top = HeaderTopPadding, end = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
            content = { topRight() },
        )
        Row(
            modifier = Modifier.padding(start = 24.dp, top = HeaderTitleGap).tapDim(onTitleClick),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Label(title, Type.LargeTitle, maxLines = 1)
            Spacer(Modifier.width(9.dp))
            DandelionIcon(
                kind = IconKind.ChevronDown,
                modifier = Modifier
                    .size(16.dp)
                    .offset(y = 2.dp)
                    .graphicsLayer { rotationZ = chevron },
                tint = Ink.Secondary,
                strokeWidth = 2.dp,
            )
        }
        Label(
            text = subtitle,
            style = Type.Caption,
            color = Ink.Secondary,
            modifier = Modifier.padding(start = 24.dp, top = 6.dp),
        )
        content()
    }
}
