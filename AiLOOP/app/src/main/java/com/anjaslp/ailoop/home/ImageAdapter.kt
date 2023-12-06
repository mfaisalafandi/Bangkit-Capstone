package com.anjaslp.ailoop.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.anjaslp.ailoop.R

class ImageAdapter (
    private val listImage: List<Int>
) : RecyclerView.Adapter<ImageAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.image_adapter, parent, false)
    )

    override fun onBindViewHolder(holder: ImageAdapter.ViewHolder, position: Int) {
        holder.image.setImageResource(listImage[position])
    }

    override fun getItemCount()= listImage.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image = view.findViewById<ImageView>(R.id.imageView)
    }
}