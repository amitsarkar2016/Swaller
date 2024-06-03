package `in`.knightcoder.wallpaper.model

data class WallpaperModel(
    var id: Int = 0, val src: Url
)

data class Url(
    var original: String? = null,
    var medium: String? = null,
)
