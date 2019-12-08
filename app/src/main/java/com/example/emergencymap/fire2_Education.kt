package com.example.emergencymap


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

class fire2_Education : AppCompatActivity() {
    private var viewList : ArrayList<View> = ArrayList<View>()
    private val imagesMASK = listOf(R.raw.fire_pump1, R.raw.fire_pump2, R.raw.fire_pump3, R.raw.fire_pump4, R.raw.fire_pump5)
    private val textDescriptionsMASK = listOf(
        "1. 발신기의 비상벨을 눌러 화재 사실을 알리십시오."
        , "2. 소화전함을 열어 노즐과 호스를 화재 발생지점으로 이동하십시오."
        , "3. 호스 전개 시 꼬이지 않도록 하십시오."
        , "4. 벨브를 왼쪽으로 돌려 개방하십시오."
        , "5. 노즐을 왼쪽으로 돌려 화재 발생지점에 방수하여 화재를 진압하십시오."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education_list)

        imagesMASK.forEachIndexed {nowIndex, idRawImage ->
            val nowView = layoutInflater.inflate(R.layout.activity_short_education, null)
            val imgView = nowView.findViewById<ImageView>(R.id.viewEducationImage)
            val txtView = nowView.findViewById<TextView>(R.id.textDescription)
            val canvas = GlideDrawableImageViewTarget(imgView)
            //val nextBtn = nowView.findViewById<Button>(R.id.nbutton)
            //nextBtn.visibility = View.INVISIBLE
            txtView.text = textDescriptionsMASK[nowIndex]

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