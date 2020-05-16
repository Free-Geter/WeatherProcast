package com.example.weatherprocast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
        CityList = new ArrayList<>();
        imgList = new ArrayList<>();

        if(CityList.size() == 0){
            CityList.add("汉川");
            CityList.add("上海");
            CityList.add("北京");
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
        switch (v.getId()){
            case R.id.main_iv_add:
                break;
            case R.id.main_iv_more:
                break;
        }
    }
}
