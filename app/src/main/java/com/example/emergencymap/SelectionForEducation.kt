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

        list.add(YoutubeItem(getDrawable(R.drawable.thumbnail_cpr)!!, getString(R.string.title1), intent))
        list.add(YoutubeItem(getDrawable(R.drawable.thumbnail_aed)!!, getString(R.string.title2), intent))
        list.add(YoutubeItem(getDrawable(R.drawable.thumbnail_fire_extinguisher)!!, getString(R.string.title3), intent))
        list.add(YoutubeItem(getDrawable(R.drawable.thumbnail_fire_pump)!!, getString(R.string.title4), intent))
        list.add(YoutubeItem(getDrawable(R.drawable.thumbnail_gasmask)!!, getString(R.string.title5), intent))
        list.add(YoutubeItem(getDrawable(R.drawable.thumbnail_boat)!!, getString(R.string.title6), intent))


        val adapter = YoutubeSelectionRecyclerViewAdapter(list)
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL))/*구분선 표시*/
        }

}
