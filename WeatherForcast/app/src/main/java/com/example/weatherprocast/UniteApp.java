package com.example.weatherprocast;

import android.app.Application;

import org.xutils.x;


  // 在此Application中全局声明xutil组件


public class UniteApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
