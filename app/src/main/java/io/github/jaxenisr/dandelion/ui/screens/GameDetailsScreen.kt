package io.github.jaxenisr.dandelion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.jaxenisr.dandelion.DandelionViewModel
import io.github.jaxenisr.dandelion.data.Options
import io.github.jaxenisr.dandelion.ui.Ink
import io.github.jaxenisr.dandelion.ui.Type
import io.github.jaxenisr.dandelion.ui.components.CoverArt
import io.github.jaxenisr.dandelion.ui.components.FilledButton
import io.github.jaxenisr.dandelion.ui.components.Hairline
import io.github.jaxenisr.dandelion.ui.components.IconKind
import io.github.jaxenisr.dandelion.ui.components.Label
import io.github.jaxenisr.dandelion.ui.components.RoundSymbolButton
import io.github.jaxenisr.dandelion.ui.components.TextRow

@Composable
fun GameDetailsScreen(vm: DandelionViewModel) {
    val game = vm.selectedGame

    Column(
        Modifier
            .fillMaxSize()
            .background(Ink.Background)
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RoundSymbolButton(IconKind.ChevronLeft, "Back", iconSize = 14.dp, onClick = vm::back)
            RoundSymbolButton(IconKind.Ellipsis, "More options") {}
        }

        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(24.dp))
            CoverArt(
                game = game,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(140.dp)
                    .height(194.dp),
            )
            Spacer(Modifier.height(18.dp))
            Label(
                text = game.title,
                style = Type.ScreenTitle,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                maxLines = 1,
                align = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            Label(
                text = "Nintendo 3DS · ${game.year} · ${game.size}",
                style = Type.Caption,
                color = Ink.Secondary,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                maxLines = 1,
                align = TextAlign.Center,
            )
            Spacer(Modifier.height(26.dp))
            FilledButton(
                text = "Play",
                color = Ink.Blue,
                modifier = Modifier.padding(horizontal = 24.dp),
                onClick = vm::play,
            )
            Spacer(Modifier.height(26.dp))

            TextRow("Save State", vm.settings.saveState, chevrons = true) {
                vm.updateSettings { it.copy(saveState = Options.next(it.saveState, Options.saveStates)) }
            }
            Hairline(Modifier.padding(horizontal = 24.dp))
            TextRow("Cheats", "Off")
            Hairline(Modifier.padding(horizontal = 24.dp))
            TextRow("Region", game.region)
            Hairline(Modifier.padding(horizontal = 24.dp))
            TextRow("Language", game.language)

            Spacer(Modifier.height(30.dp))
            Label(
                text = "Last played: ${game.lastPlayed}",
                style = Type.Caption,
                color = Ink.Secondary,
                modifier = Modifier.padding(horizontal = 24.dp),
            )
            Spacer(Modifier.height(48.dp))
        }
    }
}
