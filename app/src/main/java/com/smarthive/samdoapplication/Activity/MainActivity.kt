package com.smarthive.samdoapplication.Activity

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.smarthive.samdoapplication.App
import com.smarthive.samdoapplication.Fragment.MapFragment
import com.smarthive.samdoapplication.Fragment.PlasmaFragment
import com.smarthive.samdoapplication.Fragment.SensorFragment
import com.smarthive.samdoapplication.R
import com.smarthive.samdoapplication.databinding.ActivityMainBinding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding : ActivityMainBinding
    lateinit var drawerLayout: DrawerLayout
    private val TAG_PLASMA_FRAGMENT = "plasma_fragment"
    private val TAG_SENSOR_FRAGMENT = "sensor_fragment"
    private val TAG_STATE_FRAGMENT = "state_fragment"
    private val TAG_MAP_FRAGMENT = "map_fragment"
    var mBackWait:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // 네비게이션 메뉴를 초기화
        initNavigationMenu()

        // Default Fragment of MainActivity
        setFragment(TAG_MAP_FRAGMENT, MapFragment())
        setActionBarTitle("플라즈마")
    }
    /* Fragment State 유지 함수 */
    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null) {
            ft.add(R.id.main_nav_host, fragment, tag)
        }

        val plasma = manager.findFragmentByTag(TAG_PLASMA_FRAGMENT)
        val sensor = manager.findFragmentByTag(TAG_SENSOR_FRAGMENT)
        val map = manager.findFragmentByTag(TAG_MAP_FRAGMENT)

        // Hide all Fragment
        if (plasma != null) {
            ft.hide(plasma)
        }
        if (sensor != null) {
            ft.hide(sensor)
        }
        if (map != null) {
            ft.hide(map)
        }

        // Show  current Fragment
        if (tag == TAG_PLASMA_FRAGMENT) {
            if (plasma != null) {
                ft.show(plasma)
            }
        }
        if (tag == TAG_SENSOR_FRAGMENT) {
            if (sensor != null) {
                ft.show(sensor)
            }
        }
        if (tag == TAG_MAP_FRAGMENT) {
            if (map != null) {
                ft.show(map)
            }
        }

        ft.commitAllowingStateLoss()
    }
    private fun setActionBarTitle(title: String?) {
        val navMenu = binding.mainToolBar.mainToolBarLogo
        navMenu.text = title
//        val actionBar: androidx.appcompat.app.ActionBar? = supportActionBar
//        if (actionBar != null) {
//            actionBar.title = title
//        }
    }

    private fun initNavigationMenu(){
        drawerLayout = binding.mainDrawerLayout
        val navView = binding.mainNavigationView



        navView.setNavigationItemSelectedListener(this)

        // 네비게이션 아이콘에 클릭 이벤트 연결
        val navMenu = binding.mainToolBar.mainToolBarNav
        navMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // 네비게이션 헤더 메뉴에 클릭 이벤트 연결
        val headerView = navView.getHeaderView(0)
        // drawer 닫기 버튼
        val closeBtn = headerView.findViewById<ImageView>(R.id.nav_close)
        closeBtn.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        // 로그인 관련 버튼
        val loginBtn = headerView.findViewById<TextView>(R.id.nav_login)
        val id = headerView.findViewById<TextView>(R.id.nav_id)
//        if (App.prefs.myId != ""){
        id.text = "${App.prefs.myId} 님"
//        }
        loginBtn.setOnClickListener {
//            if (App.prefs.myId == ""){
            App.prefs.myId = ""
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
//            }
        }

    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean{
        when(item.itemId){
            R.id.plasmaFragment -> {
                setFragment(TAG_PLASMA_FRAGMENT, PlasmaFragment())
                drawerLayout.closeDrawer(GravityCompat.START)
                setActionBarTitle("플라즈마")
            }
            R.id.sensorFragment -> {
                setFragment(TAG_SENSOR_FRAGMENT, SensorFragment())
                drawerLayout.closeDrawer(GravityCompat.START)
                setActionBarTitle("센서")
            }
            R.id.mapFragment -> {
                setFragment(TAG_MAP_FRAGMENT, MapFragment())
                drawerLayout.closeDrawer(GravityCompat.START)
                setActionBarTitle("악취지도")
            }
        }
        return false
    }

    override fun onBackPressed() {
//        val mapFragment : MapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as MapFragment
        // 뒤로가기 버튼 클릭
//        mapFragment.onServiceConnection
        if(System.currentTimeMillis() - mBackWait >=2000 ) {
            mBackWait = System.currentTimeMillis()
            Snackbar.make(binding.mainlayout,getString(R.string.backbtn), Snackbar.LENGTH_LONG).show()
        } else {
            finish() //액티비티 종료
        }
    }
}