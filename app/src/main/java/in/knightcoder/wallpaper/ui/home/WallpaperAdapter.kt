package `in`.knightcoder.wallpaper.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import `in`.knightcoder.wallpaper.model.WallpaperModel
import `in`.knightcoder.wallpaper.databinding.ImageItemBinding

class WallpaperAdapter(
    private val wallpaperCallBack: WallpaperCallBack,
    private val wallpaperModelList: List<WallpaperModel>,
) : RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder>() {

    interface WallpaperCallBack {
        fun onItemClick(item: WallpaperModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        return WallpaperViewHolder(
            ImageItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        holder.bind(wallpaperModelList[position], position)
    }

    override fun getItemCount(): Int {
        return wallpaperModelList.size
    }

    inner class WallpaperViewHolder(private val binding: ImageItemBinding) :
        ViewHolder(binding.root) {
        fun bind(item: WallpaperModel, position: Int) {
            Glide.with(binding.imageViewItem)
                .load(item.src.original)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageViewItem)
            binding.imageViewItem.setOnClickListener {
                wallpaperCallBack.onItemClick(item)
            }
        }
    }
}
