package com.example.weatherprocast.CityManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weatherprocast.R;

import java.util.List;

public class DeleteCityAdapter extends BaseAdapter {

    Context context;
    List<String> mDatas;
    List<String> CitiesToDelete;

    public DeleteCityAdapter(Context context, List<String> mDatas,List<String>citiesToDelete) {
        this.context = context;
        this.mDatas = mDatas;
        this.CitiesToDelete = citiesToDelete;
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
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_delete_city,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final String city = mDatas.get(position);
        viewHolder.tv.setText(city);
        viewHolder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.remove(city);

                //临时标记要删除的城市
                CitiesToDelete.add(city);

                // 删除以后提示适配器更新
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView tv;
        ImageView iv;
        public ViewHolder(View itemView){
            tv = itemView.findViewById(R.id.item_delete_tv);
            iv = itemView.findViewById(R.id.item_delete_iv);
        }
    }
}
