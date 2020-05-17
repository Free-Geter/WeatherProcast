package com.example.weatherprocast.CityManager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherprocast.Base.BaseActivity;
import com.example.weatherprocast.MainActivity;
import com.example.weatherprocast.R;
import com.example.weatherprocast.bean.WeatherBean;
import com.google.gson.Gson;

import org.w3c.dom.Text;

public class SearchCityActivity extends BaseActivity implements View.OnClickListener {

    EditText searchEt;
    TextView submitTv;
    GridView searchGv;
    String[] hotCities = {"北京","上海","广州","深圳","珠海","佛山","南京","苏州","厦门","长沙","成都","福州",
            "杭州","武汉","青岛","西安","太原","沈阳","重庆","天津","南宁"};
    private ArrayAdapter<String> arrayAdapter;
    String urlp1 = "http://api.map.baidu.com/telematics/v3/weather?location=";
    String urlp2 = "&output=json&ak=FkPhtMBK0HTIQNh7gG4cNUttSTyr0nzo";
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);

        searchEt = findViewById(R.id.search_et);
        submitTv = findViewById(R.id.search_tv_submit);
        searchGv = findViewById(R.id.search_gv);

        // 为热门城市设置监听器
        searchGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city = hotCities[position];
                String url = urlp1 + city + urlp2;
                loadData(url);
            }
        });

        submitTv.setOnClickListener(this);

        // 设置适配器
        arrayAdapter = new ArrayAdapter<>(this, R.layout.item_hot_city, hotCities);
        searchGv.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_tv_submit:
                city = searchEt.getText().toString();
                if(!TextUtils.isEmpty(city)){
                    // 使用传入的city名进行查找，通过返回Error值判断输入的城市名称是否有效
                    String url = urlp1 + city + urlp2;
                    loadData(url);
                }else{
                    Toast.makeText(this,"输入内容不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onSuccess(String s) {
        WeatherBean weatherBean = new Gson().fromJson(s, WeatherBean.class);

        // Error返回值为0，表示查找成功，重置Activity栈，只放如MainActivity并返回MainActivity
        if(weatherBean.getError() == 0){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("city",city);
            startActivity(intent);
        }else{
            Toast.makeText(this,"未收录此城市天气信息",Toast.LENGTH_SHORT).show();
        }
    }
}

