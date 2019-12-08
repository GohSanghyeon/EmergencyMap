package com.example.emergencymap

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.emergencymap.notshowing.RegionInfoDownloader
import kotlinx.android.synthetic.main.activity_startup.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class StartupActivity : AppCompatActivity(){
    companion object{
        const val MILLISEC_CHECKING_DISCONNECTED = 5000L

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        val netMonitor = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        textNowWorking.text = "인터넷 연결 상태 확인중..."

        val callbacks = CallbacksForCheckingInternet(
            netMonitor,
            doingIsConnected = {
                runOnUiThread {
                    toast("인터넷 연결을 확인했습니다.")
                    textNowWorking.text = "지역별 정보를 다운로드 중..."
                }

                RegionInfoDownloader(this@StartupActivity) {
                    var strItemNumbersOfRegions = ""
                    it?.let{ strItemNumbersOfRegions = it.toString() }

                    startActivity<MainActivity>(
                        MainActivity.ITEMS_NUMBERS_OF_REGIONS to strItemNumbersOfRegions
                    )
                }.execute()
            }
            , doingIsDisconnected = {
                runOnUiThread {
                    toast("인터넷에 연결할 수 없습니다.")
                    progressBar.visibility = View.INVISIBLE

                    startActivity<ItemListActivity>(
                        ItemListActivity.KEY_MODE to ItemListActivity.OFFLINE
                    )
                }
            })

        netMonitor.registerDefaultNetworkCallback(callbacks)
        callbacks.timerForCheckStillDisconnected.completedCallbackRegistering()
        callbacks.timerForCheckStillDisconnected.start()
    }

    private class CallbacksForCheckingInternet(
        private val netMonitor: ConnectivityManager
        , private val doingIsConnected : (() -> Unit)
        , private val doingIsDisconnected : (() -> Unit)
    ) : ConnectivityManager.NetworkCallback(){
        val timerForCheckStillDisconnected = TimerForDisconnection()

        override fun onAvailable(network: Network) {
            if (timerForCheckStillDisconnected.executedDisconnected)
                return
            else
                timerForCheckStillDisconnected.connectedSoDoNotExecute()

            //if netMonitor is null, blow up this app
            netMonitor.unregisterNetworkCallback(this)
            doingIsConnected()
        }

        inner class TimerForDisconnection: Thread() {
            private var registeredCallbackConnected = false
            private var isStillDisconnected = true
            var executedDisconnected = false
                private set

            override fun run() {
                sleep(MILLISEC_CHECKING_DISCONNECTED)

                executedDisconnected = true
                if (isStillDisconnected) {
                    if (this != null && registeredCallbackConnected)
                        netMonitor.unregisterNetworkCallback(this@CallbacksForCheckingInternet)

                    doingIsDisconnected()
                }
            }

            fun connectedSoDoNotExecute() {
                isStillDisconnected = false
            }

            fun completedCallbackRegistering() {
                registeredCallbackConnected = true
            }
        }
    }
}