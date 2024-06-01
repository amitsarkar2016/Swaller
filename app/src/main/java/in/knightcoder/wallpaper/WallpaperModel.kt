package `in`.knightcoder.wallpaper

data class WallpaperResponse(val photos: List<WallpaperModel>)

data class WallpaperModel(
    var id: Int = 0,
    var originalUrl: String? = null,
    var mediumUrl: String? = null,
)
