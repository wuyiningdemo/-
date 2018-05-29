package com.bw.eastofbeijing.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.FenLeiBean;
import com.bw.eastofbeijing.model.bean.HoneBean;
import com.bw.eastofbeijing.persenter.ShouYeFragmentP;
import com.bw.eastofbeijing.utils.ChenJinUtil;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.utils.GlideImageLoader;
import com.bw.eastofbeijing.utils.custom.ObservableScrollView;
import com.bw.eastofbeijing.view.acvitity.CustomCaptrueActivity;
import com.bw.eastofbeijing.view.acvitity.DetailActivity;
import com.bw.eastofbeijing.view.acvitity.SuosouAcvitity;
import com.bw.eastofbeijing.view.acvitity.WebViewActivity;
import com.bw.eastofbeijing.view.adapter.FenleiAdapter;
import com.bw.eastofbeijing.view.adapter.MiaoShaAdapter;
import com.bw.eastofbeijing.view.adapter.TuiJianAdapter;
import com.bw.eastofbeijing.view.adapter.linter.OnItemListner;
import com.bw.eastofbeijing.view.iview.IHome;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sunfusheng.marqueeview.MarqueeView;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

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

public class ShouYeFragment extends Fragment implements IHome {
    private final String TAG_MARGIN_ADDED = "marginAdded";
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.linear_include)
    LinearLayout linear_include;
    @BindView(R.id.observe_scroll_view)
    ObservableScrollView observe_scroll_view;
    @BindView(R.id.heng_xiang_recycler)
    RecyclerView heng_xiang;
    @BindView(R.id.tui_jian_recycler)
    RecyclerView tui_jian;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.miao_sha_recycler)
    RecyclerView miao_sha;
    @BindView(R.id.smart_refresh)
    SmartRefreshLayout smart_refresh;
    @BindView(R.id.sao_yi_sao)
    LinearLayout sao_yi_sao;
    @BindView(R.id.tvtime1)
    TextView tvtime1;
    @BindView(R.id.tvtime2)
    TextView tvtime2;
    @BindView(R.id.tvtime3)
    TextView tvtime3;
    private ShouYeFragmentP shouYeFragmentP;
    List<FenLeiBean.DataBean> dataBeans = new ArrayList<>();
    List<HoneBean.TuijianBean.ListBean> list = new ArrayList<>();
    @BindView(R.id.linear_suosou)
    LinearLayout linear_suosou;
    private  long  time=10000;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            String formatLongToTimeStr = formatLongToTimeStr(time);
            String[] split = formatLongToTimeStr.split("：");
            for (int i = 0; i < split.length; i++) {
                if(i==0){
                    tvtime1.setText(split[0]+"时");
                }
                if(i==1){
                    tvtime2.setText(split[1]+"分");
                }
                if(i==2){
                    tvtime3.setText(split[2]+"秒");
                }

            }
            if(time>0){
                handler.postDelayed(this, 1000);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shouyefragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ChenJinUtil.setStatusBarColor(getActivity(), Color.TRANSPARENT);
        handler.postDelayed(runnable, 1000);
        shouYeFragmentP = new ShouYeFragmentP(this);
        linear_suosou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SuosouAcvitity.class);
                startActivity(intent);

            }
        });

        initData();
        initBanner();
        //标题栏
        initTitleBar();
        //跑马灯
        initMarqueeView();
        smart_refresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                smart_refresh.finishLoadmore(2000);
            }
        });
        smart_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                smart_refresh.finishRefresh(2000);
            }
        });
        sao_yi_sao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomCaptrueActivity.class);
                startActivityForResult(intent, 1001);
            }
        });

    }

    private void initTitleBar() {

        //linearLayout在view绘制的时候外面包裹一层relativeLayout
        //应该尽量减少使用linearLayout...view优化

        addMargin();


        ViewTreeObserver viewTreeObserver = linear_include.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                linear_include.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                final int height = linear_include.getHeight();

                observe_scroll_view.setScrollViewListener(new ObservableScrollView.IScrollViewListener() {
                    @Override
                    public void onScrollChanged(int x, int y, int oldx, int oldy) {
                        if (y <= 0) {
                            addMargin();
                            ChenJinUtil.setStatusBarColor(getActivity(), Color.TRANSPARENT);

                            linear_include.setBackgroundColor(Color.argb((int) 0, 227, 29, 26));//AGB由相关工具获得，或
                        } else if (y > 0 && y < height) {

                            if (y > ChenJinUtil.getStatusBarHeight(getActivity())) {
                                //去掉margin
                                removeMargin();
                                //状态栏为灰色
                                ChenJinUtil.setStatusBarColor(getActivity(), getResources().getColor(R.color.colorPrimaryDark));
                            }

                            float scale = (float) y / height;
                            float alpha = (255 * scale);
                            // 只是layout背景透明(仿知乎滑动效果)
                            linear_include.setBackgroundColor(Color.argb((int) alpha, 227, 29, 26));
                        } else {
                            linear_include.setBackgroundColor(Color.argb((int) 255, 227, 29, 26));
                        }
                    }
                });
            }
        });

    }

    private void removeMargin() {
        if (TAG_MARGIN_ADDED.equals(linear_include.getTag())) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) linear_include.getLayoutParams();
            // 移除的间隔大小就是状态栏的高度
            params.topMargin -= ChenJinUtil.getStatusBarHeight(getActivity());
            linear_include.setLayoutParams(params);
            linear_include.setTag(null);
        }
    }

    private void addMargin() {
        //给标题调价margin
        if (!TAG_MARGIN_ADDED.equals(linear_include.getTag())) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) linear_include.getLayoutParams();
            // 添加的间隔大小就是状态栏的高度
            params.topMargin += ChenJinUtil.getStatusBarHeight(getActivity());
            linear_include.setLayoutParams(params);
            linear_include.setTag(TAG_MARGIN_ADDED);
        }
    }

    private void initMarqueeView() {
        List<String> info = new ArrayList<>();
        info.add("欢迎访问京东app");
        info.add("大家有没有在 听课");
        info.add("是不是还有人在睡觉");
        info.add("你妈妈在旁边看着呢");
        info.add("赶紧的好好学习吧 马上毕业了");
        info.add("你没有事件睡觉了");
        marqueeView.startWithList(info);
    }

    private void initBanner() {

        //设置banner样式...CIRCLE_INDICATOR_TITLE包含标题
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);

    }

    /**
     * 获取数据
     */
    public void initData() {
        Map<String, String> map = new HashMap<>();

        shouYeFragmentP.getCartDataNet(Constants.homeUrl, map);
        shouYeFragmentP.getFenLeiData(Constants.fenLeiUrl);


    }

    @Override
    public void onSuccess(final ResponseBody responseBody) {

        try {
            final String json = responseBody.string();
            final HoneBean honeBean = new Gson().fromJson(json, HoneBean.class);


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //秒杀


                    MiaoShaAdapter miaoShaAdapter = new MiaoShaAdapter(getActivity(), honeBean.getMiaosha());
                    miao_sha.setAdapter(miaoShaAdapter);
                    miao_sha.setLayoutManager(new LinearLayoutManager(getActivity(), OrientationHelper.HORIZONTAL, false));

                    //条目的点击事件

                    miaoShaAdapter.setOnItemListner(new OnItemListner() {
                        @Override
                        public void onItemClickListner(int i) {
                            //跳转到下一个页面....传值过去...webView页面
                            Intent intent = new Intent(getActivity(), WebViewActivity.class);

                            String detailUrl = honeBean.getMiaosha().getList().get(i).getDetailUrl();
                            intent.putExtra("detailUrl", detailUrl);
                            startActivity(intent);
                        }
                    });
                    //推荐
                    list = honeBean.getTuijian().getList();
                    //创建适配器
                    TuiJianAdapter tuiJianAdapter = new TuiJianAdapter(getActivity(), list);
                    tui_jian.setAdapter(tuiJianAdapter);
                    tui_jian.setLayoutManager(new GridLayoutManager(getActivity(), 2, OrientationHelper.VERTICAL, false));
                    //为你推荐的点击事件
                    tuiJianAdapter.setOnItemListner(new OnItemListner() {
                        @Override
                        public void onItemClickListner(int i) {
                            //跳转的是自己的详情页面
                            Intent intent = new Intent(getActivity(), DetailActivity.class);
                            //传递pid
                            intent.putExtra("pid", honeBean.getTuijian().getList().get(i).getPid());
                            startActivity(intent);
                        }
                    });

                    List<HoneBean.DataBean> data = honeBean.getData();
                    List<String> imageUrls = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        imageUrls.add(data.get(i).getIcon());
                    }

                    banner.setImages(imageUrls);

                    //banner的点击事件
                    banner.setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {

                            HoneBean.DataBean dataBean = honeBean.getData().get(position);
                            if (dataBean.getType() == 0) {
                                //跳转webView
                                Intent intent = new Intent(getActivity(), WebViewActivity.class);

                                intent.putExtra("detailUrl", dataBean.getUrl());
                                startActivity(intent);

                            } else {
                                Toast.makeText(getActivity(), "即将跳转商品详情", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                    banner.start();
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onError(Throwable throwable) {
        Log.d("----", throwable.getMessage());
    }

    @Override
    public void getLoginSuccessByQQ(ResponseBody responseBody, String ni_cheng, String iconurl) {

    }

    @Override
    public void onFenLeiSuccess(ResponseBody responseBody) {
        //分类
        ;
        try {
            final String json = responseBody.string();


            final FenLeiBean fenLeiBean = new Gson().fromJson(json, FenLeiBean.class);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dataBeans = fenLeiBean.getData();
                    //创建适配器
                    FenleiAdapter fenleiAdapter = new FenleiAdapter(getActivity(), dataBeans);
                    heng_xiang.setAdapter(fenleiAdapter);
                    heng_xiang.setLayoutManager(new GridLayoutManager(getActivity(), 2, OrientationHelper.HORIZONTAL, false));
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getCartDataNull() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shouYeFragmentP.unsubcribe();//解除订阅

        if (shouYeFragmentP.isViewAttached()) {//如果产生关联
            //解除关联
            shouYeFragmentP.detachView();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                //拿到传递回来的数据
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }

                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    //解析得到的结果
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (result.startsWith("http://")) {
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("detailUrl", result);
                        startActivity(intent);
                    } else {

                        Toast.makeText(getActivity(), "暂不支持此二维码", Toast.LENGTH_LONG).show();
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * 倒计时
     * @param l
     * @return
     */
    public  String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = l.intValue() ;
        if (second > 60) {
            minute = second / 60;         //取整
            second = second % 60;         //取余
        }

        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String strtime = hour+"："+minute+"："+second;
        return strtime;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
