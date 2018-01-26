//package com.example.shizhuan.banche;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AbsListView;
//import android.widget.BaseAdapter;
//
//import com.example.shizhuan.banche.entity.NewsBean;
//
//import java.util.List;
//
///**
// * Created by ShiZhuan on 2018/1/25.
// */
//
//public class NewsAdapter extends BaseAdapter implements AbsListView.OnScrollListener,
//        View.OnTouchListener {
//    private static final String TAG = "zxt/NewsAdapter";
//    private volatile List<NewsBean> mDatas;
//    private LayoutInflater mInflater;
//    private RefreshListView mListView;
//    private Context mContext;
//    // 网络图片加载
//
//
//    public List<NewsBean> getDatas() {
//        return mDatas;
//    }
//
//    public void setDatas(List<NewsBean> mDatas) {
//        this.mDatas = mDatas;
//    }
//
//    public void setData(int pos, NewsBean bean) {
//        if (-1 != pos) {
//            mDatas.add(pos, bean);
//            //MainActivity.urls.add(pos, bean.NewsPicResUrl);
//        } else {
//            mDatas.add(bean);
//            //MainActivity.urls.add(bean.NewsPicResUrl);
//        }
//    }
//
//
//    private ImageLoader mImageLoader;
//
//
//    public NewsAdapter(Context context, List<NewsBean> mDatas, RefreshListView listView) {
//        super();
//        mContext = context;
//        this.mDatas = mDatas;
//        this.mInflater = LayoutInflater.from(context);
//        //抽象成单例后改写 begin
//        //mImageLoader = new ImageLoader(listView);
//        mImageLoader = ImageLoader.getInstance(context);
//        mImageLoader.setmListView(listView);
//        //抽象成单例后改写 end
//        mListView = listView;
//        initHeaderView();
//        initFooterView();
//    }
//
//    private void initFooterView() {
//        mFooterView = mListView.getFooterView();
//    }
//
//    //下拉刷新数据
//    public void onDateChange(List<NewsBean> mDatas) {
//        this.mDatas = mDatas;
//        this.notifyDataSetChanged();
//    }
//    //下拉刷新数据
//
//    private void initHeaderView() {
//        headerView = mListView.findViewById(R.id.headerview);
//        // 获取View高度
//        headerHeight = mListView.getHeaderHeight();
//        mTipView = (TextView) headerView.findViewById(R.id.tip);
//        mTimeView = (TextView) headerView.findViewById(R.id.lastupdate_time);
//        mArrowView = (ImageView) headerView.findViewById(R.id.arrow);
//        mBar = (ProgressBar) headerView.findViewById(R.id.progress);
//    }
//
//    @Override
//    public int getCount() {
//        if (mDatas != null)
//            return mDatas.size();
//        return 0;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        // TODO Auto-generated method stub
//        return mDatas.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        // TODO Auto-generated method stub
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
//        if (convertView == null) {
//            //Log.i(TAG, "getView->缓存为空:" + position);
//            holder = new ViewHolder();
//            convertView = mInflater.inflate(R.layout.item, parent, false);
//            holder.pic = (ImageView) convertView.findViewById(R.id.iv_pic);
//
//            holder.no = (TextView) convertView.findViewById(R.id.tv_no);
//            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
//            holder.description = (TextView) convertView
//                    .findViewById(R.id.tv_description);
//            convertView.setTag(holder);
//        } else {
//            //Log.i(TAG, "getView->缓存过:" + position);
//            holder = (ViewHolder) convertView.getTag();
//        }
//        NewsBean bean = mDatas.get(position);
//
//        // 加载网络图片
//        holder.pic.setTag(bean.NewsPicResUrl);
//        // mImageLoader.getImageByAsyncTask(holder.pic, bean.NewsPicResUrl);
//
//        holder.pic.setImageResource(R.drawable.ic_launcher);
//        // 滑动优化 如果有缓存，则加载 否则不加载
//        final Bitmap pBitmap = mImageLoader.setDefaultBitmap(holder.pic, bean.NewsPicResUrl);
//        // /滑动优化
//
//        holder.name.setText(bean.NewsName);
//        holder.description.setText(bean.NewsDescription);
//        holder.no.setText(bean.NewsNo);
//
//        //添加事件点击 2015 12 24
//        final String urlBig = bean.NewsPicBigResUrl;
//        final String urlSmall = bean.NewsPicResUrl;
//        holder.pic.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "bean.NewsNo:" + urlBig, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mContext, BigImageActivity.class);
//                intent.putExtra("urlBig", urlBig);
//                intent.putExtra("urlSmall", urlSmall);
//                mContext.startActivity(intent);
//            }
//        });
//        //
//        return convertView;
//    }
//
//    class ViewHolder {
//        public ImageView pic;
//        public TextView name;
//        public TextView description;
//        public TextView no;
//    }
//
//    // 滚动时的优化 begin
//    private int mStart, mEnd;
//
//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        if (scrollState == SCROLL_STATE_IDLE) {
//            Log.i(TAG, "scrollState:" + mStart + "-" + mEnd);
//            loadImages();
//        } else {
//            Log.i(TAG, "scrollState cancel:");
//            mImageLoader.cancelAllTasks();
//        }
//    }
//
//    private void loadImages() {
//        if (null != mDatas && mDatas.size() > 0) {
//            //修复bug 滑动时边缘不加载 begin
//            int start = mStart != 0 ? mStart - 1 : 0;
//            int end = mEnd != mDatas.size() ? mEnd + 1 : mEnd;
//            //修复bug 滑动时边缘不加载 end
//            mImageLoader.loadImages(start, end);
//        }
//    }
//
//    private boolean isInit = true;
//
//    //加载更多 begin
//    private boolean isLoading = false;
//    private View mFooterView;
//
//    public interface IOnScrollBottomListener {
//        void onScrollBottom();
//    }
//
//    private IOnScrollBottomListener mIOnScrollBottomListener;
//
//    public void setIOnScrollBottomListener(IOnScrollBottomListener onScrollBottomListener) {
//        mIOnScrollBottomListener = onScrollBottomListener;
//    }
//
//    public void bottomDataLoadComplete() {
//        //隐藏view 加载数据 改变flag
//        mFooterView.setVisibility(View.GONE);
//        isLoading = false;
//
//    }
//
//    //加载更多 end
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem,
//                         int visibleItemCount, int totalItemCount) {
//        Log.i(TAG, "onScroll():totalItemCount" + totalItemCount + "  ,isLoading:" + isLoading + ",   firstVisibleItem:"
//                + firstVisibleItem + "  ,visibleItemCount:" + visibleItemCount + ", totalItemCount:" + totalItemCount);
//        mStart = firstVisibleItem;
//        //其实现在的listview看不到那么多 应该-1 否则最后会空指针 2015 12 24 (因为我们add了一个headerview进去)  20151228 add 了 footview 所以再-1
//        mEnd = mStart + visibleItemCount - 1 - 1;
//
//        if (visibleItemCount != 0 && isInit) {
//            isInit = false;
//            loadImages();
//        }
//
//        //处理滑动加载更多: 2 != totalItemCount 防止没有数据一直在加载更多
//        if (2 != totalItemCount && firstVisibleItem + visibleItemCount == totalItemCount && !isLoading) {
//            isLoading = true;
//            mFooterView.setVisibility(View.VISIBLE);
//            //执行加载更多操作
//            if (null != mIOnScrollBottomListener) {
//                mIOnScrollBottomListener.onScrollBottom();
//            }
//        }
//
//    }
//
//    // 滚动时的优化 end
//
//    // 处理下拉刷新
//    private int lastY;
//    private View headerView;
//    private TextView mTipView;
//    private TextView mTimeView;
//    private ImageView mArrowView;
//    private ProgressBar mBar;
//
//    private int headerHeight;
//    private int gap;
//
//    public interface IRefereshListener {
//        public void onRefresh();
//    }
//
//    private IRefereshListener mRefereshListener;
//
//    public void setIRefereshListener(IRefereshListener listener) {
//        this.mRefereshListener = listener;
//    }
//
//    //监听执行完毕 继续滚动
//    public void refreshComplete(int length) {
//        Log.i(TAG, "refreshComplete()");
//        //其实这里的mStarat =0的
//        mImageLoader.loadImages(mStart, mStart + length);
//        rollToInit(1);
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        if (mStart == 0 && isMoveEnable) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    lastY = (int) event.getY();
//                    //初始化 几个view的状态显示
//                    mTipView.setText("下拉可以刷新");
//                    mArrowView.setVisibility(View.VISIBLE);
//                    mBar.setVisibility(View.GONE);
//                    //初始化 几个view的状态显示
//
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    gap = (int) (event.getY() - lastY);
//                    Log.i(TAG, " ACTION_MOVE gap:" + gap);
//                    //移动View
//                    setPaddingTop(-headerHeight + gap);
//                    //下拉可以刷新（超过宽度）-》松开可以刷新-》
//                    if (gap > headerHeight) {
//                        mTipView.setText("松开可以刷新");
//                    }
//                    //下拉可以刷新（超过宽度）-》松开可以刷新-》
//                    break;
//                case MotionEvent.ACTION_UP:
//                    //松开-》滑动到View宽度-》
//                    if (gap > headerHeight) {
//                        rollToInit(0);
//                        //正在刷新-》
//                        mTipView.setText("正在刷新");
//                        mArrowView.setVisibility(View.GONE);
//                        mBar.setVisibility(View.VISIBLE);
//
//                        //执行刷新操作
//                        if (mRefereshListener != null) {
//                            mRefereshListener.onRefresh();
//                        }
//                        //执行刷新操作
//                        //松开-》滑动到View宽度-》
//                    } else {
//                        //恢复初始。
//                        rollToInit(1);
//                    }
//
//                    break;
//
//                default:
//                    break;
//            }
//            Log.i(TAG, "lastY:" + lastY + "headerHeight:" + headerHeight);
//        }
//        return false;
//    }
//
//    private boolean isMoveEnable = true;
//
//    private void rollToInit(int endTop) {
//        //回去的动画：//使用ObjectAnimator 不好实现， 打算用ValueAnimator
//        /*ObjectAnimator animator = ObjectAnimator.ofFloat(mListView, "Y",-(headerView.getPaddingTop()+headerHeight));
//      animator.setDuration(500).start();*/
//        isMoveEnable = false;
//
//        int top = headerView.getPaddingTop();
//        //解决 拖动不超过headerheight 不会滑动回去的bug
//        int end = top > 0 ? -(top + headerHeight) : -(top + headerHeight + headerHeight);
//
//        Log.i(TAG, "rollToInit:" + endTop + "  ,top:" + top + ", -(top+headerHeight)  " + (-(top + headerHeight)));
//
//        ValueAnimator animator = ValueAnimator.ofInt(top, endTop == 1 ? end : endTop);
//        animator.addUpdateListener(new AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                Integer paddingTop = (Integer) animation.getAnimatedValue();
//                setPaddingTop(paddingTop);
//            }
//        });
//
//        //添加动画完成监听器
//        animator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                isMoveEnable = true;
//            }
//        });
//        //添加动画完成监听器
//        animator.setDuration(500).start();
//    }
//
//    private void setPaddingTop(int top) {
//        if (null != headerView) {
//            headerView.setPadding(headerView.getPaddingLeft(), top, headerView.getPaddingRight(), headerView.getPaddingBottom());
//        }
//    }
//
//}
