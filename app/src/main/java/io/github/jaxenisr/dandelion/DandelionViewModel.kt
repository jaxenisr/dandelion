package io.github.jaxenisr.dandelion

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.github.jaxenisr.dandelion.data.EmulatorSettings
import io.github.jaxenisr.dandelion.data.Game
import io.github.jaxenisr.dandelion.data.SampleLibrary
import io.github.jaxenisr.dandelion.data.SettingsStore
import io.github.jaxenisr.dandelion.ui.nav.Destination
import io.github.jaxenisr.dandelion.ui.nav.MenuEntry
import kotlinx.coroutines.launch

class DandelionViewModel(application: Application) : AndroidViewModel(application) {

    private val store = SettingsStore(application)
    private val history = mutableStateListOf<Destination>()

    val games: List<Game> = SampleLibrary.games

    var destination by mutableStateOf<Destination>(Destination.Games)
        private set

    var menuOpen by mutableStateOf(false)
        private set

    var selectedGame by mutableStateOf(SampleLibrary.games.first())
        private set

    var settings by mutableStateOf(EmulatorSettings())
        private set

    init {
        viewModelScope.launch { settings = store.load() }
    }

    fun setMenuExpanded(open: Boolean) {
        menuOpen = open
    }

    fun openDetails(game: Game) {
        selectedGame = game
        navigate(Destination.Details(game.id))
    }

    fun play() {
        navigate(Destination.Emulation)
    }

    fun openMenuEntry(entry: MenuEntry) {
        navigate(entry.destination)
    }

    fun back() {
        if (history.isNotEmpty()) {
            destination = history.removeAt(history.lastIndex)
        } else if (destination != Destination.Games) {
            destination = Destination.Games
        }
    }

    fun updateSettings(block: (EmulatorSettings) -> EmulatorSettings) {
        val updated = block(settings)
        settings = updated
        viewModelScope.launch { store.save(updated) }
    }

    private fun navigate(target: Destination) {
        if (target != destination) {
            history.add(destination)
        }
        destination = target
        menuOpen = false
    }
}
