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

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.ChildFenLeiBean;
import com.bw.eastofbeijing.model.linear.FenLeiRightInter;
import com.bw.eastofbeijing.persenter.FragmentFenLeiRightPresenter;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.view.adapter.FenLeiRecyclerOutAdapter;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;


/**
 * Created by Dash on 2018/1/25.
 */
public class FragmentFenLeiRight extends Fragment implements FenLeiRightInter {

    private RecyclerView fen_lei_recycler_out;
    private FragmentFenLeiRightPresenter fragmentFenLeiRightPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fen_lei_right_layout,container,false);

        fen_lei_recycler_out = view.findViewById(R.id.fen_lei_recycler_out);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragmentFenLeiRightPresenter = new FragmentFenLeiRightPresenter(this);

        //获取传递的cid
        int cid = getArguments().getInt("cid", -1);
        if (cid != -1) {
            //获取展示的数据
            fragmentFenLeiRightPresenter.getChildData(Constants.childFenLeiUrl,cid);
        }

    }

    public static FragmentFenLeiRight getInstance(int cid) {
        FragmentFenLeiRight fragmentFenLeiRight = new FragmentFenLeiRight();

        //传值
        Bundle bundle = new Bundle();
        bundle.putInt("cid",cid);
        fragmentFenLeiRight.setArguments(bundle);

        return fragmentFenLeiRight;
    }

    @Override
    public void getSuccessChildData(ResponseBody responseBody) {
        try {
            String   json= responseBody.string();
            ChildFenLeiBean childFenLeiBean=new Gson().fromJson(json,ChildFenLeiBean.class);


            fen_lei_recycler_out.setLayoutManager(new LinearLayoutManager(getActivity()));
            //设置适配器
            FenLeiRecyclerOutAdapter fenLeiRecyclerOutAdapter = new FenLeiRecyclerOutAdapter(getActivity(), childFenLeiBean);
            fen_lei_recycler_out.setAdapter(fenLeiRecyclerOutAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
