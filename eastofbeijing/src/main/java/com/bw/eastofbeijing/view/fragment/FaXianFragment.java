package com.bw.eastofbeijing.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.VideioBean;
import com.bw.eastofbeijing.utils.ChenJinUtil;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.utils.RetrofitUtil;
import com.bw.eastofbeijing.view.adapter.VideoAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/17.
 */

public class FaXianFragment extends Fragment {
    @BindView(R.id.faxian_view)
    RecyclerView faxianView;
    Unbinder unbinder;
    private List<VideioBean.DataBean> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.faxianfragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initChenJin();

        Map<String, String> map = new HashMap<>();
        map.put("uid", "2");
        map.put("type", "1");
        map.put("page", "1");

        map.put("appVersion", "101");
        RetrofitUtil.getService().doGet(Constants.faxianUrl, map)
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
                            VideioBean videioBean = new Gson().fromJson(json, VideioBean.class);
                            data = videioBean.getData();
                            faxianView.setLayoutManager(new LinearLayoutManager(getActivity()));

                            VideoAdapter videoAdapter = new VideoAdapter(getActivity(),data);

                                faxianView.setAdapter(videoAdapter);



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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            initChenJin();
        }
    }

    private void initChenJin() {
        ChenJinUtil.setStatusBarColor(getActivity(), getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
