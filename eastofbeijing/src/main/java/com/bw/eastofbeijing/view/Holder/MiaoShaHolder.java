package com.bw.eastofbeijing.view.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bw.eastofbeijing.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 兰昊琼 on 2018/3/20.
 */

public class MiaoShaHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.miaosha_img)
    public  ImageView miaosha_img;
    public MiaoShaHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
