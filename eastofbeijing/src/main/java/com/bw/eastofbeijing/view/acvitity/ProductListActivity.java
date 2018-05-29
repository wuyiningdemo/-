package com.bw.eastofbeijing.view.acvitity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.ProductListBean;
import com.bw.eastofbeijing.persenter.ProductListPresenter;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.view.adapter.ProDuctGridAdapter;
import com.bw.eastofbeijing.view.adapter.ProDuctListAdapter;
import com.bw.eastofbeijing.view.adapter.linter.OnItemListner;
import com.bw.eastofbeijing.view.iview.IHome;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;


public class ProductListActivity extends AppCompatActivity implements IHome,View.OnClickListener{

    private String keywords;
    @BindView(R.id.product_list_recycler)
     RecyclerView product_list_recycler;
    @BindView(R.id.product_grid_recycler)
     RecyclerView product_grid_recycler;
    private ProductListPresenter productListPresenter;
    private int page = 1;
    private ProDuctListAdapter proDuctListAdapter;
    private ProDuctGridAdapter proDuctGridAdapter;
    @BindView(R.id.product_image_back)
     ImageView product_image_back;
    @BindView(R.id.linear_search)
     LinearLayout linear_search;
    @BindView(R.id.image_change)
     ImageView image_change;
    private boolean isList = true;//是否是列表展示
    @BindView(R.id.refreshLayout)
     RefreshLayout refreshLayout;
    private List<ProductListBean.DataBean> listAll  = new ArrayList<>();//装当前页面所有的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);

        productListPresenter = new ProductListPresenter(this);

        //接收传递的关键词
        keywords = getIntent().getStringExtra("keywords");
        if (keywords != null) {
            //根据关键词和page去请求列表数据

            productListPresenter.getProductData(Constants.seartchUrl,keywords,page);

        }
        //设置列表布局
        product_list_recycler.setLayoutManager(new LinearLayoutManager(ProductListActivity.this));
        product_grid_recycler.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));


        //设置点击事件
        product_image_back.setOnClickListener(this);
        linear_search.setOnClickListener(this);
        image_change.setOnClickListener(this);

        //下拉刷新的监听
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                //集合清空
                listAll.clear();
                //重新获取数据
                productListPresenter.getProductData(Constants.seartchUrl,keywords,page);

            }
        });
        //上拉加载的监听
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page ++;
                //重新获取数据
                productListPresenter.getProductData(Constants.seartchUrl,keywords,page);

            }
        });

    }

    @Override
    public void onSuccess(ResponseBody responseBody) {
        try {
            String  json=responseBody.string();
            ProductListBean  productListBean=new Gson().fromJson(json,ProductListBean.class);


            //先把数据添加到大集合
            listAll.addAll(productListBean.getData());

            //设置适配器就可以了

            setAdapter();
            //条目的点击事件 调到详情页面
            proDuctListAdapter.setOnItemListner(new OnItemListner() {
                @Override
                public void onItemClickListner(int i) {
                    //跳转详情
                    Intent intent = new Intent(ProductListActivity.this,DetailActivity.class);
                    intent.putExtra("pid",listAll.get(i).getPid());
                    startActivity(intent);

                }

            });
            proDuctGridAdapter.setOnItemListner(new OnItemListner() {
                @Override
                public void onItemClickListner(int i) {
                    //跳转详情
                    Intent intent = new Intent(ProductListActivity.this, DetailActivity.class);
                    intent.putExtra("pid", listAll.get(i).getPid());
                    startActivity(intent);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(Throwable throwable) {
        Log.d("throwable",throwable.getMessage());
    }

    @Override
    public void getLoginSuccessByQQ(ResponseBody responseBody, String ni_cheng, String iconurl) {

    }

    @Override
    public void onFenLeiSuccess(ResponseBody responseBody) {

    }

    @Override
    public void getCartDataNull() {

    }

    /**
     * Called when a view has been clicked.
     *点击事件
     * @param
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_search:
                Intent  intent=new Intent(ProductListActivity.this,SuosouAcvitity.class);
                startActivity(intent);
                break;
            case R.id.product_image_back:
                finish();
                break;
            case R.id.image_change:

                if (isList) {//表示当前展示的是列表..图标变成列表样式...表格进行显示,列表隐藏...isList---false
                    image_change.setImageResource(R.drawable.kind_liner);

                    product_grid_recycler.setVisibility(View.VISIBLE);
                    product_list_recycler.setVisibility(View.GONE);

                    isList = false;
                }else {
                    image_change.setImageResource(R.drawable.kind_grid);

                    product_list_recycler.setVisibility(View.VISIBLE);
                    product_grid_recycler.setVisibility(View.GONE);

                    isList = true;
                }

                break;
        }
    }


    /**
     * 适配器
     */
    private void setAdapter() {

        //设置列表设备器
        if (proDuctListAdapter == null) {
            proDuctListAdapter = new ProDuctListAdapter(ProductListActivity.this, listAll);
            product_list_recycler.setAdapter(proDuctListAdapter);
        }else {
            proDuctListAdapter.notifyDataSetChanged();
        }

        //设置表格适配器
        if (proDuctGridAdapter == null) {
            proDuctGridAdapter = new ProDuctGridAdapter(ProductListActivity.this, listAll);
            product_grid_recycler.setAdapter(proDuctGridAdapter);
        }else {
            proDuctGridAdapter.notifyDataSetChanged();
        }

        //停止刷新和加载更多
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadmore();


    }

}

