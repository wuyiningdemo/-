package com.bw.eastofbeijing.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bw.eastofbeijing.utils.ChenJinUtil;
import com.bw.eastofbeijing.utils.CommonUtils;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.utils.custom.MyExpanableView;
import com.bw.eastofbeijing.view.acvitity.DetailActivity;
import com.bw.eastofbeijing.view.acvitity.LoginActivity;
import com.bw.eastofbeijing.view.acvitity.MakeSureOrderActivity;
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

/**
 * Created by 兰昊琼 on 2018/3/17.
 */

public class GuoWuCheFragment extends Fragment implements IHome, CartInlet,View.OnClickListener{
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
    private CartBean cartBean;
    private HoneBean honeBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.guowuchefragment,container,false);
        ButterKnife.bind(this,view);


        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initChenJin();


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
        text_buy.setOnClickListener(this);
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (! hidden) {
            initChenJin();
        }
        if (hidden) {
            Log.e("----","隐藏");


        }else {
            Log.e("----","显示");

            initData();

        }
    }

    private void initChenJin() {
        ChenJinUtil.setStatusBarColor(getActivity(), getResources().getColor(R.color.colorPrimaryDark));
    }
    @Override
    public void onResume() {
        //Log.e("----","获取到焦点");
        super.onResume();

        initData();

        //为你推荐的数据请求一次....其实为你推荐的数据也是随着登录和不登录改变


    }
    private void initData() {

        ChenJinUtil.setStatusBarColor(getActivity(), Color.parseColor("#484648"));

        //判断是否登录...没有,则显示登录按钮....已经登录显示购物车数据
        if (CommonUtils.getBoolean("isLogin")) {
            //请求购物车的数据...显示购物车
            my_expanable_view.setVisibility(View.VISIBLE);
            empty_cart_image.setVisibility(View.VISIBLE);
            linear_login.setVisibility(View.GONE);

            //请求购物车的数据
            getCartData();


        }else {
            //登录按钮的显示
            linear_login.setVisibility(View.VISIBLE);
            my_expanable_view.setVisibility(View.GONE);
            empty_cart_image.setVisibility(View.GONE);
            //登录的点击事件
            cart_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
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

    /**
     * Called when a view has been clicked.
     *点击事件
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cart_check_all://全选
                //点击全选的时候,,,,根据全选的状态 改变购物车所有商品的选中状态

                myExpanableAdapter.setAllChildChecked(cart_check_all.isChecked());
                break;
            case R.id.text_buy://去结算
                //当显示的是去结算(0)....打击没有任何反应
                if ("去结算(0)".equals(text_buy.getText().toString())) {
                    return;
                }

                //选中了商品之后,,,跳转到确认订单页面
                Intent intent = new Intent(getActivity(), MakeSureOrderActivity.class);
                //跳转传值...选中的商品的数据...传递一个集合过去...选中的商品的集合

                //先清空一下集合
                list_selected.clear();

                for (int i = 0;i<cartBean.getData().size();i++) {

                    List<CartBean.DataBean.ListBean> list = cartBean.getData().get(i).getList();
                    for (int j = 0;j<list.size();j++) {

                        //判断是否选中,,,如果选中存放到集合中去
                        if (list.get(j).getSelected() == 1) {
                            list_selected.add(list.get(j));
                        }
                    }
                }
                //通过intent把集合传到下一个页面
                intent.putExtra("list_selected",list_selected);

                startActivity(intent);
                break;
        }
    }

    /**
     * 获取购物车的数据
     * @param
     */

    @Override
    public void onCartScuess(final CartBean cartBean) {
         this.cartBean=cartBean;
            //进度条隐藏
            relative_progress.setVisibility(View.GONE);
            empty_cart_image.setVisibility(View.GONE);
            my_expanable_view.setVisibility(View.VISIBLE);

            //显示购物车的数据....二级列表设置适配器

            //1.根据某一组里面所有子孩子是否选中,决定当前组是否选中
            for (int i = 0; i< cartBean.getData().size(); i++) {

                //遍历每一组的数据,,,设置是否选中...有所有的子孩子决定
                CartBean.DataBean dataBean = cartBean.getData().get(i);
                dataBean.setGroupChecked(isAllChildInGroupChecked(dataBean.getList()));
            }
            //2.根据所有的组是否选中,,,决定全选是否选中
            cart_check_all.setChecked(isAllGroupChecked(cartBean));

            //3.
            myExpanableAdapter = new MyExpanableAdapter(getActivity(), cartBean,handler,relative_progress,fragmentCartPresenter);
            my_expanable_view.setAdapter(myExpanableAdapter);

            //展开所有的每一组
            for (int i = 0; i< cartBean.getData().size(); i++) {
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
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    //传递pid
                    intent.putExtra("pid", cartBean.getData().get(groupPosition).getList().get(childPosition).getPid());
                    startActivity(intent);

                    return false;
                }
            });
    }

    @Override
    public void onSuccess(ResponseBody responseBody) {
        try {
           String json= responseBody.string();
          final HoneBean honeBean=new Gson().fromJson(json,HoneBean.class);



            //瀑布流
            tui_jian_recycler.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
            //为你推荐设置适配器
            list = honeBean.getTuijian().getList();
            TuiJianAdapter tuiJianAdapter = new TuiJianAdapter(getActivity(), list);
            tui_jian_recycler.setAdapter(tuiJianAdapter);

            //为你推荐的点击事件
            tuiJianAdapter.setOnItemListner(new OnItemListner() {
                @Override
                public void onItemClickListner(int position) {

                    //跳转的是自己的详情页面
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    //传递pid
                    intent.putExtra("pid", honeBean.getTuijian().getList().get(position).getPid());
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


    @Override
    public void onFenLeiSuccess(ResponseBody responseBody) {


    }

    @Override
    public void getCartDataNull() {
        relative_progress.setVisibility(View.GONE);
        empty_cart_image.setVisibility(View.VISIBLE);
        my_expanable_view.setVisibility(View.GONE);
        Toast.makeText(getActivity(),"购物车为空,先去逛逛吧",Toast.LENGTH_SHORT).show();
    }
}
