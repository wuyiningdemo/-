package com.bw.eastofbeijing.view.acvitity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.CartBean;
import com.bw.eastofbeijing.model.bean.CountPriceBean;
import com.bw.eastofbeijing.model.bean.HoneBean;
import com.bw.eastofbeijing.persenter.FragmentCartPresenter;
import com.bw.eastofbeijing.persenter.ShouYeFragmentP;
import com.bw.eastofbeijing.persenter.linter.CartInlet;
import com.bw.eastofbeijing.utils.CommonUtils;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.utils.custom.MyExpanableView;
import com.bw.eastofbeijing.view.adapter.MyExpanableAdapter;
import com.bw.eastofbeijing.view.adapter.TuiJianAdapter;
import com.bw.eastofbeijing.view.adapter.linter.OnItemListner;
import com.bw.eastofbeijing.view.iview.IHome;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class CartActivity extends AppCompatActivity implements IHome,View.OnClickListener,CartInlet{
    @BindView(R.id.linear_login)
     LinearLayout linear_login;
    @BindView(R.id.cart_login)
     Button cart_login;
    @BindView(R.id.tui_jian_recycler)
     RecyclerView tui_jian_recycler;
    @BindView(R.id.my_expanable_view)
     MyExpanableView my_expanable_view;

    private ShouYeFragmentP fragmentHomeP;
    @BindView(R.id.relative_progress)
     RelativeLayout relative_progress;
    private FragmentCartPresenter fragmentCartPresenter;
    @BindView(R.id.cart_check_all)
     CheckBox cart_check_all;
    @BindView(R.id.text_total)
     TextView text_total;
    @BindView(R.id.text_buy)
     TextView text_buy;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                CountPriceBean countPriceBean = (CountPriceBean) msg.obj;

                //设置显示
                text_total.setText("合计:¥"+countPriceBean.getPriceString());
                text_buy.setText("去结算("+countPriceBean.getCount()+")");
            }
        }
    };
    private MyExpanableAdapter myExpanableAdapter;
    @BindView(R.id.empty_cart_image)
     ImageView empty_cart_image;
    @BindView(R.id.detail_image_back)
     ImageView detail_image_back;
    private List<HoneBean.TuijianBean.ListBean> list=new ArrayList<>();
    //创建一个集合,,,装的是去结算的时候 选中的所有商品的数据
    private ArrayList<CartBean.DataBean.ListBean> list_selected = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guowuchefragment);
        ButterKnife.bind(this);
        detail_image_back.setVisibility(View.VISIBLE);

        //去掉默认的指示器
        my_expanable_view.setGroupIndicator(null);

        //失去焦点....界面不是从recyclerView开始显示
        tui_jian_recycler.setFocusable(false);

        //2.为你推荐,,,只需要获取一次
        fragmentHomeP = new ShouYeFragmentP(this);
        //调用p层里面的方法....https://www.zhaoapi.cn/ad/getAd
        Map<String, String> map=new HashMap<>();
        fragmentHomeP.getCartDataNet(Constants.homeUrl,map);

        fragmentCartPresenter = new FragmentCartPresenter(this);


        //全选 设置点击事件
        cart_check_all.setOnClickListener(this);
        detail_image_back.setOnClickListener(this);

    }

    /**
     * Called when a view has been clicked.
     *点击事件
     * @param
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cart_check_all://全选
                //点击全选的时候,,,,根据全选的状态 改变购物车所有商品的选中状态

                myExpanableAdapter.setAllChildChecked(cart_check_all.isChecked());
                break;
            case R.id.detail_image_back:

                finish();
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        initData();

        //为你推荐的数据请求一次....其实为你推荐的数据也是随着登录和不登录改变
    }
    private void initData() {
        //判断是否登录...没有,则显示登录按钮....已经登录显示购物车数据
        if (CommonUtils.getBoolean("isLogin")) {
            //请求购物车的数据...显示购物车
            my_expanable_view.setVisibility(View.VISIBLE);
            linear_login.setVisibility(View.GONE);

            //请求购物车的数据
            getCartData();


        }else {
            //登录按钮的显示
            linear_login.setVisibility(View.VISIBLE);
            my_expanable_view.setVisibility(View.GONE);

            //登录的点击事件
            cart_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CartActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });

        }

    }
    /**
     * 获取购物车数据
     */
    private void getCartData() {
        //显示进度条
        relative_progress.setVisibility(View.VISIBLE);

        //查询购物车的数据
        fragmentCartPresenter.getCartData(Constants.selectCartUrl,CommonUtils.getString("uid"));

    }
    @Override
    public void getCartDataNull() {
        relative_progress.setVisibility(View.GONE);
        empty_cart_image.setVisibility(View.VISIBLE);
        Toast.makeText(CartActivity.this,"购物车为空,先去逛逛吧",Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取推荐的数据
     * @param responseBody
     */
    @Override
    public void onSuccess(ResponseBody responseBody) {
        try {
            String json= responseBody.string();
            final HoneBean honeBean=new Gson().fromJson(json,HoneBean.class);
            //瀑布流
            tui_jian_recycler.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
            //为你推荐设置适配器
            list = honeBean.getTuijian().getList();
            TuiJianAdapter tuiJianAdapter = new TuiJianAdapter(CartActivity.this, list);
            tui_jian_recycler.setAdapter(tuiJianAdapter);

            //为你推荐的点击事件
            tuiJianAdapter.setOnItemListner(new OnItemListner() {
                @Override
                public void onItemClickListner(int position) {

                    //跳转的是自己的详情页面
                    Intent intent = new Intent(CartActivity.this, DetailActivity.class);
                    //传递pid
                    intent.putExtra("pid", honeBean.getTuijian().getList().get(position).getPid());
                    startActivity(intent);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取购物车的数据
     * @param
     */
    @Override
    public void onCartScuess(final CartBean cartBean) {

            //进度条隐藏
            relative_progress.setVisibility(View.GONE);
            empty_cart_image.setVisibility(View.GONE);

            //显示购物车的数据....二级列表设置适配器

            //1.根据某一组里面所有子孩子是否选中,决定当前组是否选中
            for (int i =0;i<cartBean.getData().size();i++) {

                //遍历每一组的数据,,,设置是否选中...有所有的子孩子决定
                CartBean.DataBean dataBean = cartBean.getData().get(i);
                dataBean.setGroupChecked(isAllChildInGroupChecked(dataBean.getList()));
            }

            //2.根据所有的组是否选中,,,决定全选是否选中
            cart_check_all.setChecked(isAllGroupChecked(cartBean));

            //3.
            myExpanableAdapter = new MyExpanableAdapter(CartActivity.this, cartBean,handler,relative_progress,fragmentCartPresenter);
            my_expanable_view.setAdapter(myExpanableAdapter);

            //展开所有的每一组
            for (int i = 0;i<cartBean.getData().size();i++) {
                my_expanable_view.expandGroup(i);
            }

            //4.计算商品的总价和数量
            myExpanableAdapter.sendPriceAndCount();

            //子条目的点击事件
            my_expanable_view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {

                    //Toast.makeText(getActivity(),"点击了",Toast.LENGTH_SHORT).show();

                    //跳转的是自己的详情页面
                    Intent intent = new Intent(CartActivity.this, DetailActivity.class);
                    //传递pid
                    intent.putExtra("pid",cartBean.getData().get(groupPosition).getList().get(childPosition).getPid());
                    startActivity(intent);

                    return false;
                }
            });


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

    /**
     * 所有的商家是否选中
     * @param cartBean
     * @return
     */
    private boolean isAllGroupChecked(CartBean cartBean) {


        for (int i=0;i<cartBean.getData().size();i++){
            //只要有一个组未选中 返回false
            if (! cartBean.getData().get(i).isGroupChecked()){
                return false;
            }
        }


        return true;
    }
    /**
     * 当前组中所有的孩子是否选中
     * @param listBeans 当前组中所有的孩子的数据
     * @return
     */
    private boolean isAllChildInGroupChecked(List<CartBean.DataBean.ListBean> listBeans) {


        for (int i=0;i<listBeans.size();i++){
            if (listBeans.get(i).getSelected() == 0){
                return false;
            }
        }


        return true;
    }


}
