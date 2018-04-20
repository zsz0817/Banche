package com.example.shizhuan.banche.Setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shizhuan.banche.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


/**
 * Created by ShiZhuan on 2018/1/17.
 */

public class MessageActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private List<Map<String,Object>> listdata = new ArrayList<>();
    private ImageView back_btn;
    private ListView messagelist;
    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_layout);
        init();
        listdata = getData();
        MessageAdapter messageAdapter = new MessageAdapter(this);
        messagelist.setAdapter(messageAdapter);
    }

    private void init(){
        back_btn = (ImageView)findViewById(R.id.back_btn);
        messagelist = (ListView)findViewById(R.id.message_list);
        back_btn.setOnClickListener(this);
        messagelist.setOnItemClickListener(this);
//        messagelist.setOnScrollListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                intent = new Intent(MessageActivity.this,SettingActivity.class);
                startActivity(intent);
            break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //通过view获取其内部的组件，进而进行操作
        String text = (String) ((TextView)view.findViewById(R.id.title)).getText();
        //大多数情况下，position和id相同，并且都从0开始
        String showText = "点击第" + position + "项，文本内容为：" + text + "，ID为：" + id;
        Toast.makeText(this, showText, Toast.LENGTH_LONG).show();

        intent = new Intent(MessageActivity.this,Detail_mess.class);
        startActivity(intent);

    }
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//    }
//
//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//    }

    private List<Map<String,Object>> getData(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "班车动态");
        map.put("date", "2018-01-01");
        listdata.add(map);
        return listdata;
    }

    class MessageAdapter extends BaseAdapter {

//        private int mScrollState = SCROLL_STATE_IDLE;
        private List<Runnable> mPendingNotify = new ArrayList<Runnable>();

        private LayoutInflater mLayoutInflater;

        public MessageAdapter(Context context){
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                view = mLayoutInflater.inflate(R.layout.messitem, parent, false);
                viewHolder.mess_title = (TextView)view.findViewById(R.id.title);
                viewHolder.create_date = (TextView)view.findViewById(R.id.date);
                view.setTag(viewHolder);
            }else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            viewHolder.mess_title.setText((String)listdata.get(position).get("title"));
            viewHolder.create_date.setText((String)listdata.get(position).get("date"));

            return view;
        }

        @Override
        public int getCount() {
            return listdata.size();
        }

        @Override
        public Object getItem(int position) {
            return listdata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        class ViewHolder {
            TextView mess_title;
            TextView create_date;
        }

//
//        public void updateView(ViewHolder holder, Integer data) {
//            if (holder != null && data != null) {
//                holder.mess_title.setVisibility(View.VISIBLE);
//                holder.mess_title.setText(data + "");
//                holder.create_date.setVisibility(View.VISIBLE);
//                holder.create_date.setText(data + "");
//            }
//        }
//
//
//        public void notifyDataSetChanged(final int position) {
//            final Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    final int firstVisiablePosition = messagelist.getFirstVisiblePosition();
//                    final int lastVisiablePosition = messagelist.getLastVisiblePosition();
//                    final int relativePosition = position - firstVisiablePosition;
//                    if (position >= firstVisiablePosition && position <= lastVisiablePosition) {
//                        if (mScrollState == SCROLL_STATE_IDLE) {
//                            //当前不在滚动，立刻刷新
//                            Log.d("Snser", "notifyDataSetChanged position=" + position + " update now");
//                            updateView((ViewHolder)messagelist.getChildAt(relativePosition).getTag(), (Integer)getItem(position));
//                        } else {
//                            synchronized (mPendingNotify) {
//                                //当前正在滚动，等滚动停止再刷新
//                                Log.d("Snser", "notifyDataSetChanged position=" + position + " update pending");
//                                mPendingNotify.add(this);
//                            }
//                        }
//                    } else {
//                        //不在可视范围内的listitem不需要手动刷新，等其可见时会通过getView自动刷新
//                        Log.d("Snser", "notifyDataSetChanged position=" + position + " update skip");
//                    }
//                }
//            };
//            runnable.run();
//        }
//
//        public void onScrollStateChanged(AbsListView view, int scrollState) {
//            mScrollState = scrollState;
//            if (mScrollState == SCROLL_STATE_IDLE) {
//                //滚动已停止，把需要刷新的listitem都刷新一下
//                synchronized (mPendingNotify) {
//                    final Iterator<Runnable> iter = mPendingNotify.iterator();
//                    while (iter.hasNext()) {
//                        iter.next().run();
//                        iter.remove();
//                    }
//                }
//            }
//        }
//
//        @Override
//        public void notifyDataSetChanged() {
//            super.notifyDataSetChanged();
//        }
    }
}
