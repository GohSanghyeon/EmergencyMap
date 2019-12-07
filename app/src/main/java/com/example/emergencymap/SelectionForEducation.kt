package com.example.emergencymap

import android.content.Intent
import android.os.Bundle
import android.system.Os.bind
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeIntents
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_howtool.*
import kotlinx.android.synthetic.main.list_item.view.*


class SelectionForEducation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_howtool)
        title = "구조 용품 사용법"

        val list = ArrayList<YoutubeItem>()
        val intent = Intent(this, HowToWithYoutube::class.java)

        list.add(YoutubeItem(getDrawable(R.drawable.thumnailaed)!!, getString(R.string.title1), intent))
        list.add(YoutubeItem(getDrawable(R.drawable.thumnailmask)!!, getString(R.string.title2), intent))
        list.add(YoutubeItem(getDrawable(R.drawable.thumnailfire)!!, getString(R.string.title3), intent))
        list.add(YoutubeItem(getDrawable(R.drawable.thumnailsea)!!, getString(R.string.title4), intent))


        val adapter = RecyclerAdapter(list)
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL))/*구분선 표시*/
        }



    /* intent.putExtra(HowToWithYoutube.ITEM_IS, HowToWithYoutube.AED)
     intent.putExtra(HowToWithYoutube.ITEM_IS, HowToWithYoutube.GASMASK)
     intent.putExtra(HowToWithYoutube.ITEM_IS, HowToWithYoutube.FIREITEM)
     intent.putExtra(HowToWithYoutube.ITEM_IS, HowToWithYoutube.WATERITEM)*/



    /*  val pos0 = adapter.getItemViewType(0).apply {
            val intent = Intent(applicationContext, HowToWithYoutube::class.java)
            intent.putExtra(HowToWithYoutube.ITEM_IS, HowToWithYoutube.AED)
            startActivity(intent)
        }*/
        /*val pos1 = adapter.getItemId(1).apply {
            val intent = Intent(applicationContext, HowToWithYoutube::class.java)
            intent.putExtra(HowToWithYoutube.ITEM_IS, HowToWithYoutube.AED)
            startActivity(intent)
        }
        val pos2 = adapter.getItemId(2)
        val pos3 = adapter.getItemId(3)*/










}





/*aed_btn.setOnClickListener {
           val intent = Intent(this, HowToWithYoutube::class.java)
               intent.putExtra(HowToWithYoutube.ITEM_IS, HowToWithYoutube.AED)
           startActivity(intent)
       }

       bangdok_btn.setOnClickListener {
           val intent = Intent(this, HowToWithYoutube::class.java)
           intent.putExtra(HowToWithYoutube.ITEM_IS, HowToWithYoutube.GASMASK)
           startActivity(intent)
       }

       fireProtect_btn.setOnClickListener {
            val intent = Intent(this, HowToWithYoutube::class.java)
           intent.putExtra(HowToWithYoutube.ITEM_IS, HowToWithYoutube.FIREITEM)
           startActivity(intent)
       }

       seaProtect_btn.setOnClickListener{
           val intent = Intent(this, HowToWithYoutube::class.java)
           intent.putExtra(HowToWithYoutube.ITEM_IS, HowToWithYoutube.WATERITEM)
           startActivity(intent)
       }*/