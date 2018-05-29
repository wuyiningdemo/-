package com.bw.eastofbeijing.view.acvitity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.utils.CommonUtils;
import com.bw.eastofbeijing.utils.GlideImgManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserSettingActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.detail_image_back)
     ImageView detail_image_back;
    @BindView(R.id.setting_icon)
     ImageView setting_icon;
    @BindView(R.id.setting_name)
     TextView setting_name;
    @BindView(R.id.text_exit)
     TextView text_exit;
    @BindView(R.id.relative_user)
     RelativeLayout relative_user;
    @BindView(R.id.text_manage)
     TextView text_manage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        ButterKnife.bind(this);
        detail_image_back.setOnClickListener(this);
        text_exit.setOnClickListener(this);
        relative_user.setOnClickListener(this);
        text_manage.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //判断一下是否登录..,..当登录成功之后,先存一下boolean值,,,在这里取出来判断
        boolean isLogin = CommonUtils.getBoolean("isLogin");

        if (isLogin) {
            if ("".equals(CommonUtils.getString("iconUrl")) || "null".equals(CommonUtils.getString("iconUrl"))) {
                //显示默认头像
                setting_icon.setImageResource(R.drawable.user);
            } else {

                //1.加载一下头像显示...判断一下是否有头像的路径,,,没有则显示默认的头像
                //Glide.with(this).load(CommonUtils.getString("iconUrl")).into(setting_icon);

                //加载圆形
                GlideImgManager.glideLoader(UserSettingActivity.this, CommonUtils.getString("iconUrl"), R.drawable.user, R.drawable.user, setting_icon, 0);
            }
            //2.先试一下用户名
            setting_name.setText(CommonUtils.getString("name"));

        } else {
            //显示默认头像
            setting_icon.setImageResource(R.drawable.user);
            //用户名显示 登录/注册 >
            setting_name.setText("登录/注册 >");

        }
    }
    @Override
    public void onClick(View v) {
            switch (v.getId()) {
                case R.id.detail_image_back:

                    finish();

                    break;
                case R.id.text_exit://退出登录...实际上是请求退出的接口..在这把登录成功保存的信息清空..结束当前页面的显示

                    CommonUtils.clearSp("isLogin");
                    CommonUtils.clearSp("uid");
                    CommonUtils.clearSp("name");
                    CommonUtils.clearSp("iconUrl");

                    finish();
                    break;
                case R.id.relative_user://进入个人中心

                    Intent intent = new Intent(UserSettingActivity.this,UserInfoActivity.class);

                    startActivity(intent);
                    break;
                case R.id.text_manage://跳转地址管理
                    Intent intent1 = new Intent(UserSettingActivity.this,ManageAddrActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    }

