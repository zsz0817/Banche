package com.example.shizhuan.banche;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;
import com.example.shizhuan.banche.Http.OkHttpClientManager;
import com.example.shizhuan.banche.Search.BusLocateActivity;
import com.example.shizhuan.banche.Search.SearchActivity;
import com.example.shizhuan.banche.Setting.SettingActivity;
import com.example.shizhuan.banche.util.Constants;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener,AMapLocationListener{

    private final int MSG_HELLO = 0;
    private Handler mHandler;

    ImageButton imageButton1,imageButton2,imageButton3;
    TextView stop;
    Intent intent;
    private ImageView bus;
    private MapView mapView;
    private AMap aMap;
    private MarkerOptions markerOption;
    CameraUpdate cameraUpdate;

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    MyLocationStyle myLocationStyle;

    //以前的定位点
    private LatLng oldLatLng;
    //是否是第一次定位
    private boolean isFirstLatLng;
    //查询班车位置
    private boolean isQueryNow;

    Map<String,Map<String,Object>> param = new HashMap<>();
    Map<String,Object> map1,map2;


    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mapView.onCreate(saveInstanceState);
        startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                amapLocation.getAoiName();//获取当前定位点的AOI信息
                amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                amapLocation.getFloor();//获取当前室内定位的楼层
                amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Toast.makeText(MainActivity.this,"AmapError"+amapLocation.getErrorCode()+amapLocation.getErrorInfo(),Toast.LENGTH_LONG).show();
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ImageButton1:
//                intent = new Intent(MainActivity.this,SearchActivity.class);
//                startActivity(intent);
                break;
            case R.id.ImageButton2:
                intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.ImageButton3:
                intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.bus:
                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                df1.format(date);//定位时间
                map1 = new HashMap<>();
                map2 = new HashMap<>();
                map1.put("TRACDE","BC00001");
                map1.put("TRADAT",df1.format(date));
                map1.put("TRATIM",df2.format(date));
                map1.put("USRNAM","zhou");
                map2.put("line","1");
                param.put("head",map1);
                param.put("body",map2);
                new CustomThread().start();//新建并启动CustomThread实例
                net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(param);
                Toast.makeText(MainActivity.this,jsonArray.toString(),Toast.LENGTH_SHORT).show();
                String tmp = jsonArray.toString().substring(1,jsonArray.toString().length()-1);
                Log.d("Test", "MainThread is ready to send msg:" + tmp);
                mHandler.obtainMessage(MSG_HELLO, tmp).sendToTarget();//发送消息到CustomThread实例
                bus.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);
                aMap.getUiSettings().setMyLocationButtonEnabled(false);
//                deactivate();
                mlocationClient.stopLocation();
