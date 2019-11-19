package com.example.emergencymap

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.annotation.UiThread
import androidx.core.view.isVisible
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.toast






class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var locationSource: FusedLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(PermissionManager.isExist_deniedPermission(this, permissionForEssential))
            PermissionManager.showRequest(this, permissionForEssential, PERMISSIONCODE_Essential,
                "어플리케이션의 원활한 ", "위치 조회, SMS, 파일 읽기")

        mountMap()
        setSupportActionBar(toolbar)

        val navHostFragment = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
            R.id.nav_tools, R.id.nav_share, R.id.nav_send), drawer_layout)
        setupActionBarWithNavController(navHostFragment, appBarConfiguration)
        nav_view.setupWithNavController(navHostFragment)

        buttonNowLocation.setOnClickListener{
            toast("현재 위치 조회")
        }

        buttonEmergency.setOnClickListener {
            layoutEmergencySelection.visibility = View.VISIBLE
        }
    }

    private fun mountMap() {
        //네이버 맵 클라이언트 ID 받아오기
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient("af5bvg9isp")

        val fm = supportFragmentManager
        val mapFragment = map_view as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_view, it).commit()
            }

        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, PERMISSIONCODE_Essential)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.apptuto ->{

            }
            R.id.howtool ->
                startActivity(Intent(this, SelectionForEducation::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        val marker = Marker()
        val heungup = LatLng(37.30260779, 127.9211684)
        marker.position = heungup
        naverMap.moveCamera(CameraUpdate.scrollTo(heungup))
        naverMap.cameraPosition
        marker.map = naverMap
        marker.width = 50
        marker.height = 80
        marker.icon = OverlayImage.fromResource(R.drawable.aed)
    }
}
