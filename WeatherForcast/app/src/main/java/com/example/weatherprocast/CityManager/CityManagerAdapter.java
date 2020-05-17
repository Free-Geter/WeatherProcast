package com.example.weatherprocast.CityManager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weatherprocast.DataBase.DatabaseBean;
import com.example.weatherprocast.R;
import com.example.weatherprocast.bean.WeatherBean;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.List;

public class CityManagerAdapter extends BaseAdapter {

    Context context;
    List<DatabaseBean> mDatas;
    LayoutInflater myLayoutInflater;

    public CityManagerAdapter(Context context, List<DatabaseBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        myLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("city message", "getView: add a city");
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = myLayoutInflater.inflate(R.layout.item_city,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DatabaseBean bean = mDatas.get(position);
        viewHolder.cityTv.setText(bean.getCity());

        // 解析此view的天气信息
        WeatherBean weatherBean = new Gson().fromJson(bean.getContent(), WeatherBean.class);
        // 获取今日天气情况
        WeatherBean.ResultsBean.WeatherDataBean dataBean = weatherBean.getResults().get(0).getWeather_data().get(0);
        viewHolder.conTv.setText("天气：" + dataBean.getWeather());
        // 截取实时温度
        String[] split = dataBean.getDate().split("：");
        String todayTemp = split[1].replace(")","");
        viewHolder.currentTempTv.setText(todayTemp);

        viewHolder.windTv.setText(dataBean.getWind());
        viewHolder.tempRangeTv.setText(dataBean.getTemperature());
        return convertView;
    }

    class ViewHolder{
        TextView cityTv,conTv,currentTempTv,windTv,tempRangeTv;
        public ViewHolder(View itemView){
            cityTv = itemView.findViewById(R.id.item_city_tv_name);
            conTv = itemView.findViewById(R.id.item_city_tv_condition);
            currentTempTv = itemView.findViewById(R.id.item_city_tv_temp);
            windTv = itemView.findViewById(R.id.item_city_wind);
            tempRangeTv = itemView.findViewById(R.id.item_city_tempRange);
        }
    }
}
