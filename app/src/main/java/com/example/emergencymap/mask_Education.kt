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

class mask_Education : AppCompatActivity() {
    private var viewList : ArrayList<View> = ArrayList<View>()
    private val imagesMASK = listOf(R.raw.mask1, R.raw.mask2, R.raw.mask3, R.raw.mask4, R.raw.mask5, R.raw.mask6, R.raw.mask7)
    private val textDescriptionsMASK = listOf(
        "1.휴대주머니를 열어 포장된 방독면과 정화통을 꺼냅니다."
        , "2. 포장된 방독면의 은박포장과 정화통의 은박포장을 제거하십시오."
        , "3. 정화통 마개를 제거하고 시계방향으로 돌려 방독면에 장착하십시오."
        , "4. 방독면을 장착한 후 흡입막을 제거하십시오."
        , "5. 렌즈쪽을 아래로 향하도록 잡고, 방독면을 안전하게 착용하십시오."
        , "6. 방독면을 착용한 후에 얼굴에 밀착 되도록 머리끈을 조절하고 내부 목조임끈을 조이십시오."
        , "7. 정화통의 공기 흡입구를 손바닥으로 막고 숨을 길게 들이마셔 안면부가 얼굴에 밀착되는지 확인하십시오."
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