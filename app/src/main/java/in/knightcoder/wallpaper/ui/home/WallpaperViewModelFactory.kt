package `in`.knightcoder.wallpaper.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import `in`.knightcoder.wallpaper.repository.WallpaperRepository

class WallpaperViewModelFactory(private val repository: WallpaperRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WallpaperViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WallpaperViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



