package com.example.weatherprocast;

import android.telecom.Call;

import androidx.fragment.app.Fragment;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

// 利用xutils组件加载网络数据

public class BaseFragment extends Fragment implements Callback.CommonCallback<String> {

    //1.声明模块整体，定义了一个UniteApp类，在其onCreate方法中掉用全局声明语句。

    //2. 执行网络请求操作
    public void loadData(String path){
        RequestParams params = new RequestParams(path);
        x.http().get(params,this);
    }

// 成功获取网络数据后的回调接口
    @Override
    public void onSuccess(String result) {

    }
// 获取数据失败后的回调接口
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }
// 取消请求后的回调接口
    @Override
    public void onCancelled(CancelledException cex) {

    }
// 请求完成后的回调接口
    @Override
    public void onFinished() {

    }
}