//                    OkHttpClientManager.getAsyn(Constants.url_getLocation, new OkHttpClientManager.StringCallback() {
//                        @Override
//                        public void onFailure(Request request, IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject allObject = new JSONObject(response);
//                                JSONObject head = allObject.getJSONObject("head");
//                                JSONObject body = allObject.getJSONObject("body");
//                                String retcode = head.getString("RTNSTS");
//                                double longitude = body.getDouble("LONGITUDE");
//                                double latitude = body.getDouble("LATITUDE");
//                                LatLng newLatLng = new LatLng(latitude,longitude);
//                                changeCamera(
//                                        CameraUpdateFactory.newCameraPosition(new CameraPosition(
//                                                newLatLng, 18, 30, 0)));
//                                aMap.clear();
//                                aMap.addMarker(new MarkerOptions().position(newLatLng)
//                                        .icon(BitmapDescriptorFactory
//                                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//
//                                //判断是否第一次查询班车位置
//                                if(isFirstLatLng){
//                                    //记录第一次的位置信息
//                                    oldLatLng = newLatLng;
//                                    isFirstLatLng = false;
//                                }
//
//                                //位置有变化
//                                if(oldLatLng != newLatLng){
//                                    setUpMap( oldLatLng , newLatLng );
//                                    oldLatLng = newLatLng;
//                                }
//
//                            } catch (JSONException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            } catch (Exception e){
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                }
                break;
            case R.id.stop:
                bus.setVisibility(View.VISIBLE);
                stop.setVisibility(View.GONE);
                isQueryNow = false;  //停止查询班车位置
                aMap.clear();
                startLocation();
                break;
        }
    }

    class CustomThread extends Thread {
        @Override
        public void run() {
            //建立消息循环的步骤
            Looper.prepare();//1、初始化Looper
            mHandler = new Handler(){//2、绑定handler到CustomThread实例的Looper对象
                public void handleMessage (Message msg) {//3、定义处理消息的方法
                    switch(msg.what) {
                        case MSG_HELLO:
                            isQueryNow = true;
                            isFirstLatLng = true;
                            while (isQueryNow) {
                                try {
                                    Response response = OkHttpClientManager.getAsyn(Constants.url_getLocation + msg.obj);
                                    JSONObject allObject = new JSONObject(response.body().string());
                                    JSONObject head = allObject.getJSONObject("head");
                                    JSONObject body = allObject.getJSONObject("body");
                                    String retcode = head.getString("RTNSTS");
                                    double longitude = body.getDouble("LONGITUDE");
                                    double latitude = body.getDouble("LATITUDE");
                                    LatLng newLatLng = new LatLng(latitude, longitude);

                                    aMap.clear();
                                    aMap.addMarker(new MarkerOptions().position(newLatLng)
                                            .icon(BitmapDescriptorFactory
                                                    .fromResource(R.drawable.navi_car_circle)));
                                    //判断是否第一次查询班车位置
                                    if (isFirstLatLng) {
                                        //记录第一次的位置信息
                                        oldLatLng = newLatLng;
                                        isFirstLatLng = false;

                                        changeCamera(
                                                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                                                        newLatLng, 18, 30, 0)));
                                    }

                                    //位置有变化
                                    if (oldLatLng != newLatLng) {
                                        setUpMap(oldLatLng, newLatLng);
                                        oldLatLng = newLatLng;
                                    }

                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                    }
                }
            };
            Looper.loop();//4、启动消息循环
        }
    }

    /**
     * 初始化AMap对象
     */
    private void init(){
        imageButton1 = (ImageButton)findViewById(R.id.ImageButton1);
        imageButton2 = (ImageButton)findViewById(R.id.ImageButton2);
        imageButton3 = (ImageButton)findViewById(R.id.ImageButton3);
        stop = (TextView)findViewById(R.id.stop);

        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        stop.setOnClickListener(this);

        bus = (ImageView) findViewById(R.id.bus);
        bus.setOnClickListener(this);

        mapView = (MapView)findViewById(R.id.map);
        if (aMap==null)
            aMap=mapView.getMap();
    }

    private void startLocation(){
        mlocationClient = new AMapLocationClient(MainActivity.this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //检测系统是否打开开启了地理定位权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String []{android.Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
        mLocationOption.setOnceLocation(true);//单次定位

        mLocationOption.setNeedAddress(true);

        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除

        //启动定位
        mlocationClient.startLocation();

        myLocationStyle = new MyLocationStyle();
        myLocationStyle.strokeWidth(0);
        myLocationStyle.radiusFillColor(R.color.sss);

//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style

        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setTrafficEnabled(false);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));//设置地图缩放级别，越大越详细
    }

    /**
     * 停止定位
     */
    public void deactivate() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(null!=mlocationClient){
            mlocationClient.onDestroy();
        }
    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap(LatLng latLng) {
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(latLng)
                .draggable(true);
        aMap.addMarker(markerOption);
    }


    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update) {
        aMap.moveCamera(update);
    }

    /**绘制两个坐标点之间的线段,从以前位置到现在位置*/
    private void setUpMap(LatLng oldData,LatLng newData ) {
        // 绘制一个大地曲线
        aMap.addPolyline((new PolylineOptions())
                .add(oldData, newData)
                .geodesic(true).color(Color.GREEN));
    }

}
