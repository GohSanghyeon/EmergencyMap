package com.example.emergencymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_location_list.*

class LocationList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)

        val address = ArrayList<locationData>()

        val keyaddress = resources.getString(R.string.BuildAddress)
        val keyDetail = resources.getString(R.string.DetailedPlace)

        address.add(locationData(getDrawable(R.drawable.aed_marker)!!, keyaddress, keyDetail))
        address.add(locationData(getDrawable(R.drawable.aed_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(locationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(locationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(locationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(locationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(locationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(locationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(locationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(locationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(locationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(locationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(locationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(locationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))



        val adapter = RecyclerAdapter1(address)
        recycler_View.adapter = adapter

    }


}
