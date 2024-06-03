package `in`.knightcoder.wallpaper.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `in`.knightcoder.wallpaper.R
import `in`.knightcoder.wallpaper.api.RetrofitInstance
import `in`.knightcoder.wallpaper.databinding.ActivityHomeBinding
import `in`.knightcoder.wallpaper.model.WallpaperModel
import `in`.knightcoder.wallpaper.repository.WallpaperRepository
import `in`.knightcoder.wallpaper.ui.setWallpaper.SetWallpaperActivity
import java.util.Locale

class HomeActivity : AppCompatActivity(), WallpaperAdapter.WallpaperCallBack {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var wallpaperAdapter: WallpaperAdapter
    private lateinit var viewModel: WallpaperViewModel
    var wallpaperModelList = mutableListOf<WallpaperModel>()
    var pageNumber: Int = 1

    var isScrolling: Boolean = false
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0
    var url: String = "https://api.pexels.com/v1/curated/?page=$pageNumber&per_page=80"

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, WallpaperViewModelFactory(
                WallpaperRepository(
                    RetrofitInstance.api
                )
            )
        )[WallpaperViewModel::class.java]

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

        viewModel.wallpapers.observe(this) { wallpapers ->
            wallpaperModelList.addAll(wallpapers)
            wallpaperAdapter.notifyDataSetChanged()
            Log.e("wallpaperModelList", wallpaperModelList.toString())
        }

        viewModel.errorMessage.observe(this) { message ->
//            Toast.makeText(this@HomeActivity, message, Toast.LENGTH_SHORT).show()
        }


        fetchWallpaper()
    }

    fun fetchWallpaper() {
        viewModel.getCuratedWallpapers(pageNumber, 80)
        pageNumber++
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
                viewModel.searchWallpapers(query, pageNumber, 80)
            }

            alert.setNegativeButton("No") { _, i -> }

            alert.show()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(item: WallpaperModel) {
        startActivity(
            Intent(this, SetWallpaperActivity::class.java).putExtra(
                "originalUrl", item.src.original
            )
        )
    }
}