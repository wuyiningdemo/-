package com.bw.eastofbeijing.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.FenLeiBean;
import com.bw.eastofbeijing.persenter.ShouYeFragmentP;
import com.bw.eastofbeijing.utils.ChenJinUtil;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.view.acvitity.CustomCaptrueActivity;
import com.bw.eastofbeijing.view.acvitity.SuosouAcvitity;
import com.bw.eastofbeijing.view.adapter.FenLeiLelftAdapter;
import com.bw.eastofbeijing.view.iview.IHome;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/17.
 */

public class FenLeiFragment extends Fragment implements IHome,View.OnClickListener{
    @BindView(R.id.fen_lei_list_view)
     ListView fen_lei_list_view;
    @BindView(R.id.fen_lei_frame)
     FrameLayout fen_lei_frame;
    @BindView(R.id.linear_layout)
    LinearLayout linear_layout;
    @BindView(R.id.sao_hei)
    ImageView sao_hei;
    private ShouYeFragmentP fragmentHomeP;

    private List<FenLeiBean.DataBean> dataBeans=new ArrayList<>();
    private FenLeiLelftAdapter fenLeiLelftAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fenleifragment,container,false);
        ButterKnife.bind(this,view);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initChenJin();
        //获取左边listView展示的数据
        fragmentHomeP = new ShouYeFragmentP(this);
       //获取分类的数据
        fragmentHomeP.getFenLeiData(Constants.fenLeiUrl);
        sao_hei.setOnClickListener(this);
        linear_layout.setOnClickListener(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        initChenJin();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (! hidden) {
            initChenJin();
        }
    }

    private void initChenJin() {
        ChenJinUtil.setStatusBarColor(getActivity(), getResources().getColor(R.color.colorPrimaryDark));
    }
    /**
     * 这个回调在fragment分类中没有用
     * @param
     */
    @Override
    public void onSuccess(ResponseBody responseBody) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void getLoginSuccessByQQ(ResponseBody responseBody, String ni_cheng, String iconurl) {

    }

    @Override
    public void onFenLeiSuccess(ResponseBody responseBody) {
        try {
            String json= responseBody.string();
            final FenLeiBean fenLeiBean=new Gson().fromJson(json,FenLeiBean.class);

            //给listView设置适配器
            fenLeiLelftAdapter = new FenLeiLelftAdapter(getActivity(), fenLeiBean);
            fen_lei_list_view.setAdapter(fenLeiLelftAdapter);

            //默认显示京东超市右边对应的数据
            FragmentFenLeiRight fragmentFenLeiRight = FragmentFenLeiRight.getInstance(fenLeiBean.getData().get(0).getCid());

            getChildFragmentManager().beginTransaction().replace(R.id.fen_lei_frame,fragmentFenLeiRight).commit();


            //条目点击事件
            fen_lei_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    //设置适配器当前位置的方法
                    fenLeiLelftAdapter.setCurPositon(i);
                    //刷新适配器...getView重新执行
                    fenLeiLelftAdapter.notifyDataSetChanged();
                    //滚动到指定的位置,,,第一个参数是滚动哪一个条目,,,滚动到哪个位置/偏移量
                    fen_lei_list_view.smoothScrollToPositionFromTop(i,(adapterView.getHeight()-view.getHeight())/2);

                    //使用fragment替换右边frameLayout....fragment之间的传值
                    FragmentFenLeiRight fragmentFenLeiRight = FragmentFenLeiRight.getInstance(fenLeiBean.getData().get(i).getCid());


                    getChildFragmentManager().beginTransaction().replace(R.id.fen_lei_frame,fragmentFenLeiRight).commit();


                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getCartDataNull() {

    }

    /**
     * Called when a view has been clicked.
     *点击跳转
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.linear_layout:
                Intent intent=new Intent(getActivity(), SuosouAcvitity.class);
                startActivity(intent);
                break;
            case  R.id.sao_hei:
                Intent intent2=new Intent(getActivity(), CustomCaptrueActivity.class);
                startActivity(intent2);
                break;
        }
    }
}






