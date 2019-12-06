package com.example.emergencymap

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.emergencymap.notshowing.RegionInfoDownloader
import kotlinx.android.synthetic.main.activity_startup.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

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
                toast("Network : On-line")

                textNowWorking.text = "지역별 정보를 다운로드 중..."
                RegionInfoDownloader(this@StartupActivity){
                    startActivity<MainActivity>()
                }.execute()
            }

            override fun onUnavailable() {
                netMonitor.unregisterNetworkCallback(this)
                toast("Network : Off-line")
                //TODO : 인터넷 연결이 되지 않았을때 초기화면 액티비티 실행하기

            }
        })
    }
}