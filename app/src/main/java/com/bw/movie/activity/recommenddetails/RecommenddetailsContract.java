package com.bw.movie.activity.recommenddetails;

import android.content.Context;

import com.bw.movie.mvp.BasePresenter;
import com.bw.movie.mvp.BaseView;

import java.util.Map;

import retrofit2.http.Query;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class RecommenddetailsContract {
    interface View extends BaseView {
        void recommendDetailsView(Object obj);
        void filmFromIdView(Object obj);
        void movieIdAndfilmIdView(Object obj);
        void cinemaCommentView(Object obj);
        void cinemaPriaseView(Object obj);
    }

    interface  Presenter extends BasePresenter<View> {
        void recommendDetailsPresenter(Map<String,Object> headMap,String cinemaId);
        void filmFromIdPresenter(String cinemaId);
        void movieIdAndfilmIdPresenter(String movieId,String cinemasId);
        void cinemaCommentPresenter(Map<String,Object> headMap,Map<String,Object> parms);
        void cinemaPriasePresenter(Map<String,Object> headMap,String commentId);
    }
}
