package com.example.emergencymap

import android.Manifest
import android.os.Bundle
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
import com.example.emergencymap.notshowing.Boundary
import com.example.emergencymap.notshowing.ItemsDownloader
import com.example.emergencymap.notshowing.LocationProvider
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import org.json.JSONException
import org.json.JSONObject

val permissionUsing: Array<out String> = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.SEND_SMS,
    Manifest.permission.READ_EXTERNAL_STORAGE
)
    get() = field.clone()

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var locationSource: LocationProvider
    private var map: NaverMap? = null
    private var itemsOnMap: MutableList<ItemInfo> = mutableListOf()

    companion object{
        //for permission check
        private const val STARTING = 10000
        private const val MOVE_TO_NOW_LOCATION = 10001

        private var markerWidth = 80
        private var markerHeight = 100
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

        checkShowingTutorial()

        mountMap()
        locationSource = LocationProvider(this)

        buttonNowLocation.setOnClickListener{
            setMapToNowLocation()
        }

        buttonAEDVisible.setOnCheckedChangeListener { compoundButton, isOn ->
            if(isOn) {
                compoundButton.background = getDrawable(R.color.colorRedCloud)
            }
            else {
                compoundButton.background = getDrawable(R.color.transparency)
            }
        }

        setToggleButtonAED()
        setToggleButtonShelter()
        setEmergencyButton()
    }

    private fun checkShowingTutorial() {
        val pref
                = getSharedPreferences(TutorialActivity.PREFERENCES_TUTORIAL_NAME, MODE_PRIVATE)

        val isCheckedNeverShow
                = pref.getInt("First", TutorialActivity.UNCHECKED_NEVER_SHOW_TUTORIAL)

        if(isCheckedNeverShow == TutorialActivity.UNCHECKED_NEVER_SHOW_TUTORIAL){
            val intent = Intent(this, TutorialActivity::class.java)
            startActivity(intent)
        }
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
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        val heungup = LatLng(37.30260779, 127.9211684)
        naverMap.moveCamera(CameraUpdate.scrollTo(heungup))

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

                val requestBoundary =
                    Boundary(
                        if ((mapEast - nowLongitude) <= limitDistance) mapEast else nowLongitude + limitDistance
                        ,
                        if ((nowLongitude - mapWest) <= limitDistance) mapWest else nowLongitude - limitDistance
                        ,
                        if ((nowLatitude - mapSouth) <= limitDistance) mapSouth else nowLatitude - limitDistance
                        ,
                        if ((mapNorth - nowLatitude) <= limitDistance) mapNorth else nowLatitude + limitDistance
                    )

                val taskDownload =
                    ItemsDownloader(
                        requestBoundary,
                        this
                    ) { items ->
                        items?.let { itemArray ->
                            for (itemPosition in 0 until itemArray.length()) {
                                (itemArray[itemPosition] as? JSONObject)?.let checkNewItem@{ nowItem ->
                                    var nowItemLatitude: Double
                                    var nowItemLongitude: Double
                                    var nowItemDistinction: Int
                                    var nowItemLatLng: LatLng
                                    var nowItemNo: Int

                                    try {
                                        nowItemNo = nowItem.getInt(getString(R.string.ItemNo))
                                        nowItemLatitude =
                                            nowItem.getDouble(getString(R.string.Latitude))
                                        nowItemLongitude =
                                            nowItem.getDouble(getString(R.string.Longitude))
                                        nowItemDistinction =
                                            nowItem.getInt(getString(R.string.Distinction))
                                        nowItemLatLng = LatLng(nowItemLatitude, nowItemLongitude)
                                    } catch (e: JSONException) {
                                        Log.d("Item Check", "잘못된 JSON형식!", e)
                                        return@checkNewItem
                                    }

                                    val isNewItem = itemsOnMap.count { oneItemOnMap ->
                                        oneItemOnMap.itemNo == nowItemNo
                                                && oneItemOnMap.itemDistinction == nowItemDistinction
                                    } == 0

                                    if (isNewItem) {
                                        itemsOnMap.add(
                                            ItemInfo(
                                                nowItemNo
                                                ,
                                                nowItemLatitude
                                                ,
                                                nowItemLongitude
                                                ,
                                                nowItemDistinction
                                                ,
                                                nowItem
                                                ,
                                                putMarker(
                                                    nowItem,
                                                    nowItemLatLng,
                                                    nowItemDistinction,
                                                    map
                                                )
                                            )
                                        )
                                    }

                                }
                            }
                        }
                    }

                taskDownload.execute()
            }
        }
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

    private fun setToggleButtonAED(){
        buttonAEDVisible.setOnCheckedChangeListener { compoundButton, isOn ->
            itemsOnMap.filter {
                it.itemDistinction == resources.getInteger(R.integer.AED)
            }.forEach { checkVisibleAndSetMarker(it.itemMarker, it.itemDistinction) }

            if(isOn)
                compoundButton.background = getDrawable(R.color.colorRedCloud)
            else
                compoundButton.background = getDrawable(R.color.transparency)
        }
    }
    private fun setToggleButtonShelter(){
        buttonShelterVisible.setOnCheckedChangeListener { compoundButton, isOn ->
            val isTsunamiShelter = resources.getInteger(R.integer.TsunamiShelter)
            val isMBWShelter = resources.getInteger(R.integer.MBWShelter)

            itemsOnMap.filter {
                listOf(
                    resources.getInteger(R.integer.TsunamiShelter)
                    , resources.getInteger(R.integer.MBWShelter)
                ).contains(it.itemDistinction)
            }.forEach { checkVisibleAndSetMarker(it.itemMarker, it.itemDistinction) }

            if(isOn)
                compoundButton.background = getDrawable(R.color.colorRedCloud)
            else
                compoundButton.background = getDrawable(R.color.transparency)
        }
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
                startActivity(Intent(this, TutorialActivity::class.java))
            }
            R.id.howtool ->
                startActivity(Intent(this, SelectionForEducation::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setMapToNowLocation(){
        locationSource.requestNowLocation(MOVE_TO_NOW_LOCATION) {
            it?.let{
                map?.moveCamera(CameraUpdate.scrollTo(LatLng(it)))
                toast("조회 완료")
            }
        }
    }

    private fun putMarker(
        item: JSONObject
        , itemLatLng: LatLng
        , itemDistinction: Int
        , drawingMap: NaverMap) = Marker().apply {

        position = itemLatLng
        map = drawingMap
        width = markerWidth
        height = markerHeight

        checkVisibleAndSetMarker(this, itemDistinction)
    }

    private fun checkVisibleAndSetMarker(marker: Marker, distinction: Int){
        val isAED = resources.getInteger(R.integer.AED)

        val isTsunamiShelter = resources.getInteger(R.integer.TsunamiShelter)
        val isMBWShelter = resources.getInteger(R.integer.MBWShelter)

        var isVisible = when(distinction){
            isAED -> buttonAEDVisible.isChecked
            isTsunamiShelter, isMBWShelter -> buttonShelterVisible.isChecked
            else -> false
        }

        if(isVisible)
            marker.icon = when(distinction)
            {
                isAED ->  OverlayImage.fromResource(R.drawable.aed_marker)
                isTsunamiShelter -> OverlayImage.fromResource(R.drawable.tsunami_shelter_marker)
                isMBWShelter -> OverlayImage.fromResource(R.drawable.mbw_shelter_marker)
                else -> { Log.d("setMarkerImage", "잘못된 distinction : $distinction"); marker.icon }
            }
        else
            marker.icon = OverlayImage.fromResource(R.drawable.transparent_pixel)
    }
}
