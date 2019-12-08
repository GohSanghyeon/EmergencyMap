package com.example.emergencymap

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import kotlinx.android.synthetic.main.activity_education_list.*
import kotlinx.android.synthetic.main.dialog_emergencyeducation.*
import java.util.*
import kotlin.collections.ArrayList

class EmergencyEducationList : AppCompatActivity() {
    lateinit var tts : TextToSpeech
    private var viewList : ArrayList<View> = ArrayList<View>()
    private val imagesAED = listOf(R.raw.aed1, R.raw.aed2, R.raw.aed2)
    private val imagesFIRE_EX = listOf(R.raw.fire_ex_1, R.raw.fire_ex_2, R.raw.fire_ex_3)
    private val imagesFIRE_HY = listOf(R.raw.fire_hy_1, R.raw.fire_hy_2, R.raw.fire_hy_3, R.raw.fire_hy_4, R.raw.fire_hy_5)



    private val textDescriptionsAED = listOf(
        "1. AED를 열어 전원을 누릅니다.",
        "2. 봉투를 열어 패드를 꺼내고 분리해줍니다.",
        "3. 패드를 다음과 같이 환자에게 부착합니다."
    )
    private val textDescriptionFIRE_EX = listOf(
        "1. 소화전 덥개를 열어 소화기를 꺼냅니다.",
        "2. 소화기의 안전핀을 제거합니다.",
        "3. 소화기의 노즐을 화재 발생 부위로 조정하여 손잡이를 눌러 분사합니다."
    )
    private val textDescriptionFIRE_HY = listOf(
        "1. 발신기의 비상벨을 눌러 화재 사실을 알립니다.",
        "2. 소화전함을 열어 노즐과 호스를 화재 발생지점으로 이동합니다.",
        "3. 호스 전개 시 꼬이지 않게 합니다.",
        "4. 밸브를 왼쪽으로 돌려 개방합니다.",
        "5. 노즐을 왼쪽으로 돌려 화재 발생지점에 방수하여 화재를 진압합니다."
    )



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
            imagesAED.forEachIndexed {nowIndex, idRawImage ->
                val nowView = layoutInflater.inflate(R.layout.activity_short_education, null)
                val imgView = nowView.findViewById<ImageView>(R.id.viewEducationImage)
                val txtView = nowView.findViewById<TextView>(R.id.textDescription)
                val canvas = GlideDrawableImageViewTarget(imgView)

                txtView.text = textDescriptionsAED[nowIndex]

                //tts.speak(textAED[nowIndex], TextToSpeech.QUEUE_FLUSH, null)
                //이 형식으로 넣으면 됨(tts)
                Glide.with(applicationContext).load(idRawImage).into(canvas)
                viewList.add(nowView)

        }

       imagesFIRE_EX.forEachIndexed {nowIndex, idRawImage ->
            val nowView = layoutInflater.inflate(R.layout.activity_short_education, null)
            val imgView = nowView.findViewById<ImageView>(R.id.viewEducationImage)
            val txtView = nowView.findViewById<TextView>(R.id.textDescription)
            val canvas = GlideDrawableImageViewTarget(imgView)

            txtView.text = textDescriptionFIRE_EX[nowIndex]

            //tts.speak(textAED[nowIndex], TextToSpeech.QUEUE_FLUSH, null)
            //이 형식으로 넣으면 됨(tts)
            Glide.with(applicationContext).load(idRawImage).into(canvas)
            viewList.add(nowView)

        }


        imagesFIRE_HY.forEachIndexed {nowIndex, idRawImage ->
            val nowView = layoutInflater.inflate(R.layout.activity_short_education, null)
            val imgView = nowView.findViewById<ImageView>(R.id.viewEducationImage)
            val txtView = nowView.findViewById<TextView>(R.id.textDescription)
            val canvas = GlideDrawableImageViewTarget(imgView)

            txtView.text = textDescriptionFIRE_HY[nowIndex]

            //tts.speak(textAED[nowIndex], TextToSpeech.QUEUE_FLUSH, null)
            //이 형식으로 넣으면 됨(tts)
            Glide.with(applicationContext).load(idRawImage).into(canvas)
            viewList.add(nowView)

        }





        pagerEducation.adapter = PagerEducationAdapter()
    }

    private inner class PagerEducationAdapter : PagerAdapter() {


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