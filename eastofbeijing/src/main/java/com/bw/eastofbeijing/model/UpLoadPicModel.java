package com.bw.eastofbeijing.model;

import com.bw.eastofbeijing.model.linear.BaseUpModel;
import com.bw.eastofbeijing.persenter.linter.IUpLoadPLinter;
import com.bw.eastofbeijing.utils.RetrofitUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/23.
 */

public class UpLoadPicModel implements BaseUpModel{
    private  IUpLoadPLinter iUpLoadPLinter;
    private Disposable d;

    public UpLoadPicModel(IUpLoadPLinter iUpLoadPLinter) {
        this.iUpLoadPLinter=iUpLoadPLinter;
    }

    public void uploadPic(String uploadUrl, File saveIconFile, String uid, String s) {
        Map<String, String> map=new HashMap<>();
        map.put("file", String.valueOf(saveIconFile));
        map.put("uid",uid);
        map.put("s",s);
        RetrofitUtil.getService().doPost(uploadUrl,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                          UpLoadPicModel .this.d=d;
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        iUpLoadPLinter.uploadPicSuccess(responseBody);
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
    public void unsubcribe() {
      d.dispose();
    }
}
