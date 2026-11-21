# Design notes

Four mockups define the interface: Games with the title menu open, Emulation, Settings and Game
details. They were handed over as images outside the repository, so this folder records what was
measured from them instead of holding copies.

## Tokens

| Token | Value |
| --- | --- |
| Background | `#FFFFFF` |
| Text | `#000000` |
| Secondary text | `#86868B` |
| Hairline | `#E3E3E7` |
| Blue (action) | `#007AFF` |
| Green (toggle on) | `#34C759` |
| Purple (accent) | `#6E56E2` |

Type scale in `app/src/main/java/io/github/jaxenisr/dandelion/ui/Tokens.kt`: large title 33 sp bold,
screen title 24 sp bold, section title 20 sp bold, body and row values 17 sp, nav bar title and game
name 15 sp semibold, captions 13 sp, game size 12 sp. Inter ships as Regular, Medium, SemiBold and
Bold in `app/src/main/res/font`.

## Measurements

- List row height 46 dp, hairline 0.5 dp inset 24 dp on both sides.
- Page margins 24 dp. Covers 163 × 202 dp with 12 dp corners, 18 dp between columns.
- Round symbol buttons 34 dp across with a 0.5 dp hairline border and a 13 dp glyph.
- Titles and subtitles: 20 dp between the button row and the title, 6 dp between title and subtitle.
- Game details cover 140 × 194 dp, Play button 50 dp high with 12 dp corners, full width minus margins.
- Toggles 51 × 31 dp with a 27 dp white knob inset by 2 dp.
- Emulation: top screen 16:9 edge to edge, progress panel inset 64 dp with 10 dp corners, circle
  pad 70 dp with a 38 dp centre, ABXY buttons 36 dp in a 108 dp diamond, Continue pill 142 × 36 dp.

## Glass menu

Starts at the title, 292 dp wide, 22 dp corners, translucent white at 66%, 0.5 dp white border and
the only soft shadow in the app. On API 31 and newer the page behind it is drawn again and blurred
at 24 dp, clipped to the menu shape. It opens with a 160 ms fade and scale, nothing else animates.

The mockups are localised in German; the app ships English strings as specified in the brief.
