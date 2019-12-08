

package com.example.emergencymap

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class RecyclerAdapter(private val items: ArrayList<YoutubeItem>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val item = items[position]

        val listener = View.OnClickListener {
            val intent = Intent(it.context, HowToWithYoutube::class.java)

            intent.putExtra(HowToWithYoutube.ITEM_IS, when(position){
                0 -> HowToWithYoutube.CPR
                1 -> HowToWithYoutube.AED
                2 -> HowToWithYoutube.FIRE_EXTINGUISHER
                3 -> HowToWithYoutube.FIREPUMP
                4 -> HowToWithYoutube.GASMASK
                5 -> HowToWithYoutube.WATERITEM
                else -> HowToWithYoutube.AED
            })

            startActivity(it.context, intent, null)
        }

        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ViewHolder(inflatedView)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun bind(listener: View.OnClickListener, item: YoutubeItem) {
            view.thumbnail.setImageDrawable(item.image)
            view.title.text = item.title
            view.setOnClickListener(listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

}

