package io.github.jaxenisr.dandelion.data

import androidx.compose.ui.graphics.Color

data class Game(
    val id: String,
    val title: String,
    val year: Int,
    val size: String,
    val region: String,
    val language: String,
    val lastPlayed: String,
    val levelName: String,
    val levelNumber: Int,
    val levelTime: String,
    val levelProgress: Float,
    val skyTop: Color,
    val skyBottom: Color,
    val horizon: Color,
)

/**
 * Placeholder library. No cover art ships with the app: every cover is drawn from these
 * palettes at runtime, so nothing here is game material from anyone else.
 */
object SampleLibrary {
    val games = listOf(
        Game(
            id = "aurora-quest",
            title = "Aurora Quest",
            year = 2019,
            size = "1.4 GB",
            region = "Europe",
            language = "English",
            lastPlayed = "today, 8:03",
            levelName = "Cloud Island",
            levelNumber = 3,
            levelTime = "2:14",
            levelProgress = 0.62f,
            skyTop = Color(0xFFA6D9F0),
            skyBottom = Color(0xFFF6DCC8),
            horizon = Color(0xFF6FBF8B),
        ),
        Game(
            id = "kart-rush-3d",
            title = "Kart Rush 3D",
            year = 2018,
            size = "1.2 GB",
            region = "Europe",
            language = "English",
            lastPlayed = "yesterday, 21:47",
            levelName = "Rainbow Shore",
            levelNumber = 5,
            levelTime = "1:52",
            levelProgress = 0.34f,
            skyTop = Color(0xFFFFC46B),
            skyBottom = Color(0xFFFF6FA8),
            horizon = Color(0xFF6E56E2),
        ),
        Game(
            id = "coral-world",
            title = "Coral World",
            year = 2020,
            size = "980 MB",
            region = "Europe",
            language = "English",
            lastPlayed = "last week",
            levelName = "Lagoon",
            levelNumber = 2,
            levelTime = "3:40",
            levelProgress = 0.81f,
            skyTop = Color(0xFFBDE9E4),
            skyBottom = Color(0xFFF4E3C6),
            horizon = Color(0xFF239FAD),
        ),
        Game(
            id = "moon-temple",
            title = "Moon Temple",
            year = 2021,
            size = "1.6 GB",
            region = "Japan",
            language = "English",
            lastPlayed = "last month",
            levelName = "Star Shrine",
            levelNumber = 7,
            levelTime = "0:58",
            levelProgress = 0.18f,
            skyTop = Color(0xFF1B1740),
            skyBottom = Color(0xFF33296E),
            horizon = Color(0xFF8B7BF0),
        ),
    )

    fun byId(id: String): Game = games.firstOrNull { it.id == id } ?: games.first()
}
