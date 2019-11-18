package com.example.emergencymap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Video.Thumbnails.VIDEO_ID
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_howto_aed.*
import kotlinx.android.synthetic.main.activity_howtool.*


class HowToolActivity : YouTubeBaseActivity() {

    companion object{
        var VIDEO_ID = arrayOf<String>("A_3fH2zZ9i4", "kcmLkxibz7I", "GsN85aHcl14", "09puep_BEu8")
        val YOUTUBE_API_KEY: String = "AIzaSyDMvyFMCLyeilRPFf4WHuge72wUg6eXZJ4"
    }
        lateinit var youtubePlayerInit: YouTubePlayer.OnInitializedListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_howtool)
        setTitle("구조 용품 사용법")
        initUI()

        aed_btn.setOnClickListener {
            val intent = Intent(this, HowToAED::class.java)
            intent.putExtra(VIDEO_ID[0], "A_3fH2zZ9i4")
            startActivity(intent)
        }

        bangdok_btn.setOnClickListener {
            val intent = Intent(this, HowToAED::class.java)
            intent.putExtra(VIDEO_ID[1], "kcmLkxibz7I")
            startActivity(intent)
        }

        fireProtect_btn.setOnClickListener {
             val intent = Intent(this, HowToAED::class.java)
            intent.putExtra(VIDEO_ID[2], "GsN85aHcl14")
            startActivity(intent)
        }

        seaProtect_btn.setOnClickListener{
            val intent = Intent(this, HowToAED::class.java)
            intent.putExtra(VIDEO_ID[3], "09puep_BEu8")
            startActivity(intent)
        }

    }

    private fun initUI() {
        youtubePlayerInit = object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, youTubePlayer: YouTubePlayer, p2: Boolean) {

                    youTubePlayer.loadVideo(VIDEO_ID[0])

            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
                Toast.makeText(applicationContext, "something went worng", Toast.LENGTH_SHORT).show()
            }

        }
       // playaedvideo.setOnClickListener(View.OnClickListener { v ->
        //    youtube_view.initialize(YOUTUBE_API_KEY, youtubePlayerInit)
        //})
    }

}
