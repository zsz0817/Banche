package com.example.shizhuan.banche.Search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
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
        AMap.InfoWindowAdapter, CloudSearch.OnCloudSearchListener, AMap.OnInfoWindowClickListener, View.OnClickListener{

    private CloudSearch mCloudSearch;
    private MapView mapView;
    private AMap mAMap;
    private ImageView tothere;

    private Marker mCloudIDMarer;
    private String TAG = "AMapYunTuDemo";
    private String mLocalCityName = "深圳市";
    private ArrayList<CloudItem> items = new ArrayList<CloudItem>();

    private ArrayList<CloudItem> mCloudItems;
    private List<CloudOverlay> overlays;
    private String mTableID = "5a49c02f2376c17f01cddda9";
    private String mId = "2"; // 用户table 行编号
    private String mKeyWord; // 搜索关键字
    private CloudSearch.Query mQuery;
    private CloudOverlay mPoiCloudOverlay;


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
//            mCloudSearch = new CloudSearch(this);// 初始化查询类
//            mCloudSearch.setOnCloudSearchListener(this);// 设置回调函数
            try {
                mQuery = new CloudSearch.Query(mTableID, mKeyWord, bound);
                mCloudSearch.searchCloudAsyn(mQuery);
            } catch (AMapException e) {
                ToastUtil.show(this, e.getErrorMessage());
                e.printStackTrace();
            }
//            mCloudSearch.searchCloudDetailAsyn(mTableID, mId);
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
        tothere = (ImageView)findViewById(R.id.toThere);
        tothere.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toThere:
                Intent intent = new Intent(SearchResult.this,WalkRouteCalculateActivity.class);
                //Toast.makeText(SearchResult.this,mCloudItems.size(),Toast.LENGTH_SHORT).show();
                intent.putExtra("clouditem", mCloudItems.get(0));
                startActivity(intent);
                break;
        }
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
//        if (rCode == AMapException.CODE_AMAP_SUCCESS && item != null) {
//            if (mCloudIDMarer != null) {
//                mCloudIDMarer.destroy();
//            }
//            mAMap.clear();
//            LatLng position = AMapUtil.convertToLatLng(item.getLatLonPoint());
//            mAMap.animateCamera(CameraUpdateFactory
//                    .newCameraPosition(new CameraPosition(position, 18, 0, 30)));
////            mCloudIDMarer = mAMap.addMarker(new MarkerOptions()
////                    .position(position)
////                    .title(item.getTitle())
////                    .icon(BitmapDescriptorFactory
////                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//            items.add(item);
//            Log.i(TAG, "_id" + item.getID());
//            Log.i(TAG, "_location" + item.getLatLonPoint().toString());
//            Log.i(TAG, "_name" + item.getTitle());
//            Log.i(TAG, "_address" + item.getSnippet());
//            Toast.makeText(SearchResult.this,"_address" + item.getSnippet(),Toast.LENGTH_SHORT).show();
//            Log.i(TAG, "_caretetime" + item.getCreatetime());
//            Log.i(TAG, "_updatetime" + item.getUpdatetime());
//            Log.i(TAG, "_distance" + item.getDistance());
//            Iterator iter = item.getCustomfield().entrySet().iterator();
//            while (iter.hasNext()) {
//                Map.Entry entry = (Map.Entry) iter.next();
//                Object key = entry.getKey();
//                Object val = entry.getValue();
//                Log.i(TAG, key + "   " + val);
//            }
//        } else {
//            ToastUtil.showerror(SearchResult.this, rCode);
//        }

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
                        //overlays.add(mPoiCloudOverlay);
                        for (CloudItem item : mCloudItems) {
                            items.add(item);
                            Log.i(TAG, "_id " + item.getID());
                            Log.i(TAG, "_location "
                                    + item.getLatLonPoint().toString());
                            Log.i(TAG, "_name " + item.getTitle());
                            Log.i(TAG, "_address " + item.getSnippet());
                            ToastUtil.showShortToast(SearchResult.this,"_address1" + item.getSnippet());
                            Log.i(TAG, "_caretetime " + item.getCreatetime());
                            Log.i(TAG, "_updatetime " + item.getUpdatetime());
                            Log.i(TAG, "_distance " + item.getDistance());
                            Iterator iter = item.getCustomfield().entrySet()
                                    .iterator();
                            while (iter.hasNext()) {
                                Map.Entry entry = (Map.Entry) iter.next();
                                Object key = entry.getKey();
                                Object val = entry.getValue();
                                Log.i(TAG, key + "   " + val);
                            }
                        }
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
