package io.github.jaxenisr.dandelion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jaxenisr.dandelion.DandelionViewModel
import io.github.jaxenisr.dandelion.data.Game
import io.github.jaxenisr.dandelion.ui.Ink
import io.github.jaxenisr.dandelion.ui.Type
import io.github.jaxenisr.dandelion.ui.components.CoverArt
import io.github.jaxenisr.dandelion.ui.components.IconKind
import io.github.jaxenisr.dandelion.ui.components.Label
import io.github.jaxenisr.dandelion.ui.components.RoundSymbolButton
import io.github.jaxenisr.dandelion.ui.components.TitleMenuPage
import io.github.jaxenisr.dandelion.ui.components.tapScale
import io.github.jaxenisr.dandelion.ui.nav.MenuEntry

@Composable
fun GamesScreen(vm: DandelionViewModel) {
    TitleMenuPage(
        title = "Games",
        subtitle = "Nintendo 3DS",
        menuOpen = vm.menuOpen,
        onMenuOpenChange = vm::setMenuExpanded,
        current = MenuEntry.Games,
        onMenuSelect = vm::openMenuEntry,
        topRight = {
            RoundSymbolButton(IconKind.Plus, "Add game") {}
            RoundSymbolButton(IconKind.Ellipsis, "More options") {}
        },
    ) {
        Spacer(Modifier.height(26.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
        ) {
            vm.games.chunked(2).forEach { pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                    pair.forEach { game ->
                        GameCell(game, Modifier.weight(1f)) { vm.openDetails(game) }
                    }
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
                Spacer(Modifier.height(28.dp))
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun GameCell(game: Game, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(modifier.tapScale(onClick = onClick, scale = 1.02f, alpha = 1f)) {
        CoverArt(game, Modifier.fillMaxWidth().aspectRatio(163f / 202f))
        Spacer(Modifier.height(12.dp))
        Label(game.title, Type.GameName, maxLines = 1)
        Spacer(Modifier.height(3.dp))
        Label(game.size, Type.GameSize, Ink.Secondary, maxLines = 1)
    }
}
