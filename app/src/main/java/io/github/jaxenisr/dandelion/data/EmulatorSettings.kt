package io.github.jaxenisr.dandelion.data

/**
 * Values shown on the settings screen. They are stored locally and persisted, but nothing
 * reads them yet: without an emulator core there is nothing that could act on them.
 */
data class EmulatorSettings(
    val cpuJit: Boolean = true,
    val cpuClock: String = "Default",
    val new3dsMode: Boolean = true,
    val onlineLleModules: Boolean = false,
    val fastInterpreter: Boolean = true,
    val graphicsApi: String = "Vulkan",
    val hardwareShaders: Boolean = true,
    val asyncPresentation: Boolean = true,
    val saveState: String = "Automatic",
)

object Options {
    val cpuClocks = listOf("Default", "25%", "50%", "75%", "150%")
    val graphicsApis = listOf("Vulkan", "OpenGL")
    val saveStates = listOf("Automatic", "Manual")

    fun next(current: String, values: List<String>): String {
        val index = values.indexOf(current)
        if (index < 0) return values.first()
        return values[(index + 1) % values.size]
    }
}
