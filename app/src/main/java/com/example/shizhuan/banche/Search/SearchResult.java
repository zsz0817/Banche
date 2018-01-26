package com.example.shizhuan.banche.Search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.example.shizhuan.banche.entity.CloudOverlay;
import com.example.shizhuan.banche.R;
import com.example.shizhuan.banche.util.AMapUtil;
import com.example.shizhuan.banche.util.ToastUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ShiZhuan on 2018/1/1.
 */

public class SearchResult extends Activity implements AMap.OnMarkerClickListener,
        AMap.InfoWindowAdapter, CloudSearch.OnCloudSearchListener, AMap.OnInfoWindowClickListener {

    private CloudSearch mCloudSearch;
    private MapView mapView;
    private AMap mAMap;

    private Marker mCloudIDMarer;
    private String TAG = "AMapYunTuDemo";
    private String mLocalCityName = "深圳市";
    private ArrayList<CloudItem> items = new ArrayList<CloudItem>();

    private List<CloudItem> mCloudItems;
    private List<CloudOverlay> overlays;
    private String mTableID = "5a49c02f2376c17f01cddda9";
    private String mId = "2"; // 用户table 行编号
    private String mKeyWord; // 搜索关键字
    private CloudSearch.Query mQuery;
    private CloudOverlay mPoiCloudOverlay;

    private LatLonPoint mCenterPoint = new LatLonPoint(39.942753, 116.428650); // 周边搜索中心点
    private LatLonPoint mPoint1 = new LatLonPoint(39.941711, 116.382248);
    private LatLonPoint mPoint2 = new LatLonPoint(39.884882, 116.359566);
    private LatLonPoint mPoint3 = new LatLonPoint(39.878120, 116.437630);
    private LatLonPoint mPoint4 = new LatLonPoint(39.941711, 116.382248);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresult);
        mapView = (MapView) findViewById(R.id.search_result);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();

        //取得从上一个Activity当中传递过来的Intent对象
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            mKeyWord = intent.getStringExtra("ID");
            items.clear();
            CloudSearch.SearchBound bound = new CloudSearch.SearchBound(mLocalCityName);
            mCloudSearch = new CloudSearch(this);// 初始化查询类
            mCloudSearch.setOnCloudSearchListener(this);// 设置回调函数
