package io.github.jaxenisr.dandelion.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.jaxenisr.dandelion.R

/** Flat colour tokens. Nothing here comes from a Material colour role. */
object Ink {
    val Background = Color(0xFFFFFFFF)
    val Text = Color(0xFF000000)
    val Secondary = Color(0xFF86868B)
    val Hairline = Color(0xFFE3E3E7)
    val Blue = Color(0xFF007AFF)
    val Green = Color(0xFF34C759)
    val Purple = Color(0xFF6E56E2)

    val ToggleOff = Color(0xFFE5E5EA)
    val Track = Color(0xFF3A3A3C)
    val EmulatorSurface = Color(0xFF141416)
    val EmulatorControl = Color(0xFF1C1C1E)
    val EmulatorControlStroke = Color(0xFF2E2E31)
    val EmulatorSecondary = Color(0xFF8E8E93)
}

val Inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold),
)

private fun line(
    size: Int,
    weight: FontWeight,
    tracking: Double,
    height: Int,
): TextStyle = TextStyle(
    fontFamily = Inter,
    fontSize = size.sp,
    fontWeight = weight,
    letterSpacing = tracking.sp,
    lineHeight = height.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
)

/** Own type scale, modelled on the iOS text styles the screens in design/ use. */
object Type {
    val LargeTitle = line(33, FontWeight.Bold, -0.7, 40)
    val ScreenTitle = line(24, FontWeight.Bold, -0.4, 30)
    val SectionTitle = line(20, FontWeight.Bold, -0.3, 26)
    val Body = line(17, FontWeight.Normal, -0.2, 22)
    val Value = line(17, FontWeight.Normal, -0.1, 22)
    val MenuItem = line(17, FontWeight.Normal, -0.2, 22)
    val TabSelected = line(17, FontWeight.SemiBold, -0.2, 22)
    val TabIdle = line(17, FontWeight.Normal, -0.2, 22)
    val Button = line(17, FontWeight.SemiBold, -0.2, 22)
    val NavBarTitle = line(15, FontWeight.SemiBold, -0.1, 20)
    val GameName = line(15, FontWeight.SemiBold, -0.2, 20)
    val CoverTitle = line(14, FontWeight.SemiBold, 0.0, 18)
    val FaceButton = line(15, FontWeight.Medium, 0.0, 18)
    val Caption = line(13, FontWeight.Normal, 0.0, 18)
    val GameSize = line(12, FontWeight.Normal, 0.0, 16)
}
