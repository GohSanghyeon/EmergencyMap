package com.example.emergencymap.notshowing

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import com.example.emergencymap.PermissionManager
import com.google.android.gms.location.*
import org.jetbrains.anko.toast

class LocationProvider(private val appContext : Context) {

    private lateinit var locationProvider: FusedLocationProviderClient
    private lateinit var settingLocationRequest: LocationRequest
    private var nowActiveCallback: OnlyForOneLocation? = null

    companion object{
        val permissionForLocation: Array<out String> = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private inner class OnlyForOneLocation(
        private val locationProvider: FusedLocationProviderClient
        , private val callbackWithNewLocation: ((Location?) -> Unit)? = null
    ) : LocationCallback() {

        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            val positionCurrent = locationResult?.lastLocation
            positionCurrent?.let{
                Log.d("New Location"
                    , "${"%.6f".format(it.latitude)}, ${"%.6f".format(it.longitude)}")
            }

            locationProvider.removeLocationUpdates(this)
            if(nowActiveCallback === this)   //this is the latest callback
                nowActiveCallback = null

            callbackWithNewLocation?.invoke(positionCurrent)
        }
    }

    init{
        setLocationProvider()
    }

    private fun setLocationProvider(){
        locationProvider = LocationServices.getFusedLocationProviderClient(appContext)

        settingLocationRequest = LocationRequest().apply{
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 4000
            fastestInterval = 1500
            maxWaitTime = 10000                                  //milliseconds
        }
    }

    fun requestNowLocation(
        functionCode: Int
        , doingWithNewLocation: ((Location?) -> Unit)? = null
    ){
        val nowActivity: Activity = appContext as Activity
        if(nowActivity != null)             //If invoker is in activity
            nowActivity.let {
                if(!isOnDeviceLocationSetting()) {
                    showSelectionForLocationSetting(it)
                    return
                }

                if (PermissionManager.existDeniedpermission(it, permissionForLocation)) {
                    PermissionManager.showOnlyRequestAnd(
                        it, permissionForLocation, functionCode
                        , "현재 위치를 파악하려면 위치 조회 권한이 필요합니다."
                    )
                    { _, _ ->
                        it.toast("권한이 없어 현재 위치를 조회할 수 없습니다.")
                    }

                    return
                }

                updateTheLatestLocation(doingWithNewLocation)
            }

        else                                //If invoker is not in activity
            if (!PermissionManager.existDeniedpermission(appContext, permissionForLocation)
                && isOnDeviceLocationSetting())
                updateTheLatestLocation(doingWithNewLocation)
    }

    private fun updateTheLatestLocation(doingWithNewLocation: ((Location?) -> Unit)? = null){
        val nowActivity: Activity = appContext as Activity
        nowActivity?.let{ it.toast("현재 위치 조회중...") }

        if(nowActiveCallback == null)
            nowActiveCallback = OnlyForOneLocation(locationProvider, doingWithNewLocation)
        locationProvider.requestLocationUpdates(settingLocationRequest, nowActiveCallback, Looper.myLooper())
    }

    private fun isOnDeviceLocationSetting() : Boolean{
        val lm = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun showSelectionForLocationSetting(activity: Activity){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)

        builder.setMessage("위치 조회를 위해서 휴대폰의 위치 조회 기능을 켜야 합니다.")
        builder.setPositiveButton("예") { _, _ ->
            activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        builder.setNegativeButton("아니오"){ _, _ ->
            activity.toast("기능이 켜져있지 않아 위치 조회를 할 수 없습니다.")
        }

        builder.show()
    }
}