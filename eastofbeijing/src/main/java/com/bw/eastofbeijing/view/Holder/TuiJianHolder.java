package com.bw.eastofbeijing.view.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.eastofbeijing.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 兰昊琼 on 2018/3/21.
 */

public class TuiJianHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tuijian_img)
    public ImageView tuijian_img;
    @BindView(R.id.tuijian_tv)
    public TextView tuijian_tv;
    public TuiJianHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
