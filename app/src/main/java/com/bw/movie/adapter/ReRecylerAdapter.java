package com.bw.movie.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.movie.R;
import com.bw.movie.activity.NetActivity;
import com.bw.movie.activity.filmdetails.FilmDetailsActivity;
import com.bw.movie.bean.ReFilmBean;
import com.bw.movie.net.NetWorkUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReRecylerAdapter extends RecyclerView.Adapter<ReRecylerAdapter.ViewHolder> {

    private Context context;
    private List<ReFilmBean.ResultBean> reFilmBeanResult;

    public ReRecylerAdapter(Context context, List<ReFilmBean.ResultBean> reFilmBeanResult) {
        this.context = context;
        this.reFilmBeanResult = reFilmBeanResult;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.film_item_show_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (NetWorkUtils.isNetworkAvailable(context)) {
            holder.filmName.getBackground().setAlpha(200);
            holder.filmName.setText(reFilmBeanResult.get(position).getName());

            Uri uri = Uri.parse(reFilmBeanResult.get(position).getImageUrl());
            holder.filmSimpleView.setImageURI(uri);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetWorkUtils.isNetworkAvailable(context)) {
                        context.startActivity(new Intent(context, FilmDetailsActivity.class));
                        EventBus.getDefault().postSticky(reFilmBeanResult.get(position).getId());
                    }else{
                        context.startActivity(new Intent(context,NetActivity.class));
                        Toast.makeText(context,"没网还点啥呀",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return reFilmBeanResult.size();
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
