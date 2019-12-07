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
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap

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

    private val regionsOnMap: ConcurrentHashMap<String, RegionInfo> = ConcurrentHashMap()
    private val regionInfoWindows: MutableList<InfoWindow> = mutableListOf()

    private var isItemMarkerZoomLevel = true
    private var wasItemMarkerZoomLevel = true

    companion object{
        //for permission check
        private const val STARTING = 10000
        private const val MOVE_TO_NOW_LOCATION = 10001

        const val ITEMS_NUMBERS_OF_REGIONS = "Items numbers of Regions"

        private var markerWidth = 80
        private var markerHeight = 100
        private var limitDistance = 0.1        //Coordinate Compensation Value
        private var minZoom = 5.0
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
        setToggleButtonEmergencyRoom()
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

        setRegionMarkers(naverMap)

        map = naverMap
        map?.let { map->
            map.uiSettings.isCompassEnabled = false
            map.minZoom = minZoom
            map.addOnCameraIdleListener {
                isItemMarkerZoomLevel = map.cameraPosition.zoom > 12.0

                refreshAllMarkersVisibility(isItemMarkerZoomLevel)

                val nowLatitude = map.cameraPosition.target.latitude
                val nowLongitude = map.cameraPosition.target.longitude

                val coordinationBoundary = map.contentBounds
                val mapEast = coordinationBoundary.eastLongitude
                val mapWest = coordinationBoundary.westLongitude
                val mapSouth = coordinationBoundary.southLatitude
                val mapNorth = coordinationBoundary.northLatitude

                val requestBoundary =
                    Boundary(
                        if ((mapEast - nowLongitude) <= limitDistance) mapEast else nowLongitude + limitDistance
                        , if ((nowLongitude - mapWest) <= limitDistance) mapWest else nowLongitude - limitDistance
                        , if ((nowLatitude - mapSouth) <= limitDistance) mapSouth else nowLatitude - limitDistance
                        , if ((mapNorth - nowLatitude) <= limitDistance) mapNorth else nowLatitude + limitDistance
                    )

                val taskDownload =
                    ItemsDownloader(
                        requestBoundary,
                        this
                    ) { items ->
                        items?.let { itemArray ->
                            for (itemPosition in 0 until itemArray.length()) {
                                (itemArray[itemPosition] as? JSONObject)?.let checkNewItem@{ nowItem ->
                                    val nowItemLatitude: Double
                                    val nowItemLongitude: Double
                                    val nowItemDistinction: Int
                                    val nowItemLatLng: LatLng
                                    val nowItemNo: Int

                                    try {
                                        nowItemNo = nowItem.getInt(getString(R.string.ItemNo))
                                        nowItemLatitude =
                                            nowItem.getDouble(getString(R.string.Latitude))
                                        nowItemLongitude =
                                            nowItem.getDouble(getString(R.string.Longitude))
                                        nowItemDistinction =
                                            nowItem.getInt(getString(R.string.Distinction))
                                        nowItemLatLng =
                                            LatLng(nowItemLatitude, nowItemLongitude)
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
                                                , nowItemLatitude
                                                , nowItemLongitude
                                                , nowItemDistinction
                                                , nowItem
                                                , putMarker(
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

    private fun setRegionMarkers(map: NaverMap) {
        intent.getStringExtra(ITEMS_NUMBERS_OF_REGIONS)?.let{
            var jsonItemNumberOfRegion: JSONArray
            try {
                jsonItemNumberOfRegion = JSONArray(it)

                for(regionIndex in 0 until jsonItemNumberOfRegion.length()){
                    val nowRegionInfo = jsonItemNumberOfRegion[regionIndex] as JSONObject

                    val sidoName = nowRegionInfo.getString(getString(R.string.sido))
                    val centerLatitude = nowRegionInfo.getDouble(getString(R.string.centerLatitude))
                    val centerLongitude = nowRegionInfo.getDouble(getString(R.string.centerLongitude))
                    val nowRegionMarker = putRegionMarker(LatLng(centerLatitude, centerLongitude), sidoName, map)

                    regionsOnMap[sidoName] = RegionInfo(
                        sidoName
                        , nowRegionInfo.getInt(getString(R.string.itemNumAED))
                        , nowRegionInfo.getInt(getString(R.string.itemNumShelters))
                        , nowRegionInfo.getInt(getString(R.string.itemNumEmergencyRooms))
                        , centerLatitude
                        , centerLongitude
                        , nowRegionMarker
                    )

                    InfoWindow().apply{
                        adapter = RegionInfoWindowAdapter(regionsOnMap, this@MainActivity)
                        open(nowRegionMarker)
                    }
                }
            }
            catch(e: JSONException){
                Log.d(ITEMS_NUMBERS_OF_REGIONS, "JSONArray 변환 실패", e)
            }
            catch(e: TypeCastException){
                Log.d(ITEMS_NUMBERS_OF_REGIONS, "지역별 JSONObject 변환 실패", e)
            }


        } ?: Log.d(ITEMS_NUMBERS_OF_REGIONS, "JSONArray 받기 실패(null)")
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
            }.forEach {
                it.itemMarker.isVisible = isItemMarkerZoomLevel
                        && buttonAEDVisible.isChecked
            }

            regionsOnMap.forEach {
                val nowRegionInfoAdapter
                        = it.value.RegionMarker.infoWindow?.adapter as? RegionInfoWindowAdapter

                nowRegionInfoAdapter?.let{ it.addAED = isOn }
            }

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
            }.forEach {
                it.itemMarker.isVisible = isItemMarkerZoomLevel
                        && buttonShelterVisible.isChecked
            }

            regionsOnMap.forEach {
                val nowRegionInfoAdapter
                        = it.value.RegionMarker.infoWindow?.adapter as? RegionInfoWindowAdapter

                nowRegionInfoAdapter?.let{ it.addShelters = isOn }
            }

            if(isOn)
                compoundButton.background = getDrawable(R.color.colorRedCloud)
            else
                compoundButton.background = getDrawable(R.color.transparency)
        }
    }

    private fun setToggleButtonEmergencyRoom() {
        buttonEmergencyRoomVisible.setOnCheckedChangeListener { compoundButton, isOn ->
            itemsOnMap.filter {
                it.itemDistinction == resources.getInteger(R.integer.EmergencyRoom)
            }.forEach {
                it.itemMarker.isVisible = isItemMarkerZoomLevel
                        && buttonEmergencyRoomVisible.isChecked
            }

            regionsOnMap.forEach {
                val nowRegionInfoAdapter
                        = it.value.RegionMarker.infoWindow?.adapter as? RegionInfoWindowAdapter

                nowRegionInfoAdapter?.let{ it.addEmergencyRooms = isOn }
            }

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

        initializeMarkerShow(this, itemDistinction)
    }

    private fun initializeMarkerShow(marker: Marker, distinction: Int){
        val isAED = resources.getInteger(R.integer.AED)

        val isTsunamiShelter = resources.getInteger(R.integer.TsunamiShelter)
        val isMBWShelter = resources.getInteger(R.integer.MBWShelter)
        val isEmergencyRoom = resources.getInteger(R.integer.EmergencyRoom)

        marker.icon =
            when(distinction) {
                isAED ->  OverlayImage.fromResource(R.drawable.aed_marker)
                isTsunamiShelter -> OverlayImage.fromResource(R.drawable.tsunami_shelter_marker)
                isMBWShelter -> OverlayImage.fromResource(R.drawable.mbw_shelter_marker)
                isEmergencyRoom -> OverlayImage.fromResource(R.drawable.emergency_room_marker)
                else -> {
                    Log.d("setMarkerImage", "잘못된 distinction : $distinction");
                    OverlayImage.fromResource(R.drawable.transparent_pixel)
            }
        }

        marker.isVisible = wasItemMarkerZoomLevel
                && when(distinction){
            isAED -> buttonAEDVisible.isChecked
            isTsunamiShelter, isMBWShelter -> buttonShelterVisible.isChecked
            isEmergencyRoom -> buttonEmergencyRoomVisible.isChecked
            else -> false
        }
    }

    private fun refreshAllMarkersVisibility(isItemMarkerZoomLevel: Boolean){
        val needChange = isItemMarkerZoomLevel.xor(wasItemMarkerZoomLevel)

        if(needChange) {
            itemsOnMap.forEach {
                it.itemMarker.isVisible = isItemMarkerZoomLevel
                        && when (it.itemDistinction) {
                    resources.getInteger(R.integer.AED)
                    -> buttonAEDVisible.isChecked
                    resources.getInteger(R.integer.TsunamiShelter)
                    -> buttonShelterVisible.isChecked
                    resources.getInteger(R.integer.MBWShelter)
                    -> buttonShelterVisible.isChecked
                    resources.getInteger(R.integer.EmergencyRoom)
                    -> buttonEmergencyRoomVisible.isChecked
                    else -> false
                }
            }

            regionsOnMap.forEach{
                val nowRegionInfo = it.value
                nowRegionInfo.RegionMarker.isVisible = !isItemMarkerZoomLevel
            }
        }
        else
            return

        wasItemMarkerZoomLevel = isItemMarkerZoomLevel
    }

    private fun putRegionMarker(
        regionPosition: LatLng
        , regionName: String
        , drawingMap: NaverMap
    ) = Marker().apply {

        position = regionPosition
        map = drawingMap
        tag = regionName
        width = markerWidth
        height = markerHeight
    }
}
