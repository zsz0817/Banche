package com.example.shizhuan.banche.Http;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ShiZhuan on 2018/4/13.
 */

public class JsonUtils {


    public JsonUtils(){
            // TODO Auto-generated constructor stub
    }

    public List<Map<String,Object>> JsonToList(JSONObject jsonObject){
        List<Map<String,Object>> jsonlist = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("body");
            for (int i=0;i<jsonArray.length();i++){
                Map<String, Object> map = new HashMap<>();
                jsonlist.add(map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonlist;
    }

    public JSONObject StringToJson(){
        JSONObject jsonObject = new JSONObject();

        return jsonObject;
    }
}
