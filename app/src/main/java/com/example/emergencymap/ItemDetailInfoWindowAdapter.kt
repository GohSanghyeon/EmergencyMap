package com.example.emergencymap

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.naver.maps.map.overlay.InfoWindow
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ItemDetailInfoWindowAdapter(
    private val appContext: Context)
    : InfoWindow.DefaultViewAdapter(appContext) {
    private val lockItemInfo = ReentrantLock()

    var itemInfo: ItemInfo? = null
        set(value){ lockItemInfo.withLock { field = value }}

    override fun getContentView(nowInfoWindow: InfoWindow): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val infoView = inflater.inflate(R.layout.view_item_detail_info, null)

        lockItemInfo.withLock {
            itemInfo?.let { itemInfo ->
                val textViewItemTitle = infoView?.findViewById<TextView>(R.id.textItemTitle)
                val textViewItemAddress = infoView?.findViewById<TextView>(R.id.textItemAddress)
                val textViewItemDetail = infoView?.findViewById<TextView>(R.id.textItemDetail)
                val textViewItemManagerTel =
                    infoView?.findViewById<TextView>(R.id.textItemManagerTel)

                textViewItemTitle?.text = when (itemInfo.itemDistinction) {
                    appContext.resources.getInteger(R.integer.AED) -> "AED"
                    appContext.resources.getInteger(R.integer.TsunamiShelter) -> "지진해일대피소"
                    appContext.resources.getInteger(R.integer.MBWShelter) -> "민방위대피소"
                    appContext.resources.getInteger(R.integer.EmergencyRoom) -> "응급의료기관"
                    appContext.resources.getInteger(R.integer.Pharmacy) -> "약국"
                    else -> {
                        Log.d(
                            "ItemDetailInfoWindow"
                            , "잘못된 Distinction : ${itemInfo.itemDistinction}"
                        );
                        ""
                    }
                }

                val keyAddress = appContext.resources.getString(R.string.BuildAddress)
                val keyDetail = appContext.resources.getString(R.string.DetailedPlace)
                val keyManagerTel = appContext.resources.getString(R.string.ManagerTel)

                textViewItemAddress?.text = "주소 : ${itemInfo.itemAttributes[keyAddress]}"
                textViewItemDetail?.text = itemInfo.itemAttributes[keyDetail]

                val managerTel = itemInfo.itemAttributes[keyManagerTel]
                textViewItemManagerTel?.text = if(managerTel == "") ""
                    else "관리자 전화번호 : ${itemInfo.itemAttributes[keyManagerTel]}"

            } ?: Log.d("itemDetailInfoWindow", "itemInfo가 설정되지 않음(null)")
        }

        return infoView
    }
}