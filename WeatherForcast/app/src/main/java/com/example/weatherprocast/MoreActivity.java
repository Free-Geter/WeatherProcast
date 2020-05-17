package com.example.weatherprocast;


import com.example.weatherprocast.DataBase.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener {

    TextView bgTv,cacheTv,versionTv,shareTv;
    RadioGroup exbgRg;
    ImageView backIv;

    // 存储需要更换的壁纸信息，下一次启动MainActivity时更换壁纸
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        bgTv = findViewById(R.id.more_tv_exchange_bg);
        cacheTv = findViewById(R.id.more_tv_clear_cache);
        versionTv = findViewById(R.id.more_tv_current_version);
        shareTv = findViewById(R.id.more_tv_share);

        backIv = findViewById(R.id.more_iv_back);
        exbgRg = findViewById(R.id.more_rg);

        bgTv.setOnClickListener(this);
        cacheTv.setOnClickListener(this);
        shareTv.setOnClickListener(this);
        backIv.setOnClickListener(this);

        pref = getSharedPreferences("bg_pref", MODE_PRIVATE);

        // 获取并展示版本信息
        String versionName = getVersionName();
        versionTv.setText("当前版本：v." + versionName);

        // 修改壁纸监听器
        exbgRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 获取当前壁纸信息: 第一张壁纸对应0，第二张1，第三章2
                // 成功修改后，直接跳转到MainActivity，清空backStack
                int bg = pref.getInt("bg", 0);
                SharedPreferences.Editor editor = pref.edit();
                Intent intent = new Intent(MoreActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                switch (checkedId){
                    case R.id.more_rb_1:
                        if (bg == 0){
                            Toast.makeText(MoreActivity.this,"此壁纸正在应用中...",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg",0).commit();
                        break;
                    case R.id.more_rb_2:
                        if (bg == 1){
                            Toast.makeText(MoreActivity.this,"此壁纸正在应用中...",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg",1).commit();
                        break;
                    case R.id.more_rb_3:
                        if (bg == 2){
                            Toast.makeText(MoreActivity.this,"此壁纸正在应用中...",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putInt("bg",2).commit();
                        break;
                }
                startActivity(intent);
            }
        });

    }


    // 获取应用版本名称
    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        String vName = null;
        try {
            PackageInfo info = packageManager.getPackageInfo(getPackageName(), 0);
            vName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return vName;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more_iv_back:
                finish();
                break;
            case R.id.more_tv_clear_cache:
                clearCache();
                break;
            case R.id.more_tv_share:
                shareSoftwareMsg("天气预报app，下载地址：https://github.com/Free-Geter/WeatherProcast.git");
                break;
            case R.id.more_tv_exchange_bg:
                if (exbgRg.getVisibility() == View.VISIBLE) {
                    exbgRg.setVisibility(View.GONE);
                }else{
                    exbgRg.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    // 分享软件
    private void shareSoftwareMsg(String s) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,s);
        startActivity(Intent.createChooser(intent,"天气"));
    }


    //清楚缓存方法
    private void clearCache() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("确定要删除所有缓存？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbManager.deleteAllInfo();
                        Toast.makeText(MoreActivity.this,"已清除所有缓存数据",Toast.LENGTH_SHORT).show();

                        // 返回到MainActivity并重置backStack
                        Intent intent = new Intent(MoreActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消",null)
                .create().show();
    }
}
