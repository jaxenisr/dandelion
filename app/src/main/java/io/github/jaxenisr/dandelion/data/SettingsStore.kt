package io.github.jaxenisr.dandelion.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dandelionStore by preferencesDataStore(name = "dandelion")

/** Settings live in DataStore, which is all the local storage this frontend needs. */
class SettingsStore(private val context: Context) {

    private object Keys {
        val cpuJit = booleanPreferencesKey("cpu_jit")
        val cpuClock = stringPreferencesKey("cpu_clock")
        val new3dsMode = booleanPreferencesKey("new_3ds_mode")
        val onlineLle = booleanPreferencesKey("online_lle_modules")
        val fastInterpreter = booleanPreferencesKey("fast_interpreter")
        val graphicsApi = stringPreferencesKey("graphics_api")
        val hardwareShaders = booleanPreferencesKey("hardware_shaders")
        val asyncPresentation = booleanPreferencesKey("async_presentation")
        val saveState = stringPreferencesKey("save_state")
    }

    suspend fun load(): EmulatorSettings {
        val stored = context.dandelionStore.data.first()
        val defaults = EmulatorSettings()
        return EmulatorSettings(
            cpuJit = stored[Keys.cpuJit] ?: defaults.cpuJit,
            cpuClock = stored[Keys.cpuClock] ?: defaults.cpuClock,
            new3dsMode = stored[Keys.new3dsMode] ?: defaults.new3dsMode,
            onlineLleModules = stored[Keys.onlineLle] ?: defaults.onlineLleModules,
            fastInterpreter = stored[Keys.fastInterpreter] ?: defaults.fastInterpreter,
            graphicsApi = stored[Keys.graphicsApi] ?: defaults.graphicsApi,
            hardwareShaders = stored[Keys.hardwareShaders] ?: defaults.hardwareShaders,
            asyncPresentation = stored[Keys.asyncPresentation] ?: defaults.asyncPresentation,
            saveState = stored[Keys.saveState] ?: defaults.saveState,
        )
    }

    suspend fun save(settings: EmulatorSettings) {
        context.dandelionStore.edit { stored ->
            stored[Keys.cpuJit] = settings.cpuJit
            stored[Keys.cpuClock] = settings.cpuClock
            stored[Keys.new3dsMode] = settings.new3dsMode
            stored[Keys.onlineLle] = settings.onlineLleModules
            stored[Keys.fastInterpreter] = settings.fastInterpreter
            stored[Keys.graphicsApi] = settings.graphicsApi
            stored[Keys.hardwareShaders] = settings.hardwareShaders
            stored[Keys.asyncPresentation] = settings.asyncPresentation
            stored[Keys.saveState] = settings.saveState
        }
    }
}
