package com.example.emergencymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import kotlinx.android.synthetic.main.activity_education_list.*

class EmergencyEducationList : AppCompatActivity() {

    var viewList : ArrayList<View> = ArrayList<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education_list)

        for(nowPosition in 0 .. 2){
            val nowView = layoutInflater.inflate(R.layout.activity_short_education, null)

            val rabbit1 = nowView.findViewById(R.id.aed1_view) as ImageView
            val gifImage1 = GlideDrawableImageViewTarget(rabbit1)
            when(nowPosition){
                0 -> {
                    Glide.with(applicationContext).load(R.raw.aed1).into(gifImage1)
                }
                1 -> {
                    Glide.with(applicationContext).load(R.raw.aed2).into(gifImage1)
                }
                2 -> {
                    Glide.with(applicationContext).load(R.raw.aed3).into(gifImage1)
                }
            }

            viewList.add(nowView)
        }

        pageview.adapter = pagerAdapter()
        pageview.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

            }

        })

    }

    inner class pagerAdapter : PagerAdapter() {
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
            pageview.removeView(obj as View)
        }
    }
}