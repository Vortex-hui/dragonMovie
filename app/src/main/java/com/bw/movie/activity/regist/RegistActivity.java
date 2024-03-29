package com.bw.movie.activity.regist;


import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bw.movie.R;
import com.bw.movie.activity.login.LoginActivity;
import com.bw.movie.aes.EncryptUtil;
import com.bw.movie.bean.RegistBean;
import com.bw.movie.mvp.MVPBaseActivity;
import com.bw.movie.net.NetWorkUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class RegistActivity extends MVPBaseActivity<RegistContract.View, RegistPresenter> implements RegistContract.View {

    @BindView(R.id.edit_name_regist)
    EditText editNameRegist;
    @BindView(R.id.edit_sex_regist)
    EditText editSexRegist;
    @BindView(R.id.edit_date_regist)
    EditText editDateRegist;
    @BindView(R.id.edit_phone_regist)
    EditText editPhoneRegist;
    @BindView(R.id.edit_email_regist)
    EditText editEmailRegist;
    @BindView(R.id.edit_pwd_regist)
    EditText editPwdRegist;
    @BindView(R.id.btn_regist)
    Button btnRegist;
    private int sexNow;
    private TelephonyManager mTm;
    private DisplayMetrics metric;
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        getWindow().setEnterTransition(new Explode().setDuration(800));
        getWindow().setExitTransition(new Explode().setDuration(800));
        ButterKnife.bind(this);
        if (NetWorkUtils.isNetworkAvailable(RegistActivity.this)) {
            sp = getSharedPreferences("config", Context.MODE_PRIVATE);
            mTm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            final TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    editDateRegist.setText(getTime(date));
                }
            })
                    .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                    .setCancelText("取消")//取消按钮文字
                    .setSubmitText("确定")//确认按钮文字
//                .setContentSize(18)//滚轮文字大小
//                .setTitleSize(20)//标题文字大小
//                //.setTitleText("Title")//标题文字
//                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
//                .isCyclic(true)//是否循环滚动
//                //.setTitleColor(Color.BLACK)//标题文字颜色
//                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
//                .setCancelColor(Color.BLUE)//取消按钮文字颜色
//                //.setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
//                .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
////                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
////                .setRangDate(startDate,endDate)//起始终止年月日设定
//                //.setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                    .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                    //.isDialog(true)//是否显示为对话框样式
                    .build();


            editDateRegist.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    pvTime.show();
                }
            });


        }
    }


    public String getTime(Date date) { //可根据需要自行截取数据显示
            SimpleDateFormat format = new SimpleDateFormat(" yyyy-MM-dd ");
            return format.format(date);
    }


    @Override
    public void registView(Object obj) {
        if (NetWorkUtils.isNetworkAvailable(RegistActivity.this)) {
            RegistBean registBean = (RegistBean) obj;
            if (registBean.getMessage().equals("注册成功")) {
                Toast.makeText(RegistActivity.this, registBean.getMessage(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(RegistActivity.this, LoginActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                finish();
            } else {
                Toast.makeText(RegistActivity.this, "注册失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    //1971658757@qq.com
    @OnClick(R.id.btn_regist)
    public void onViewClicked() {
        if (NetWorkUtils.isNetworkAvailable(RegistActivity.this)) {
            SharedPreferences.Editor edit = sp.edit();
            String name = editNameRegist.getText().toString().trim();
            String sex = editSexRegist.getText().toString().trim();
            if (sex.equals("男")) {
                sexNow = 1;
            } else if (sex.equals("nv")) {
                sexNow = 2;
            } else {
                sexNow = 1;
            }
            String phone = editPhoneRegist.getText().toString().trim();
            String pwd = editPwdRegist.getText().toString().trim();
            String encrypt = EncryptUtil.encrypt(pwd);
            String date = editDateRegist.getText().toString().trim();
            String emain = editEmailRegist.getText().toString().trim();

            @SuppressLint("MissingPermission") String imei = mTm.getDeviceId();//得到用户Id
            @SuppressLint("MissingPermission") String imsi = mTm.getSubscriberId();
            @SuppressLint("MissingPermission") String deviceid = mTm.getDeviceId();//获取智能设备唯一编号
            String mtyb = android.os.Build.BRAND;//手机品牌
            float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5，小米4的是3.0）
            Map<String, Object> parms = new HashMap<>();
            parms.put("nickName", name);
            parms.put("phone", phone);
            parms.put("pwd", encrypt);
            parms.put("pwd2", encrypt);
            parms.put("sex", sexNow);
            parms.put("birthday", date);
            parms.put("email", emain);
            //移动设备唯一识别码
            parms.put("imei", deviceid);
            //设备类型
            parms.put("ua", mtyb);
            //屏幕尺寸
            parms.put("screenSize", density);
            //手机系统
            parms.put("os", "android");
            edit.putString("phone", phone);
            edit.putBoolean("flag", true);
            edit.putString("pwd", encrypt);
            edit.commit();
            mPresenter.registPresenter(parms);
        }
    }
}
