package com.example.emergencymap

import android.Manifest
import android.content.ContentValues
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.core.view.iterator
import com.example.emergencymap.notshowing.LocationProvider
import com.google.android.youtube.player.internal.s
import com.google.android.youtube.player.internal.u
import com.google.android.youtube.player.internal.x
import com.google.android.youtube.player.internal.y
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.android.synthetic.main.activity_location_list.*
import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.app_bar_main.*
//import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection

val permissionUsing: Array<out String> = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.SEND_SMS,
    Manifest.permission.READ_EXTERNAL_STORAGE
)
    get() = field.clone()

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var locationSource: LocationProvider
    private var map: NaverMap? = null
    lateinit var url : URL
    var data : String? = null

    companion object{
        //for permission check
        private const val STARTING = 10000
        private const val MOVE_TO_NOW_LOCATION = 10001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(PermissionManager.existDeniedpermission(this, permissionUsing))
            PermissionManager.showOnlyRequestAnd(this, permissionUsing, STARTING,
                "어플리케이션의 기능을 정상적으로 사용하기 위해 " +
                        "위치 조회, SMS, 파일 읽기권한이 필요합니다.")
            { _, _ ->
                toast("일부 기능이 제한될 수 있습니다.")
            }

        mountMap()
        locationSource = LocationProvider(this)

        buttonNowLocation.setOnClickListener{
            setMapToNowLocation()
        }

        setEmergencyButton()
    }

    private fun mountMap() {
        //네이버 맵 클라이언트 ID 받아오기
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("af5bvg9isp")

        val fm = supportFragmentManager
        val mapFragment = map_view as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_view, it).commit()
            }

        mapFragment.getMapAsync(this)
        locationSource = LocationProvider(this)

        val marker = Marker()
        marker.map
    }

    private fun setEmergencyButton(){
        buttonEmergency.setOnClickListener {
            //show emergency menu selections
            if(layoutEmergencySelection.isVisible)
                layoutEmergencySelection.visibility = View.INVISIBLE
            else
                layoutEmergencySelection.visibility = View.VISIBLE
        }

        //emergency menu click listener setting
        if(layoutEmergencySelection is ViewGroup)
            for(menuItem in layoutEmergencySelection)
                menuItem.setOnClickListener(EmergencyMenuClickListener(layoutEmergencySelection as ViewGroup, this))
    }

    override fun onRequestPermissionsResult(
        functionCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(functionCode){
            STARTING -> {
                if(PermissionManager.existDeniedpermission(this, permissions))
                   toast("일부 기능이 제한될 수 있습니다.")
            }
            MOVE_TO_NOW_LOCATION -> {
                if(!PermissionManager.existDeniedpermission(this, permissions))
                    setMapToNowLocation()
                else
                    toast("권한이 허가되지 않아 기능을 이용할 수 없습니다.")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.apptuto ->{

            }
            R.id.howtool ->
                startActivity(Intent(this, SelectionForEducation::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        val marker = Marker()
        val heungup = LatLng(37.30260779, 127.9211684)
        marker.position = heungup
        naverMap.moveCamera(CameraUpdate.scrollTo(heungup))
        marker.map = naverMap
        marker.width = 60
        marker.height = 80
        marker.icon = OverlayImage.fromResource(R.drawable.aed)
    //
        map = naverMap
        map?.uiSettings?.isCompassEnabled = false
        map?.addOnCameraIdleListener {
            map?.let { map ->
                toast("위도 : ${map.cameraPosition.target.latitude}\n" +
                        "경도 : ${map.cameraPosition.target.longitude}")

                val coordinationBoundary = map.contentBounds
                textTestEast.text = "맵 동단 : ${coordinationBoundary.eastLongitude}"
                textTestWest.text = "맵 서단 : ${coordinationBoundary.westLongitude}"
                textTestSouth.text = "맵 남단 : ${coordinationBoundary.southLatitude}"
                textTestNorth.text = "맵 북단 : ${coordinationBoundary.northLatitude}"
                var surl : String= "http://15.164.116.17/xy.php?east=${coordinationBoundary.eastLongitude}" +
                            "&west=${coordinationBoundary.westLongitude}&south=${coordinationBoundary.southLatitude}&" +
                            "north=${coordinationBoundary.northLatitude}"
                url = URL(surl)

                Log.d("line", url.toString())
                val networkTask = NetworkTask(surl, null)
                networkTask.execute()
                try {
                    if (data != null) {
                        val jObject = JSONObject(data)
                        val jArray = jObject.getJSONArray("123")
                        for (i in 0 until jArray.length()) {
                            val obj = jArray.getJSONObject(i)
                            val x = obj.getDouble("x")
                            //val y = obj.getDouble("y")
                            Log.d("line x=", "$x\n")
                            //Log.d("line y=", "$y\n")
                            //
                        }
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }
    fun readStream(inputStream: InputStreamReader): String {
        val bufferedReader = BufferedReader(inputStream)
        val stringBuffer = StringBuffer("")
        bufferedReader.forEachLine { stringBuffer.append(it) }
        Log.d("dusxo", stringBuffer.toString())
        return stringBuffer.toString().replaceFirst("\uFEFF\uFEFF", "")
    }

    private fun setMapToNowLocation(){
        locationSource.requestNowLocation(MOVE_TO_NOW_LOCATION) {
            it?.let{
                map?.moveCamera(CameraUpdate.scrollTo(LatLng(it)))
                toast("조회 완료")
            }
        }
    }
    inner class NetworkTask(private val url: String, private val values: ContentValues?) :
        AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void): String? {

            val result: String? // 요청 결과를 저장할 변수.
            val requestHttpURLConnection = RequestHttpURLConnection()
            result = requestHttpURLConnection.request(url, values) // 해당 URL로 부터 결과물을 얻어온다.

            return result
        }

        override fun onPostExecute(s: String){
            super.onPostExecute(s)
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            Log.d("line", "$s\n")
            data = s
        }
    }
}
