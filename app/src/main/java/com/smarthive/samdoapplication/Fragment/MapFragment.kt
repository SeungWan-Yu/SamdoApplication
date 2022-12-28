package com.smarthive.samdoapplication.Fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.smarthive.samdoapplication.Activity.MainActivity
import com.smarthive.samdoapplication.App
import com.smarthive.samdoapplication.R
import com.smarthive.samdoapplication.databinding.FragmentMapBinding
import com.smarthive.samdoapplication.model.MapPointData
import com.smarthive.samdoapplication.model.MapPointModel
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.daum.mf.map.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log


private var mapView: MapView? = null
private var zoomlevel = 0
var slidePanel : SlidingUpPanelLayout? = null
var addr : TextView? = null
var odor : TextView? = null
var date : TextView? = null
private var marker = arrayListOf<MapPOIItem>()
var mapdata : List<MapPointData>? = null


class MapFragment : Fragment() {
    var onResumeCheck = 0
    private lateinit var binding: FragmentMapBinding
    @SuppressLint("StaticFieldLeak")
    lateinit var mContext: Context
    @SuppressLint("StaticFieldLeak")
    private lateinit var eventListener : MarkerEventListener // 마커 클릭 이벤트 리스너
    @SuppressLint("StaticFieldLeak")
    private lateinit var mapviewListener : MapViewEventListener // 맵 클릭 이벤트 리스너
    @SuppressLint("StaticFieldLeak")
    private lateinit var currentlocationListner : MapView.CurrentLocationEventListener // 현재위치 이벤트 리스너
    val AddMarker = 0
    val GetMyLocation = 1
    var lm: LocationManager?= null
    @SuppressLint("StaticFieldLeak")
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var checkfloatingPermission = 0
    private val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1
    lateinit var resultListner : ActivityResultLauncher<Intent>



    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        eventListener = MarkerEventListener(mContext)
        mapviewListener = MapViewEventListener(mContext)
        currentlocationListner = CurrentLocationEventListener(mContext)
    }

    override fun onResume() {
        super.onResume()
        Log.e("my fragment" ," Resume")
        if(onResumeCheck != 0){
            if (checkfloatingPermission == 1) {
//                floatingService.floatingHeadWindow.hide()
            }
        }
        onResumeCheck += 1
//        CoroutineScope(Dispatchers.Main).launch {
////            progressBar6.visibility = View.VISIBLE
//            mapView = MapView(activity)
//            mapView?.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))  // 커스텀 말풍선 등록
//            mapView?.setPOIItemEventListener(eventListener)  // 마커 클릭 이벤트 리스너 등록
//            mapView?.setMapViewEventListener(mapviewListener) // 마커 클릭 이벤트 리스너 등록
////                Toast.makeText(activity,"현재 위치를 찾습니다.",Toast.LENGTH_SHORT).show()
////            getMyLocation()
////            getFarmMarker(FarmData)
//            binding.mapView.addView(mapView)
////            getList()
////            progressBar6.visibility = View.INVISIBLE
//        }
    }

    override fun onPause() {
        super.onPause()
//        binding.mapView.removeAllViews()
        Log.e("my fragment" ," Pause")
        if (checkfloatingPermission == 1) {
            if (onResumeCheck != 0) {
//                floatingService.floatingHeadWindow.show()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Log.e("my fragment" ," Stop")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
        resultListner = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                fromSub ->
            when(fromSub.resultCode){
//                0 ->
//                    toast("취소")
//                1 ->
//                    toast("확인")
                2 ->
                    Handler(Looper.getMainLooper()).postDelayed({
                        initmapview()
                    },1000)
            }

        }
        lm = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Handler(Looper.getMainLooper()).postDelayed({
            initmapview()
        },1000)

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        return binding.root
    }

    private fun initmapview() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.progressBar6.visibility = View.VISIBLE
            mapView = MapView(activity)
//            mapView?.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))  // 커스텀 말풍선 등록
            mapView?.setPOIItemEventListener(eventListener)  // 마커 클릭 이벤트 리스너 등록
            mapView?.setMapViewEventListener(mapviewListener) // 맵뷰 이벤트 리스너 등록
            val uNowPosition = MapPoint.mapPointWithGeoCoord(35.824092864990234, 127.14814758300781)
            zoomlevel = 6
            mapView?.setMapCenterPointAndZoomLevel(uNowPosition,zoomlevel, true)
//            mapView?.mapType = MapView.MapType.Hybrid
//                Toast.makeText(activity,"현재 위치를 찾습니다.",Toast.LENGTH_SHORT).show()
//            getMyLocation()
            getFarmMarker()
            when(App.prefs.mapType) {
                0 -> {
                    mapView!!.mapType = MapView.MapType.Standard
                    binding.mapNormal.setTextColor(Color.rgb(0,120,255))
                }
                1 -> {
                    mapView!!.mapType = MapView.MapType.Hybrid
                    binding.mapSkyview.setTextColor(Color.rgb(0,120,255))
                }
            }
//            mapView?.mapType = MapView.MapType.Hybrid
//            mapView?.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeadingWithoutMapMoving
            binding.mapView.addView(mapView)
//            getList()
            binding.progressBar6.visibility = View.INVISIBLE
        }
    }

    private fun getFarmMarker() {
        App.retrofitService.getMapPoint().enqueue(object : Callback<MapPointModel>{
            override fun onResponse(call: Call<MapPointModel>, response: Response<MapPointModel>) {
                val body = response.body()
                if (body?.result == true){
                    if(body.data.isEmpty()) {
                        Toast.makeText(context, "데이터가 없습니다", Toast.LENGTH_SHORT).show()
                    }else {
                        mapView?.removeAllPOIItems()
                        mapdata = body.data
                        val uNowPosition = MapPoint.mapPointWithGeoCoord(mapdata!![0].GPS_LATITUDE.toDouble(), mapdata!![0].GPS_LONGITUDE.toDouble())
                        zoomlevel = 6
                        mapView?.setMapCenterPointAndZoomLevel(uNowPosition,zoomlevel, true)

                        for (i in mapdata!!.indices) {
                            val marker1 = MapPOIItem()
                            marker1.apply {
                                itemName = mapdata!![i].SENSOR_IDX.toString()
                                userObject = mapdata!![i].ADDR
                                mapPoint = MapPoint.mapPointWithGeoCoord(
                                    mapdata!![i].GPS_LATITUDE.toDouble(),
                                    mapdata!![i].GPS_LONGITUDE.toDouble()
                                )   // 좌표
                                markerType = MapPOIItem.MarkerType.CustomImage
                                customImageResourceId = R.drawable.marker
                                selectedMarkerType = MapPOIItem.MarkerType.CustomImage
                                customSelectedImageResourceId = R.drawable.clickmarker
                                isCustomImageAutoscale = true
                                marker.add(marker1)
                            }
                        }
                        setAdapter(marker)
                    }
                }else if(body?.result == false){
                    Toast.makeText(context, "등록된 센서가 없습니다", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "에러", Toast.LENGTH_SHORT).show()
                }

            }
            override fun onFailure(call: Call<MapPointModel>, t: Throwable) {
                Toast.makeText(context, "에러", Toast.LENGTH_SHORT).show()
                Log.e("asdasd",t.message.toString())
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("my fragment" ," onDestroy")
        if (checkfloatingPermission == 1) {
//            activity?.unbindService(onServiceConnection)
        }
//        lm?.removeUpdates(lmlistner!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1 ->
                startActivity(Intent(context, MainActivity::class.java))
        }
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mapNormal.setOnClickListener {
            mapView?.mapType = MapView.MapType.Standard
            App.prefs.mapType = 0
            binding.mapNormal.setTextColor(Color.rgb(0,120,255))
            binding.mapSkyview.setTextColor(Color.BLACK)
        }
        binding.mapSkyview.setOnClickListener {
            mapView?.mapType = MapView.MapType.Hybrid
            App.prefs.mapType = 1
            binding.mapSkyview.setTextColor(Color.rgb(0,120,255))
            binding.mapNormal.setTextColor(Color.BLACK)
        }

        addr = binding.textView5
        odor = binding.textView12
        date = binding.textView17

        slidePanel = binding.mainFrame
//        slidePanel.anchorPoint = 0.2f
//        slidePanel!!.anchorPoint = 0.8f
        slidePanel!!.addPanelSlideListener(PanelEventListener())

        //마커 추가
        binding.addmarker.setOnClickListener {
//            PermissionCheck(AddMarker)
            getFarmMarker()
        }
        //현재위치
        binding.mylocation.setOnClickListener {
            PermissionCheck(GetMyLocation)
            getMyLocation()
        }
//        //네비
//        binding.navi.setOnClickListener {
//            PermissionCheck(Navi)
//        }
        //맵 확대
        binding.zoomin.setOnClickListener {
            if (zoomlevel != -2) {
                zoomlevel -= 1
                mapView?.setZoomLevel(zoomlevel, true)
            }
        }
        //맵 축소
        binding.zoomout.setOnClickListener {
            if (zoomlevel != 12) {
                zoomlevel += 1
                mapView?.setZoomLevel(zoomlevel, true)
            }
        }
    }

    // 이벤트 리스너
    inner class PanelEventListener : SlidingUpPanelLayout.PanelSlideListener {
        // 패널이 슬라이드 중일 때
        override fun onPanelSlide(panel: View?, slideOffset: Float) {
//            binding.tvSlideOffset.text = slideOffset.toString()
        }

        // 패널의 상태가 변했을 때
        override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
            if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
//                binding.btnToggle.text = "열기"
            } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
//                binding.btnToggle.text = "닫기"
            }
        }
    }
    private fun PermissionCheck(value:Int) {
        val permissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
        val backpermissionCheck = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if(backpermissionCheck == PackageManager.PERMISSION_DENIED) {
//                    backpermissionDialog(mContext)
                }else{
                    when (value) {
                        GetMyLocation ->
                            getMyLocation()
//                        AddMarker ->
//                            addmarker()
//                        Navi ->
//                            locationtracking(mContext)

                    }
                }
            }else {
                when (value) {
                    GetMyLocation ->
                        getMyLocation()
//                    AddMarker ->
//                        addmarker()
//                    Navi ->
//                        locationtracking(mContext)
                }
            }
        }else{
            // 권한 설정
            if (App.prefs.permissionDialogCheck == 0) {
                permissionDialog(mContext)
            }else{
                getPermission()
            }
        }
    }

