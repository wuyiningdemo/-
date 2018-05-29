package com.bw.eastofbeijing.model.linear;

import okhttp3.ResponseBody;

/**
 * Created by 兰昊琼 on 2018/3/23.
 */

public interface IUpLoadLinter  {
   void uploadPicSuccess(ResponseBody responseBody);
   void unsubcribe();
}
