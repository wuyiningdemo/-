package com.bw.eastofbeijing.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bw.eastofbeijing.R;

import java.util.List;


public class MylistAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;

    public MylistAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vie = null;
        if(convertView == null){
            vie = new ViewHolder();
            convertView = View.inflate(context, R.layout.itemlist,null);
            vie.litev = (TextView) convertView.findViewById(R.id.litev);
            convertView.setTag(vie);
        }else{
            vie = (ViewHolder) convertView.getTag();
        }
        vie.litev.setText(list.get(position));
        return convertView;
    }

    static class ViewHolder{
        TextView litev;
    }
}
