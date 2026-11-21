package io.github.jaxenisr.dandelion.ui.nav

/** The four screens. Navigation is a plain stack in the view model, no navigation library. */
sealed interface Destination {
    data object Games : Destination
    data object Emulation : Destination
    data object Settings : Destination
    data class Details(val gameId: String) : Destination
}

/** Entries of the title menu. */
enum class MenuEntry(val title: String, val destination: Destination) {
    Games("Games", Destination.Games),
    Emulation("Emulation", Destination.Emulation),
    Settings("Settings", Destination.Settings),
}
