package com.example.weatherprocast;

import com.example.weatherprocast.Base.BaseFragment;
import com.example.weatherprocast.DataBase.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weatherprocast.bean.WeatherBean;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityWeatherFragment extends BaseFragment implements View.OnClickListener {

    //  让此Fragment继承BaseFragment类，在Base Fragment类中封装着xutil组件获取网络数据的方法，继承以后可以更加方便地调用这些方法

    TextView tempTv,cityTv,conditionTv,windTv,tempRangeTv,dateTv,PM25Tv,clothIndexTv,carIndexTv,clodIndexTv,sportIndexTv,raysIndexTv;
    ImageView dayIv;
    LinearLayout futureLayout;


    String urlp1 = "http://api.map.baidu.com/telematics/v3/weather?location=";
    String urlp2 = "&output=json&ak=FkPhtMBK0HTIQNh7gG4cNUttSTyr0nzo";
    String city;
    private List<WeatherBean.ResultsBean.IndexBean> indexList;


    public CityWeatherFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);
        InitView (view);

        //从Activity获取城市信息，完善url
        Bundle bundle = getArguments();
        city = bundle.getString("city");
        String url = urlp1 + city + urlp2 ;
        Log.d("message", url);

        //利用完善的url调用父类的xutil组件方法，获取网络数据
        loadData(url);
        return view;
    }

    //Override Base Fragment中的回调函数，通过这写回调函数对本类中的成员进行数据修改和其他操作。
    @Override
    public void onSuccess(String result) {
        // 解析并展示获取的数据
        parseShowData(result);

        // 对当前显示的所有城市进行检测，如果该城市在数据库中没有缓存，则在数据库中备份数据
        String i = dbManager.queryInfoByCity(city);
            if (i == null){
            Log.d(" city message", "onSuccess: add to database");
            //更新数据库失败，说明数据库中不含该城市的相关信息
            dbManager.addCityInfo(city,result);
        }
    }

    private void parseShowData(String result) {
        // step1：使用Gson解析数据,按照自定义的WeatherBean类中所定义的格式进行解析
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class);
        // step2：将数据解析后，获取Result中的信息
        WeatherBean.ResultsBean resultsBean = weatherBean.getResults().get(0);
        indexList = resultsBean.getIndex();
        // step3:将获取的数据赋值
        dateTv.setText(weatherBean.getDate());
        cityTv.setText(resultsBean.getCurrentCity());
        PM25Tv.setText("PM25: " + resultsBean.getPm25());

        //获取当天天气信息集合
        WeatherBean.ResultsBean.WeatherDataBean todayData = resultsBean.getWeather_data().get(0);
        windTv.setText(todayData.getWind());
        tempRangeTv.setText("温度区间: " + todayData.getTemperature());
        conditionTv.setText(todayData.getWeather());

        //分割出实时气温
        String[] split = todayData.getDate().split("：");
        String today_current_temp = split[1].replace(")", "");
        tempTv.setText(today_current_temp);
        Picasso.with(getActivity()).load(todayData.getDayPictureUrl()).into(dayIv);

        //加载未来三天的天气
        List<WeatherBean.ResultsBean.WeatherDataBean> futureDataList = resultsBean.getWeather_data();
        futureDataList.remove(0);

        //每次循环从xml布局生成一个view对象，并用未来的天气为view对象中的控件进行赋值
        for (int i=0; i < futureDataList.size(); i++){
            Log.d("message", "" + i + "," + futureDataList.size());
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_main_center,null);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            futureLayout.addView(itemView);
            TextView item_date,item_condition,item_wind,item_temp;
            ImageView item_iv;
            item_date= itemView.findViewById(R.id.item_center_tv_date);
            item_condition= itemView.findViewById(R.id.item_center_tv_con);
            item_wind= itemView.findViewById(R.id.item_center_tv_wind);
            item_iv= itemView.findViewById(R.id.item_center_iv);
            item_temp = itemView.findViewById(R.id.item_center_tv_temp);

            WeatherBean.ResultsBean.WeatherDataBean iDataBean = futureDataList.get(i);
            item_date.setText(iDataBean.getDate());
            item_condition.setText(iDataBean.getWeather());
            item_wind.setText(iDataBean.getWind());
            item_temp.setText(iDataBean.getTemperature());
            Picasso.with(getActivity()).load(iDataBean.getDayPictureUrl()).into(item_iv);
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        Log.d("message", "onError: 1");

        // 网络数据获取失败，使用数据库中的天气数据缓存
        String s = dbManager.queryInfoByCity(city);
        if (!TextUtils.isEmpty(s)){
            parseShowData(s);
        }
    }

    @Override
    public void onCancelled(CancelledException cex) {
        super.onCancelled(cex);
    }

    @Override
    public void onFinished() {
        super.onFinished();
    }

    //初始化控件
    private void InitView(View view) {
        tempTv = view.findViewById(R.id.frag_tv_current_temp);
        cityTv = view.findViewById(R.id.frag_tv_city);
        conditionTv = view.findViewById(R.id.frag_tv_condition);
        windTv = view.findViewById(R.id.frag_tv_wind);
        tempRangeTv = view.findViewById(R.id.frag_tv_temp_range);
        dateTv = view.findViewById(R.id.frag_tv_date);
        PM25Tv = view.findViewById(R.id.frag_tv_PM25);
        clothIndexTv = view.findViewById(R.id.frag_index_tv_dress);
        carIndexTv = view.findViewById(R.id.frag_index_tv_washcar);
        clodIndexTv = view.findViewById(R.id.frag_index_tv_cold);
        sportIndexTv = view.findViewById(R.id.frag_index_tv_sport);
        raysIndexTv = view.findViewById(R.id.frag_index_tv_light);

        dayIv = view.findViewById(R.id.frag_iv_today);

        futureLayout = view.findViewById(R.id.frag_center_layout);

        //设置监听器
        clothIndexTv.setOnClickListener(this);
        carIndexTv.setOnClickListener(this);
        clodIndexTv.setOnClickListener(this);
        sportIndexTv.setOnClickListener(this);
        raysIndexTv.setOnClickListener(this);
    }

    // 监听器的作用：在用户点击指数图标后，显示对应的信息在一个Dialog中
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        WeatherBean.ResultsBean.IndexBean indexBean;
        String msg;
        switch (v.getId()){
            case R.id.frag_index_tv_dress:
                indexBean = indexList.get(0);
                builder.setTitle(indexBean.getTipt());
                msg = indexBean.getZs() + "\n" + indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_washcar:
                indexBean = indexList.get(1);
                builder.setTitle(indexBean.getTipt());
                msg = indexBean.getZs() + "\n" + indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_cold:
                indexBean = indexList.get(2);
                builder.setTitle(indexBean.getTipt());
                msg = indexBean.getZs() + "\n" + indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_sport:
                indexBean = indexList.get(3);
                builder.setTitle(indexBean.getTipt());
                msg = indexBean.getZs() + "\n" + indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_light:
                indexBean = indexList.get(4);
                builder.setTitle(indexBean.getTipt());
                msg = indexBean.getZs() + "\n" + indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
        }
        builder.create().show();
    }
}
