package `in`.knightcoder.wallpaper.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.knightcoder.wallpaper.model.WallpaperModel
import `in`.knightcoder.wallpaper.repository.WallpaperRepository
import kotlinx.coroutines.launch

class WallpaperViewModel(private val repository: WallpaperRepository) : ViewModel() {

    val wallpapers = MutableLiveData<List<WallpaperModel>>()
    val errorMessage = MutableLiveData<String>()

    fun getCuratedWallpapers(page: Int, perPage: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCuratedWallpapers(page, perPage)
                val oldValue = wallpapers.value?.toMutableList() ?: mutableListOf()
                oldValue.addAll(response.photos)
                wallpapers.value = oldValue
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
        }
    }

    fun searchWallpapers(query: String, page: Int, perPage: Int) {
        viewModelScope.launch {
            try {
                val response = repository.searchWallpapers(query, page, perPage)
                val oldValue = wallpapers.value?.toMutableList() ?: mutableListOf()
                oldValue.addAll(response.photos)
                wallpapers.value = oldValue
            } catch (e: Exception) {
                errorMessage.value = e.message
            }
        }
    }
}
