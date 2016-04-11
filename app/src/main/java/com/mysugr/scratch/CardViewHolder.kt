package com.mysugr.scratch

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView

class CardViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    private val imageView = itemView!!.findViewById(R.id.card_image) as ImageView
    private val defaultImage = imageView.drawable;

    fun reset(){
        imageView.setImageDrawable(defaultImage)
    }

    fun setImage(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }
}