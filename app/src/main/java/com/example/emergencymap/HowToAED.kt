package com.example.emergencymap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.emergencymap.HowToolActivity.Companion.YOUTUBE_API_KEY
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayerView
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.internal.*
import kotlinx.android.synthetic.main.activity_howto_aed.*
import kotlinx.android.synthetic.main.activity_howtool.*

class HowToAED : YouTubeBaseActivity() {

    companion object{
        var VIDEO_ID = arrayOf("A_3fH2zZ9i4", "kcmLkxibz7I", "GsN85aHcl14", "09puep_BEu8")
        val YOUTUBE_API_KEY: String = "AIzaSyDMvyFMCLyeilRPFf4WHuge72wUg6eXZJ4"
    }
    lateinit var youtubePlayerInit: YouTubePlayer.OnInitializedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_howto_aed)
        setTitle("AED사용법")
        val intent = Intent(this, HowToolActivity::class.java)
        initUI()
    }
    private fun initUI() {
        youtubePlayerInit = object :YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, youTubePlayer: YouTubePlayer, p2: Boolean) {
                //if(VIDEO_ID != null)
                    val aed = intent.getStringExtra(VIDEO_ID[0]) //유튜브 영상 작동 액티비티 간소화
                    val mask = intent.getStringExtra(VIDEO_ID[1])
                    val fire = intent.getStringExtra(VIDEO_ID[2])
                    val sea = intent.getStringExtra(VIDEO_ID[3])

                        youTubePlayer.loadVideo(aed)
                        youTubePlayer.loadVideo(mask)
                        youTubePlayer.loadVideo(fire)
                        youTubePlayer.loadVideo(sea)

            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
            Toast.makeText(applicationContext, "something went worng", Toast.LENGTH_SHORT).show()
            }
        }
        playaedvideo.setOnClickListener(View.OnClickListener { v ->
            youtube_view.initialize(YOUTUBE_API_KEY, youtubePlayerInit)
        })
    }
}
