package `in`.knightcoder.wallpaper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import `in`.knightcoder.wallpaper.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject
import java.util.Locale

class MainActivity : AppCompatActivity(), WallpaperAdapter.WallpaperCallBack {
    private lateinit var binding: ActivityMainBinding
    private lateinit var wallpaperAdapter: WallpaperAdapter
    var wallpaperModelList = mutableListOf<WallpaperModel>()
    var pageNumber: Int = 1

    var isScrolling: Boolean = false
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0
    var url: String = "https://api.pexels.com/v1/curated/?page=$pageNumber&per_page=80"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wallpaperModelList = ArrayList()
        wallpaperAdapter = WallpaperAdapter(this, wallpaperModelList)

        binding.recyclerView.setAdapter(wallpaperAdapter)

        val gridLayoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.setLayoutManager(gridLayoutManager)

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                currentItems = gridLayoutManager.childCount
                totalItems = gridLayoutManager.itemCount
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition()

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false
                    fetchWallpaper()
                }
            }
        })


        fetchWallpaper()
    }

    fun fetchWallpaper() {
        val request: StringRequest =
            object : StringRequest(Method.GET, url, Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)

                    val jsonArray = jsonObject.getJSONArray("photos")

                    val length = jsonArray.length()

                    for (i in 0 until length) {
                        val `object` = jsonArray.getJSONObject(i)

                        val id = `object`.getInt("id")

                        val objectImages = `object`.getJSONObject("src")

                        val orignalUrl = objectImages.getString("original")
                        val mediumUrl = objectImages.getString("medium")

                        val wallpaperModel = WallpaperModel(id, orignalUrl, mediumUrl)
                        wallpaperModelList!!.add(wallpaperModel)
                    }

                    wallpaperAdapter!!.notifyDataSetChanged()
                    pageNumber++
                } catch (e: JSONException) {
                }
            }, Response.ErrorListener { }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Authorization"] =
                        "563492ad6f917000010000011069c623c7ad4919b7f934771d8adbe9"

                    return params
                }
            }

        val requestQueue = Volley.newRequestQueue(applicationContext)
        requestQueue.add(request)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_search) {
            val alert = AlertDialog.Builder(this)
            val editText = EditText(this)
            editText.textAlignment = View.TEXT_ALIGNMENT_CENTER

            alert.setMessage("Enter Category e.g. Nature")
            alert.setTitle("Search Wallpaper")

            alert.setView(editText)

            alert.setPositiveButton("Yes") { _, i ->
                val query = editText.text.toString().lowercase(Locale.getDefault())
                url = "https://api.pexels.com/v1/search/?page=$pageNumber&per_page=80&query=$query"
                wallpaperModelList.clear()
                fetchWallpaper()
            }

            alert.setNegativeButton("No") { _, i -> }

            alert.show()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(item: WallpaperModel) {
        startActivity(
            Intent(this, FullScreenWallpaper::class.java).putExtra(
                "originalUrl", item.originalUrl
            )
        )
    }
}