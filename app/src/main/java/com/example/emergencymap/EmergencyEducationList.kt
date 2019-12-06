package com.example.emergencymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import kotlinx.android.synthetic.main.activity_education_list.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_short_education.*
import org.w3c.dom.Text
import java.lang.Error
import java.util.*
import kotlin.collections.ArrayList

class EmergencyEducationList : AppCompatActivity() {
    lateinit var tts : TextToSpeech
    private var viewList : ArrayList<View> = ArrayList<View>()
    private val imagesAED = listOf(R.raw.aed1, R.raw.aed2, R.raw.aed2)
    private val textAED = listOf("1.AED를 열어 전원을 누릅니다.", "2. 봉투를 열어 패드를 꺼내고 분리해줍니다.", "3. 패드를 다음과 같이 환자에게 부착합니다.")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education_list)

        tts = TextToSpeech(this, TextToSpeech.OnInitListener {
            fun onInit(status: Int){
                if(status != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.KOREA)
                }
            }
        })
        var i = 0
        imagesAED.forEach {idRawImage ->
            val nowView = layoutInflater.inflate(R.layout.activity_short_education, null)
            val imgView = nowView.findViewById<ImageView>(R.id.viewEducationImage)
            val txtView = nowView.findViewById<TextView>(R.id.textView)
            val canvas = GlideDrawableImageViewTarget(imgView)
            txtView.text = textAED[i]
            //tts.speak(textAED[i], TextToSpeech.QUEUE_FLUSH, null)
            //이 형식으로 넣으면 됨(tts)
            Glide.with(applicationContext).load(idRawImage).into(canvas)
            viewList.add(nowView)
            i++
        }

        pagerEducation.adapter = PagerEducationAdapter()
    }

    inner class PagerEducationAdapter : PagerAdapter() {
        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun getCount(): Int {
            return viewList.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var temp = viewList[position]
            container.addView(temp)

            return viewList[position]
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            pagerEducation.removeView(obj as View)
        }
    }
}