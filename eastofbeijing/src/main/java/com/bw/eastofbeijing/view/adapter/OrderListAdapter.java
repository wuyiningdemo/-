package com.bw.eastofbeijing.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.OrderListBean;
import com.bw.eastofbeijing.model.bean.UpLoadPicBean;
import com.bw.eastofbeijing.utils.CommonUtils;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.utils.RetrofitUtil;
import com.bw.eastofbeijing.view.Holder.OrderListHolder;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Dash on 2018/2/25.
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListHolder>{
    private List<OrderListBean.DataBean> listAll;
    private Context context;

    public OrderListAdapter(Context context, List<OrderListBean.DataBean> listAll) {
        this.context = context;
        this.listAll = listAll;
    }

    @Override
    public OrderListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.order_item_layout,null);
        OrderListHolder orderListHolder = new OrderListHolder(view);
        return orderListHolder;
    }

    @Override
    public void onBindViewHolder(final OrderListHolder holder, int position) {
        final OrderListBean.DataBean dataBean = listAll.get(position);
        holder.text_title.setText(dataBean.getTitle());
        holder.text_price.setText("价格:"+dataBean.getPrice());

        //0 待支付1 已支付2 已取消
        int status = dataBean.getStatus();
        if (status == 0) {
            holder.text_flag.setText("待支付");
            holder.order_button.setText("取消订单");
        }else if (status == 1) {
            holder.text_flag.setText("已支付");
            holder.order_button.setText("查看订单");
        }else if (status == 2) {
            holder.text_flag.setText("已取消");
            holder.order_button.setText("查看订单");
        }

        holder.text_time.setText("创建时间:"+dataBean.getCreatetime());

        //点击事件
        holder.order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示的是 取消订单...取消成功后 显示查看订单,,,flag显示已取消
                if ("取消订单".equals(holder.order_button.getText().toString())) {
                    //弹出对话框
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("提示")
                            .setMessage("确定取消订单吗?")
                            .setNegativeButton("否",null)
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //请求取消订单的接口,,,成功之后 文字显示改变
                                    Map<String, String> params = new HashMap<>();
                                    params.put("uid", CommonUtils.getString("uid"));
                                    params.put("status", String.valueOf(2));
                                    params.put("orderId", String.valueOf(dataBean.getOrderid()));

                                    RetrofitUtil.getService().doPost(Constants.UPDATE_ORDER_URL, params)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Observer<ResponseBody>() {
                                                @Override
                                                public void onSubscribe(Disposable d) {

                                                }

                                                @Override
                                                public void onNext(ResponseBody responseBody) {
                                                    try {
                                                        String json = responseBody.string();
                                                        UpLoadPicBean updateOrderBean=new Gson().fromJson(json,UpLoadPicBean.class);
                                                        if ("0".equals(updateOrderBean.getCode())) {
                                                            holder.text_flag.setText("已取消");
                                                            holder.order_button.setText("查看订单");
                                                        }
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onError(Throwable e) {

                                                }

                                                @Override
                                                public void onComplete() {

                                                }
                                            });
                                   }
                            })
                            .show();

                }else {
                    //如果显示查看订单...去查看订单的信息...吐司
                    Toast.makeText(context,"即将跳转查看订单",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return listAll.size();
    }
}
