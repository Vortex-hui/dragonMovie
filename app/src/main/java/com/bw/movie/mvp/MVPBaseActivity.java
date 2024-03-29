package com.bw.movie.mvp;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.bw.movie.net.NetWorkUtils;

import java.lang.reflect.ParameterizedType;

import static com.bw.movie.net.NetWorkUtils.connectionReceiver;


/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public abstract class MVPBaseActivity<V extends BaseView,T extends BasePresenterImpl<V>> extends AppCompatActivity implements BaseView{
    public T mPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (NetWorkUtils.isNetworkAvailable(this)){
            mPresenter= getInstance(this,1);
            mPresenter.attachView((V) this);
        }
        connectionReceiver = connectionReceiver;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectionReceiver);
        if (mPresenter!=null)
        mPresenter.detachView();
    }



    @Override
    public Context getContext(){
        return this;
    }

    public  <T> T getInstance(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }



}