//    @SuppressLint("MissingPermission")
//    fun floating(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 마시멜로우 이상일 경우
//            if (!Settings.canDrawOverlays(mContext)) {// 체크
//                floatingPermissionDialog(mContext)
//            }else{
//                val intent = Intent(requireContext(), LocationTrackingActivity::class.java)
//                resultListner.launch(intent)
//                binding.mapView.removeAllViews()
//            }
//        }
//    }




//    @SuppressLint("MissingPermission")
//    fun  addmarker() {
//        onResumeCheck = 0
//        try {
//            val userNowLocation: Location? =
//                lm!!.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: lm!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//            val uLatitude = userNowLocation!!.latitude
//            val uLongitude = userNowLocation.longitude
//            val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)
//            zoomlevel = 2
//            Toast.makeText(context, "현재위치 추적", Toast.LENGTH_SHORT).show()
//            mapView?.setMapCenterPointAndZoomLevel(uNowPosition, zoomlevel, true)
//            val nIntent = Intent(context, PopupActivity::class.java)
//            nIntent.putExtra("latitude",uLatitude)
//            nIntent.putExtra("longitude",uLongitude)
//            resultListner.launch(nIntent)
//        }catch(e: NullPointerException){
//            Log.e("LOCATION_ERROR", e.toString())
//            if (!lm!!.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//                toast("GPS를 켜주세요")
//            }
//            else if(lm?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) == null){
//                toast("잠시 후 다시 시도해주세요")
////                    lm!!.getCurrentLocation(
////                        LocationManager.GPS_PROVIDER,
////                        null,
////                        activity!!.mainExecutor,
////                        object : Consumer<Location?> {
////                            override fun accept(location: Location?) {
////                                // code
////                                toast("${location}")
////                            }
////                        })
//            }
//////                ActivityCompat.finishAffinity(requireActivity())
//////
//////                val intent = Intent(requireContext(), MainActivity::class.java)
//////                startActivity(intent)
//////                exitProcess(0)
//        }
//    }

    @SuppressLint("MissingPermission")
    fun getMyLocation() {
        marker.clear()
        try {
            //googleAPI 현재위치
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null){
                    val uNowPosition = MapPoint.mapPointWithGeoCoord(it.latitude, it.longitude)
                    zoomlevel = 2
                    mapView?.setMapCenterPointAndZoomLevel(uNowPosition, zoomlevel, true)
                    val mylocation = MapPOIItem()
                    mylocation.apply {
                        itemName = ""
                        userObject = ""
                        mapPoint = MapPoint.mapPointWithGeoCoord(it.latitude, it.longitude)   // 좌표
                        markerType = MapPOIItem.MarkerType.CustomImage
                        customImageResourceId = R.drawable.marker
                        selectedMarkerType = MapPOIItem.MarkerType.CustomImage
                        customSelectedImageResourceId = R.drawable.clickmarker
                        isCustomImageAutoscale = true
                    }
                    //                marker.add(mylocation)
                    currentlocationAdapter(mylocation)
                    //                marker.clear()
                }
            }
            //
            //LocationManager-getLastKnownLocation
