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

/*
class fire1_Education : AppCompatActivity() {
    private var viewList : ArrayList<View> = ArrayList<View>()
    private val imagesFIRE1 = listOf(R.raw.fire_ex1, R.raw.fire_ex2, R.raw.fire_ex3)
    private val textDescriptionsMASK = listOf(
        "1. 소화전 덮개를 열어 소화기를 꺼내십시오."
        , "2. 소화기의 안전핀을 제거하십시오."
        , "3. 소화기의 노즐을 화재 발생 부위로 조정하여 손잡이를 눌러 분사하십시오."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education_list)

        imagesFIRE1.forEachIndexed {nowIndex, idRawImage ->
            val nowView = layoutInflater.inflate(R.layout.activity_short_education, null)
            val imgView = nowView.findViewById<ImageView>(R.id.viewEducationImage)
            val txtView = nowView.findViewById<TextView>(R.id.textDescription)
            val canvas = GlideDrawableImageViewTarget(imgView)
            //val nextBtn = nowView.findViewById<Button>(R.id.nbutton)
            txtView.text = textDescriptionsMASK[nowIndex]
            if(nowIndex == imagesFIRE1.size-1){
                //nextBtn.visibility = View.VISIBLE
            }
            else{
                //nextBtn.visibility = View.INVISIBLE
            }
            //nextBtn.setOnClickListener {
                val intent  = Intent(this, fire2_Education::class.java)
                startActivity(intent)
            }
            //Glide.with(applicationContext).load(idRawImage).into(canvas)
            //viewList.add(nowView)
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
}*/
