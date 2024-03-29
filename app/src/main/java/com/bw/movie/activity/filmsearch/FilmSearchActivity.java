package com.bw.movie.activity.filmsearch;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bw.movie.R;
import com.bw.movie.adapter.MyJiAdapter;
import com.bw.movie.adapter.MyReMenAdapter;
import com.bw.movie.adapter.MyZhengAdapter;
import com.bw.movie.bean.CancelFollowMovieBean;
import com.bw.movie.bean.FlowllMovieBean;
import com.bw.movie.bean.JiFilmBean;
import com.bw.movie.bean.ReFilmBean;
import com.bw.movie.bean.ShangFilmBean;
import com.bw.movie.mvp.MVPBaseActivity;
import com.bw.movie.net.NetWorkUtils;
import com.bw.movie.utils.AlertDialogUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class FilmSearchActivity extends MVPBaseActivity<FilmSearchContract.View, FilmSearchPresenter> implements FilmSearchContract.View {
    public static final String TAG = "FilmDetailsActivity";
    @BindView(R.id.btn_re)
    Button btnRe;
    @BindView(R.id.btn_zheng)
    Button btnZheng;
    @BindView(R.id.btn_jijiang)
    Button btnJijiang;
    @BindView(R.id.search_recycler_view)
    RecyclerView searchRecyclerView;
    @BindView(R.id.search_return)
    ImageView searchReturn;
    @BindView(R.id.search_linear)
    LinearLayout searchLinear;
    private List<ReFilmBean.ResultBean> reBeanList;
    private List<ShangFilmBean.ResultBean> shangBeanList;
    private List<JiFilmBean.ResultBean> jiBeanList;
    private SharedPreferences preferences;
    private String userId;
    private String sessionId;

    @Override
    protected void onResume() {
        super.onResume();
        userId = preferences.getString("userId", "");
        sessionId = preferences.getString("sessionId", "");
        reBeanList = (List<ReFilmBean.ResultBean>) getIntent().getSerializableExtra("reBeanList");
        shangBeanList = (List<ShangFilmBean.ResultBean>) getIntent().getSerializableExtra("shangBeanList");
        jiBeanList = (List<JiFilmBean.ResultBean>) getIntent().getSerializableExtra("jiBeanList");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_search);
        if (NetWorkUtils.isNetworkAvailable(FilmSearchActivity.this)) {
            preferences = getSharedPreferences("config", MODE_PRIVATE);
            userId = preferences.getString("userId", "");
            sessionId = preferences.getString("sessionId", "");
            ButterKnife.bind(this);
            //接收intent的值
            reBeanList = (List<ReFilmBean.ResultBean>) getIntent().getSerializableExtra("reBeanList");
            shangBeanList = (List<ShangFilmBean.ResultBean>) getIntent().getSerializableExtra("shangBeanList");
            jiBeanList = (List<JiFilmBean.ResultBean>) getIntent().getSerializableExtra("jiBeanList");
            //Log.i(TAG, "onCreate: " + reBeanList.get(0).getName() + shangBeanList.get(0).getName() + jiBeanList.get(0).getName());
            //布局管理器
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            searchRecyclerView.setLayoutManager(linearLayoutManager);
            MyReMenAdapter reMenAdapter = new MyReMenAdapter(this, reBeanList);
            searchRecyclerView.setAdapter(reMenAdapter);
            int type = getIntent().getIntExtra("type", 0);
            if (type == 0) {
                setReAdapter();
            } else if (type == 1) {
                setZhengAdapter();
            } else if (type == 2) {
                setJiAdapter();
            }
            searchReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


        }
    }


    @OnClick({R.id.btn_re, R.id.btn_zheng, R.id.btn_jijiang})
    public void onViewClicked(View view) {
        if (NetWorkUtils.isNetworkAvailable(FilmSearchActivity.this)) {
            switch (view.getId()) {
                case R.id.btn_re:
                    setReAdapter();
                    break;
                case R.id.btn_zheng:
                    setZhengAdapter();
                    break;
                case R.id.btn_jijiang:
                    setJiAdapter();
                    break;
            }
        }
    }

    private void setJiAdapter() {
        if (NetWorkUtils.isNetworkAvailable(FilmSearchActivity.this)) {
            btnJijiang.setBackgroundResource(R.drawable.button_ripple);
            btnJijiang.setTextColor(Color.parseColor("#ffffff"));
            btnRe.setTextColor(Color.BLACK);
            btnRe.setBackgroundColor(Color.parseColor("#ffffff"));
            btnZheng.setTextColor(Color.BLACK);
            btnZheng.setBackgroundColor(Color.parseColor("#ffffff"));
            final MyJiAdapter jiAdapter = new MyJiAdapter(this, jiBeanList);
            searchRecyclerView.setAdapter(jiAdapter);
            jiAdapter.setAttentionClick(new MyJiAdapter.AttentionClick() {
                @Override
                public void clickattention(String cinemaId, int i, boolean b) {
                    if (b) {
                        if (!userId.equals("") && !sessionId.equals("")) {
                            //Toast.makeText(FilmSearchActivity.this, cinemaId, Toast.LENGTH_LONG).show();
                            Map<String, Object> headMap = new HashMap<>();
                            headMap.put("userId", userId);
                            headMap.put("sessionId", sessionId);

                            mPresenter.getFlowllMoviePresenter(headMap, cinemaId);
                            jiBeanList.get(i).setFollowMovie(1);
                            jiAdapter.notifyDataSetChanged();
                        } else {
                            AlertDialogUtils.AlertDialogLogin(FilmSearchActivity.this);
                        }
                        jiAdapter.notifyDataSetChanged();
                    } else {
                        if (!userId.equals("") && !sessionId.equals("")) {
                            HashMap<String, Object> headMap = new HashMap<>();
                            HashMap<String, Object> canclePrams = new HashMap<>();
                            canclePrams.put("movieId", cinemaId);
                            headMap.put("userId", userId);
                            headMap.put("sessionId", sessionId);
                            mPresenter.cancelFollowMoviePresenter(headMap, canclePrams);
                            jiBeanList.get(i).setFollowMovie(2);
                            jiAdapter.notifyDataSetChanged();
                        } else {
                            AlertDialogUtils.AlertDialogLogin(FilmSearchActivity.this);
                        }
                    }
                }
            });
        }
    }

    private void setZhengAdapter() {
        if (NetWorkUtils.isNetworkAvailable(FilmSearchActivity.this)) {
            btnZheng.setBackgroundResource(R.drawable.button_ripple);
            btnZheng.setTextColor(Color.parseColor("#ffffff"));
            btnRe.setTextColor(Color.BLACK);
            btnRe.setBackgroundColor(Color.parseColor("#ffffff"));
            btnJijiang.setTextColor(Color.BLACK);
            btnJijiang.setBackgroundColor(Color.parseColor("#ffffff"));
            final MyZhengAdapter zhengAdapter = new MyZhengAdapter(this, shangBeanList);
            searchRecyclerView.setAdapter(zhengAdapter);
            zhengAdapter.setAttentionClick(new MyZhengAdapter.AttentionClick() {
                @Override
                public void clickattention(String cinemaId, int i, boolean b) {
                    if (b) {
                        if (!userId.equals("") && !sessionId.equals("")) {
                            // Toast.makeText(FilmSearchActivity.this, cinemaId, Toast.LENGTH_LONG).show();
                            Map<String, Object> headMap = new HashMap<>();
                            headMap.put("userId", userId);
                            headMap.put("sessionId", sessionId);
                            mPresenter.getFlowllMoviePresenter(headMap, cinemaId);
                            shangBeanList.get(i).setFollowMovie(1);
                            zhengAdapter.notifyDataSetChanged();
                        } else {
                            AlertDialogUtils.AlertDialogLogin(FilmSearchActivity.this);
                        }
                        zhengAdapter.notifyDataSetChanged();
                    } else {
                        if (!userId.equals("") && !sessionId.equals("")) {
                            HashMap<String, Object> headMap = new HashMap<>();
                            HashMap<String, Object> canclePrams = new HashMap<>();
                            canclePrams.put("movieId", cinemaId);
                            headMap.put("userId", userId);
                            headMap.put("sessionId", sessionId);
                            mPresenter.cancelFollowMoviePresenter(headMap, canclePrams);
                            shangBeanList.get(i).setFollowMovie(2);
                            zhengAdapter.notifyDataSetChanged();
                        } else {
                            AlertDialogUtils.AlertDialogLogin(FilmSearchActivity.this);
                        }
                    }
                }
            });
        }
    }

    private void setReAdapter() {
        if (NetWorkUtils.isNetworkAvailable(FilmSearchActivity.this)) {
            btnRe.setBackgroundResource(R.drawable.button_ripple);
            btnRe.setTextColor(Color.parseColor("#ffffff"));
            btnJijiang.setTextColor(Color.BLACK);
            btnJijiang.setBackgroundColor(Color.parseColor("#ffffff"));
            btnZheng.setTextColor(Color.BLACK);
            btnZheng.setBackgroundColor(Color.parseColor("#ffffff"));
            final MyReMenAdapter reMenAdapter = new MyReMenAdapter(this, reBeanList);
            searchRecyclerView.setAdapter(reMenAdapter);
            reMenAdapter.setAttentionClick(new MyReMenAdapter.AttentionClick() {
                @Override
                public void clickattention(String cinemaId, int i, boolean b) {
                    if (b) {
                        if (!userId.equals("") && !sessionId.equals("")) {
                            // Toast.makeText(FilmSearchActivity.this, cinemaId, Toast.LENGTH_LONG).show();
                            Map<String, Object> headMap = new HashMap<>();
                            headMap.put("userId", userId);
                            headMap.put("sessionId", sessionId);

                            mPresenter.getFlowllMoviePresenter(headMap, cinemaId);
                            reBeanList.get(i).setFollowMovie(1);
                            reMenAdapter.notifyDataSetChanged();
                        } else {
                            AlertDialogUtils.AlertDialogLogin(FilmSearchActivity.this);
                        }
                        reMenAdapter.notifyDataSetChanged();
                    } else {
                        if (!userId.equals("") && !sessionId.equals("")) {
                            HashMap<String, Object> headMap = new HashMap<>();
                            headMap.put("userId", userId);
                            headMap.put("sessionId", sessionId);
                            HashMap<String, Object> canclePrams = new HashMap<>();
                            canclePrams.put("movieId", cinemaId);
                            mPresenter.cancelFollowMoviePresenter(headMap, canclePrams);
                            reBeanList.get(i).setFollowMovie(2);
                            reMenAdapter.notifyDataSetChanged();
                        } else {
                            AlertDialogUtils.AlertDialogLogin(FilmSearchActivity.this);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void getFlowllMovieData(Object object) {
        if (NetWorkUtils.isNetworkAvailable(FilmSearchActivity.this)) {
            if (object != null) {
                FlowllMovieBean flowllMovieBean = (FlowllMovieBean) object;
                if (flowllMovieBean.getStatus().equals("0000")) {
                    Toast.makeText(this, flowllMovieBean.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void cancelFollowMovieData(Object object) {
        if (NetWorkUtils.isNetworkAvailable(FilmSearchActivity.this)) {
            if (object != null) {
                CancelFollowMovieBean cancelFollowMovieBean = (CancelFollowMovieBean) object;
                if (cancelFollowMovieBean.getStatus().equals("0000")) {
                    Toast.makeText(this, cancelFollowMovieBean.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(this, cancelFollowMovieBean.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
