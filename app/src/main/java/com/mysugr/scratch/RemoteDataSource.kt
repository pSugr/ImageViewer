package com.mysugr.scratch

import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * Created by pmessenger on 11.04.16.
 */
class RemoteDataSource(private val url: String, private val downloader: HttpDownloader) {

    fun loadDataAsync(finishedAction: (ArrayList<Pair<String, String>>) -> Unit) {
        downloader.downloadText(url, { jsonString -> parseJson(jsonString, finishedAction) })
    }

    private fun parseJson(jsonString: String?, finishedAction: (ArrayList<Pair<String, String>>) -> Unit) {
        val res = arrayListOf<Pair<String, String>>()
        val jsonObject = JSONObject(jsonString)
        val jsonImageArray = jsonObject.getJSONArray("images");

        jsonImageArray.toIterable().forEach { jo -> res.add(jo.getString("url") to jo.getString("description")) }

        finishedAction(res)
    }

    private fun JSONArray.toIterable(): Iterable<JSONObject> = (0 until length()).asIterable().map { getJSONObject(it) }

}