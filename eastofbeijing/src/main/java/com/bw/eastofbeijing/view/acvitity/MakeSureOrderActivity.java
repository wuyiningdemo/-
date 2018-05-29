package com.bw.eastofbeijing.view.acvitity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.CartBean;
import com.bw.eastofbeijing.model.bean.CreateOrderBean;
import com.bw.eastofbeijing.model.bean.DefaultAddrBean;
import com.bw.eastofbeijing.model.bean.GetAllAddrBean;
import com.bw.eastofbeijing.model.linear.GetDefaultAddrInletr;
import com.bw.eastofbeijing.persenter.CreateOrderPresenter;
import com.bw.eastofbeijing.persenter.GetDefaultAddrPresenter;
import com.bw.eastofbeijing.persenter.linter.ICreateOrderLinter;
import com.bw.eastofbeijing.utils.CommonUtils;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.utils.RetrofitUtil;
import com.bw.eastofbeijing.utils.alipay.AlipayUtil;
import com.bw.eastofbeijing.utils.alipay.PayResult;
import com.bw.eastofbeijing.view.adapter.SureOrderAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MakeSureOrderActivity extends AppCompatActivity implements View.OnClickListener,GetDefaultAddrInletr,ICreateOrderLinter{
    private ArrayList<CartBean.DataBean.ListBean> list_selected;
    @BindView(R.id.detail_image_back)
     ImageView detail_image_back;
    @BindView(R.id.product_list_recycler)
     RecyclerView product_list_recycler;
    @BindView(R.id.text_shi_fu_ku)
     TextView text_shi_fu_kuan;
    @BindView(R.id.text_submit_order)
     TextView text_submit_order;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private double price;
    private CreateOrderPresenter createOrderPresenter;
    private int index;
    private GetDefaultAddrPresenter getDefaultAddrPresenter;
    @BindView(R.id.text_name)
     TextView text_name;
    @BindView(R.id.text_phone)
     TextView text_phone;
    @BindView(R.id.text_addr)
     TextView text_addr;
    @BindView(R.id.relative_addr_01)
     RelativeLayout relative_addr;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    //使用返回的支付结果字符串构建一个支付结果对象
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    //----------app端拿到支付宝同步返回的结果,需要发送给服务器端,服务器端经过验证签名和解析支付结果,然后给app端返回最终支付的结果

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(MakeSureOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(MakeSureOrderActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(MakeSureOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }

                    /**
                     * 不管订单支付成功或者失败 订单是已经生成了 如果成功 跳转订单列表的已支付,,,如果失败跳转的是订单列表的待支付
                     */

                    break;
                }
                default:
                    break;
            }
        }

    };


    /**
     * 真是开发过程中 这些参数是都不能出现在代码中的 尤其是私钥 放在服务器端
     */
    private final String PARTNER = "2088221871911835"; //Pid
    private final String SELLER = "zhuangshiyigou@163.com";// 商户收款账号
    // 商户私钥，pkcs8格式
    private final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALmhW0c+ZO7sB3utNmkQu5hkpqnw+nAPLCdBoPEw+E+7qcOZBdXxwZsG05yr2BYp5j0MTE0FpKRu+uNTVTAX/MzhcInAColtnWkF1R8rsIdlLdleRoMcM3Lo4XesctdsyxLeummrFLKQfASMqS64kkTUoEFGb3tlYZs7iSFgpM1pAgMBAAECgYAGm6zhK2JycuqNR4xBTzwuX57jO9XeeVvMBfURwPmF9RtFAESJ6jJHL4YG9MMbfuBYWgC5WTMUO3Mo9oV40dHI9dPwANL5aPeKEIUawoQyuCyY/js84fOY3+TbEnXym8G6+Zxm+bGKQn3ZZqlSgR3CHk5f/CceoXvPWDLwl//RtQJBANzDaQTeckTUEtxVyZ9vM26Sv+TKULvqf8OHdrGm7WOWY6/CbChrxMKHxkdrNwZPNIhozNfTaOmnJaPqeMKo8SMCQQDXQmEPHj0h4Qjn3D6n6WiNOvUsNbzVpLP/TgwHFYkRLcz+GPRkbXXdvkSUKxNo7vwZr8vwTIquYA+K3CFTpr4DAkEAu3Ox2NCJdqgc27p8WUSzB1DUYBDqPKYBlqWPw4laSRWJz9Pmwuu/Ru7DDiGbt1/J24ohZaG9k6i57VVK9P8+wQJBAIgGtFrfWvY7xGrwbM+i2aTVqvTDCI9hQzWEVmlrnHA0pyOzFU0ZNrBneeK/zcYzry90PcWeOMy0e13eeVjpN40CQQCMBjVBeTdQ9afgGnBR6glIWrCtqTBAsUIr3gvNZYWdaznr0FmG2pjLwDLUsx0SUCpcrTQxhWu16HDEyQuCm7Ar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_sure_order);
        ButterKnife.bind(this);
        //获取选中的购物车数据的集合
        list_selected = (ArrayList<CartBean.DataBean.ListBean>) getIntent().getSerializableExtra("list_selected");

        //获取默认的地址...有默认地址则显示,,,没有则弹出一个dialog
        getDefaultAddrPresenter = new GetDefaultAddrPresenter(this);

        getDefaultAddrPresenter.getDefaultAddr(Constants.GET_DEFAULT_ADDR_URL, CommonUtils.getString("uid"));
      initData();
    }
    private void initData() {
        createOrderPresenter = new CreateOrderPresenter(this);


        //设置点击事件
        detail_image_back.setOnClickListener(this);
        text_submit_order.setOnClickListener(this);
        relative_addr.setOnClickListener(this);

        //布局管理器
        product_list_recycler.setLayoutManager(new LinearLayoutManager(MakeSureOrderActivity.this));

        //添加分割线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MakeSureOrderActivity.this, LinearLayout.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.item_decoration_shape));

        product_list_recycler.addItemDecoration(dividerItemDecoration);
        //设置适配器
        SureOrderAdapter sureOrderAdapter = new SureOrderAdapter(MakeSureOrderActivity.this, list_selected);
        product_list_recycler.setAdapter(sureOrderAdapter);

        price = 0;
        //显示实付款...计算价格
        for (int i = 0;i<list_selected.size(); i++) {
            price += list_selected.get(i).getBargainPrice() * list_selected.get(i).getNum();
        }
        //格式化两位
        String priceString = decimalFormat.format(price);
        text_shi_fu_kuan.setText("实付款:¥"+priceString);
    }

    /**
     * Called when a view has been clicked.
     *点击事件
     * @param
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_image://返回

                finish();

                break;
            case R.id.text_submit_order://提交订单...生成订单

                //实际上点击提交订单的操作是:1.生成这个订单2.调用支付宝/微信/网银进行付款(付款的操作在后边学)

                //https://www.zhaoapi.cn/product/createOrder?uid=71&price=99.99
                pay();
                createOrderPresenter.createOrder(Constants.CREATE_ORDER_URL, CommonUtils.getString("uid"),price);
                break;
            case R.id.relative_addr_01://跳转到选择收货地址页面...回传数据
                Intent intent = new Intent(MakeSureOrderActivity.this,ChooseAddrActivity.class);

                startActivityForResult(intent,2001);
                break;
        }
    }



    @Override
    public void onGetDefaultAddrscuess(ResponseBody responseBody) {
        try {
           String  json= responseBody.string();
           DefaultAddrBean defaultAddrBean=new Gson().fromJson(json,DefaultAddrBean.class);
            Log.d("defaultAddrBean",defaultAddrBean.getMsg());
           //显示地址...设置适配器
        text_name.setText("收货人: "+defaultAddrBean.getData().getName());
        text_phone.setText(defaultAddrBean.getData().getMobile()+"");
        text_addr.setText("收货地址: "+defaultAddrBean.getData().getAddr());

        //有地址的时候进行设置适配器
        initData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onGetDefaultAddrEmpty() {
        //弹出对话框...取消,,,finish/////确定...添加新地址...没添加点击了返回,当前确认订单页面finish,,,如果添加了显示地址
        AlertDialog.Builder builder = new AlertDialog.Builder(MakeSureOrderActivity.this);
        builder.setTitle("提示")
                .setMessage("您还没有默认的收货地址,请设置收货地址")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //结束确认订单显示
                        MakeSureOrderActivity.this.finish();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //确定...跳转添加新地址...如果没有保存地址,确认订单finish,,,
                        //如果保存了地址...数据传回来进行显示(带有回传值的跳转)
                        Intent intent = new Intent(MakeSureOrderActivity.this,AddNewAddrActivity.class);

                        startActivityForResult(intent,1001);
                    }
                })
                .show();
    }

    @Override
    public void onOrderDataSuccess(ResponseBody responseBody) {
        try {
            String json=responseBody.string();
            CreateOrderBean createOrderBean=new Gson().fromJson(json,CreateOrderBean.class);
            Log.d("defaultAddrBean",createOrderBean.getMsg());
            if ("0".equals(createOrderBean.getCode())) {//创建订单成功...成功之后才能去付款
                //无论付款成功/失败/取消 该订单都已经创建了,,,需要跳转到订单列表,,,并且购物车里面相关商品需要删除

                //1.订单创建成功之后 删除购物车列表中对应的商品...使用递归删除选中的商品
                index = 0;
                deleteProductInCart(list_selected);

            }else {
                Toast.makeText(MakeSureOrderActivity.this,createOrderBean.getMsg(),Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    /**
     * 删除购物车
     * @param list_selected
     */
    private void deleteProductInCart(final ArrayList<CartBean.DataBean.ListBean> list_selected) {

        CartBean.DataBean.ListBean listBean = list_selected.get(index);

        //请求删除购物车的接口...删除成功之后 再次请求查询购物车
        Map<String, String> params = new HashMap<>();
        //?uid=72&pid=1
        params.put("uid",CommonUtils.getString("uid"));
        params.put("pid", String.valueOf(listBean.getPid()));

        RetrofitUtil.getService().doPost(Constants.DELETE_CART_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {

                            index ++;//判断是否继续删除.,..如果index<list.size() 继续,,,不是代表全部删完了
                            if (index < list_selected.size()) {
                                //继续
                                deleteProductInCart(list_selected);
                            }else {
                                //删除完成...//1.调支付的操作...//2.跳转到订单列表页面
                                pay();
                               // Toast.makeText(MakeSureOrderActivity.this, "应该调用支付的操作,然后再跳转订单列表", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MakeSureOrderActivity.this, OrderListActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("e",e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 && resultCode == 1002) {
            //如果没有数据...没有保存,,,finish当前订单页面
            if (data == null) {
                finish();

                return;
            }
            //如果有数据...显示地址...设置适配器
            text_name.setText("收货人: "+data.getStringExtra("name"));
            text_phone.setText(data.getStringExtra("phone"));
            text_addr.setText("收货地址: "+data.getStringExtra("addr"));

            initData();
        }

        if (requestCode == 2001 && resultCode == 2002) {//选择新地址过来的
            if (data == null) {
                return;
            }

            GetAllAddrBean.DataBean dataBean = (GetAllAddrBean.DataBean) data.getSerializableExtra("addrBean");

            text_name.setText("收货人: "+dataBean.getName());
            text_phone.setText(String.valueOf(dataBean.getMobile()));
            text_addr.setText("收货地址: "+dataBean.getAddr());

        }

    }

    public  void  pay(){
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }
        //-------------------------------------------
        //这里需要自己根据实际去传值
        String orderInfo = AlipayUtil.getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01",PARTNER,SELLER);


        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！...实际sign签名的字符串是通过与公司服务器签名得到的
         */
        String sign = AlipayUtil.sign(orderInfo,RSA_PRIVATE);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + AlipayUtil.getSignType();

        //-------------------------------------都可以放到服务器端,,,,,,PARTNER,SELLER,RSA_PRIVATE放在服务器端,,,商品的名字,价钱,描述需要传给服务器,然后是调用接口返回的是符合支付宝规范的完整订单信息

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(MakeSureOrderActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = 0;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();


    }

}

