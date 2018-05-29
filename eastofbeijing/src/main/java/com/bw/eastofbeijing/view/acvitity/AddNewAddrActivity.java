package com.bw.eastofbeijing.view.acvitity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.AddNewAddrBean;
import com.bw.eastofbeijing.persenter.AddNewAddrPresenter;
import com.bw.eastofbeijing.utils.CommonUtils;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.view.iview.IHome;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class AddNewAddrActivity extends AppCompatActivity implements View.OnClickListener,IHome{
    @BindView(R.id.detail_image)
     ImageView detail_image_back;
    @BindView(R.id.text_save)
     TextView text_save;
    @BindView(R.id.edit_person)
     EditText edit_person;
    @BindView(R.id.edit_phone)
     EditText edit_phone;
    @BindView(R.id.edit_area)
     TextView edit_area;
    @BindView(R.id.edit_road)
     EditText edit_road;
    private AddNewAddrPresenter addNewAddrPresenter;
    @BindView(R.id.linear_area)
     LinearLayout linear_area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_addr);
        ButterKnife.bind(this);

        detail_image_back.setOnClickListener(this);
        text_save.setOnClickListener(this);
        linear_area.setOnClickListener(this);//选择地址

        addNewAddrPresenter = new AddNewAddrPresenter(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_image_back:

                finish();//没传值回去...此时确认订单需要finish

                break;
            case R.id.text_save://保存
                //请求添加新地址的接口...点击保存的时候需要做一系列的非空判断
                //uid=71&addr=北京市昌平区金域国际1-1-1&mobile=18612991023&name=kson
                addNewAddrPresenter.addNewAddr(Constants.ADD_NEW_ADDR_URL, CommonUtils.getString("uid"),edit_area.getText().toString()+edit_road.getText().toString(),edit_phone.getText().toString(),edit_person.getText().toString());


                break;
            case R.id.linear_area://选择地址
                Intent intent = new Intent(AddNewAddrActivity.this,ChooseDistinctActivity.class);
                startActivityForResult(intent,3001);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==3001 && resultCode == 3002) {
            if (data == null) {
                return;
            }

            String addr = data.getStringExtra("addr");
            edit_area.setText(addr);

        }

    }

    @Override
    public void onSuccess(ResponseBody responseBody) {
        try {
            String  json= responseBody.string();
            AddNewAddrBean addNewAddrBean=new Gson().fromJson(json,AddNewAddrBean.class);
            if ("0".equals(addNewAddrBean.getCode())) {//保存成功
                //请求添加成功之后...回传值给确认订单页面进行显示

                Intent intent = new Intent();

                intent.putExtra("name",edit_person.getText().toString());
                intent.putExtra("phone",edit_phone.getText().toString());
                intent.putExtra("addr",edit_area.getText().toString()+edit_road.getText().toString());

                setResult(1002,intent);

                finish();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable throwable) {

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
