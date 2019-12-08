package com.example.emergencymap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.address_list.view.*

class ItemRecyclerViewAdapter (private  val items : ArrayList<LocationData>) : RecyclerView.Adapter<RecyclerView.ViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.address_list, parent, false)
        return YoutubeSelectionRecyclerViewAdapter.ViewHolder(inflatedView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private var view: View = v

        fun bind(listener: View.OnClickListener, item: LocationData) {
                view.imageView.setImageDrawable(item.image)
                view.textView1.text = item.address
                view.textView2.text = item.detailaddress
        }
    }

}