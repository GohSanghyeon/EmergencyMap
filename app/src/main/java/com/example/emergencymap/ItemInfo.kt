package com.example.emergencymap

import android.os.Parcel
import android.os.Parcelable
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject
import java.io.Serializable

class ItemInfo(
    val itemNo:Int
    , val itemLatitude: Double
    , val itemLongitude: Double
    , val itemDistinction: Int
    , private val info: JSONObject
    , val itemMarker: Marker)
{
    val itemLatLng
        get() = LatLng(itemLatitude, itemLongitude)

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