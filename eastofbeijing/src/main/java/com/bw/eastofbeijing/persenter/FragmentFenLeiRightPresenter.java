package com.bw.eastofbeijing.persenter;


import com.bw.eastofbeijing.model.FragmentFenLeiRightModel;
import com.bw.eastofbeijing.model.linear.FenLeiRightInter;
import com.bw.eastofbeijing.persenter.linter.FenLeiRightPresenterInter;

import okhttp3.ResponseBody;

/**
 * Created by Dash on 2018/1/25.
 */
public class FragmentFenLeiRightPresenter implements FenLeiRightPresenterInter {

    private FenLeiRightInter fenLeiRightInter;
    private FragmentFenLeiRightModel fragmentFenLeiRightModel;

    public FragmentFenLeiRightPresenter(FenLeiRightInter fenLeiRightInter) {
        this.fenLeiRightInter = fenLeiRightInter;

        fragmentFenLeiRightModel = new FragmentFenLeiRightModel(this);
    }

    public void getChildData(String childFenLeiUrl, int cid) {

        fragmentFenLeiRightModel.getChildData(childFenLeiUrl,cid);
    }

    @Override
    public void onSuncess(ResponseBody responseBody) {

        fenLeiRightInter.getSuccessChildData(responseBody);

    }
}
