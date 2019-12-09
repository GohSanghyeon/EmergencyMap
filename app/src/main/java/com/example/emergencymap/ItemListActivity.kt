package com.example.emergencymap

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Network
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.iterator
import com.example.emergencymap.notshowing.LocationProvider
import com.example.emergencymap.notshowing.OfflineItemDBHelper
import com.example.emergencymap.notshowing.RegionInfoDownloader
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import kotlinx.android.synthetic.main.activity_location_list.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.lang.Integer.min

class ItemListActivity: AppCompatActivity() {

    companion object{
        const val KEY_MODE = "Mode"
        const val OFFLINE = "Offline"
        const val ONLINE = "Online"

        const val CENTER_LOCATION_LATITUDE = "Location Latitude"
        const val CENTER_LOCATION_LONGITUDE = "Location Longitude"

        const val SET_LOCATION = 20000
    }

    private var locationSource: LocationProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)

        intent.getStringExtra(KEY_MODE)?.let { mode ->
            when(mode) {
                OFFLINE -> doOfflineMode()
                ONLINE -> doOnlineMode(intent)
            }
        }
    }

    private fun doOnlineMode(intentWithItems: Intent){
        val keyAddress = resources.getString(R.string.BuildAddress)
        val keyDetail = resources.getString(R.string.DetailedPlace)
        val keyDistinction = resources.getString(R.string.Distinction)
        val keyLatitude = resources.getString(R.string.Latitude)
        val keyLongitude = resources.getString(R.string.Longitude)

        val showingBuildAddresses = intentWithItems.getStringArrayExtra(keyAddress)
        val showingDetailPlaces = intentWithItems.getStringArrayExtra(keyDetail)
        val showingDistinction = intentWithItems.getIntArrayExtra(keyDistinction)
        val itemsLatitude = intentWithItems.getDoubleArrayExtra(keyLatitude)
        val itemsLongitude = intentWithItems.getDoubleArrayExtra(keyLongitude)

        val itemList = mutableListOf<ListItemData>()
        var dataNum: Int

        showingBuildAddresses.size

        if((showingBuildAddresses.size != showingDetailPlaces.size)
            || (showingDetailPlaces.size != showingDistinction.size)
            || (showingDistinction.size != itemsLatitude.size)
            || (itemsLatitude.size != itemsLongitude.size)
        ) {

            Log.d("ItemList-Online", "데이터 불균형 발생")
            dataNum = min(showingBuildAddresses.size, min(showingDetailPlaces.size, showingDistinction.size))
        }
        else
            dataNum = showingBuildAddresses.size

        for(nowPosition in 0 until dataNum){
            val nowDrawable = getDrawableFromDistinction(showingDistinction[nowPosition])
            nowDrawable?.let { nowDrawable ->
                itemList.add(
                    ListItemData(
                        nowDrawable,
                        showingBuildAddresses[nowPosition],
                        showingDetailPlaces[nowPosition],
                        itemsLatitude[nowPosition],
                        itemsLongitude[nowPosition]))
            }
        }

        val centerLatitude = intentWithItems.getDoubleExtra(CENTER_LOCATION_LATITUDE, 0.0)
        val centerLongitude = intentWithItems.getDoubleExtra(CENTER_LOCATION_LONGITUDE, 0.0)

        recycler_View.adapter = ItemRecyclerViewAdapter(
            //맵 중심으로부터 가까운 순으로 정렬
            itemList.sortedBy {
            Math.pow(centerLatitude - it.latitude, 2.0)
            + Math.pow(centerLongitude - it.longitude, 2.0)
        })
    }

    private fun doOfflineMode(){
        if(PermissionManager.existDeniedpermission(this, LocationProvider.permissionForLocation))
            PermissionManager.showOnlyRequestAnd(this, permissionUsing, SET_LOCATION,
                "가까운 아이템 순으로 정렬하기 위해 " +
                        "위치 조회 권한이 필요합니다.")
            { _, _ ->
                toast("가까운 아이템 순 정렬 기능이 제한됩니다.")
            }
        buttonNowLocationOffline.visibility = View.VISIBLE
        val offlineItemDB = OfflineItemDBHelper(this)

        val itemMappedForList: MutableList<ListItemData> = mutableListOf()
        offlineItemDB.loadOfflineItems().forEach{ nowItemInfo ->
            val itemDrawable = getDrawableFromDistinction(nowItemInfo.itemDistinction)
            var buildAddress = nowItemInfo.itemAttributes.get(AppConstantInitializer.ITEMKEY_BUILDADDRESS)
            var detailedPlace = nowItemInfo.itemAttributes.get(AppConstantInitializer.ITEMKEY_DETAILEDPLACE)
            var managerTel = nowItemInfo.itemAttributes.get(AppConstantInitializer.ITEMKEY_MANAGERTEL)

            itemDrawable?.let{ buildAddress?.let{ detailedPlace?.let{ managerTel?.let{

                Log.d("아이템 주소", "$buildAddress")
                itemMappedForList.add(
                    ListItemData(
                    itemDrawable
                    , buildAddress
                    , detailedPlace
                    , nowItemInfo.itemLatitude
                    , nowItemInfo.itemLongitude
                ))
            }}}}
        }

        recycler_View.adapter = ItemRecyclerViewAdapter(itemMappedForList)

        locationSource = LocationProvider(this)

        buttonNowLocationOffline.setOnClickListener {
            setNewLocationToList(itemMappedForList)
        }

        setEmergencyButton()

        val netMonitor = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        netMonitor.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                super.onAvailable(network)

                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                netMonitor.unregisterNetworkCallback(this)
                RegionInfoDownloader(this@ItemListActivity) {
                    var strItemNumbersOfRegions = ""
                    it?.let{ strItemNumbersOfRegions = it.toString() }

                    startActivity<MainActivity>(
                        MainActivity.ITEMS_NUMBERS_OF_REGIONS to strItemNumbersOfRegions
                    )
                    toast("인터넷 연결을 확인했습니다.")
                }.execute()
            }
        })
    }

    private fun getDrawableFromDistinction(distinction: Int) : Drawable?
            = when(distinction){
                resources.getInteger(R.integer.AED) -> getDrawable(R.drawable.aed_marker)
                resources.getInteger(R.integer.TsunamiShelter) -> getDrawable(R.drawable.tsunami_shelter_marker)
                resources.getInteger(R.integer.MBWShelter) -> getDrawable(R.drawable.mbw_shelter_marker)
                resources.getInteger(R.integer.EmergencyRoom) -> getDrawable(R.drawable.emergency_room_marker)
                resources.getInteger(R.integer.Pharmacy) -> getDrawable(R.drawable.pharmacy_marker)
                else -> null
            }

    private fun setNewLocationToList(showingItems: List<ListItemData>) =
        locationSource?.requestNowLocation(SET_LOCATION) {
            it?.let{ nowLocation ->
                recycler_View.adapter = ItemRecyclerViewAdapter(
                    showingItems.sortedBy {
                        Math.pow(nowLocation.latitude - it.latitude, 2.0)
                        + Math.pow(nowLocation.longitude - it.longitude, 2.0)
                    })
            }
            toast("조회 완료")
        }

    private fun setEmergencyButton(){
        buttonEmergencyOffline.visibility = View.VISIBLE
        buttonEmergencyOffline.setOnClickListener {
            //show emergency menu selections
            if(layoutEmergencySelectionOffline.isVisible)
                layoutEmergencySelectionOffline.visibility = View.INVISIBLE
            else
                layoutEmergencySelectionOffline.visibility = View.VISIBLE
        }

        //emergency menu click listener setting
        if(layoutEmergencySelectionOffline is ViewGroup)
            for(menuItem in layoutEmergencySelectionOffline)
                menuItem.setOnClickListener(EmergencyMenuClickListener(layoutEmergencySelectionOffline as ViewGroup, this))
    }

    override fun onRequestPermissionsResult(
        functionCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(functionCode){
            SET_LOCATION -> {
                if(PermissionManager.existDeniedpermission(this, permissions))
                    toast("권한이 허가되지 않아 위치 탐색 기능을 이용할 수 없습니다.")
            }
        }
    }


}
