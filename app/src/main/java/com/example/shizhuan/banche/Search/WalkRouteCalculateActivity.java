package com.example.shizhuan.banche.Search;

import android.content.Intent;
import android.os.Bundle;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.core.LatLonPoint;
import com.example.shizhuan.banche.R;


public class WalkRouteCalculateActivity extends BaseActivity implements INaviInfoCallback {

    private CloudItem item;
    private LatLonPoint mEndPoint;
    LatLng p1 = new LatLng(39.993266, 116.473193);//首开广场
    LatLng p2 = new LatLng(39.917337, 116.397056);//故宫博物院
    LatLng p3 = new LatLng(39.904556, 116.427231);//北京站
    LatLng p4 = new LatLng(39.773801, 116.368984);//新三余公园(南5环)
    LatLng p5 = new LatLng(40.041986, 116.414496);//立水桥(北5环)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_basic_navi);
//        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
//        mAMapNaviView.onCreate(savedInstanceState);
//        mAMapNaviView.setAMapNaviViewListener(this);
//        mAMapNaviView.setNaviMode(AMapNaviView.NORTH_UP_MODE);

        //取得从上一个Activity当中传递过来的Intent对象
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            item = intent.getParcelableExtra("clouditem");
            mEndPoint = item.getLatLonPoint();
        }

        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null, null, new Poi(item.getTitle(), new LatLng(mEndPoint.getLatitude(), mEndPoint.getLongitude()), ""), AmapNaviType.DRIVER), WalkRouteCalculateActivity.this);
//        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null, null, new Poi("故宫博物院", p2, ""), AmapNaviType.DRIVER), WalkRouteCalculateActivity.this);
//        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(new Poi("北京站", p3, ""), null, new Poi("故宫博物院", p5, ""), AmapNaviType.DRIVER), WalkRouteCalculateActivity.this);
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
//        mAMapNavi.calculateWalkRoute(new NaviLatLng(39.925846, 116.435765), new NaviLatLng(mEndPoint.getLatitude(), mEndPoint.getLongitude()));
        mAMapNavi.calculateWalkRoute(new NaviLatLng(39.925846, 116.435765), new NaviLatLng(39.925846, 116.532765));
    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        super.onCalculateRouteSuccess(ids);
        mAMapNavi.startNavi(NaviType.GPS);
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }


    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onGetNavigationText(String s) {
//        amapTTSController.onGetNavigationText(s);
    }

    @Override
    public void onStopSpeaking() {
//        amapTTSController.stopSpeaking();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        amapTTSController.destroy();
    }
}
