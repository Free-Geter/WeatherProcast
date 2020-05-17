package com.example.weatherprocast.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public  class dbManager {

    public static SQLiteDatabase database;

    // 初始化数据库信息
    public static void initDB(Context context){
        DBHelper dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        Log.d("city message", "initDB: init");
    }


    // 查找数据库中城市名称列表
    public static List<String> queryAllCityName(){
        // columns 参数取null，表示选取所有列
        Cursor cursor = database.query(dbContract.ContactEntry.TABLE_NAME, null, null, null, null, null, null);
        List<String> cityList = new ArrayList<>();
        while (cursor.moveToNext()){
            String city = cursor.getString(cursor.getColumnIndex(dbContract.ContactEntry.NAME));
            cityList.add(city);
        }
        return cityList;
    }

    // 根据城市名称，替换信息数据库中的内容
    public static int updateInfoByCity (String city,String content){
        Log.d("debugmessgae", "updateInfoByCity: ");
        ContentValues values = new ContentValues();
        values.put("content",content);
        int i = database.update(dbContract.ContactEntry.TABLE_NAME, values, dbContract.ContactEntry.NAME + "=?", new String[]{city});
        return i;
    }

    // 根据城市名称，添加城市信息
    public static long addCityInfo (String city,String content){
        ContentValues values = new ContentValues();
        values.put(dbContract.ContactEntry.NAME,city);
        values.put(dbContract.ContactEntry.CONTENT,content);
        return database.insert(dbContract.ContactEntry.TABLE_NAME,null,values);
    }

    // 根据城市名称，查询数据库中的信息缓存
    public static String queryInfoByCity(String city){
        Cursor cursor = database.query(dbContract.ContactEntry.TABLE_NAME,null, dbContract.ContactEntry.NAME + "=?",new String[] {city},null,null,null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            String content = cursor.getString(cursor.getColumnIndex(dbContract.ContactEntry.CONTENT));
            return content;
        }
        return null;
    }

    // 查询存储城市的个数
    public static int getCityCount(){
        final Cursor cursor = database.query(dbContract.ContactEntry.TABLE_NAME, null, null, null, null, null, null);
        int i = cursor.getCount();
        Log.d("messagedebug", "getCityCount: " + i);
        return i;
    }

    // 查询数据库中的全部信息
    public static List<DatabaseBean> queryAllInfo(){
        Cursor cursor = database.query(dbContract.ContactEntry.TABLE_NAME,null,null,null,null,null,null);
        List<DatabaseBean> infoList = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(dbContract.ContactEntry.CONTACT_ID));
            String city = cursor.getString(cursor.getColumnIndex(dbContract.ContactEntry.NAME));
            String content = cursor.getString(cursor.getColumnIndex(dbContract.ContactEntry.CONTENT));
            DatabaseBean data = new DatabaseBean(id,city,content);
            infoList.add(data);
        }
        return infoList;
    }

    // 根据城市名称，删除该城市的数据缓存
    public static int deleteInfo(String city){
        return database.delete(dbContract.ContactEntry.TABLE_NAME, dbContract.ContactEntry.NAME + "=?",new String[] {city});
    }

    // 删除表中所有信息
    public static void deleteAllInfo(){
        // 调用数据库操作语句，清空表中的所有数据，但是表任然存在
        String sql = "delete from " + dbContract.ContactEntry.TABLE_NAME;
        database.execSQL(sql);
    }
}