//            try {
//                mQuery = new CloudSearch.Query(mTableID, mKeyWord, bound);
//                mCloudSearch.searchCloudAsyn(mQuery);
//            } catch (AMapException e) {
//                ToastUtil.show(this, e.getErrorMessage());
//                e.printStackTrace();
//            }
            mCloudSearch.searchCloudDetailAsyn(mTableID, mId);
        }
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (mAMap == null) {
            mAMap = mapView.getMap();
        }
        mCloudSearch = new CloudSearch(this);// 初始化查询类
        mCloudSearch.setOnCloudSearchListener(this);// 设置回调函数
        mAMap.setOnMarkerClickListener(this);
        mAMap.setOnInfoWindowClickListener(this);
        mAMap.setInfoWindowAdapter(this);
        mAMap.setOnInfoWindowClickListener(this);

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onCloudItemDetailSearched(CloudItemDetail item, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS && item != null) {
            if (mCloudIDMarer != null) {
                mCloudIDMarer.destroy();
            }
            mAMap.clear();
            LatLng position = AMapUtil.convertToLatLng(item.getLatLonPoint());
            mAMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(new CameraPosition(position, 18, 0, 30)));
            mCloudIDMarer = mAMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(item.getTitle())
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            items.add(item);
            Log.d(TAG, "_id" + item.getID());
            Log.d(TAG, "_location" + item.getLatLonPoint().toString());
            Log.d(TAG, "_name" + item.getTitle());
            Log.d(TAG, "_address" + item.getSnippet());
            Log.d(TAG, "_caretetime" + item.getCreatetime());
            Log.d(TAG, "_updatetime" + item.getUpdatetime());
            Log.d(TAG, "_distance" + item.getDistance());
            Iterator iter = item.getCustomfield().entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                Log.d(TAG, key + "   " + val);
            }
        } else {
            ToastUtil.showerror(SearchResult.this, rCode);
        }

    }

    @Override
    public void onCloudSearched(CloudResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {
                if (result.getQuery().equals(mQuery)) {
                    mCloudItems = result.getClouds();

                    if (mCloudItems != null && mCloudItems.size() > 0) {
                        mAMap.clear();
                        mPoiCloudOverlay = new CloudOverlay(mAMap, mCloudItems);
                        mPoiCloudOverlay.removeFromMap();
                        mPoiCloudOverlay.addToMap();
                        mPoiCloudOverlay.zoomToSpan();
                        overlays.add(mPoiCloudOverlay);
                        for (CloudItem item : mCloudItems) {
                            items.add(item);
                            Log.d(TAG, "_id " + item.getID());
                            Log.d(TAG, "_location "
                                    + item.getLatLonPoint().toString());
                            Log.d(TAG, "_name " + item.getTitle());
                            Log.d(TAG, "_address " + item.getSnippet());
                            Log.d(TAG, "_caretetime " + item.getCreatetime());
                            Log.d(TAG, "_updatetime " + item.getUpdatetime());
                            Log.d(TAG, "_distance " + item.getDistance());
                            Iterator iter = item.getCustomfield().entrySet()
                                    .iterator();
                            while (iter.hasNext()) {
                                Map.Entry entry = (Map.Entry) iter.next();
                                Object key = entry.getKey();
                                Object val = entry.getValue();
                                Log.d(TAG, key + "   " + val);
                            }
                        }
//                        if (mQuery.getBound().getShape()
//                                .equals(CloudSearch.SearchBound.BOUND_SHAPE)) {// 圆形
//                            mAMap.addCircle(new CircleOptions()
//                                    .center(new LatLng(mCenterPoint
//                                            .getLatitude(), mCenterPoint
//                                            .getLongitude())).radius(5000)
//                                    .strokeColor(
//                                            // Color.argb(50, 1, 1, 1)
//                                            Color.RED)
//                                    .fillColor(Color.argb(50, 1, 1, 1))
//                                    .strokeWidth(5));
//
//                            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                    new LatLng(mCenterPoint.getLatitude(),
//                                            mCenterPoint.getLongitude()), 12));
//
//                        } else if (mQuery.getBound().getShape()
//                                .equals(CloudSearch.SearchBound.POLYGON_SHAPE)) {
//                            mAMap.addPolygon(new PolygonOptions()
//                                    .add(AMapUtil.convertToLatLng(mPoint1))
//                                    .add(AMapUtil.convertToLatLng(mPoint2))
//                                    .add(AMapUtil.convertToLatLng(mPoint3))
//                                    .add(AMapUtil.convertToLatLng(mPoint4))
//                                    .fillColor(Color.argb(50, 1, 1, 1))
//                                    .strokeColor(Color.RED).strokeWidth(1));
//                            LatLngBounds bounds = new LatLngBounds.Builder()
//                                    .include(AMapUtil.convertToLatLng(mPoint1))
//                                    .include(AMapUtil.convertToLatLng(mPoint2))
//                                    .include(AMapUtil.convertToLatLng(mPoint3))
//                                    .build();
//                            mAMap.moveCamera(CameraUpdateFactory
//                                    .newLatLngBounds(bounds, 50));
//                        }
//                        else if ((mQuery.getBound().getShape()
//                                .equals(CloudSearch.SearchBound.LOCAL_SHAPE))) {
//                            mPoiCloudOverlay.zoomToSpan();
//                        }

                    } else {
                        ToastUtil.show(this, R.string.no_result);
                    }
                }
            } else {
                ToastUtil.show(this, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(SearchResult.this, rCode);
        }

    }

    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {
        String tile = arg0.getTitle();
        for (CloudItem item : items) {
            if (tile.equals(item.getTitle())) {
                Toast.makeText(SearchResult.this,"定位成功"+item.getTitle(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SearchResult.this,
                        WalkRouteActivity.class);
                intent.putExtra("clouditem", item);
                startActivity(intent);
                break;
            }

        }

    }

    /**
     *
     * 返回键监听
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();

        }
        return super.onKeyDown(keyCode, event);
    }

}
