package com.example.emergencymap

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_howto.*
import kotlinx.android.synthetic.main.activity_howto.view.*

class HowToWithYoutube : YouTubeBaseActivity() {

    companion object{
        val VIDEO_ID = arrayOf(
            Pair("CPR(심폐소생술) 방법", "Zbp74ri21YE"),
            Pair("AED 사용법", "A_3fH2zZ9i4"),
            Pair("소화기 사용법", "rcW7sGDUaJM"),
            Pair("소화전 사용법", "GsN85aHcl14"),
            Pair("방독면 사용법", "kcmLkxibz7I"),
            Pair("해상 구조용품 사용법", "09puep_BEu8")
        )
        val YOUTUBE_API_KEY: String = "AIzaSyDMvyFMCLyeilRPFf4WHuge72wUg6eXZJ4"

        const val ITEM_IS = "ItemIs"
        const val CPR = 0
        const val AED = 1
        const val FIRE_EXTINGUISHER = 2
        const val FIREPUMP = 3
        const val GASMASK = 4
        const val WATERITEM = 5
    }
    lateinit var youtubePlayerInit: YouTubePlayer.OnInitializedListener
    var itemIs = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_howto)
        itemIs = intent.getIntExtra(ITEM_IS, itemIs)

        Log.d("Youtube Item Number", itemIs.toString())
        title = VIDEO_ID[itemIs].first
        initUI()
        youtube_view.initialize(YOUTUBE_API_KEY, youtubePlayerInit)
    }
    private fun initUI() {
        youtubePlayerInit = object :YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, youTubePlayer: YouTubePlayer, p2: Boolean){
                youTubePlayer.loadVideo(VIDEO_ID[itemIs].second)
                youTubePlayer.setOnFullscreenListener { youTubePlayer.setFullscreen(false) }
                youTubePlayer.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener{
                    override fun onAdStarted() {}
                    override fun onLoading() {}
                    override fun onVideoStarted() {}
                    override fun onLoaded(p0: String?) {
                        youTubePlayer.pause()
                    }
                    override fun onVideoEnded() {}
                    override fun onError(p0: YouTubePlayer.ErrorReason?) {}
                })
                youTubePlayer.pause()
            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
                Toast.makeText(applicationContext, "something went wrong", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
