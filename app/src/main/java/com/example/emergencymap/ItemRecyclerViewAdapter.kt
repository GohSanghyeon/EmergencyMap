package com.example.emergencymap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_one_item_line.view.*

class ItemRecyclerViewAdapter (private  val items : List<ListItemData>)
    : RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_one_item_line, parent, false)
        return ItemRecyclerViewAdapter.ViewHolder(inflatedView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nowItem = items[position]

        holder.apply{ bind(nowItem) }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bind(item: ListItemData) {
                view.imageView.setImageDrawable(item.image)
                view.textView1.text = item.address
                view.textView2.text = item.addressDetail
        }
    }

}