package com.example.emergencymap

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_startup.*
import org.jetbrains.anko.startActivity

class StartupActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

        textNowWorking.text = "인터넷 연결 상태 확인중..."
        val netMonitor = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        netMonitor.registerDefaultNetworkCallback(
            object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                netMonitor.unregisterNetworkCallback(this)
                startActivity<MainActivity>()
            }

            override fun onLost(network: Network) {
                netMonitor.unregisterNetworkCallback(this)
                //TODO : 인터넷 연결이 되지 않았을때 초기화면 액티비티 실행하기

            }
        })
    }
}