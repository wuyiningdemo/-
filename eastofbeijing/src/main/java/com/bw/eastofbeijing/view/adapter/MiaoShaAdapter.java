package com.bw.eastofbeijing.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.HoneBean;
import com.bw.eastofbeijing.view.Holder.MiaoShaHolder;
import com.bw.eastofbeijing.view.adapter.linter.OnItemListner;

/**
 * Created by 兰昊琼 on 2018/3/20.
 */

public class MiaoShaAdapter extends RecyclerView.Adapter<MiaoShaHolder> {
     private Context context;
     private  HoneBean.MiaoshaBean miaosha;
    private OnItemListner onItemListner;

    public MiaoShaAdapter(Context context, HoneBean.MiaoshaBean miaosha) {
        this.context = context;
        this.miaosha = miaosha;
    }

    @Override
    public MiaoShaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= View.inflate(context, R.layout.miaosha_item,null);
        MiaoShaHolder miaoShaHolder=new MiaoShaHolder(view);
        return miaoShaHolder;
    }


    @Override
    public void onBindViewHolder(MiaoShaHolder holder, final int position) {
        String[] strings = miaosha.getList().get(position).getImages().split("\\|");
       /* Uri uri = Uri.parse(strings[0]);
        holder.miaosha_img.setImageURI(uri);*/
        Glide.with(context).load(strings[0]).into(holder.miaosha_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListner.onItemClickListner(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return miaosha.getList().size();
    }

    public void setOnItemListner(OnItemListner onItemListner) {
        this.onItemListner = onItemListner;
    }
}
