package com.example.emergencymap

import android.view.View
import android.view.ViewGroup

class EmergencyMenuClickListener(val groupSelection: ViewGroup) : View.OnClickListener {
    override fun onClick(nowSelectionView: View?) {
        groupSelection.visibility = View.INVISIBLE

        when(nowSelectionView?.id){
          //  R.id.buttonEmergencySMS -> sendEmergencySMS()
          //  R.id.buttonEmergencyManual -> buttonEmergencyManual()
        }
    }

    private fun sendEmergencySMS() {
        //TODO : 비상신고 기능
    }

    private fun buttonEmergencyManual() {
        //TODO : 응급교육 기능
    }
}