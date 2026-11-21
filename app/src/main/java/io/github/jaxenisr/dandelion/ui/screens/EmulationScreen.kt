package io.github.jaxenisr.dandelion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jaxenisr.dandelion.DandelionViewModel
import io.github.jaxenisr.dandelion.data.Game
import io.github.jaxenisr.dandelion.ui.Ink
import io.github.jaxenisr.dandelion.ui.Type
import io.github.jaxenisr.dandelion.ui.components.CirclePad
import io.github.jaxenisr.dandelion.ui.components.FaceButton
import io.github.jaxenisr.dandelion.ui.components.GameplayArt
import io.github.jaxenisr.dandelion.ui.components.IconKind
import io.github.jaxenisr.dandelion.ui.components.Label
import io.github.jaxenisr.dandelion.ui.components.ProgressBar
import io.github.jaxenisr.dandelion.ui.components.RoundSymbolButton

@Composable
fun EmulationScreen(vm: DandelionViewModel) {
    val game = vm.selectedGame
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .windowInsetsPadding(WindowInsets.navigationBars),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            RoundSymbolButton(
                icon = IconKind.ChevronLeft,
                description = "Back",
                onClick = vm::back,
                modifier = Modifier.align(Alignment.CenterStart),
                dark = true,
                iconSize = 14.dp,
            )
            Label(
                text = game.title,
                style = Type.NavBarTitle,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center).padding(horizontal = 56.dp),
                maxLines = 1,
                align = TextAlign.Center,
            )
            RoundSymbolButton(
                icon = IconKind.Pause,
                description = "Pause",
                onClick = {},
                modifier = Modifier.align(Alignment.CenterEnd),
                dark = true,
                iconSize = 12.dp,
            )
        }

        GameplayArt(game, Modifier.fillMaxWidth().aspectRatio(16f / 9f))

        Spacer(Modifier.height(20.dp))
        LevelPanel(game)
        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 40.dp, end = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CirclePad()
            FaceButtons()
        }

        Spacer(Modifier.height(26.dp))
        Label(
            text = "Start · Select",
            style = Type.GameName,
            color = Ink.EmulatorSecondary,
            modifier = Modifier.fillMaxWidth(),
            align = TextAlign.Center,
        )
        Spacer(Modifier.height(28.dp))
    }
}

@Composable
private fun LevelPanel(game: Game) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 64.dp)
            .height(258.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Ink.EmulatorSurface)
            .padding(start = 26.dp, end = 26.dp, top = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Label(game.levelName, Type.SectionTitle, Color.White, maxLines = 1)
        Spacer(Modifier.height(7.dp))
        Label(
            text = "Level ${game.levelNumber} · Time ${game.levelTime}",
            style = Type.Caption,
            color = Ink.EmulatorSecondary,
            maxLines = 1,
        )
        Spacer(Modifier.height(24.dp))
        ProgressBar(game.levelProgress, Modifier.fillMaxWidth(), 4.dp)
        Spacer(Modifier.height(24.dp))
        Box(
            Modifier
                .width(142.dp)
                .height(36.dp)
                .clip(CircleShape)
                .background(Ink.Purple),
            contentAlignment = Alignment.Center,
        ) {
            Label("Continue", Type.Button.copy(fontSize = 15.sp), Color.White)
        }
    }
}

@Composable
private fun FaceButtons() {
    Box(Modifier.size(108.dp)) {
        FaceButton("X", Modifier.align(Alignment.TopCenter))
        FaceButton("Y", Modifier.align(Alignment.CenterStart))
        FaceButton("A", Modifier.align(Alignment.CenterEnd))
        FaceButton("B", Modifier.align(Alignment.BottomCenter))
    }
}
