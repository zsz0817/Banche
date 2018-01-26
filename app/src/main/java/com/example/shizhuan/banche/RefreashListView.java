//package com.example.shizhuan.banche;
//
///**
// * Created by ShiZhuan on 2018/1/25.
// */
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
//public class RefreshListView extends android.widget.ListView {
//    private final static String TAG ="zxt/refreshListView";
//    private View headerView ;
//    private View footerView;
//
//    public View getFooterView() {
//        return footerView;
//    }
//
//   /*public void bottomDataLoadComplete(){
//      //隐藏view 加载数据 改变flag
//      footerView.setVisibility(View.GONE);
//   }*/
//
//    private int headerHeight;
//
//    public int getHeaderHeight() {
//        return headerHeight;
//    }
//
//    public RefreshListView(Context context) {
//        this(context, null);
//    }
//
//    public RefreshListView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initView(context);
//
//    }
//
//    private void initView(Context context) {
//        headerView = LayoutInflater.from(context).inflate(R.layout.header,null);
//        this.addHeaderView(headerView);
//        // 测量View
//        headerView.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        // 获取View高度
//        headerHeight = headerView.getMeasuredHeight();
//        Log.d(TAG, "headerHeight:"+headerHeight);
//        //设置默认隐藏
//        setPaddingTop(-headerHeight);
//
//        //上啦刷新
//        View footerView = LayoutInflater.from(context).inflate(R.layout.footer_layout,this,false);
//        this.addFooterView(footerView);
//        this.footerView = footerView.findViewById(R.id.load_layout);
//        this.footerView.setVisibility(View.GONE);
//    }
//
//
//    private void setPaddingTop(int top){
//        if(null!=headerView){
//            headerView.setPadding(headerView.getPaddingLeft(), top, headerView.getPaddingRight(), headerView.getPaddingBottom());
//        }
//    }
//
//}
