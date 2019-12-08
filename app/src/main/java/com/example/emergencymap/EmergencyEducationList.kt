package com.example.emergencymap

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
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
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.dialog_emergencyeducation.*
import java.util.*

class EmergencyEducationList : AppCompatActivity() {
    lateinit var tts : TextToSpeech
    private var viewList : ArrayList<View> = ArrayList<View>()

    companion object {
        val imagesCPR = listOf(R.raw.cpr1, R.raw.cpr2, R.raw.cpr3, R.raw.cpr4)
        val imagesAED = listOf(R.raw.aed1, R.raw.aed2, R.raw.aed2)
        val imagesFireExtinguisher = listOf(R.raw.fire_ex1, R.raw.fire_ex2, R.raw.fire_ex3)
        val imagesFirePump = listOf(R.raw.fire_pump1, R.raw.fire_pump2, R.raw.fire_pump3, R.raw.fire_pump4, R.raw.fire_pump5)
        val imagesGasMask = listOf(R.raw.mask1, R.raw.mask2, R.raw.mask3, R.raw.mask4, R.raw.mask5, R.raw.mask6, R.raw.mask7)

        const val KEY_CONTENTS = "CONTENTS"
        const val CPR = "CPR"
        const val AED = "AED"
        const val FIRE_EXTINGUISHER = "FIRE EXTINGUISHER"
        const val FIRE_PUMP = "FIRE PUMP"
        const val GAS_MASK = "GAS MASK"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education_list)

        val nowContents = intent.getStringExtra(KEY_CONTENTS)
        val nowImageId = when(nowContents){
            CPR -> imagesCPR
            AED -> imagesAED
            FIRE_EXTINGUISHER -> imagesFireExtinguisher
            FIRE_PUMP -> imagesFirePump
            GAS_MASK -> imagesGasMask
            else -> {
                Log.d("EmergencyEducationImage", "없는 Contents : $nowContents")
                return
            }
        }

        val nowDescription = when(nowContents){
            CPR -> R.array.instructionCPR
            AED -> R.array.instructionAED
            FIRE_EXTINGUISHER -> R.array.instructionFireExtinguisher
            FIRE_PUMP -> R.array.instructionFirePump
            GAS_MASK -> R.array.instructionGasMask
            else -> {
                Log.d("EmergencyEducationDescr", "없는 Contents : $nowContents)")
                return
            }
        }

        nowImageId.forEachIndexed { nowIndex, idRawImage ->
            val nowView = layoutInflater.inflate(R.layout.activity_short_education, null)
            val imgView = nowView.findViewById<ImageView>(R.id.viewEducationImage)
            val txtView = nowView.findViewById<TextView>(R.id.textDescription)
            val canvas = GlideDrawableImageViewTarget(imgView)

            //val nextBtn = nowView.findViewById<Button>(R.id.nbutton)
            //nextBtn.visibility = View.INVISIBLE
            txtView.text = resources.getStringArray(nowDescription)[nowIndex]
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