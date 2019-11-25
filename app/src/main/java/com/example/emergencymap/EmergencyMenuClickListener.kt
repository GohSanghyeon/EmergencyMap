package com.example.emergencymap

import android.app.AlertDialog
import android.content.Context
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity




class EmergencyMenuClickListener(val groupSelection: ViewGroup, val activity: AppCompatActivity) : View.OnClickListener {
    override fun onClick(nowSelectionView: View?) {
        groupSelection.visibility = View.INVISIBLE

        when(nowSelectionView?.id){
          R.id.buttonEmergencySMS -> sendEmergencySMS()
          R.id.buttonEmergencyManual -> buttonEmergencyManual()
        }
    }

    private fun sendEmergencySMS() {
        // 비상신고 기능

        val inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val builder = AlertDialog.Builder(activity)
        val dialogView = inflater.inflate(R.layout.dialog_sms, null)

        (activity as MainActivity)?.let {

            //dialogBuilder.setView(dialogView);

            val case = arrayOf("심정지 환자","화재 사건","지진","해일")

            val adapter = ArrayAdapter(
                activity, // Context
                android.R.layout.simple_spinner_item, // Layout
                case // Array
            )

            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

            var spinner = dialogView.findViewById<Spinner>(R.id.spinner)
            spinner.adapter = adapter;

            val mainTv = dialogView.findViewById<EditText>(R.id.editorSMSContents)

            spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                    // Display the selected item text on text view
                    //mainTv.text = "${parent.getItemAtPosition(position).toString()} 발생하였습니다."
                    mainTv.setText("${parent.getItemAtPosition(position).toString()} 발생했습니다.")
                }

                override fun onNothingSelected(parent: AdapterView<*>){
                    // Another interface callback
                }
            }

            //val mainTv = dialogView.findViewById<EditText>(R.id.editorSMSContents)
            //mainTv.setText("심정지 환자가 발생했습니다.")

            //var mainTv = activity.findViewById<EditText>(R.id.mainTv);
            //mainTv.setText("What?");

            builder.setView(dialogView)
                .setPositiveButton("신고") { dialogInterface, i ->
                    /* 확인일 때 main의 View의 값에 dialog View에 있는 값을 적용 */
                    var obj = SmsManager.getDefault()
                    obj.sendTextMessage("01087751432", null, "${mainTv.text.toString()}", null, null)

                }
                .setNegativeButton("취소") { dialogInterface, i ->
                    /* 취소일 때 아무 액션이 없으므로 빈칸 */
                }
                .show()
        }
    }

    private fun buttonEmergencyManual() {
        //TODO : 응급교육 기능
    }
}