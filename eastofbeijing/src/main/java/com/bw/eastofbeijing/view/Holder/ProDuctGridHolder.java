package com.bw.eastofbeijing.view.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.eastofbeijing.R;


/**
 * Created by Dash on 2018/1/26.
 */
public class ProDuctGridHolder extends RecyclerView.ViewHolder {

    public ImageView product_list_image;
    public TextView product_list_title;
    public TextView product_list_price;

    public ProDuctGridHolder(View itemView) {
        super(itemView);

        product_list_image = itemView.findViewById(R.id.product_list_image);
        product_list_title = itemView.findViewById(R.id.product_list_title);
        product_list_price = itemView.findViewById(R.id.product_list_price);

    }



}
