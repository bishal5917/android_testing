package com.example.android_testing.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.android_testing.R
import com.example.android_testing.data.remote.response.ImageResponse
import com.example.android_testing.data.remote.response.ImageResult
import javax.inject.Inject

class ImageAdapter @Inject constructor(
    private val glide : RequestManager
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffCallback = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }
    }

    private val differ = AsyncListDiffer(this,diffCallback)

    var images : List<String>
              get()=differ.currentList
              set(value)=differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return images.size
    }

    //On click listeners
    private var onItemClickListener : ((String) -> Unit) ? = null

    fun setOnItemClickListener(listener:(String) -> Unit) {
        onItemClickListener = listener
    }
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val url = images[position]
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { click ->
                click(url)
            }
        }
        holder.itemView.apply {
            glide.load(url).into(findViewById(R.id.ivShoppingImage))
        }
    }

    fun setData(newData: ImageResponse) {
        val allImages = mutableListOf<String>()
        for (url : ImageResult in newData.hits){
            allImages.add(url.previewURL)
        }
        images = allImages
        notifyDataSetChanged()
    }
}