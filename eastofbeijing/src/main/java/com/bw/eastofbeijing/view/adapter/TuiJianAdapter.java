package com.bw.eastofbeijing.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.HoneBean;
import com.bw.eastofbeijing.view.Holder.TuiJianHolder;
import com.bw.eastofbeijing.view.adapter.linter.OnItemListner;

import java.util.List;

/**
 * Created by 兰昊琼 on 2018/3/21.
 */

public class TuiJianAdapter extends RecyclerView.Adapter<TuiJianHolder> {
     private Context context;
     private List<HoneBean.TuijianBean.ListBean> list;
    private OnItemListner onItemListner;

    public TuiJianAdapter(Context context, List<HoneBean.TuijianBean.ListBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public TuiJianHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= View.inflate(context, R.layout.tuijian_item,null);
        TuiJianHolder tuiJianHolder=new TuiJianHolder(view);

        return tuiJianHolder;
    }


    @Override
    public void onBindViewHolder(TuiJianHolder holder, final int position) {
        holder.tuijian_tv.setText(list.get(position).getTitle());
        String[] strings = list.get(position).getImages().split("\\|");
        Glide.with(context).load(strings[0]).into(holder.tuijian_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListner.onItemClickListner(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemListner(OnItemListner onItemListner) {
        this.onItemListner = onItemListner;
    }
}
