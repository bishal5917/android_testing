package com.example.android_testing.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.android_testing.R
import com.example.android_testing.data.local.ShoppingItem
import javax.inject.Inject

class ItemAdapter @Inject constructor(
    private val glide : RequestManager
) : RecyclerView.Adapter<ItemAdapter.ImageViewHolder>() {
    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffCallback = object : DiffUtil.ItemCallback<ShoppingItem>(){
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem==newItem
        }
    }

    private val differ = AsyncListDiffer(this,diffCallback)

    var items : List<ShoppingItem>
        get()=differ.currentList
        set(value)=differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_shopping,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.apply {
            glide.load(item.imageUrl).into(findViewById(R.id.ivItem))
        }
        holder.itemView.findViewById<TextView>(R.id.tvItemName).text=item.name.toUpperCase()
        holder.itemView.findViewById<TextView>(R.id.tvQuantity).text=item.amount.toString()
        holder.itemView.findViewById<TextView>(R.id.tvPrice).text=item.price.toString()
    }

    fun setData(newData: List<ShoppingItem>) {
        items = newData
        notifyDataSetChanged()
    }
}