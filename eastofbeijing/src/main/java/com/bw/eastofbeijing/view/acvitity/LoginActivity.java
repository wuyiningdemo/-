package com.bw.eastofbeijing.view.acvitity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.LoginBean;
import com.bw.eastofbeijing.persenter.LoginPresenter;
import com.bw.eastofbeijing.utils.CommonUtils;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.view.iview.IHome;
import com.google.gson.Gson;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,IHome {
    @BindView(R.id.edit_phone)
    EditText edit_phone;
    @BindView(R.id.edit_pwd)
     EditText edit_pwd;

    private LoginPresenter loginPresenter;
    @BindView(R.id.text_regist)
     TextView text_regist;
    @BindView(R.id.login_by_wechat)
     ImageView login_by_wechat;
    @BindView(R.id.login_by_qq)
     ImageView login_by_qq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //登录的presenter
        loginPresenter = new LoginPresenter(this);

        //点击事件
        text_regist.setOnClickListener(this);
        login_by_wechat.setOnClickListener(this);
        login_by_qq.setOnClickListener(this);
    }

    /**
     * 登录的点击事件
     *
     * @param view
     */
    public void login(View view) {
        String phone = edit_phone.getText().toString();
        String pwd = edit_pwd.getText().toString();

        loginPresenter.getLogin(Constants.loginUrl, phone, pwd);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_regist:
                //跳转注册页面
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
                break;
            case R.id.login_by_wechat://微信登录

                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, authListener);

                break;
            case R.id.login_by_qq://qq登录

                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, authListener);

                break;
        }
    }

    @Override
    public void onSuccess(ResponseBody responseBody)  {

        try {
            String json = responseBody.string();

            final LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);

                    Toast.makeText(LoginActivity.this, loginBean.getMsg(), Toast.LENGTH_SHORT).show();

                    if ("0".equals(loginBean.getCode())) {//登录成功
                        //登录成功之后需要做:.....保存状态true...
                        CommonUtils.putBoolean("isLogin", true);
                        CommonUtils.saveString("uid", String.valueOf(loginBean.getData().getUid()));
                        CommonUtils.saveString("name", loginBean.getData().getUsername());
                        CommonUtils.saveString("iconUrl", loginBean.getData().getIcon());

                        //结束当前界面
                       LoginActivity.this.finish();
                    }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onError(Throwable throwable) {
       Log.d("throwable",throwable.getMessage());
    }





    @Override
    public void getLoginSuccessByQQ(ResponseBody responseBody, final String ni_cheng, final String iconurl) {
        try {
            String json = responseBody.string();
            final LoginBean loginBean=new Gson().fromJson(json,LoginBean.class);

                    if ("0".equals(loginBean.getCode())) {//登录成功
                        //登录成功之后需要做:.....保存状态true...
                        CommonUtils.putBoolean("isLogin",true);
                        CommonUtils.saveString("uid", String.valueOf(loginBean.getData().getUid()));
                        CommonUtils.saveString("name",ni_cheng);
                        CommonUtils.saveString("iconUrl",iconurl);

                        //结束当前界面
                        LoginActivity.this.finish();
                    }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * 授权的监听事件
     */
    private UMAuthListener authListener = new UMAuthListener() {
        /**
         * @param platform 平台名称
         * @desc 授权开始的回调
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @param platform 平台名称
         * @param action   行为序号，开发者用不上
         * @param data     用户资料返回
         * @desc 授权成功的回调
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {


            String qq_uid = data.get("uid");
            String ni_cheng = data.get("name");
            String iconurl = data.get("iconurl");

            //实际上是根据这些qq提供的信息,去服务器拿到分配的临时账号,登录进京东的服务器
            //授权成功拿到信息模拟登录
            Log.i("----", ni_cheng + "--" + iconurl);

            loginPresenter.getLoginByQQ("15718844563", "123456", ni_cheng, iconurl);


        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Toast.makeText(LoginActivity.this, "失败：" + throwable.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(LoginActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onFenLeiSuccess(ResponseBody responseBody) {

    }

    @Override
    public void getCartDataNull() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        Log.i("-------------1111111","");
    }

}
