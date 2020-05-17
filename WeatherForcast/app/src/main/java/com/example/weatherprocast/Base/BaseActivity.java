package com.example.weatherprocast.Base;

import androidx.appcompat.app.AppCompatActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

      // 实现Activity联网

public class BaseActivity extends AppCompatActivity implements Callback.CommonCallback<String> {

    public void loadData(String url){
        RequestParams params = new RequestParams(url);
        x.http().get(params,this);
    }

    @Override
    public void onSuccess(String s) {

    }

    @Override
    public void onError(Throwable throwable, boolean b) {

    }

    @Override
    public void onCancelled(CancelledException e) {

    }

    @Override
    public void onFinished() {

    }
}
