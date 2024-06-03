package `in`.knightcoder.wallpaper.repository

import `in`.knightcoder.wallpaper.api.ApiService

class WallpaperRepository(private val apiService: ApiService) {

    suspend fun getCuratedWallpapers(page: Int, perPage: Int) = apiService.getCuratedWallpapers(page, perPage)

    suspend fun searchWallpapers(query: String, page: Int, perPage: Int) = apiService.searchWallpapers(query, page, perPage)
}
