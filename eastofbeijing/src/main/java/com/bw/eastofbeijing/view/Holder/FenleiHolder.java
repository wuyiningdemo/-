package com.bw.eastofbeijing.view.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.eastofbeijing.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 兰昊琼 on 2018/3/20.
 */

public class FenleiHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.fenlei_img)
    public   ImageView  fenlei_img;
    @BindView(R.id.fenlei_tv)
    public TextView fenlei_tv;
    public FenleiHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
