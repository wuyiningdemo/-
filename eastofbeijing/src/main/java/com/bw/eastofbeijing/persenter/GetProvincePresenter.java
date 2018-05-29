package com.bw.eastofbeijing.persenter;

import android.content.Context;

import com.bw.eastofbeijing.model.GetProvinceModel;
import com.bw.eastofbeijing.model.bean.ProvinceBean;
import com.bw.eastofbeijing.model.linear.GetProvinceInter;
import com.bw.eastofbeijing.persenter.linter.GetProvincePresenterInter;

import java.util.List;

/**
 * Created by Dash on 2018/2/27.
 */
public class GetProvincePresenter implements GetProvincePresenterInter {

    private GetProvinceInter getProvinceInter;
    private GetProvinceModel getProvinceModel;

    public GetProvincePresenter(GetProvinceInter getProvinceInter) {
        this.getProvinceInter = getProvinceInter;
        getProvinceModel = new GetProvinceModel(this);
    }

    public void getProvince(Context context) {
        getProvinceModel.getProvince(context);
    }

    @Override
    public void onGetProvince(List<ProvinceBean> list) {
        getProvinceInter.onGetProvince(list);
    }
}
