package com.example.emergencymap

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_howtool.*

class howtoolActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_howtool)
        setTitle("구조 용품 사용법")
        aed_btn.setOnClickListener {
            val intent = Intent(this, howtoAED::class.java)
            startActivity(intent)
        }

        bangdok_btn.setOnClickListener {
            val intent = Intent(this, howtoBangdock::class.java)
            startActivity(intent)
        }
    }
}
