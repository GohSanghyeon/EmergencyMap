package com.example.emergencymap

import android.Manifest
import android.os.Bundle
import androidx.navigation.ui.AppBarConfiguration
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.core.view.isVisible
import androidx.core.view.iterator
import com.example.emergencymap.notshowing.LocationProvider
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.app_bar_main.*
//import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL

val permissionUsing: Array<out String> = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.SEND_SMS,
    Manifest.permission.READ_EXTERNAL_STORAGE
)
    get() = field.clone()

typealias ItemsInfo = JSONArray?

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var locationSource: LocationProvider
    private var map: NaverMap? = null
    private var itemsOnMap: MutableList<ItemInfo> = mutableListOf()

    companion object{
        //for permission check
        private const val STARTING = 10000
        private const val MOVE_TO_NOW_LOCATION = 10001

        private var markerWidth = 60
        private var markerHeight = 80
        private var limitDistance = 0.1        //Coordinate Compensation Value
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

        buttonLocationAED.setOnCheckedChangeListener { compoundButton, isOn ->
            if(isOn) {
                compoundButton.background = getDrawable(R.color.colorRedCloud)
            }
            else {
                compoundButton.background = getDrawable(R.color.transparency)
            }
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
        marker.width = markerWidth
        marker.height = markerHeight
        marker.icon = OverlayImage.fromResource(R.drawable.aed)
    //
        map = naverMap
        map?.uiSettings?.isCompassEnabled = false
        map?.addOnCameraIdleListener {
            map?.let { map ->
                val nowLatitude = map.cameraPosition.target.latitude
                val nowLongitude = map.cameraPosition.target.longitude

                val coordinationBoundary = map.contentBounds
                val mapEast = coordinationBoundary.eastLongitude
                val mapWest = coordinationBoundary.westLongitude
                val mapSouth = coordinationBoundary.southLatitude
                val mapNorth = coordinationBoundary.northLatitude

                textTestEast.text = "맵 동단 : ${coordinationBoundary.eastLongitude}"
                textTestWest.text = "맵 서단 : ${coordinationBoundary.westLongitude}"
                textTestSouth.text = "맵 남단 : ${coordinationBoundary.southLatitude}"
                textTestNorth.text = "맵 북단 : ${coordinationBoundary.northLatitude}"

                val requestBoundary = Boundary(
                    if((mapEast - nowLongitude) <= limitDistance) mapEast else nowLongitude + limitDistance
                    , if((nowLongitude - mapWest) <= limitDistance) mapWest else nowLongitude - limitDistance
                    , if((nowLatitude - mapSouth) <= limitDistance) mapSouth else nowLatitude - limitDistance
                    , if((mapNorth - nowLatitude) <= limitDistance) mapNorth else nowLatitude + limitDistance
                )

                val taskDownload = ItemsDownloader(requestBoundary, this) { items ->
                    items?.let { itemArray ->
                        for (itemPosition in 0 until itemArray.length()) {
                            (itemArray[itemPosition] as? JSONObject)?.let checkNewItem@ { nowItem ->
                                var nowItemLatitude: Double
                                var nowItemLongitude: Double
                                var nowItemDistinction: Int
                                var nowItemLatLng: LatLng

                                try{
                                    nowItemLatitude = nowItem.getDouble(getString(R.string.Latitude))
                                    nowItemLongitude = nowItem.getDouble(getString(R.string.Longitude))
                                    nowItemDistinction = nowItem.getInt(getString(R.string.Distinction))
                                    nowItemLatLng = LatLng(nowItemLatitude, nowItemLongitude)
                                }catch(e: JSONException){
                                    Log.d("Item Check", "잘못된 JSON형식!", e)
                                    return@checkNewItem
                                }

                                val isNewItem = itemsOnMap.count { oneItemOnMap ->
                                    oneItemOnMap.itemLatLng == nowItemLatLng
                                            && oneItemOnMap.itemDistinction == nowItemDistinction
                                } == 0

                                if(isNewItem){
                                    itemsOnMap.add(ItemInfo(
                                        nowItemLatitude
                                        , nowItemLongitude
                                        , nowItemDistinction
                                        , nowItem))

                                    putMarker(nowItem, nowItemLatLng, nowItemDistinction, map)
                                }
                            }
                        }
                    }
                }

                taskDownload.execute()
            }
        }
    }

    private fun setMapToNowLocation(){
        locationSource.requestNowLocation(MOVE_TO_NOW_LOCATION) {
            it?.let{
                map?.moveCamera(CameraUpdate.scrollTo(LatLng(it)))
                toast("조회 완료")
            }
        }
    }

    private fun putMarkers(items: JSONArray, drawingMap: NaverMap){
        for(nowOrder in 0 until items.length()) {
            val nowItem = items[nowOrder] as JSONObject?;

            nowItem?.let{
                val latitude: Double
                val longitude: Double

                try {
                    latitude = it.getDouble(getString(R.string.Latitude))
                    longitude = it.getDouble(getString(R.string.Longitude))
                }catch(e: JSONException){
                    Log.d("putting Marker : ", "잘못된 위도-경도")
                    return@let
                }

                val newMarker = Marker().apply{
                    position = LatLng(latitude, longitude)
                    map = drawingMap
                    width = markerWidth
                    height = markerHeight
                    icon = OverlayImage.fromResource(R.drawable.aed)
                }
            }
        }
    }

    private fun putMarker(
        item: JSONObject
        , itemLatLng: LatLng
        , itemDistinction: Int
        , drawingMap: NaverMap) = Marker().apply{
            position = itemLatLng
            map = drawingMap
            width = markerWidth
            height = markerHeight
            icon = OverlayImage.fromResource(R.drawable.aed)
        }
}
