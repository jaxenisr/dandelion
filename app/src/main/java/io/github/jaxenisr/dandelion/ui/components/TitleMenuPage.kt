package io.github.jaxenisr.dandelion.ui.components

import android.os.Build
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
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.dp
import io.github.jaxenisr.dandelion.ui.Ink
import io.github.jaxenisr.dandelion.ui.Type
import io.github.jaxenisr.dandelion.ui.nav.MenuEntry

private val HeaderTopPadding = 12.dp
private val HeaderButtons = 34.dp
private val HeaderTitleGap = 20.dp

internal val MenuLeft = 12.dp
internal val MenuTop = 48.dp
internal val MenuWidth = 292.dp
internal val MenuHeight = 222.5.dp
internal val MenuCorner = 22.dp

private val MenuTitleBlock = 72.dp
private val MenuRowHeight = 46.dp
private val MenuInset = 12.dp

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

    BoxWithConstraints(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars)) {
        PageLayout(
            title = title,
            subtitle = subtitle,
            menuOpen = menuOpen,
            onTitleClick = { onMenuOpenChange(!menuOpen) },
            topRight = topRight,
            content = content,
        )

        if (menuOpen) {
            Box(Modifier.matchParentSize().tapOnly { onMenuOpenChange(false) })

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // The page is painted a second time behind the menu, blurred and clipped to its shape.
                Box(
                    Modifier
                        .matchParentSize()
                        .drawWithContent {
                            val path = Path().apply {
                                addRoundRect(
                                    RoundRect(
                                        Rect(
                                            Offset(MenuLeft.toPx(), MenuTop.toPx()),
                                            Size(MenuWidth.toPx(), MenuHeight.toPx()),
                                        ),
                                        CornerRadius(MenuCorner.toPx()),
                                    ),
                                )
                            }
                            clipPath(path) { this@drawWithContent.drawContent() }
                        },
                ) {
                    Box(Modifier.fillMaxSize().blur(24.dp)) {
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
                            .tapDim { onMenuOpenChange(false) },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Label(title, Type.LargeTitle, maxLines = 1)
                        Spacer(Modifier.width(9.dp))
                        DandelionIcon(
                            kind = IconKind.ChevronUp,
                            modifier = Modifier.size(16.dp).offset(y = 2.dp),
                            tint = Ink.Secondary,
                            strokeWidth = 2.dp,
                        )
                    }
                    Hairline(Modifier.padding(horizontal = MenuInset), Color.Black.copy(alpha = 0.08f))
                    MenuEntry.entries.forEach { entry ->
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(MenuRowHeight)
                                .tapDim { onMenuSelect(entry) },
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
                kind = if (menuOpen) IconKind.ChevronUp else IconKind.ChevronDown,
                modifier = Modifier.size(16.dp).offset(y = 2.dp),
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
