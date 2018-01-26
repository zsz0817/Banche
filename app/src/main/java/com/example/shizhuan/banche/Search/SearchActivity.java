package com.example.shizhuan.banche.Search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shizhuan.banche.MainActivity;
import com.example.shizhuan.banche.R;
import com.example.shizhuan.banche.Setting.SettingActivity;
import com.example.shizhuan.banche.util.ToastUtils;

import java.util.ArrayList;

/**
 * Created by ShiZhuan on 2018/1/25.
 */

public class SearchActivity extends Activity implements View.OnClickListener{

    private EditText et_search_keyword;
    private ImageButton imageButton1,imageButton2,imageButton3;
    private Button btn_search;
    private TextView tv_back;
    private TextView tv_clear;
    private LinearLayout ll_content;
    private ListView lv_history_word;

    private SearchHistorysDao dao;
    private ArrayList<SearchHistorysBean> historywordsList = new ArrayList<SearchHistorysBean>();
    private SearchHistoryAdapter mAdapter;
    private int count;

    private Intent intent;


    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.search);

        imageButton1 = (ImageButton)findViewById(R.id.ImageButton1);
        imageButton2 = (ImageButton)findViewById(R.id.ImageButton2);
        imageButton3 = (ImageButton)findViewById(R.id.ImageButton3);

        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);

        dao = new SearchHistorysDao(this);
        historywordsList = dao.findAll();

        et_search_keyword = (EditText) findViewById(R.id.et_search_keyword);

        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);

        tv_clear = (TextView) findViewById(R.id.tv_clear);
        tv_clear.setOnClickListener(this);

        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        lv_history_word = (ListView) findViewById(R.id.lv_history_word);
        mAdapter = new SearchHistoryAdapter(historywordsList);
        count = mAdapter.getCount();
        if (count > 0) {

            tv_clear.setVisibility(View.VISIBLE);
        } else {
            tv_clear.setVisibility(View.GONE);
        }
        lv_history_word.setAdapter(mAdapter);
        lv_history_word.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SearchHistorysBean bean = (SearchHistorysBean) mAdapter.getItem(position);

                et_search_keyword.setText(bean.historyword);

                mAdapter.notifyDataSetChanged();
                btn_search.performClick();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:// 点击搜索，先拿关键词
                String searchWord = et_search_keyword.getText().toString().trim();
                if (TextUtils.isEmpty(searchWord)) {
                    ToastUtils.show(SearchActivity.this, "搜索词不能为空！");
                } else {
                    // 存储数据
                    dao.addOrUpdate(searchWord);
                    historywordsList.clear();
                    historywordsList.addAll(dao.findAll());
                    mAdapter.notifyDataSetChanged();
                    tv_clear.setVisibility(View.VISIBLE);


                    //跳转页面：
                    Intent intent = new Intent(this, SearchResult.class);
                    //在Intent对象当中添加一个键值对
                    intent.putExtra("ID",searchWord);
                    startActivity(intent);
                }

                break;
            case R.id.tv_clear:
                // 点击清除历史的时候：清除数据库中所有的数据
                dao.deleteAll();
                historywordsList.clear();
                mAdapter.notifyDataSetChanged();
                tv_clear.setVisibility(View.GONE);
                break;
            case R.id.ImageButton1:
                intent = new Intent(SearchActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.ImageButton2:
//                intent = new Intent(SearchActivity.this,SearchActivity.class);
//                startActivity(intent);
                break;
            case R.id.ImageButton3:
                intent = new Intent(SearchActivity.this,SettingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    class SearchHistoryAdapter extends BaseAdapter {

        private ArrayList<SearchHistorysBean> historywordsList;

        public SearchHistoryAdapter(ArrayList<SearchHistorysBean> historywordsList) {
            this.historywordsList = historywordsList;
        }

        @Override
        public int getCount() {

            return historywordsList == null ? 0 : historywordsList.size();
        }

        @Override
        public Object getItem(int position) {

            return historywordsList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_search_history_word, null);
                holder.tv_word = (TextView) convertView.findViewById(R.id.tv_search_record);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_word.setText(historywordsList.get(position).toString());

            return convertView;
        }

    }

    class ViewHolder {
        TextView tv_word;
    }
}