//            val userNowLocation: Location? =
//                lm!!.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: lm!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//            val uLatitude = userNowLocation!!.latitude
//            val uLongitude = userNowLocation.longitude
//            val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)
////                toast("${userNowLocation.elapsedRealtimeNanos}")
//            zoomlevel = 2
//            mapView?.setMapCenterPointAndZoomLevel(uNowPosition, zoomlevel, true)
//            val mylocation = MapPOIItem()
//            mylocation.apply {
//                itemName = ""
//                userObject = ""
//                isShowCalloutBalloonOnTouch = false
//                mapPoint = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)   // 좌표
//                markerType = MapPOIItem.MarkerType.CustomImage
//                customImageResourceId = R.drawable.marker
////                    selectedMarkerType = MapPOIItem.MarkerType.CustomImage
////                    customSelectedImageResourceId = R.drawable.clickmarker
//                isCustomImageAutoscale = true
//            }
//            //                marker.add(mylocation)
//            currentlocationAdapter(mylocation)
//            //                marker.clear()

        }catch(e: Exception){
            Log.e("LOCATION_ERROR", e.toString())
            if (!lm!!.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                Toast.makeText(context, "GPS를 켜주세요", Toast.LENGTH_SHORT).show()
            }
            else if(lm?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) == null){
                Toast.makeText(context, "잠시 후 다시 시도해주세요", Toast.LENGTH_SHORT).show()
//                    lm!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f, lmlistner!!)
//                    lm!!.getCurrentLocation(
//                        LocationManager.GPS_PROVIDER,
//                        null,
//                        activity!!.mainExecutor,
//                        object : Consumer<Location?> {
//                            override fun accept(location: Location?) {
//                                // code
//                                toast("${location}")
//                            }
//                        })
            }
