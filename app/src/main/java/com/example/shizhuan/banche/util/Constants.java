package com.example.shizhuan.banche.util;

/**
 * Created by ShiZhuan on 2018/4/18.
 */

public class Constants {

    //BC0001-应用模式选择
    public static String url_mode="http://111.230.148.118:8080/BocbusServer/Mode.do?param={'head':{'TRACDE':'BC00001','TRADAT':'11111','TRATIM':'11111','USRNAM':'ling123'},'body':{'line':'2','mode':'P'}}";

    //BC0002-获取位置
    public static String url_getLocation="http://111.230.148.118:8080/BocbusServer/QueryLocation.do?param=";

    //BC0004-查看路线
    public static String url_queryline="http://111.230.148.118:8080/BocbusServer/BusLine.do?param={'head':{'TRACDE':'BC00004','TRADAT':'11111','TRATIM':'11111','USRNAM':'ling123'},'body':{'line':'2'}}";


}
