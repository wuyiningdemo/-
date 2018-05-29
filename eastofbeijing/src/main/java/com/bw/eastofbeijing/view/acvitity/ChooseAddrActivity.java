package com.bw.eastofbeijing.view.acvitity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.bw.eastofbeijing.view.adapter.GetAllAddrAdapter;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class ChooseAddrActivity extends AppCompatActivity implements View.OnClickListener,GetAllAddrInter {
    @BindView(R.id.detail_image_back)
     ImageView imageView;
    @BindView(R.id.text_manage)
     TextView text_manage;
    @BindView(R.id.list_view_addr)
     ListView list_view_addr;
    private GetAllAddrPresenter getAllAddrPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_addr);
        ButterKnife.bind(this);
        //点击事件
        imageView.setOnClickListener(this);
        text_manage.setOnClickListener(this);

        getAllAddrPresenter = new GetAllAddrPresenter(this);
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

                finish();
                break;
            case R.id.text_manage://管理地址

                Intent intent = new Intent(ChooseAddrActivity.this,ManageAddrActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        //获取地址列表的最新数据
        getAllAddrPresenter.getAllAddr(Constants.GET_ALL_ADDR_URL, CommonUtils.getString("uid"));

    }

    @Override
    public void onGetAllAddrSuccess(ResponseBody responseBody) {
        try {
            String json = responseBody.string();
            Log.d("json",json);
            final GetAllAddrBean getAllAddrBean=new Gson().fromJson(json,GetAllAddrBean.class);
            if ("0".equals(getAllAddrBean.getCode())) {

                //设置适配器
                GetAllAddrAdapter getAllAddrAdapter = new GetAllAddrAdapter(ChooseAddrActivity.this, getAllAddrBean.getData());
                list_view_addr.setAdapter(getAllAddrAdapter);

                //设置条目的点击事件
                list_view_addr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent();

                        intent.putExtra("addrBean",getAllAddrBean.getData().get(position));

                        setResult(2002,intent);
                        finish();

                    }
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
