package `in`.knightcoder.wallpaper.receiver

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri

class DownloadReceiver(private val downloadManager: DownloadManager, private val downloadId: Long) : BroadcastReceiver() {

    @SuppressLint("Range")
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
            val query = DownloadManager.Query()
            query.setFilterById(downloadId)
            val cursor: Cursor = downloadManager.query(query)
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    val downloadedUri: Uri = downloadManager.getUriForDownloadedFile(downloadId)
                    val openIntent = Intent(Intent.ACTION_VIEW)
                    openIntent.setDataAndType(downloadedUri, "image/*")
                    openIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context?.startActivity(openIntent)
                }
            }
            cursor.close()
        }
    }
}
