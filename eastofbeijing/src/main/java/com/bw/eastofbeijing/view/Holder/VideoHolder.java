package com.bw.eastofbeijing.view.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.eastofbeijing.R;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 兰昊琼 on 2018/4/25.
 */

public class VideoHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.img_touxiang)
   public SimpleDraweeView imgTouxiang;
    @BindView(R.id.tv_name)
    public   TextView tvName;
    @BindView(R.id.workDesc)
    public    TextView workDesc;
    @BindView(R.id.txet_time)
    public TextView txetTime;
    @BindView(R.id.cover_simleview)
    public ImageView coverSimleview;
    @BindView(R.id.videoUrl_simleview)
    public ImageView videoUrlSimleview;
    public VideoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
