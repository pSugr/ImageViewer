package com.mysugr.scratch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity() {

    private val IMAGE_DATA_URL = "http://assets.mysugr.com/test/images/images.json"

    private lateinit var recyclerView: RecyclerView

    private val httpDownloader: HttpDownloader = HttpDownloader()
    private val remoteDataSource: RemoteDataSource = RemoteDataSource(IMAGE_DATA_URL, httpDownloader)
    private val imageAdapter: ImageAdapter = ImageAdapter({ url, onImageLoaded -> httpDownloader.downloadImage(url, onImageLoaded) })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView();
        loadData()
    }

    private fun loadData() {
        remoteDataSource.loadDataAsync { res -> fillListFromJson(res) }
    }

    private fun fillListFromJson(imageList: ArrayList<Pair<String, String>>) {
        imageAdapter.imageUrls = imageList.map { it.first }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.my_recycler_view) as RecyclerView
        recyclerView.adapter = imageAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

}
