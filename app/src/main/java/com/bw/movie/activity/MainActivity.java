package com.bw.movie.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.bw.movie.R;
import com.bw.movie.net.NetWorkUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences.Editor edit;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            preferences = getSharedPreferences("confing", Context.MODE_PRIVATE);
            edit = preferences.edit();
            Flowable.intervalRange(0, 3, 0, 1, TimeUnit.SECONDS)
                    .doOnNext(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {

                        }
                    })
                    .doOnComplete(new Action() {
                        @Override
                        public void run() throws Exception {

                            if (preferences.getBoolean("flag", true)) {
                                edit.putBoolean("flag", false);
                                edit.commit();
                                Looper.prepare();
                                if (NetWorkUtils.isNetworkAvailable(MainActivity.this)) {
                                    startActivity(new Intent(MainActivity.this, GuideActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                                }else{
                                    startActivity(new Intent(MainActivity.this,NetActivity.class));
                                    Toast.makeText(MainActivity.this,"没网还点啥呀",Toast.LENGTH_LONG).show();
                                }
                                Looper.loop();
                                finish();
                            } else {
                                Looper.prepare();
                                if (NetWorkUtils.isNetworkAvailable(MainActivity.this)) {
                                    startActivity(new Intent(MainActivity.this, ShowActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                                    finish();
                                }else{
                                    startActivity(new Intent(MainActivity.this,NetActivity.class));
                                    Toast.makeText(MainActivity.this,"没网还点啥呀",Toast.LENGTH_LONG).show();

                                }
                                Looper.loop();

                            }

                        }
                    })
                    .subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetWorkUtils.isNetworkAvailable(MainActivity.this)){
            startActivity(new Intent(MainActivity.this, ShowActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
//            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
