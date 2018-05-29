package com.bw.eastofbeijing.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.OrderListBean;
import com.bw.eastofbeijing.model.linear.FragmentOrderListInter;
import com.bw.eastofbeijing.persenter.OrderListPresenter;
import com.bw.eastofbeijing.utils.CommonUtils;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.view.adapter.OrderListAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

/**
 * Created by Dash on 2018/2/25.
 */
public class FragmentAlreadyPayOrder extends Fragment implements FragmentOrderListInter {
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private OrderListPresenter orderListPresenter;
    //分页
    private int page = 1;
    //大集合
    private List<OrderListBean.DataBean> listAll = new ArrayList<>();
    private OrderListAdapter orderListAdapter;
    private RelativeLayout relative_empty_order;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_all_layout,container,false);
        recyclerView = view.findViewById(R.id.recycler_order);
        smartRefreshLayout = view.findViewById(R.id.smart_refresh);
        relative_empty_order = view.findViewById(R.id.relative_empty_order);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //获取订单列表的数据
        orderListPresenter = new OrderListPresenter((com.bw.eastofbeijing.model.linear.FragmentOrderListInter) this);
        orderListPresenter.getOrderData(Constants.ORDER_LIST_URL, CommonUtils.getString("uid"),page);

    }


    @Override
    public void onOrderDataSuccess(ResponseBody responseBody) {
        try {
            String  json=responseBody.string();
            OrderListBean orderListBean=new Gson().fromJson(json,OrderListBean.class);
            if ("0".equals(orderListBean.getCode())) {
                //添加到大集合
                for (int i=0;i<orderListBean.getData().size();i++) {
                    if (orderListBean.getData().get(i).getStatus() == 1) {
                        listAll.add(orderListBean.getData().get(i));
                    }
                }

                if (listAll.size() == 0) {
                    relative_empty_order.setVisibility(View.VISIBLE);
                    smartRefreshLayout.setVisibility(View.GONE);
                }else {
                    relative_empty_order.setVisibility(View.GONE);
                    smartRefreshLayout.setVisibility(View.VISIBLE);
                }
                //设置适配器
                setAdapter();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {

        if (orderListAdapter == null) {
            orderListAdapter = new OrderListAdapter(getActivity(), listAll);
            recyclerView.setAdapter(orderListAdapter);
        }else {
            orderListAdapter.notifyDataSetChanged();
        }


    }


}
