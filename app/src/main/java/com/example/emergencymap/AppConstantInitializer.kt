package com.example.emergencymap

import android.app.Application

class AppConstantInitializer : Application(){
    companion object{
        var ITEMKEY_ITEMNO = ""
            private set
        var ITEMKEY_DISTINCTION = ""
            private set
        var ITEMKEY_BUILDADDRESS = ""
            private set
        var ITEMKEY_LATITUDE = ""
            private set
        var ITEMKEY_LONGITUDE = ""
            private set
        var ITEMKEY_DETAILEDPLACE = ""
            private set
        var ITEMKEY_MANAGERTEL = ""
            private set
    }

    override fun onCreate() {
        super.onCreate()
        
        ITEMKEY_ITEMNO = resources.getString(R.string.ItemNo)
        ITEMKEY_DISTINCTION = resources.getString(R.string.Distinction)
        ITEMKEY_BUILDADDRESS = resources.getString(R.string.BuildAddress)
        ITEMKEY_LATITUDE = resources.getString(R.string.Latitude)
        ITEMKEY_LONGITUDE = resources.getString(R.string.Longitude)
        ITEMKEY_DETAILEDPLACE = resources.getString(R.string.DetailedPlace)
        ITEMKEY_MANAGERTEL = resources.getString(R.string.ManagerTel)
    }
}