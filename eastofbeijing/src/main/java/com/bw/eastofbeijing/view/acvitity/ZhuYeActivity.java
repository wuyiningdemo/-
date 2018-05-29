package com.bw.eastofbeijing.view.acvitity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.utils.ChenJinUtil;
import com.bw.eastofbeijing.view.MyApplication;
import com.bw.eastofbeijing.view.fragment.FaXianFragment;
import com.bw.eastofbeijing.view.fragment.FenLeiFragment;
import com.bw.eastofbeijing.view.fragment.GuoWuCheFragment;
import com.bw.eastofbeijing.view.fragment.MeFragment;
import com.bw.eastofbeijing.view.fragment.ShouYeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZhuYeActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.radio_group)
    RadioGroup radio_group;
    private FragmentManager manager;
    private ShouYeFragment fragmentHome;
    private MeFragment fragmentMy;
    private FenLeiFragment fragmentFenLei;
    private FaXianFragment fragmentFaXian;
    private GuoWuCheFragment fragmentShoppingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhu_ye);
        ButterKnife.bind(this);
        radio_group.setOnCheckedChangeListener(this);

        //管理者...开启事务(一个事务只能执行一次)....默认的是要显示第一个首页
        manager = getSupportFragmentManager();
        //沉浸
        ChenJinUtil.startChenJin(this);
        fragmentHome = new ShouYeFragment();
        //添加这个方法在使用的时候同一个fragment只能添加一次,否则会报错...结合着show和hide方法去使用
        manager.beginTransaction().add(R.id.frame, fragmentHome).commit();
        initWidthAndHeight();

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //事务的对象
        FragmentTransaction transaction = manager.beginTransaction();

        //首先隐藏所有的fragment ...前提是不为空
        //在刚开始点击的时候,先判断fragment是否为空,,,如果不为空先让他隐藏
        if (fragmentHome != null) {
            transaction.hide(fragmentHome);
        }

        if (fragmentFenLei != null) {
            transaction.hide(fragmentFenLei);
        }

        if (fragmentFaXian != null) {
            transaction.hide(fragmentFaXian);
        }

        if (fragmentShoppingCart != null) {
            transaction.hide(fragmentShoppingCart);
        }

        if (fragmentMy != null) {
            transaction.hide(fragmentMy);
        }
        switch (checkedId) {//点击选中某个button的时候要么去显示要么去添加,,,没有去添加,,,有则显示出来
            case R.id.radio_01:
                //manager.beginTransaction().replace(arg0, arg1).commit()
                if (fragmentHome == null) {
                    fragmentHome = new ShouYeFragment();
                    transaction.add(R.id.frame, fragmentHome);
                } else {
                    transaction.show(fragmentHome);
                }
                break;
            case R.id.radio_02:
                if (fragmentFenLei == null) {
                    fragmentFenLei = new FenLeiFragment();
                    transaction.add(R.id.frame, fragmentFenLei);
                } else {
                    transaction.show(fragmentFenLei);
                }
                break;
            case R.id.radio_03:
                if (fragmentFaXian == null) {
                    fragmentFaXian = new FaXianFragment();
                    transaction.add(R.id.frame, fragmentFaXian);
                } else {
                    transaction.show(fragmentFaXian);
                }
                break;
            case R.id.radio_04:
                if (fragmentShoppingCart == null) {
                    fragmentShoppingCart = new GuoWuCheFragment();
                    transaction.add(R.id.frame, fragmentShoppingCart);
                } else {
                    transaction.show(fragmentShoppingCart);
                }
                break;
            case R.id.radio_05:
                if (fragmentMy == null) {
                    fragmentMy = new MeFragment();
                    transaction.add(R.id.frame, fragmentMy);
                } else {
                    transaction.show(fragmentMy);
                }
                break;

            default:
                break;
        }


        //只能提交一次
        transaction.commit();
    }

/**
 * 初始化控件的宽度和高度
 */

    private void initWidthAndHeight() {

        int deviceHeight = MyApplication.getDeviceHeight();
        int deviceWidth = MyApplication.getDeviceWidth();
        if (deviceHeight == 1920 && deviceWidth == 1080) {
            //标准的手机 不需要计算
            return;
        }else {
            //计算每一个需要微调控件的宽度和高度 并设置给该控件
            //一般match_parent和wrap_content不需要计算
            //计算的是布局文件写死的哪些dp值

            //根据标准app的标注计算出实际像素值---540/1080
            int width_text_01 = (int) (1200*deviceWidth/1080+0.5);
            int height_text_01 = (int) (150*deviceHeight/1920+0.5);


            //把计算的宽度和高度给text_01使用代码设置上

            RelativeLayout.LayoutParams layoutParams_text_01 = (RelativeLayout.LayoutParams) radio_group.getLayoutParams();
            layoutParams_text_01.height = height_text_01;
            layoutParams_text_01.width = width_text_01;

            radio_group.setLayoutParams(layoutParams_text_01);


            //.............


        }
    }
}
