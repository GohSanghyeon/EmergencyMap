package com.example.emergencymap


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import kotlinx.android.synthetic.main.activity_education_list.*
import kotlin.collections.ArrayList

class cpr_Education : AppCompatActivity() {
    private var viewList : ArrayList<View> = ArrayList<View>()
    private val imagesCPR = listOf(R.raw.cpr1, R.raw.cpr2, R.raw.cpr3, R.raw.cpr4, R.raw.cpr_5, R.raw.cpr_6)
    private val textDescriptionsMASK = listOf(
        "1. 환자의 의식을 확인 후 119에 신고하십시오."
        , "2. 주변의 사람들에게 자동 심장 충격기(AED)를 찾아달라고 부탁하십시오."
        , "3. 손에 깍지를 끼고 손꿈치 부분으로 환자의 흉부를 압박하십시오."
        , "4. 분당 100~120회 약 5cm깊이로 압박하십시오."
        , "5. 팔꿈치는 수직을 유지하십시오."
        , "6. 환자가 의식을 회복하거나 119구급대원이 도착할 때까지 반복하십시오."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education_list)
        lateinit var nextBtn : Button
        imagesCPR.forEachIndexed {nowIndex, idRawImage ->
            val nowView = layoutInflater.inflate(R.layout.activity_short_education, null)
            //nextBtn = nowView.findViewById<Button>(R.id.nbutton)
            val imgView = nowView.findViewById<ImageView>(R.id.viewEducationImage)
            val txtView = nowView.findViewById<TextView>(R.id.textDescription)
            val canvas = GlideDrawableImageViewTarget(imgView)
            txtView.text = textDescriptionsMASK[nowIndex]
            Glide.with(applicationContext).load(idRawImage).into(canvas)
            viewList.add(nowView)
            if(nowIndex == imagesCPR.size-1){
                nextBtn.visibility = View.VISIBLE
            }
            else{
                nextBtn.visibility = View.INVISIBLE
            }
            nextBtn.setOnClickListener {
                val intent  = Intent(this, EmergencyEducationList::class.java)
                startActivity(intent)
            }
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