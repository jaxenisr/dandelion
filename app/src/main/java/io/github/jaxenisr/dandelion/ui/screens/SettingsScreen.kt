package io.github.jaxenisr.dandelion.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.jaxenisr.dandelion.DandelionViewModel
import io.github.jaxenisr.dandelion.data.Options
import io.github.jaxenisr.dandelion.ui.Ink
import io.github.jaxenisr.dandelion.ui.components.Hairline
import io.github.jaxenisr.dandelion.ui.components.IconKind
import io.github.jaxenisr.dandelion.ui.components.RoundSymbolButton
import io.github.jaxenisr.dandelion.ui.components.SectionHeader
import io.github.jaxenisr.dandelion.ui.components.SegmentedRow
import io.github.jaxenisr.dandelion.ui.components.TextRow
import io.github.jaxenisr.dandelion.ui.components.TitleMenuPage
import io.github.jaxenisr.dandelion.ui.components.ToggleRow
import io.github.jaxenisr.dandelion.ui.nav.MenuEntry

@Composable
fun SettingsScreen(vm: DandelionViewModel) {
    val settings = vm.settings

    TitleMenuPage(
        title = "Settings",
        subtitle = "Nintendo 3DS",
        menuOpen = vm.menuOpen,
        onMenuOpenChange = vm::setMenuExpanded,
        current = MenuEntry.Settings,
        onMenuSelect = vm::openMenuEntry,
        topRight = { RoundSymbolButton(IconKind.Ellipsis, "More options") {} },
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(34.dp))
            SectionHeader("Core", "General")
            Spacer(Modifier.height(10.dp))

            ToggleRow("CPU JIT", settings.cpuJit) { checked ->
                vm.updateSettings { it.copy(cpuJit = checked) }
            }
            Hairline(Modifier.padding(horizontal = 24.dp))
            TextRow("CPU Clock", settings.cpuClock, chevrons = true) {
                vm.updateSettings { it.copy(cpuClock = Options.next(it.cpuClock, Options.cpuClocks)) }
            }
            Hairline(Modifier.padding(horizontal = 24.dp))
            SegmentedRow(
                title = "New 3DS Mode",
                options = listOf("Old", "New"),
                selected = if (settings.new3dsMode) "New" else "Old",
                onSelect = { option -> vm.updateSettings { it.copy(new3dsMode = option == "New") } },
            )
            Hairline(Modifier.padding(horizontal = 24.dp))
            ToggleRow("Online LLE Modules", settings.onlineLleModules) { checked ->
                vm.updateSettings { it.copy(onlineLleModules = checked) }
            }
            Hairline(Modifier.padding(horizontal = 24.dp))
            ToggleRow("Fast Interpreter", settings.fastInterpreter) { checked ->
                vm.updateSettings { it.copy(fastInterpreter = checked) }
            }

            Spacer(Modifier.height(40.dp))
            SectionHeader("Graphics", "Rendering")
            Spacer(Modifier.height(10.dp))

            TextRow("Graphics API", settings.graphicsApi, chevrons = true) {
                vm.updateSettings { it.copy(graphicsApi = Options.next(it.graphicsApi, Options.graphicsApis)) }
            }
            Hairline(Modifier.padding(horizontal = 24.dp))
            ToggleRow("Hardware Shaders", settings.hardwareShaders) { checked ->
                vm.updateSettings { it.copy(hardwareShaders = checked) }
            }
            Hairline(Modifier.padding(horizontal = 24.dp))
            ToggleRow("Async Presentation", settings.asyncPresentation) { checked ->
                vm.updateSettings { it.copy(asyncPresentation = checked) }
            }
            Spacer(Modifier.height(48.dp))
        }
    }
}
