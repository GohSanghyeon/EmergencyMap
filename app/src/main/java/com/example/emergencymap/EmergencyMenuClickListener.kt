package com.example.emergencymap

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.emergencymap.EmergencyMenuClickListener.Companion.permissionForSMS
import com.example.emergencymap.notshowing.LocationProvider
import kotlinx.android.synthetic.main.dialog_sms.view.*
import org.jetbrains.anko.toast


class EmergencyMenuClickListener(
    private val groupSelection: ViewGroup
    , private val activity: AppCompatActivity)
    : View.OnClickListener {

    companion object{
        val permissionForSMS: Array<out String> = arrayOf(
            Manifest.permission.SEND_SMS
        )
    }

    var setting :Int = 0
    override fun onClick(nowSelectionView: View?) {
        groupSelection.visibility = View.INVISIBLE

        when(nowSelectionView?.id){
            R.id.buttonEmergencySMS ->
                if (PermissionManager.existDeniedpermission(activity, permissionForSMS))
                    requestSMSPermission()
                else
                    sendEmergencySMS()
            R.id.buttonEmergencyManual -> buttonEmergencyManual()
        }
    }

    private var messageNowLocation = ""

    private fun sendEmergencySMS() {
        // 비상신고 기능
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val builder = AlertDialog.Builder(activity)
        val dialogView = inflater.inflate(R.layout.dialog_sms, null)
        messageNowLocation = ""

        (activity as? MainActivity)?.let { mainActivity ->
            val editorSending = dialogView.findViewById<EditText>(R.id.editorSMSContents)

            if(!PermissionManager.existDeniedpermission(activity, LocationProvider.permissionForLocation))
                mainActivity.locationSource
                    .requestNowLocation(LocationProvider.OMIT_PERMISSION_CHECK_AND_CANCEL)
                    { nowLocation ->
                        nowLocation?.let { nowLocation ->
                            messageNowLocation =
                                "현재 위치 : (${nowLocation.latitude}, ${nowLocation.longitude})\n"

                            editorSending?.setText(
                                String.format("%s%s", messageNowLocation, editorSending.text))
                        } ?: activity.toast("현재위치 받아오기 실패")

                        dialogView.findViewById<ProgressBar>(R.id.progressLocation).visibility = View.GONE
                        dialogView.findViewById<TextView>(R.id.textLocationCancel).visibility = View.GONE
                    }
            else{
                dialogView.findViewById<ProgressBar>(R.id.progressLocation).visibility = View.GONE
                dialogView.findViewById<TextView>(R.id.textLocationCancel).visibility = View.GONE
            }

            val case = arrayOf("심정지 환자","화재 사건(대피)", "화재 사건(진화)")

            val adapter = ArrayAdapter(
                activity, // Context
                android.R.layout.simple_spinner_item, // Layout
                case // Array
            )
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

            val selectionPatientStatus = dialogView.selectionPatientStatus.apply{
                this.adapter = adapter
            }

            selectionPatientStatus.onItemSelectedListener =
                object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                        setting = position

                        editorSending.setText(
                            String.format("%s%s"
                                , messageNowLocation
                                , "${parent.getItemAtPosition(position)} 발생했습니다.\n"))
                    }

                    override fun onNothingSelected(parent: AdapterView<*>){}
                }

            builder.setView(dialogView)
                .setPositiveButton("신고") { dialogInterface, i ->
                    /* 확인일 때 main의 View의 값에 dialog View에 있는 값을 적용 */
                    var obj = SmsManager.getDefault()
                    obj.sendTextMessage("01029355768", null, "${editorSending.text}", null, null)
                    when(setting){
                        0 -> {
                            val intent = Intent(activity, cpr_Education::class.java) // 심정지
                            startActivity(activity, intent, null)
                        }
                        1 ->{
                            val intent = Intent(activity, mask_Education::class.java) //화재대피
                            startActivity(activity, intent, null)
                        }
                        2 -> {
                            val intent = Intent(activity, fire1_Education::class.java) //화재 진화
                            startActivity(activity, intent, null)
                        }
                    }

                }
                .setNegativeButton("취소") { dialogInterface, i ->
                    /* 취소일 때 아무 액션이 없으므로 빈칸 */
                }.show()
        }
    }

    private fun requestSMSPermission(){
        PermissionManager.showOnlyRequestAnd(
            activity, permissionForSMS, MainActivity.SEND_SMS_WITH_NOW_LOCATION
            , "SMS 비상신고를 위한 SMS보내기 권한이 필요합니다."
        )
        { _, _ ->
            activity.toast("SMS권한이 없으면 신고기능을 이용할 수 없습니다!")
        }
    }

    private fun buttonEmergencyManual() {
        //TODO : 응급교육 기능
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val builder = AlertDialog.Builder(activity)
        val dialogView = inflater.inflate(R.layout.dialog_emergencyeducation, null)

        builder.setView(dialogView)
            .show()

        val buttonAED = dialogView.findViewById<Button>(R.id.buttonAED)
        val buttonMASK = dialogView.findViewById<Button>(R.id.buttonMASK)
        val buttonFIRE1 = dialogView.findViewById<Button>(R.id.buttonFIRE1)
        val buttonFIRE2 = dialogView.findViewById<Button>(R.id.buttonFIRE2)
        val buttonCPR = dialogView.findViewById<Button>(R.id.buttonCPR)

        buttonAED.setOnClickListener {
            val intent = Intent(activity, EmergencyEducationList::class.java)
            startActivity(activity, intent, null)
        }

        buttonMASK.setOnClickListener {
            val intent = Intent(activity, mask_Education::class.java)
            startActivity(activity, intent, null)
        }

        buttonFIRE1.setOnClickListener {//소화기
            val intent = Intent(activity, fire1_Education::class.java)
            startActivity(activity, intent, null)
        }

        buttonFIRE2.setOnClickListener {//소화전
            val intent = Intent(activity, fire2_Education::class.java)
            startActivity(activity, intent, null)
        }

        buttonCPR.setOnClickListener {
            val intent = Intent(activity, cpr_Education::class.java)
            startActivity(activity, intent, null)
        }
    }



}