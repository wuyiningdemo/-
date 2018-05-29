package com.bw.eastofbeijing.view.acvitity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.RegistBean;
import com.bw.eastofbeijing.persenter.RegistPresenter;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.view.iview.IHome;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener,IHome {
    @BindView(R.id.edit_phone)
     EditText edit_phone;
    @BindView(R.id.edit_pwd)
     EditText edit_pwd;
    private RegistPresenter registPresenter;
    @BindView(R.id.cha_iamge)
     ImageView cha_iamge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);

        //中间者
        registPresenter = new RegistPresenter(this);

        cha_iamge.setOnClickListener(this);
    }

    /**
     * 注册的点击事件
     * @param view
     */
    public void regist(View view) {

        String name = edit_phone.getText().toString();
        String pwd = edit_pwd.getText().toString();

        registPresenter.registUser(Constants.registUrl,name,pwd);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cha_iamge://叉

                finish();
                break;
        }
    }

    @Override
    public void onSuccess(ResponseBody responseBody) {
        try {
            String json = responseBody.string();
            final RegistBean registBean=new Gson().fromJson(json,RegistBean.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String code = registBean.getCode();
                    if ("1".equals(code)) {//注册失败
                        Toast.makeText(RegistActivity.this,registBean.getMsg(),Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(RegistActivity.this,registBean.getMsg(),Toast.LENGTH_SHORT).show();
                        //并且结束显示
                        RegistActivity.this.finish();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(Throwable throwable) {
        Log.d("throwable",throwable.getMessage());
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
}
