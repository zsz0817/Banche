package com.example.shizhuan.banche.Search;

import java.util.ArrayList;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.shizhuan.banche.DBSearchHelper;

public class SearchHistorysDao {
	
	private DBSearchHelper helper;

	private SQLiteDatabase db;
	
	public SearchHistorysDao(Context context) {
		helper = new DBSearchHelper(context);
	}

	
	/**
	 * �����ݿ�������ӻ���update�Ĳ���
	 *
	 */

	public void addOrUpdate(String word){
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "select _id from t_historywords where historyword = ? ";
		Cursor cursor = db.rawQuery(sql, new String[]{word});
		if(cursor.getCount()>0){
			//˵�����ݿ����Ѿ������ݣ��������ݿ��ʱ�䣺
			String sql_update = "update t_historywords set updatetime = ? where historyword = ? ";
			db.execSQL(sql_update, new String[]{System.currentTimeMillis()+"",word});
		}else{
			//ֱ�Ӳ���һ����¼��
			String sql_add = "insert into t_historywords(historyword,updatetime) values (?,?);";
			db.execSQL(sql_add, new String[]{word,System.currentTimeMillis()+""});
		}

		cursor.close();
		db.close();
	}

	/**
	 * ��ѯ���ݿ������е�����
	 *
	 */

	public ArrayList<SearchHistorysBean> findAll(){
		ArrayList<SearchHistorysBean> data = new ArrayList<SearchHistorysBean>();;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("t_historywords", null, null, null, null, null, "updatetime desc");
		//�����α꣬�����ݴ洢��
		while(cursor.moveToNext()){
			SearchHistorysBean searchDBData = new SearchHistorysBean();
			searchDBData._id =cursor.getInt(cursor.getColumnIndex("_id"));
			searchDBData.historyword = cursor.getString(cursor.getColumnIndex("historyword"));
			searchDBData.updatetime = cursor.getLong(cursor.getColumnIndex("updatetime"));
			data.add(searchDBData);
		}
		cursor.close();
		db.close();
		return data;
	}


	/**
	 *
	 * ɾ�����ݿ��е���������
	 */

	public void deleteAll(){
		SQLiteDatabase db = helper.getReadableDatabase();
		db.execSQL("delete from t_historywords");
		db.close();
	}
}
