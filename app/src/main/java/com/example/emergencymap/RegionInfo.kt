package com.example.emergencymap

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker

class RegionInfo(
    sido: String
    , numberAED: Int
    , numberShelters: Int
    , numberEmergencyRooms: Int
    , centerLatitude: Double
    , centerLongitude: Double
    , marker: Marker){
    val sido = sido

    val numberAED = numberAED
    val numberShelters = numberShelters
    val numberEmergencyRooms = numberEmergencyRooms
    val numberAll get() = numberAED + numberShelters + numberEmergencyRooms

    val centerLatitude = centerLatitude
    val centerLongitude = centerLongitude
    val centerLatLng get() = LatLng(centerLatitude, centerLongitude)

    val RegionMarker = marker
}