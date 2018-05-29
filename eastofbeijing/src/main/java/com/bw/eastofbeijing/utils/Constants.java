package com.bw.eastofbeijing.utils;

/**
 * Created by 兰昊琼 on 2018/3/20.
 */

public class Constants {
    public  static  String  baseUrl="https://www.zhaoapi.cn/";
    //首页
    public  static   String  homeUrl="ad/getAd";
    //分类
    public  static   String  fenLeiUrl="product/getCatagory";
    //商品详情
    public  static   String  detailUrl="product/getProductDetail";
    //添加购物车
    public  static   String  addCartUrl="product/addCart";
    //登录
    public  static   String  loginUrl="user/login";
    //注册
    public  static  String   registUrl="user/reg";
    //用户信息
    public  static   String   userInfoUrl="user/getUserInfo";
    //上传头像
    public  static   String    uploadUrl="file/upload";
    //子分类
    public  static   String    childFenLeiUrl="product/getProductCatagory";
    //搜索
    public  static   String     seartchUrl="product/searchProducts";
    //查询购物车
    public  static   String     selectCartUrl="product/getCarts";

    //更新购物车 product/updateCarts?uid=71&sellerid=1&pid=1&selected=0&num=10
    public static  String UPDATE_CART_URL = "product/updateCarts";
    //删除购物车...product/deleteCart?uid=72&pid=1
    public static  String  DELETE_CART_URL = "product/deleteCart";
    //设置默认地址...https://www.zhaoapi.cn/user/setAddr?uid=71&addrid=3&status=1
    public static  String SET_DEFAULT_ADDR_URL ="user/setAddr";
    //获取地址列表...https://www.zhaoapi.cn/user/getAddrs?uid=71
    public static final String GET_ALL_ADDR_URL ="user/getAddrs";
    //查询默认地址...https://www.zhaoapi.cn/user/getDefaultAddr?uid=71
    public static final String GET_DEFAULT_ADDR_URL ="user/getDefaultAddr";
    //创建订单...https://www.zhaoapi.cn/product/createOrder
    public static final String CREATE_ORDER_URL = "product/createOrder";
    //订单信息....https://www.zhaoapi.cn/product/getOrders?uid=71
    public static final String ORDER_LIST_URL = "product/getOrders";
    //修改订单状态...https://www.zhaoapi.cn/product/updateOrder?uid=71&status=1&orderId=1
    public static final String UPDATE_ORDER_URL = "product/updateOrder";
    //添加新地址
    public  static   String  ADD_NEW_ADDR_URL="user/addAddr";
    public  static  String   faxianUrl="quarter/getVideos";
}