//                ActivityCompat.finishAffinity(requireActivity())
//
//                val intent = Intent(requireContext(), MainActivity::class.java)
//                startActivity(intent)
//                exitProcess(0)
        }
    }

    //
    fun getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 0)
        }
        // API 23 미만 버전에서는 ACCESS_BACKGROUND_LOCATION X
        else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1)
        }
    }

    // 백그라운드 권한 설정
    private fun backgroundPermission(){
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            ), 2)
    }

    fun floatingPermission(){
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:com.smarthive.plasmaapplication")
        )
        startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
    }

    //플로팅 위젯 권한 요청 다이얼로그
//    fun floatingPermissionDialog(context: Context){
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle("다른 앱 위에 그리기를 허용해주세요.")
//        val listener = DialogInterface.OnClickListener { _, p1 ->
//            when (p1) {
//                DialogInterface.BUTTON_POSITIVE ->
//                    floatingPermission()
//            }
//        }
//        builder.setPositiveButton("네", listener)
//        builder.setNegativeButton("아니오", null)
//        builder.show()
//    }

    // 백그라운드 권한 요청 다이얼로그
//    fun backpermissionDialog(context : Context){
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle("백그라운드 위치 권한을 위해 항상 허용으로 설정해주세요.")
//        val listener = DialogInterface.OnClickListener { _, p1 ->
//            when (p1) {
//                DialogInterface.BUTTON_POSITIVE ->
//                    backgroundPermission()
//            }
//        }
//        builder.setPositiveButton("네", listener)
//        builder.setNegativeButton("아니오", null)
//        builder.show()
//    }

    // 권한 요청
    fun permissionDialog(context : Context){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("지도 사용을 위해 위치 권한을 허용해주세요.")
        val listener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE ->
                    getPermission()
            }
        }
        builder.setPositiveButton("네", listener)
        builder.setNegativeButton("아니오", null)
        builder.show()
    }
    // 위치 추적 시작 요청
//    fun locationtracking(context : Context){
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle("위치 추적을 시작하시겠습니까?")
//        val listener = DialogInterface.OnClickListener { _, p1 ->
//            when (p1) {
//                DialogInterface.BUTTON_POSITIVE ->
//                    //새로운 acticity에서 실행하게 만들기
//                    floating()
//            }
//        }
//        builder.setPositiveButton("네", listener)
//        builder.setNegativeButton("아니오", null)
//        builder.show()
//    }

    private fun currentlocationAdapter(marker: MapPOIItem) {
        mapView?.removeAllPOIItems()
        mapView?.addPOIItem(marker)
    }
}
//    private fun setAdapter(marker: java.util.ArrayList<MapPOIItem>) {
//        mapView?.removeAllPOIItems()
//        for (i in 0 until marker.size){
//            mapView?.addPOIItem(marker[i])
//        }

