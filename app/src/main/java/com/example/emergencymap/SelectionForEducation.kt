package com.example.emergencymap

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_howtool.*


class SelectionForEducation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_howtool)
        title = "구조 용품 사용법"

        val list = ArrayList<YoutubeItem>()
        val intent = Intent(this, HowToWithYoutube::class.java)

        list.add(
            YoutubeItem(
                getDrawable(R.drawable.thumnailaed)!!,
                getString(R.string.title1),
                intent
            )
        )
        list.add(
            YoutubeItem(
                getDrawable(R.drawable.thumnailmask)!!,
                getString(R.string.title2),
                intent
            )
        )
        list.add(
            YoutubeItem(
                getDrawable(R.drawable.thumnailfire)!!,
                getString(R.string.title3),
                intent
            )
        )
        list.add(
            YoutubeItem(
                getDrawable(R.drawable.thumnailsea)!!,
                getString(R.string.title4),
                intent
            )
        )

        val adapter = RecyclerAdapter(list)
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(
          DividerItemDecoration(this, DividerItemDecoration.VERTICAL))/*구분선 표시*/

        }

}
