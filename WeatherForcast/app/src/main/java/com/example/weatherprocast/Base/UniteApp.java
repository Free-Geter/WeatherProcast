package com.example.weatherprocast.Base;

import android.app.Application;
import android.util.Log;

import com.example.weatherprocast.DataBase.*;

import org.xutils.x;

public class UniteApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("message", "onCreate: UniteApp");
        // 在此Application中全局声明xutil组件
        x.Ext.init(this);

        dbManager.initDB(this);
    }
}
