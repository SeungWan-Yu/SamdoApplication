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
    private lateinit var eventListener : MarkerEventListener // ?????? ?????? ????????? ?????????
    @SuppressLint("StaticFieldLeak")
    private lateinit var mapviewListener : MapViewEventListener // ??? ?????? ????????? ?????????
    @SuppressLint("StaticFieldLeak")
    private lateinit var currentlocationListner : MapView.CurrentLocationEventListener // ???????????? ????????? ?????????
    val AddMarker = 0
    val GetMyLocation = 1
    var lm: LocationManager?= null
    @SuppressLint("StaticFieldLeak")
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var checkfloatingPermission = 0
    private val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1
    lateinit var resultListner : ActivityResultLauncher<Intent>
    var findindex = 0



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
//            mapView?.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))  // ????????? ????????? ??????
//            mapView?.setPOIItemEventListener(eventListener)  // ?????? ?????? ????????? ????????? ??????
//            mapView?.setMapViewEventListener(mapviewListener) // ?????? ?????? ????????? ????????? ??????
////                Toast.makeText(activity,"?????? ????????? ????????????.",Toast.LENGTH_SHORT).show()
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
//                    toast("??????")
//                1 ->
//                    toast("??????")
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
//            mapView?.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))  // ????????? ????????? ??????
            mapView?.setPOIItemEventListener(eventListener)  // ?????? ?????? ????????? ????????? ??????
            mapView?.setMapViewEventListener(mapviewListener) // ?????? ????????? ????????? ??????
//            val uNowPosition = MapPoint.mapPointWithGeoCoord(35.824092864990234, 127.14814758300781)
//            zoomlevel = 6
//            mapView?.setMapCenterPointAndZoomLevel(uNowPosition,zoomlevel, true)
//            mapView?.mapType = MapView.MapType.Hybrid
//                Toast.makeText(activity,"?????? ????????? ????????????.",Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(context, "???????????? ????????????", Toast.LENGTH_SHORT).show()
                    }else {
                        mapView?.removeAllPOIItems()
                        mapdata = body.data
                        val uNowPosition = MapPoint.mapPointWithGeoCoord(mapdata!![0].GPS_LATITUDE.toDouble(), mapdata!![0].GPS_LONGITUDE.toDouble())
                        zoomlevel = 3
                        mapView?.setMapCenterPointAndZoomLevel(uNowPosition,zoomlevel, true)

                        for (i in mapdata!!.indices) {
                            var red = 0
                            var green = 0
                            var blue = 0
                            if (mapdata!![i].ODOR < 50){
                                red = 0
                                green = 255
                                blue = 0
                            }else if(mapdata!![i].ODOR <100){
                                red = 255
                                green = 0
                                blue = 0
                            }else{
                                red = 127
                                green = 0
                                blue = 255
                            }
                            val MARKER_POINT = MapPoint.mapPointWithGeoCoord(mapdata!![i].GPS_LATITUDE.toDouble(), mapdata!![i].GPS_LONGITUDE.toDouble())
                            val cicle = MapCircle(MARKER_POINT, (mapdata!![i].ODOR)*10,0,Color.argb(70,red,green,blue))
                            mapView?.addCircle(cicle)
//                            val marker1 = MapPOIItem()
//                            marker1.apply {
//                                itemName = mapdata!![i].SENSOR_IDX.toString()
//                                userObject = mapdata!![i].ADDR
//                                mapPoint = MapPoint.mapPointWithGeoCoord(
//                                    mapdata!![i].GPS_LATITUDE.toDouble(),
//                                    mapdata!![i].GPS_LONGITUDE.toDouble()
//                                )   // ??????
//                                markerType = MapPOIItem.MarkerType.CustomImage
//                                customImageResourceId = R.drawable.marker
//                                selectedMarkerType = MapPOIItem.MarkerType.CustomImage
//                                customSelectedImageResourceId = R.drawable.clickmarker
//                                isCustomImageAutoscale = true
//                                marker.add(marker1)
//                            }
                        }
                        setAdapter(marker)
                    }
                }else if(body?.result == false){
                    Toast.makeText(context, "????????? ????????? ????????????", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "??????", Toast.LENGTH_SHORT).show()
                }

            }
            override fun onFailure(call: Call<MapPointModel>, t: Throwable) {
                Toast.makeText(context, "??????", Toast.LENGTH_SHORT).show()
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

        //?????? ??????
        binding.addmarker.setOnClickListener {
//            PermissionCheck(AddMarker)
            findOdor()
        }
        //????????????
        binding.mylocation.setOnClickListener {
            PermissionCheck(GetMyLocation)
            getMyLocation()
        }
//        //??????
//        binding.navi.setOnClickListener {
//            PermissionCheck(Navi)
//        }
        //??? ??????
        binding.zoomin.setOnClickListener {
            if (zoomlevel != -2) {
                zoomlevel -= 1
                mapView?.setZoomLevel(zoomlevel, true)
            }
        }
        //??? ??????
        binding.zoomout.setOnClickListener {
            if (zoomlevel != 12) {
                zoomlevel += 1
                mapView?.setZoomLevel(zoomlevel, true)
            }
        }
    }

    private fun findOdor() {
        val uNowPosition = MapPoint.mapPointWithGeoCoord(mapdata!![findindex].GPS_LATITUDE.toDouble(), mapdata!![0].GPS_LONGITUDE.toDouble())
        zoomlevel = 3
        mapView?.setMapCenterPointAndZoomLevel(uNowPosition,zoomlevel, true)
        if (findindex == mapdata!!.size -1){
            findindex = 0
        }else {
            findindex += 1
        }
    }

    // ????????? ?????????
    inner class PanelEventListener : SlidingUpPanelLayout.PanelSlideListener {
        // ????????? ???????????? ?????? ???
        override fun onPanelSlide(panel: View?, slideOffset: Float) {
//            binding.tvSlideOffset.text = slideOffset.toString()
        }

        // ????????? ????????? ????????? ???
        override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
            if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
//                binding.btnToggle.text = "??????"
            } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
