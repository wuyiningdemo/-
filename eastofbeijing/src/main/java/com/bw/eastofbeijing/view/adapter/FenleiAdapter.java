package com.bw.eastofbeijing.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.FenLeiBean;
import com.bw.eastofbeijing.view.Holder.FenleiHolder;

import java.util.List;

/**
 * Created by 兰昊琼 on 2018/3/20.
 */

public class FenleiAdapter extends RecyclerView.Adapter<FenleiHolder> {
  private Context context;
  private List<FenLeiBean.DataBean>  dataBeans;

    public FenleiAdapter(Context context, List<FenLeiBean.DataBean> dataBeans) {
        this.context = context;
        this.dataBeans = dataBeans;
    }

    @Override
    public FenleiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.fenlei_item,null);
        FenleiHolder fenleiHolder=new FenleiHolder(view);
        return fenleiHolder;
    }


    @Override
    public void onBindViewHolder(FenleiHolder holder, int position) {
      holder.fenlei_tv.setText(dataBeans.get(position).getName());
        /*Uri uri = Uri.parse(dataBeans.get(position).getIcon());
        holder.fenlei_img.setImageURI(uri);*/
        Glide.with(context).load(dataBeans.get(position).getIcon()).into(holder.fenlei_img);
    }


    @Override
    public int getItemCount() {
        return dataBeans.size();
    }
}
