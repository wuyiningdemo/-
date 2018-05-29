package com.bw.eastofbeijing.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.VideioBean;
import com.bw.eastofbeijing.view.Holder.VideoHolder;

import java.io.File;
import java.util.List;

/**
 * Created by 兰昊琼 on 2018/4/25.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoHolder> {
    private Context context;
    private List<VideioBean.DataBean> list;

    public VideoAdapter(Context context, List<VideioBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.video_faxian_item, null);
        VideoHolder videoHolder = new VideoHolder(view);
        return videoHolder;
    }


    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
      holder.imgTouxiang.setImageURI(list.get(position).getUser().getIcon());
        String nickname = String.valueOf(list.get(position).getUser().getNickname());
        holder.tvName.setText(nickname);
        String cover = list.get(position).getCover();

        Glide.with(context).load(Uri.fromFile( new File( cover) )).into(holder.coverSimleview);
        String videoUrl = list.get(position).getVideoUrl();
        Glide.with(context).load(Uri.fromFile( new File( videoUrl) )).into(holder.videoUrlSimleview);


        holder.txetTime.setText(list.get(position).getCreateTime());
        holder.workDesc.setText(list.get(position).getWorkDesc());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


}
