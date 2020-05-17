package com.example.weatherprocast;

import com.example.weatherprocast.CityManager.CityManagerActivity;
import com.example.weatherprocast.DataBase.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView addCityIv,moreIv;
    private LinearLayout pointLayout;
    private ViewPager mainVp;

    // ViewPager显示的内容
    private List<Fragment> fragmentList;

    //用户所选中的城市的集合，TODO: 添加到数据库中
    private List<String> CityList;

    //表示ViewPager的个数，“点指示器”显示的内容
    List<ImageView> imgList;
    private CityFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addCityIv = findViewById(R.id.main_iv_add);
        moreIv = findViewById(R.id.main_iv_more);
        pointLayout = findViewById(R.id.main_layout_point);
        mainVp = findViewById(R.id.main_vp);

        addCityIv.setOnClickListener(this);
        moreIv.setOnClickListener(this);

        fragmentList = new ArrayList<>();

        // 从数据库获取所有城市名称的列表
        CityList = dbManager.queryAllCityName();

        imgList = new ArrayList<>();

        // 如果数据库中一个城市都没有，添加一个default城市
        if(CityList.size() == 0){
            CityList.add("汉川");
        }

        // 如果从搜索界面跳转到此界面，会传来一个搜索的城市名称，在此处获取
        Intent intent =getIntent();
        String city = intent.getStringExtra("city");
        // 注意，此时如果程序刚开始启动，会传来一个空的city，导致程序崩溃
        if( !CityList.contains(city) && !TextUtils.isEmpty(city)){
            CityList.add(city);
        }

        // 初始化ViewPager
        initPager();
        adapter = new CityFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        mainVp.setAdapter(adapter);

        // 创建“点指示器”
        initPoint();
        // 程序默认开始时，第一个显示List中第一个城市的信息
        mainVp.setCurrentItem(0);
    }

    private void initPoint() {
        // 为每一个城市都设置一个“点指示器”，初始化为“未选中”
        for  (int i=0; i < fragmentList.size() ; i++ ){
            ImageView pIv = new ImageView(this);
            pIv.setImageResource(R.mipmap.a1);
            pIv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) pIv.getLayoutParams();
            lp.setMargins(0,0,20,0);
            imgList.add(pIv);
            pointLayout.addView(pIv);
        }
        // 将第一个城市的“点指示器”设置为“选中”
        imgList.get(0).setImageResource(R.mipmap.a2);

        // 设置ViewPager监听器
        setPagerListener();
    }

    private void setPagerListener() {
        /*设置监听事件*/
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 遍历所有“点指示器”，只将被选中的Fragment对应的指示器设置为“选中”
                for(int i=0; i<fragmentList.size() ; i++){
                    if( i == position)
                        imgList.get(i).setImageResource(R.mipmap.a2);
                    else
                        imgList.get(i).setImageResource(R.mipmap.a1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initPager() {
        //将城市的名字传给CityWeatherFragment创建Fragment对象，并添加到ViewPager数据源中
        for (int i = 0;i < CityList.size();i++){
            CityWeatherFragment cwFragemnt = new CityWeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city",CityList.get(i));
            cwFragemnt.setArguments(bundle);
            fragmentList.add(cwFragemnt);
            Log.d("message", "initPager: step" + CityList.get(i));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.main_iv_add:
                intent.setClass(this, CityManagerActivity.class);
                break;
            case R.id.main_iv_more:
                break;
        }
        startActivity(intent);
    }
}
