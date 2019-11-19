package com.example.emergencymap

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_howtool.*


class SelectionForEducation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_howtool)
        title = "구조 용품 사용법"

        aed_btn.setOnClickListener {
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
        }
    }
}
