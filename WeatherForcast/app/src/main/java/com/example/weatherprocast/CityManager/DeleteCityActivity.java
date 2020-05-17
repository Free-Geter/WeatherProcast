package com.example.weatherprocast.CityManager;


import com.example.weatherprocast.DataBase.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weatherprocast.R;

import java.util.ArrayList;
import java.util.List;

public class DeleteCityActivity extends AppCompatActivity implements View.OnClickListener {

    TextView errorTv,rightTv;
    ListView deleteLv;

    // ListView的数据源
    List<String> mDatas = dbManager.queryAllCityName();
    List<String> CityToDelete;
    private DeleteCityAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_city);
        errorTv = findViewById(R.id.delete_tv_error);
        rightTv = findViewById(R.id.delete_tv_right);
        deleteLv = findViewById(R.id.delete_lv);


        CityToDelete = new ArrayList<>();

        errorTv.setOnClickListener(this);
        rightTv.setOnClickListener(this);

        // 设置适配器
        adapter = new DeleteCityAdapter(this, mDatas, CityToDelete);
        deleteLv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete_tv_right:
                for(int i=0; i < CityToDelete.size(); i++){
                    String city = CityToDelete.get(i);

                    // 调用删除城市信息的函数
                    dbManager.deleteInfo(city);
                }
                // 完成删除，回到上级Activity
                finish();
                break;
            case R.id.delete_tv_error:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示信息").setMessage("您确定要舍弃更改吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 确认取消后，关闭当前Activity
                                finish();
                            }
                        });
                builder.setNegativeButton("取消",null);
                builder.create().show();
                break;
        }
    }


}
