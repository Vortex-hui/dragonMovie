package com.bw.movie.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bw.movie.R;
import com.bw.movie.activity.recommenddetails.RecommenddetailsActivity;
import com.bw.movie.bean.RecommendCinemasBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyRecommendAdapter extends RecyclerView.Adapter<MyRecommendAdapter.ViewHolder> {
    Context context;
    RecommendCinemasBean recommendCinemasBean;


    public MyRecommendAdapter(Context context, RecommendCinemasBean recommendCinemasBean) {
        this.context = context;
        this.recommendCinemasBean = recommendCinemasBean;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recommend_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Uri uri = Uri.parse(recommendCinemasBean.getResult().get(i).getLogo());
        viewHolder.simPleRecommend.setImageURI(uri);
        viewHolder.textNameRecommend.setText(recommendCinemasBean.getResult().get(i).getName());
        TextPaint paint = viewHolder.textNameRecommend.getPaint();
        paint.setFakeBoldText(true);
        viewHolder.textAddressRecommend.setText(recommendCinemasBean.getResult().get(i).getAddress());
        viewHolder.textKmRecommend.setText(recommendCinemasBean.getResult().get(i).getDistance()+"km");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,RecommenddetailsActivity.class);
                intent.putExtra("eid", recommendCinemasBean.getResult().get(i).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recommendCinemasBean.getResult().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.simPle_recommend)
        SimpleDraweeView simPleRecommend;
        @BindView(R.id.text_name_recommend)
        TextView textNameRecommend;
        @BindView(R.id.text_address_recommend)
        TextView textAddressRecommend;
        @BindView(R.id.text_km_recommend)
        TextView textKmRecommend;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