// 커스텀 말풍선 클래스
//class CustomBalloonAdapter(inflater: LayoutInflater): CalloutBalloonAdapter {
//    val mCalloutBalloon: View = inflater.inflate(R.layout.balloon_layout, null)
//    val name: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_name)
//    private val address: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_address)
//
//    override fun getCalloutBalloon(poiItem: MapPOIItem?): View {
//        // 마커 클릭 시 나오는 말풍선
//        name.text = poiItem?.itemName   // 해당 마커의 정보 이용 가능
//        address.text = poiItem?.userObject.toString()
//        return mCalloutBalloon
//    }
//
//    override fun getPressedCalloutBalloon(poiItem: MapPOIItem?): View {
//        // 말풍선 클릭 시
////        address.text = "getPressedCalloutBalloon"
//        return mCalloutBalloon
//    }
//}
class MarkerEventListener(val context: Context): MapView.POIItemEventListener {
    override fun onPOIItemSelected(mapView: MapView?, poiItem: MapPOIItem?) {
        // 마커 클릭 시
        slidePanel!!.panelHeight = 740
        for (i in mapdata!!.indices) {
            if (poiItem?.itemName == mapdata!![i].SENSOR_IDX.toString()){
                addr!!.text = mapdata!![i].ADDR
                odor!!.text = mapdata!![i].ODOR.toString()
                date!!.text = mapdata!![i].MESURE_DT
            }
        }

    }
    @Deprecated("Deprecated in Java")
    override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, poiItem: MapPOIItem?) {
        // 말풍선 클릭 시 (Deprecated)
        // 이 함수도 작동하지만 그냥 아래 있는 함수에 작성하자
    }
    override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, poiItem: MapPOIItem?, buttonType: MapPOIItem.CalloutBalloonButtonType?) {
        // 말풍선 클릭 시
//        val builder = AlertDialog.Builder(context)
//        val itemList = arrayOf("토스트", "마커 삭제", "취소")
//        builder.setTitle("${poiItem?.itemName}")
//        builder.setItems(itemList) { dialog, which ->
//            when(which) {
//                0 -> Toast.makeText(context, "토스트", Toast.LENGTH_SHORT).show()  // 토스트
//                1 -> mapView?.removePOIItem(poiItem)    // 마커 삭제
//                2 -> dialog.dismiss()   // 대화상자 닫기
//            }
//        }
//        builder.show()
//        context.let{
//                val intent = Intent(context, FarmDetailActivity::class.java)
//                intent.putExtra("itemname",poiItem?.itemName)
//                context.startActivity(intent)
//        }
    }

    override fun onDraggablePOIItemMoved(mapView: MapView?, poiItem: MapPOIItem?, mapPoint: MapPoint?) {
        // 마커의 속성 중 isDraggable = true 일 때 마커를 이동시켰을 경우
    }
}
class MapViewEventListener(val context: Context): MapView.MapViewEventListener {
    override fun onMapViewInitialized(p0: MapView?) {
    }
    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
    }
    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
        zoomlevel = p1
    }
    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
        slidePanel!!.panelHeight = 0
    }
    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
    }
    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
        Log.d("map","latitude : ${p1?.mapPointGeoCoord?.latitude}, longitude : ${p1?.mapPointGeoCoord?.longitude}")
//            val geocoder = Geocoder(mContext)
//            val name = geocoder.getFromLocation(p1?.mapPointGeoCoord?.latitude!!, p1.mapPointGeoCoord?.longitude!!,1)
//            val marker1 = MapPOIItem()
//            marker1.apply {
//                itemName = name[0].subLocality
//                userObject = "선택농가"
//                mapPoint = MapPoint.mapPointWithGeoCoord(p1.mapPointGeoCoord?.latitude!!,p1.mapPointGeoCoord?.longitude!!)   // 좌표
//                markerType = MapPOIItem.MarkerType.CustomImage
//                customImageResourceId = R.drawable.marker
//                selectedMarkerType = MapPOIItem.MarkerType.CustomImage
//                customSelectedImageResourceId = R.drawable.clickmarker
//                isCustomImageAutoscale = true
//                setAdapter(marker1)
//            }
    }
    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
    }
    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
    }
    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
    }
}

class CurrentLocationEventListener(val context: Context): MapView.CurrentLocationEventListener{
    override fun onCurrentLocationUpdate(p0: MapView?, p1: MapPoint?, p2: Float) {
    }

    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {
    }

    override fun onCurrentLocationUpdateFailed(p0: MapView?) {
    }

    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {
    }

}
private fun setAdapter(marker: java.util.ArrayList<MapPOIItem>) {
    Log.e("asd","갯수 : ${marker.size}")
    for (i in 0 until marker.size){
        mapView?.addPOIItem(marker[i])
        Log.e("asd","index : ${marker[i].itemName}")
    }
}
