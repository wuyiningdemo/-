package com.bw.eastofbeijing.view.acvitity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.GetAllAddrBean;
import com.bw.eastofbeijing.model.linear.GetAllAddrInter;
import com.bw.eastofbeijing.persenter.GetAllAddrPresenter;
import com.bw.eastofbeijing.utils.CommonUtils;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.view.adapter.ManageAddrAdapter;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;

public class ManageAddrActivity extends AppCompatActivity implements View.OnClickListener,GetAllAddrInter {
    private GetAllAddrPresenter getAllAddrPresenter;
    private ImageView detail_image_back;
    private ListView list_view_addr;
    private TextView text_add_new;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_addr);
        detail_image_back = findViewById(R.id.detail_image_back);
        list_view_addr = findViewById(R.id.list_view_addr);
        text_add_new = findViewById(R.id.text_add_new);

        getAllAddrPresenter = new GetAllAddrPresenter(this);

        text_add_new.setOnClickListener(this);
        detail_image_back.setOnClickListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();

        //获取地址列表的最新数据
        getAllAddrPresenter.getAllAddr(Constants.GET_ALL_ADDR_URL, CommonUtils.getString("uid"));


    }

    /**
     * Called when a view has been clicked.
     *点击事件
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_image_back:
                finish();;
                break;
            case R.id.text_add_new:
                Intent intent = new Intent(ManageAddrActivity.this,AddNewAddrActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onGetAllAddrSuccess(ResponseBody responseBody) {
        try {
            String json= responseBody.string();
            GetAllAddrBean getAllAddrBean=new Gson().fromJson(json,GetAllAddrBean.class);
            if ("0".equals(getAllAddrBean.getCode())) {

                //设置适配器
                ManageAddrAdapter manageAddrAdapter = new ManageAddrAdapter(ManageAddrActivity.this, getAllAddrBean.getData(),getAllAddrPresenter);
                list_view_addr.setAdapter(manageAddrAdapter);


                //设置条目的点击事件
                list_view_addr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                    }
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
