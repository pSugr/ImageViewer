package com.mysugr.scratch

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.InputStream
import java.net.URL

/**
 * Created by pmessenger on 09.04.16.
 */

class HttpDownloader {

    fun downloadImage(url: String, onImageLoaded: (Bitmap?) -> Unit) {
        downloadAsync(url, onImageLoaded, { inputStream -> getBitmapFromStream(inputStream) })
    }

    fun downloadText(url: String, onTextLoaded: (String?) -> Unit) {
        downloadAsync(url, onTextLoaded, { inputStream -> getStringFromStream(inputStream) })
    }

    private fun <T> downloadAsync(url: String, onDownloadComplete: (T?) -> Unit, transformation: (InputStream) -> T) {
        Observable
                .create(Observable.OnSubscribe<T> { subscriber -> downloadAndTransform(url, subscriber, transformation) })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(DownloadSubscriber(onDownloadComplete))
    }


    private fun <T> downloadAndTransform(url: String, subscriber: Subscriber<in T>, transformation: (inputStream: InputStream) -> T) {

        val connection = URL(url).openConnection()
        val transformedObject = transformation(connection.inputStream)
        subscriber.onNext(transformedObject)
        subscriber.onCompleted()
    }

    private fun getBitmapFromStream(inputStream: InputStream) = BitmapFactory.decodeStream(inputStream)
    private fun getStringFromStream(inputStream: InputStream) = inputStream.bufferedReader().readText()
}

private class DownloadSubscriber<T>(private val onDownloadComplete: (T?) -> Unit) : Subscriber<T>() {
    override fun onNext(result: T) {
        onDownloadComplete(result)
    }

    override fun onCompleted() {
        print("onCompleted")
    }

    override fun onError(e: Throwable?) {
        print("Error: $e");
    }
}
