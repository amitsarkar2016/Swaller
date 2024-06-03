package `in`.knightcoder.wallpaper.api

import `in`.knightcoder.wallpaper.response.WallpaperResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers("Authorization: 563492ad6f917000010000011069c623c7ad4919b7f934771d8adbe9")
    @GET("curated")
    suspend fun getCuratedWallpapers(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): WallpaperResponse

    @Headers("Authorization: 563492ad6f917000010000011069c623c7ad4919b7f934771d8adbe9")
    @GET("search")
    suspend fun searchWallpapers(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): WallpaperResponse
}
