package com.bw.movie.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.bean.JiFilmBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JiRecyclerAdapter extends RecyclerView.Adapter<JiRecyclerAdapter.ViewHolder> {


    private Context context;
    private List<JiFilmBean.ResultBean> jiFilmBeanResult;

    public JiRecyclerAdapter(Context context, List<JiFilmBean.ResultBean> jiFilmBeanResult) {
        this.context = context;
        this.jiFilmBeanResult = jiFilmBeanResult;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.film_item_show_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.filmName.getBackground().setAlpha(200);
        holder.filmName.setText(jiFilmBeanResult.get(position).getName());
        Uri uri = Uri.parse(jiFilmBeanResult.get(position).getImageUrl());
        holder.filmSimpleView.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return jiFilmBeanResult.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.film_simple_view)
        SimpleDraweeView filmSimpleView;
        @BindView(R.id.film_name)
        TextView filmName;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}