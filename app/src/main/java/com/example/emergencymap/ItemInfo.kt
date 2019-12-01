package com.example.emergencymap

import com.naver.maps.geometry.LatLng
import org.json.JSONObject

class ItemInfo(latitude: Double, longitude: Double, distinction: Int, info: JSONObject){
    val itemLatitude = latitude
    val itemLongitude = longitude
    val itemLatLng
        get() = LatLng(itemLatitude, itemLongitude)
    val itemDistinction = distinction

    private val itemInfo = info
}