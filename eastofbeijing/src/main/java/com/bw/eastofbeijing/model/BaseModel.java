package com.bw.eastofbeijing.model;

import java.util.Map;

/**
 * Created by Dash on 2018/3/14.
 */
public interface BaseModel {
    void getCartDataNet(String url, Map<String, String> map);
    void unsubcribe();

     void getFenLeiData(String url);
}
