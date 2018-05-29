package com.bw.eastofbeijing.view.acvitity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.AddCartBean;
import com.bw.eastofbeijing.model.bean.DetailBean;
import com.bw.eastofbeijing.persenter.AddCartPresenter;
import com.bw.eastofbeijing.persenter.DeatailPresenter;
import com.bw.eastofbeijing.persenter.linter.IAddCartPLinter;
import com.bw.eastofbeijing.utils.CommonUtils;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.utils.GlideImageLoader;
import com.bw.eastofbeijing.utils.ShareUtil;
import com.bw.eastofbeijing.view.iview.IHome;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class DetailActivity extends AppCompatActivity implements  IHome ,View.OnClickListener,IAddCartPLinter{
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.detail_title)
    TextView detail_title;
    @BindView(R.id.detail_bargin_price)
    TextView detail_bargin_price;
    @BindView(R.id.detail_yuan_price)
    TextView detail_yuan_price;
    @BindView(R.id.detai_add_cart)
    TextView detai_add_cart;
    @BindView(R.id.detail_image_back)
    ImageView detail_image_back;
    @BindView(R.id.watch_cart)
    TextView watch_cart;
    @BindView(R.id.detail_share)
    ImageView detail_share;
    private DeatailPresenter deatailPresenter;
    private AddCartPresenter addCartPresenter;
    private int pid;
    private DetailBean detailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        initBanner();
        //创建presenter
        deatailPresenter = new DeatailPresenter(this);
        addCartPresenter = new AddCartPresenter(this);

        //设置点击事件
        detai_add_cart.setOnClickListener(this);
        detail_image_back.setOnClickListener(this);
        watch_cart.setOnClickListener(this);
        detail_share.setOnClickListener(this);

        //接收传递的pid
        pid = getIntent().getIntExtra("pid", -1);
        //如果不是默认值代表传递过来数据了
        if (pid != -1){
            Log.d("d", String.valueOf(pid));
            //拿着传递的pid请求商品详情的接口,然后展示数据...MVP
            deatailPresenter.getDetailData(Constants.detailUrl, pid);
        }

    }

    @Override
    public void onSuccess(ResponseBody responseBody) {
        try {
            final String json = responseBody.string();
           final DetailBean detailBean = new Gson().fromJson(json,DetailBean.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //添加删除线
                    detail_yuan_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                    //设置数据显示
                    detail_title.setText(detailBean.getData().getTitle());
                    detail_bargin_price.setText("优惠价:"+ detailBean.getData().getBargainPrice());
                    detail_yuan_price.setText("原价:"+ detailBean.getData().getPrice());

                    String[] images = detailBean.getData().getImages().split("\\|");

                    final ArrayList<String> imageUrls = new ArrayList<>();
                    for (int i = 0;i<images.length;i++){
                        imageUrls.add(images[i]);
                    }

                    banner.setImages(imageUrls);
                    //bannner点击事件进行跳转
                    banner.setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            Intent intent = new Intent(DetailActivity.this,ImageScaleActivity.class);
                            //传递的数据...整个轮播图数据的集合需要传递,,,当前展示的图片的位置需要传递postion
                            //intent传递可以传的数据...基本数据类型...引用数据类型(必须序列化,所有的类,包括内部类实现serilizable接口)...bundle
                            intent.putStringArrayListExtra("imageUrls",imageUrls);
                            intent.putExtra("position",position);

                            startActivity(intent);
                        }
                    });

                    banner.start();


                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onAddCartScuess(ResponseBody responseBody) {
        try {
            String json= responseBody.string();

            AddCartBean addCartBean=new Gson().fromJson(json,AddCartBean.class);
            Toast.makeText(DetailActivity.this,addCartBean.getMsg(),Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        Log.d("-----",throwable.getMessage());
    }

    @Override
    public void unsubcribe() {
      addCartPresenter.unsubcribe();
    }

    @Override
    public void getLoginSuccessByQQ(ResponseBody responseBody, String ni_cheng, String iconurl) {

    }



    @Override
    public void onFenLeiSuccess(ResponseBody responseBody) {

    }

    @Override
    public void getCartDataNull() {

    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Toast.makeText(DetailActivity.this,"分享开始",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(DetailActivity.this,"分享成功",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Log.e("----",throwable.getMessage());
            Toast.makeText(DetailActivity.this,"分享失败",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(DetailActivity.this,"分享取消",Toast.LENGTH_SHORT).show();

        }
    };

    private void initBanner() {

        //设置banner样式...CIRCLE_INDICATOR_TITLE包含标题
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detai_add_cart://添加购物车...uid...pid...判断是否登录

                if (CommonUtils.getBoolean("isLogin")) {

                    addCartPresenter.addToCart(Constants.addCartUrl, CommonUtils.getString("uid"),pid);
                }else {
                    Intent intent = new Intent(DetailActivity.this,LoginActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.detail_image_back://返回
                finish();
                break;
            case R.id.watch_cart://查看购物车....跳转的是购物车的activity

                Intent intent = new Intent(DetailActivity.this,CartActivity.class);

                startActivity(intent);
                break;
            case R.id.detail_share://分享......分享的是商品链接

                if (detailBean != null) {
                    //final Activity activity 分享的activity的上下文,
                    // String WebUrl 分享的商品的链接,
                    // String title 分享的商品的标题,
                    // String description 商品的描述,
                    // String imageUrl 商品的图片...如果没有图片传"",
                    // int imageID 本地商品的图片
                    DetailBean.DataBean data = detailBean.getData();
                    ShareUtil.shareWeb(DetailActivity.this,data.getDetailUrl(),data.getTitle(),"我在京东发现一个好的商品,赶紧来看看吧!",data.getImages().split("\\|")[0],R.mipmap.ic_launcher);
                }

                new ShareAction(DetailActivity.this)
                        .withText("hello")
                        .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
                        .setCallback(shareListener)
                        .open();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
    }

