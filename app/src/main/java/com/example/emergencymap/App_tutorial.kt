package com.example.emergencymap

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.activity_app_tutorial.*
import org.jetbrains.anko.commit

class App_tutorial : AppCompatActivity() {
    var viewList : ArrayList<View> = ArrayList<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_tutorial)
        val pref : SharedPreferences = getSharedPreferences("a", MODE_PRIVATE)
        var edit = pref.edit()
        var flag : Int = 0

       closeView.setOnClickListener {
           if(checkbox.isChecked){
               flag = 1
               edit.putInt("First", flag)
               edit.commit()
               finish()
           }
           else{
               flag = 0
               edit.putInt("First", flag)
               edit.commit()
               finish()
           }
       }

        buttonEmergency.setOnClickListener {
            tip.text = "응급상황 버튼으로 119신고와 환자에 맞는 응급처치 절차를 볼 수 있습니다."
        }

        buttonList.setOnClickListener {
            tip.text = "리스트 버튼을 통해서 세부 위치를 알 수 있습니다."
        }

        buttonSaveFromMap.setOnClickListener {
            tip.text = "현재 화면에 있는 응급구조용품을 저장하여 인터넷에 연결되어있지않아도 조회가 가능합니다."
        }

        buttonNowLocation.setOnClickListener {
            tip.text = "현재 위치로 돌아옵니다."
        }

        tableLayout.setOnClickListener{
            tip.text = "버튼을 클릭하면 응급구조용품의 위치를 알 수 있습니다."
        }

    }
}
