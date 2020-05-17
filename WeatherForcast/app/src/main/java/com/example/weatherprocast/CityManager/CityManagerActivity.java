package com.example.weatherprocast.CityManager;

import com.example.weatherprocast.DataBase.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.weatherprocast.DataBase.DatabaseBean;
import com.example.weatherprocast.R;

import java.util.ArrayList;
import java.util.List;

public class CityManagerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView addIv,backIv,editIv;
    ListView cityLv;

    // 列表的数据源组织形式
    List<DatabaseBean> mDatas;

    CityManagerAdapter cityManagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        addIv = findViewById(R.id.city_iv_add);
        backIv = findViewById(R.id.city_iv_back);
        editIv = findViewById(R.id.city_iv_edit);

        cityLv = findViewById(R.id.city_lv);
        mDatas = new ArrayList<>();
        //mDatas = dbManager.queryAllInfo();

        // 添加点击事件
        addIv.setOnClickListener(this);
        editIv.setOnClickListener(this);
        backIv.setOnClickListener(this);

        // 设置ListView适配器
        cityManagerAdapter = new CityManagerAdapter(this,mDatas);
        cityLv.setAdapter(cityManagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在删除个别城市结点后，更新mDatas数组，提示适配器更新
        // 获取所有城市天气信息
        List<DatabaseBean> beanList = dbManager.queryAllInfo();
        mDatas.clear();
        mDatas.addAll(beanList);
        cityManagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.city_iv_add:
                int cityCount = dbManager.getCityCount();
                if(cityCount < 5){
                    intent.setClass(this,SearchCityActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(this,"存储城市数量已达上限，请删除后再增加",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.city_iv_edit:
                intent.setClass(this,DeleteCityActivity.class);
                startActivity(intent);
                break;
            case R.id.city_iv_back:
                finish();
                break;
        }
    }
}
