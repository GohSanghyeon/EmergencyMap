package com.example.emergencymap

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_app_tutorial.*
import kotlinx.android.synthetic.main.activity_app_tutorial.buttonEmergency
import kotlinx.android.synthetic.main.activity_app_tutorial.buttonList
import kotlinx.android.synthetic.main.activity_app_tutorial.buttonLocationAED
import kotlinx.android.synthetic.main.activity_app_tutorial.buttonLocationPharmacies
import kotlinx.android.synthetic.main.activity_app_tutorial.buttonNowLocation
import kotlinx.android.synthetic.main.activity_app_tutorial.buttonSaveFromMap

class TutorialActivity : AppCompatActivity() {
    var viewList : ArrayList<View> = ArrayList<View>()

    companion object{
        const val CHECKED_NEVER_SHOW_TUTORIAL = 1
        const val UNCHECKED_NEVER_SHOW_TUTORIAL = 0
        const val PREFERENCES_TUTORIAL_NAME = "tutorial"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_tutorial)
        val pref : SharedPreferences = getSharedPreferences(PREFERENCES_TUTORIAL_NAME, MODE_PRIVATE)
        val edit = pref.edit()
        var flag : Int = 0

        closeView.setOnClickListener {
            if(checkbox.isChecked){
                flag = CHECKED_NEVER_SHOW_TUTORIAL
                edit.putInt("First", flag)
                edit.apply()
                finish()
            }
            else{
                flag = UNCHECKED_NEVER_SHOW_TUTORIAL
                edit.putInt("First", flag)
                edit.apply()
                finish()
            }
        }

        buttonEmergency.setOnClickListener {
            tipView.text = "응급상황 버튼으로 119신고와 환자에 맞는 응급처치 절차를 볼 수 있습니다."
        }

        buttonList.setOnClickListener {
            tipView.text = "리스트 버튼을 통해서 세부 위치를 알 수 있습니다."
        }

        buttonSaveFromMap.setOnClickListener {
            tipView.text = "현재 화면에 있는 응급구조용품을 저장하여 인터넷에 연결되어있지않아도 조회가 가능합니다."
        }

        buttonNowLocation.setOnClickListener {
            tipView.text = "현재 위치로 돌아옵니다."
        }

        tableLayout.setOnClickListener{
            tipView.text = "버튼을 클릭하면 응급구조용품의 위치를 알 수 있습니다."
        }

        buttonLocationAED.setOnClickListener {
            tipView.text = "버튼을 클릭하면 응급구조용품의 위치를 알 수 있습니다."
        }

        buttonLocationShelters.setOnClickListener {
            tipView.text = "버튼을 클릭하면 응급구조용품의 위치를 알 수 있습니다."
        }

        buttonLocationPharmacies.setOnClickListener {
            tipView.text = "버튼을 클릭하면 응급구조용품의 위치를 알 수 있습니다."
        }

    }
}