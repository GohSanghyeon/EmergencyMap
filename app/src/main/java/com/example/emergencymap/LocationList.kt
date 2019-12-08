package com.example.emergencymap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_location_list.*

class LocationList: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)

        val address = ArrayList<LocationData>()

        val keyaddress = resources.getString(R.string.BuildAddress)
        val keyDetail = resources.getString(R.string.DetailedPlace)
        val keyDistinction = resources.getString(R.string.Distinction)

        val showingBuildAddresses = intent.getStringArrayExtra(keyaddress)
        val showingDetailPlaces = intent.getStringArrayExtra(keyDetail)
        val showingDistinction = intent.getIntArrayExtra(keyDistinction)

        for(nowPosition in 0 until showingBuildAddresses.size){
            Log.d("LocationList Test", "$nowPosition : ${showingBuildAddresses[nowPosition]} ${showingDetailPlaces[nowPosition]}")
        }

        address.add(LocationData(getDrawable(R.drawable.aed_marker)!!, keyaddress, keyDetail))
        address.add(LocationData(getDrawable(R.drawable.aed_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(LocationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(LocationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(LocationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(LocationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(LocationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(LocationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(LocationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(LocationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(LocationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(LocationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(LocationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))
        address.add(LocationData(getDrawable(R.drawable.pharmacy_marker)!!, getString(R.string.BuildAddress), getString(R.string.DetailedPlace)))



        val adapter = ItemRecyclerViewAdapter(address)
        recycler_View.adapter = adapter

    }


}
