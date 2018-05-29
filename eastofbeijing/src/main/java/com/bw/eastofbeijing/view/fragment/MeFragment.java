package com.bw.eastofbeijing.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.HoneBean;
import com.bw.eastofbeijing.persenter.ShouYeFragmentP;
import com.bw.eastofbeijing.utils.ChenJinUtil;
import com.bw.eastofbeijing.utils.CommonUtils;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.utils.GlideImgManager;
import com.bw.eastofbeijing.view.acvitity.DetailActivity;
import com.bw.eastofbeijing.view.acvitity.LoginActivity;
import com.bw.eastofbeijing.view.acvitity.OrderListActivity;
import com.bw.eastofbeijing.view.acvitity.UserSettingActivity;
import com.bw.eastofbeijing.view.adapter.TuiJianAdapter;
import com.bw.eastofbeijing.view.adapter.linter.OnItemListner;
import com.bw.eastofbeijing.view.iview.IHome;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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

public class MeFragment extends Fragment implements IHome,OnClickListener{
    @BindView(R.id.tui_jian_recycler)
    RecyclerView tui_jian_recycler;
    private ShouYeFragmentP shouYeFragmentP;
    @BindView(R.id.my_linear_login)
     LinearLayout my_linear_login;
    @BindView(R.id.my_user_icon)
     ImageView my_user_icon;
    @BindView(R.id.my_user_name)
     TextView my_user_name;
    @BindView(R.id.my_order_dfk)
     LinearLayout my_order_dfk;
    @BindView(R.id.my_order_dpj)
     LinearLayout my_order_dpj;
    @BindView(R.id.my_order_dsh)
     LinearLayout my_order_dsh;
    @BindView(R.id.my_order_th)
     LinearLayout my_order_th;
    @BindView(R.id.my_order_all)
     LinearLayout my_order_all;
    @BindView(R.id.fragment_my_scroll)
     ScrollView fragment_my_scroll;
    @BindView(R.id.login_back_pic)
     RelativeLayout login_back_pic;
    @BindView(R.id.smart_refresh)
     SmartRefreshLayout smart_refresh;
    private List<HoneBean.TuijianBean.ListBean> list=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.mefragment,container,false);
        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initChenJin();
        tui_jian_recycler.setFocusable(false);
        shouYeFragmentP = new ShouYeFragmentP(this);
        Map<String, String> map=new HashMap<>();
        shouYeFragmentP.getCartDataNet(Constants.homeUrl,map);
        //设置点击事件
        my_linear_login.setOnClickListener(this);
        my_order_dfk.setOnClickListener(this);
        my_order_dpj.setOnClickListener(this);
        my_order_dsh.setOnClickListener(this);
        my_order_th.setOnClickListener(this);

        smart_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //延时多久结束刷新
                smart_refresh.finishRefresh(3000);
            }
        });
        smart_refresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                smart_refresh.finishLoadmore(3000);
            }
        });
    }



    private void initChenJin() {
        ChenJinUtil.setStatusBarColor(getActivity(), getResources().getColor(R.color.colorPrimaryDark));
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
            TuiJianAdapter tuiJianAdapter = new TuiJianAdapter(getActivity(),list);
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

    @Override
    public void onFenLeiSuccess(ResponseBody responseBody) {

    }

    @Override
    public void getCartDataNull() {

    }

    @Override
    public void onResume() {
        super.onResume();

        //scrollView滚动到最上面
        //fragment_my_scroll.smoothScrollTo(0,0);

        //1.是否登录的一些操作
        initData();

    }

    /**
     * 只要对用户可见,,,就要调用
     */
    private void initData() {

        //判断一下是否登录..,..当登录成功之后,先存一下boolean值,,,在这里取出来判断
        boolean isLogin = CommonUtils.getBoolean("isLogin");

        if (isLogin) {
            if ( "".equals(CommonUtils.getString("iconUrl"))  || "null".equals(CommonUtils.getString("iconUrl"))){
                //显示默认头像
                my_user_icon.setImageResource(R.drawable.user);
            }else {

                //1.加载一下头像显示...判断一下是否有头像的路径,,,没有则显示默认的头像
                //Glide.with(getActivity()).load(CommonUtils.getString("iconUrl")).into(my_user_icon);

                //圆形
                GlideImgManager.glideLoader(getActivity(), CommonUtils.getString("iconUrl"), R.drawable.user, R.drawable.user, my_user_icon, 0);
            }
            //2.先试一下用户名
            my_user_name.setText(CommonUtils.getString("name"));

            //背景图片的切换
            login_back_pic.setBackgroundResource(R.drawable.reg_bg);
        }else {
            //显示默认头像
            my_user_icon.setImageResource(R.drawable.user);
            //用户名显示 登录/注册 >
            my_user_name.setText("登录/注册 >");

            //背景图片的切换
            login_back_pic.setBackgroundResource(R.drawable.normal_regbg);

        }

    }
    /**
     * 隐藏改变的回调...是否隐藏  true表示隐藏
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //Toast.makeText(getActivity(),hidden+"---",Toast.LENGTH_SHORT).show();

        if (! hidden) {//可见
            //scrollView滚动到最上面
            //fragment_my_scroll.smoothScrollTo(0,0);
            initChenJin();
            initData();
        }

    }



    @Override
    public void onClick(View view) {
        boolean isLogin = CommonUtils.getBoolean("isLogin");

        Intent intent = new Intent();

        if (! isLogin) {
            //跳转登录注册页面
            intent.setClass(getActivity(),LoginActivity.class);

        }else {
            switch (view.getId()) {

                case R.id.my_linear_login://跳转账户设置页面
                    intent.setClass(getActivity(), UserSettingActivity.class);

                    break;
                case R.id.my_order_dfk://我的订单...代付款
                    //可以传值过去...显示哪一个tablayout
                    intent.setClass(getActivity(), OrderListActivity.class);
                    intent.putExtra("flag",1);
                    break;
                case R.id.my_order_dpj://已支付

                    intent.setClass(getActivity(), OrderListActivity.class);
                    intent.putExtra("flag",2);

                    break;
                case R.id.my_order_dsh://已取消

                    intent.setClass(getActivity(), OrderListActivity.class);
                    intent.putExtra("flag",3);
                    break;
                case R.id.my_order_th://退化

                    intent.setClass(getActivity(), OrderListActivity.class);

                    break;

            }
        }

        //开启
        startActivity(intent);


    }


}
