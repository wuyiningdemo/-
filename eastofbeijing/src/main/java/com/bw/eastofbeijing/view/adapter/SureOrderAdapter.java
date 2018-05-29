package com.bw.eastofbeijing.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.CartBean;
import com.bw.eastofbeijing.view.Holder.SureOrderHolder;

import java.util.ArrayList;

/**
 * Created by Dash on 2018/2/24.
 */
public class SureOrderAdapter extends RecyclerView.Adapter<SureOrderHolder>{
    private ArrayList<CartBean.DataBean.ListBean> list_selected;
    private Context context;

    public SureOrderAdapter(Context context, ArrayList<CartBean.DataBean.ListBean> list_selected) {
        this.context = context;
        this.list_selected = list_selected;
    }

    @Override
    public SureOrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.sure_order_item_layout,null);
        SureOrderHolder sureOrderHolder = new SureOrderHolder(view);

        return sureOrderHolder;
    }

    @Override
    public void onBindViewHolder(SureOrderHolder holder, int position) {

        CartBean.DataBean.ListBean listBean = list_selected.get(position);

        //赋值
        Glide.with(context).load(listBean.getImages().split("\\|")[0]).into(holder.sure_item_image);
        holder.sure_item_title.setText(listBean.getTitle());
        holder.sure_item_price.setText("¥"+listBean.getBargainPrice());
        holder.sure_item_num.setText("x"+listBean.getNum());
    }

    @Override
    public int getItemCount() {
        return list_selected.size();
    }
}