//                binding.btnToggle.text = "??????"
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
            // ?????? ??????
            if (App.prefs.permissionDialogCheck == 0) {
                permissionDialog(mContext)
            }else{
                getPermission()
            }
        }
    }

//    @SuppressLint("MissingPermission")
//    fun floating(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// ??????????????? ????????? ??????
//            if (!Settings.canDrawOverlays(mContext)) {// ??????
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
//            Toast.makeText(context, "???????????? ??????", Toast.LENGTH_SHORT).show()
//            mapView?.setMapCenterPointAndZoomLevel(uNowPosition, zoomlevel, true)
//            val nIntent = Intent(context, PopupActivity::class.java)
//            nIntent.putExtra("latitude",uLatitude)
//            nIntent.putExtra("longitude",uLongitude)
//            resultListner.launch(nIntent)
//        }catch(e: NullPointerException){
//            Log.e("LOCATION_ERROR", e.toString())
//            if (!lm!!.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//                toast("GPS??? ????????????")
//            }
//            else if(lm?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) == null){
//                toast("?????? ??? ?????? ??????????????????")
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
            //googleAPI ????????????
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null){
                    val uNowPosition = MapPoint.mapPointWithGeoCoord(it.latitude, it.longitude)
                    zoomlevel = 2
                    mapView?.setMapCenterPointAndZoomLevel(uNowPosition, zoomlevel, true)
                    val mylocation = MapPOIItem()
                    mylocation.apply {
                        itemName = "????????????"
                        userObject = ""
                        mapPoint = MapPoint.mapPointWithGeoCoord(it.latitude, it.longitude)   // ??????
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
//                mapPoint = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)   // ??????
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
                Toast.makeText(context, "GPS??? ????????????", Toast.LENGTH_SHORT).show()
            }
            else if(lm?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) == null){
                Toast.makeText(context, "?????? ??? ?????? ??????????????????", Toast.LENGTH_SHORT).show()
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
        // API 23 ?????? ??????????????? ACCESS_BACKGROUND_LOCATION X
        else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1)
        }
    }

    // ??????????????? ?????? ??????
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

    //????????? ?????? ?????? ?????? ???????????????
