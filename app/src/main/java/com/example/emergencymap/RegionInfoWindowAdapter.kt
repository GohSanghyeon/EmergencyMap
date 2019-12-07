package com.example.emergencymap

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.ToggleButton
import androidx.annotation.InspectableProperty
import com.naver.maps.map.overlay.InfoWindow
import org.json.JSONException

class RegionInfoWindowAdapter(
    private val regionsInfo: Map<String, RegionInfo>
    , private val appContext: Context)
    : InfoWindow.DefaultViewAdapter(appContext){

    private var infoWindow: InfoWindow? = null
    private var regionName: String? = null

    var addAED = false
        set(value) { field = value;  infoWindow?.invalidate() }
    var addShelters = false
        set(value) { field = value;  infoWindow?.invalidate() }
    var addEmergencyRooms = false
        set(value) { field = value;  infoWindow?.invalidate() }

    override fun getContentView(nowInfoWindow: InfoWindow): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val infoView = inflater.inflate(R.layout.view_region_info, null)

        val textViewRegionName = infoView?.findViewById<TextView>(R.id.textRegionName)
        val textViewRegionInfo = infoView?.findViewById<TextView>(R.id.textRegionInfo)

        infoWindow = nowInfoWindow

        nowInfoWindow.marker?.let { nowMarker ->
            regionName = nowMarker.tag.toString()

            regionsInfo[nowMarker.tag]
                ?.let { regionInfo ->
                    textViewRegionName?.text = regionInfo.sido
                    textViewRegionInfo?.text = "총 ${calculateItemsNumber()}개"
                } ?: Log.d(this.javaClass.name, "잘못된 지역 정보 : ${nowMarker.tag}")
        } ?: Log.d(this.javaClass.name, "마커가 지정되지 않았음")

        return infoView
    }

    private fun calculateItemsNumber(): Int{
        var result = 0

        regionName?.let { regionName ->
            val nowRegionInfo = regionsInfo[regionName]

            nowRegionInfo?.let { nowRegionInfo ->
                //모든 아이템 버튼 체크 해제시 총 갯수 반환
                if(isAddNothing())
                    return nowRegionInfo.numberAll

                result += if (addAED) nowRegionInfo.numberAED else 0
                result += if (addShelters) nowRegionInfo.numberShelters else 0
                result += if (addEmergencyRooms) nowRegionInfo.numberEmergencyRooms else 0

                Log.d("현재대피소", result.toString())
            } ?: Log.d(
                "calculateItemsNumber"
                , "regionInfo에서 ${regionName}의 값을 가져오지 못함"
            )
        }

        return result
    }

    private fun isAddNothing() : Boolean{
        return !addAED && !addShelters && !addEmergencyRooms
    }
}