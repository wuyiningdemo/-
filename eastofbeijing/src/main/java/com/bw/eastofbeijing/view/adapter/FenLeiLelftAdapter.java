package com.bw.eastofbeijing.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.FenLeiBean;

/**
 * Created by 兰昊琼 on 2018/3/24.
 */

public class FenLeiLelftAdapter extends BaseAdapter {
    private final FenLeiBean fenLeiBean;
    private Context context;

    private int position;

    public FenLeiLelftAdapter(Context context, FenLeiBean fenLeiBean) {
        this.context = context;
        this.fenLeiBean = fenLeiBean;
    }

    @Override
    public int getCount() {
        return fenLeiBean.getData().size();
    }


    @Override
    public Object getItem(int i) {
        return fenLeiBean.getData().get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.fen_lei_item_layout,null);
            holder = new ViewHolder();

            holder.textView = convertView.findViewById(R.id.fen_lei_item_text);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        FenLeiBean.DataBean dataBean = fenLeiBean.getData().get(i);
        holder.textView.setText(dataBean.getName());

        //判断
        if (position == i) {
            //设置灰色的背景 和红色文字
            convertView.setBackgroundColor(Color.TRANSPARENT);
            holder.textView.setTextColor(Color.RED);
        }else {
            //白色的背景和黑色的文字
            convertView.setBackgroundColor(Color.WHITE);
            holder.textView.setTextColor(Color.BLACK);
        }
        return convertView;
    }
    /**
     * 设置的是点击了条目的位置
     * @param position
     */
    public void setCurPositon(int position) {
        this.position = position;
    }

    private class ViewHolder{
        TextView textView;
    }
}