//    fun floatingPermissionDialog(context: Context){
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle("?????? ??? ?????? ???????????? ??????????????????.")
//        val listener = DialogInterface.OnClickListener { _, p1 ->
//            when (p1) {
//                DialogInterface.BUTTON_POSITIVE ->
//                    floatingPermission()
//            }
//        }
//        builder.setPositiveButton("???", listener)
//        builder.setNegativeButton("?????????", null)
//        builder.show()
//    }

    // ??????????????? ?????? ?????? ???????????????
//    fun backpermissionDialog(context : Context){
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle("??????????????? ?????? ????????? ?????? ?????? ???????????? ??????????????????.")
//        val listener = DialogInterface.OnClickListener { _, p1 ->
//            when (p1) {
//                DialogInterface.BUTTON_POSITIVE ->
//                    backgroundPermission()
//            }
//        }
//        builder.setPositiveButton("???", listener)
//        builder.setNegativeButton("?????????", null)
//        builder.show()
//    }

    // ?????? ??????
    fun permissionDialog(context : Context){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("?????? ????????? ?????? ?????? ????????? ??????????????????.")
        val listener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE ->
                    getPermission()
            }
        }
        builder.setPositiveButton("???", listener)
        builder.setNegativeButton("?????????", null)
        builder.show()
    }
    // ?????? ?????? ?????? ??????
//    fun locationtracking(context : Context){
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle("?????? ????????? ?????????????????????????")
//        val listener = DialogInterface.OnClickListener { _, p1 ->
//            when (p1) {
//                DialogInterface.BUTTON_POSITIVE ->
//                    //????????? acticity?????? ???????????? ?????????
//                    floating()
//            }
//        }
//        builder.setPositiveButton("???", listener)
//        builder.setNegativeButton("?????????", null)
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

// ????????? ????????? ?????????
//class CustomBalloonAdapter(inflater: LayoutInflater): CalloutBalloonAdapter {
//    val mCalloutBalloon: View = inflater.inflate(R.layout.balloon_layout, null)
//    val name: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_name)
//    private val address: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_address)
//
//    override fun getCalloutBalloon(poiItem: MapPOIItem?): View {
//        // ?????? ?????? ??? ????????? ?????????
//        name.text = poiItem?.itemName   // ?????? ????????? ?????? ?????? ??????
//        address.text = poiItem?.userObject.toString()
//        return mCalloutBalloon
//    }
//
//    override fun getPressedCalloutBalloon(poiItem: MapPOIItem?): View {
//        // ????????? ?????? ???
////        address.text = "getPressedCalloutBalloon"
//        return mCalloutBalloon
//    }
//}
class MarkerEventListener(val context: Context): MapView.POIItemEventListener {
    override fun onPOIItemSelected(mapView: MapView?, poiItem: MapPOIItem?) {
        // ?????? ?????? ???
//        slidePanel!!.panelHeight = 740
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
        // ????????? ?????? ??? (Deprecated)
        // ??? ????????? ??????????????? ?????? ?????? ?????? ????????? ????????????
    }
    override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, poiItem: MapPOIItem?, buttonType: MapPOIItem.CalloutBalloonButtonType?) {
        // ????????? ?????? ???
//        val builder = AlertDialog.Builder(context)
//        val itemList = arrayOf("?????????", "?????? ??????", "??????")
//        builder.setTitle("${poiItem?.itemName}")
//        builder.setItems(itemList) { dialog, which ->
//            when(which) {
//                0 -> Toast.makeText(context, "?????????", Toast.LENGTH_SHORT).show()  // ?????????
//                1 -> mapView?.removePOIItem(poiItem)    // ?????? ??????
//                2 -> dialog.dismiss()   // ???????????? ??????
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
        // ????????? ?????? ??? isDraggable = true ??? ??? ????????? ??????????????? ??????
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
//                userObject = "????????????"
//                mapPoint = MapPoint.mapPointWithGeoCoord(p1.mapPointGeoCoord?.latitude!!,p1.mapPointGeoCoord?.longitude!!)   // ??????
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
    Log.e("asd","?????? : ${marker.size}")
    for (i in 0 until marker.size){
        mapView?.addPOIItem(marker[i])
        Log.e("asd","index : ${marker[i].itemName}")
    }
}
