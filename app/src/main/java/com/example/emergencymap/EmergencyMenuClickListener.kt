package com.example.emergencymap

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
import kotlinx.android.synthetic.main.dialog_emergencyeducation.*
import kotlinx.android.synthetic.main.dialog_sms.view.*


class EmergencyMenuClickListener(
    val groupSelection: ViewGroup
    , val activity: AppCompatActivity)
    : View.OnClickListener {

    override fun onClick(nowSelectionView: View?) {
        groupSelection.visibility = View.INVISIBLE

        when(nowSelectionView?.id){
          R.id.buttonEmergencySMS -> sendEmergencySMS()
          R.id.buttonEmergencyManual -> buttonEmergencyManual()
        }
    }

    private fun sendEmergencySMS() {
        // 비상신고 기능

        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val builder = AlertDialog.Builder(activity)
        val dialogView = inflater.inflate(R.layout.dialog_sms, null)

        (activity as MainActivity)?.let {
            val case = arrayOf("심정지 환자","화재 사건","지진","해일")

            val adapter = ArrayAdapter(
                activity, // Context
                android.R.layout.simple_spinner_item, // Layout
                case // Array
            )
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

            val selectionPatientStatus = dialogView.selectionPatientStatus.apply{
                this.adapter = adapter
            }

            val mainTv = dialogView.findViewById<EditText>(R.id.editorSMSContents)

            selectionPatientStatus.onItemSelectedListener =
                object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                        mainTv.setText("${parent.getItemAtPosition(position)} 발생했습니다.")
                    }

                    override fun onNothingSelected(parent: AdapterView<*>){}
                }

            builder.setView(dialogView)
                .setPositiveButton("신고") { dialogInterface, i ->
                    /* 확인일 때 main의 View의 값에 dialog View에 있는 값을 적용 */
                    var obj = SmsManager.getDefault()
                    obj.sendTextMessage("01087751432", null, "${mainTv.text}", null, null)

                }
                .setNegativeButton("취소") { dialogInterface, i ->
                    /* 취소일 때 아무 액션이 없으므로 빈칸 */
                }
                .show()
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
        val buttonFIRE = dialogView.findViewById<Button>(R.id.buttonFIRE)
        val buttonWATER = dialogView.findViewById<Button>(R.id.buttonWATER)

        buttonAED.setOnClickListener {
            val intent = Intent(activity, education_list::class.java)
            startActivity(activity, intent, null)
        }

        buttonMASK.setOnClickListener {

        }

        buttonFIRE.setOnClickListener {

        }

        buttonWATER.setOnClickListener {

        }
    }
}