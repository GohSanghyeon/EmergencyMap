package com.example.emergencymap

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import org.json.JSONObject

class ItemInfo(itemNo:Int, latitude: Double, longitude: Double, distinction: Int, info: JSONObject, marker: Marker){
    val itemNo = itemNo
    val itemLatitude = latitude
    val itemLongitude = longitude
    val itemLatLng
        get() = LatLng(itemLatitude, itemLongitude)
    val itemDistinction = distinction
    val itemMarker = marker

    var itemAttributes: Map<String, String>
        private set

    init{
        val infoMap = mutableMapOf<String, String>()

        info.keys().forEach { nowKey ->
            infoMap[nowKey] = info.getString(nowKey)
        }

        itemAttributes = infoMap
    }
}