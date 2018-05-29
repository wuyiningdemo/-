package com.bw.eastofbeijing.persenter;

import com.bw.eastofbeijing.model.UpLoadPicModel;
import com.bw.eastofbeijing.model.linear.IUpLoadLinter;
import com.bw.eastofbeijing.persenter.linter.IUpLoadPLinter;

import java.io.File;

import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/23.
 */

public class UpLoadPicPresenter extends BasePresenter implements IUpLoadPLinter {
    private  IUpLoadLinter iUpLoadLinter;
    private final UpLoadPicModel upLoadPicModel;

    public UpLoadPicPresenter(IUpLoadLinter iUpLoadLinter) {
        this.iUpLoadLinter=iUpLoadLinter;
        upLoadPicModel = new UpLoadPicModel(this);
    }

    public void uploadPic(String uploadUrl, File saveIconFile, String uid, String s) {
        upLoadPicModel.uploadPic(uploadUrl,saveIconFile,uid,s);

    }


    @Override
    public void uploadPicSuccess(ResponseBody responseBody) {
        iUpLoadLinter.uploadPicSuccess(responseBody);
    }
}
