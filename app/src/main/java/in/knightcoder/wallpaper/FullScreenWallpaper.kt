package `in`.knightcoder.wallpaper

import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.IntentFilter
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import `in`.knightcoder.wallpaper.DownloadReceiver
import `in`.knightcoder.wallpaper.databinding.ActivityFullScreenWallpaperBinding
import java.io.IOException

class FullScreenWallpaper : AppCompatActivity() {
    private var originalUrl: String? = null
    private lateinit var binding: ActivityFullScreenWallpaperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenWallpaperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        originalUrl = intent.getStringExtra("originalUrl")
        Glide.with(this).load(originalUrl).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(binding.photoView)

        handleClick()
    }

    private fun handleClick() {
        binding.btnSetWallpaper.setOnClickListener {
            setWallpaperEvent()
        }
        binding.btnDownloadWallpaper.setOnClickListener {
            downloadWallpaperEvent()
        }
    }

    private fun setWallpaperEvent() {
        val wallpaperManager = WallpaperManager.getInstance(this)
        val bitmap = (binding.photoView.drawable as BitmapDrawable).bitmap
        try {
            wallpaperManager.setBitmap(bitmap)
            Toast.makeText(this, "Wallpaper Set", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun downloadWallpaperEvent() {
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(originalUrl)
        val request = DownloadManager.Request(uri)

        val fileName = uri.lastPathSegment
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        request.setMimeType("image/jpeg")

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        request.setTitle("Wallpaper Download")
        request.setDescription("Downloading wallpaper")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        val downloadId = downloadManager.enqueue(request)

        val onComplete = DownloadReceiver(downloadManager, downloadId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(
                    onComplete,
                    IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                    RECEIVER_NOT_EXPORTED
                )
            } else {
                registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            }
        } else {
            registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        }

        Toast.makeText(this, "Downloading Start", Toast.LENGTH_SHORT).show()
    }

}