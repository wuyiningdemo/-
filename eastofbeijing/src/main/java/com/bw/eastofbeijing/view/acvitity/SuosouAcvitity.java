package com.bw.eastofbeijing.view.acvitity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.utils.custom.XCFlowLayout;
import com.bw.eastofbeijing.view.adapter.MylistAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SuosouAcvitity extends AppCompatActivity {
    private String mNames[] = {
            "苹果x","罗汉果茶","无纺布鞋套",
            "不锈钢蒸锅","篮球鞋","养生壶",
            "橄榄调和油","斯若克球杆","办公室靠枕",
            "应急启动电源","真丝衫","帆布鞋",
            "连衣裙","毛呢衫","数据线","华为"
    };
    private XCFlowLayout mFlowLayout;
    @BindView(R.id.fanhui)
     ImageView fanhui;
    @BindView(R.id.main_name)
     EditText main_name;
    @BindView(R.id.seltxt)
     TextView seltxt;
    @BindView(R.id.lv)
     ListView lv;
    @BindView(R.id.delAll)
     Button delAll;

    private List<String> list=new ArrayList<>();
    private MylistAdapter mylistAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suosou_acvitity);
        ButterKnife.bind(this);

        dt();
        initChildViews();
        //清空数据并且隐藏
        delAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();

                mylistAdapter.notifyDataSetChanged();
                delAll.setVisibility(View.GONE);
            }
        });
    }
    public void dt(){
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SuosouAcvitity.this,ZhuYeActivity.class);
                startActivity(it);
            }
        });

        //点击搜索并且显示按钮
        seltxt.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                 String shu = main_name.getText().toString();
                list.add(shu);
                mylistAdapter = new MylistAdapter(SuosouAcvitity.this,list);
                lv.setAdapter(mylistAdapter);
                delAll.setVisibility(View.VISIBLE);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent =new Intent(SuosouAcvitity.this,DetailActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
    //流式布局
    public void initChildViews() {
        // TODO Auto-generated method stub
        mFlowLayout = (XCFlowLayout) findViewById(R.id.flowlayout);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        for(int i = 0; i < mNames.length; i ++){
            TextView view = new TextView(this);
            view.setText(mNames[i]);
            view.setTextColor(Color.WHITE);
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_bg));
            mFlowLayout.addView(view,lp);
        }
    }


}
