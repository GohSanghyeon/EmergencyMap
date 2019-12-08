package com.example.emergencymap

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import org.json.JSONObject

class ItemInfo(
    val itemNo:Int
    , val itemLatitude: Double
    , val itemLongitude: Double
    , val itemDistinction: Int
    , private val info: JSONObject
    , val itemMarker: Marker? = null)
{
    val itemLatLng
        get() = LatLng(itemLatitude, itemLongitude)

    constructor(
        itemNo: Int
        , itemLatitude: Double
        , itemLongitude: Double
        , itemDistinction: Int
        , itemBuildAddress: String
        , itemDetailedPlace: String
        , itemManagerTel: String
    ) : this(itemNo, itemLatitude, itemLongitude, itemDistinction
        , JSONObject("{}")) {
        val infoMap = mutableMapOf<String, String>()
        infoMap[AppConstantInitializer.ITEMKEY_ITEMNO] = itemNo.toString()
        infoMap[AppConstantInitializer.ITEMKEY_LATITUDE] = itemLatitude.toString()
        infoMap[AppConstantInitializer.ITEMKEY_LONGITUDE] = itemLongitude.toString()
        infoMap[AppConstantInitializer.ITEMKEY_DISTINCTION] = itemDistinction.toString()
        infoMap[AppConstantInitializer.ITEMKEY_BUILDADDRESS] = itemBuildAddress
        infoMap[AppConstantInitializer.ITEMKEY_DETAILEDPLACE] = itemDetailedPlace
        infoMap[AppConstantInitializer.ITEMKEY_MANAGERTEL] = itemManagerTel

        itemAttributes = infoMap
    }

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