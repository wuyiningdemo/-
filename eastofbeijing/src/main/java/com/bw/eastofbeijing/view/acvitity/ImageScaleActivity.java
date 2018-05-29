package com.bw.eastofbeijing.view.acvitity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.utils.custom.ZoomImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageScaleActivity extends AppCompatActivity {
    private ArrayList<String> imageUrls;
    private int position;
    @BindView(R.id.iamge_scale_pager)
     ViewPager iamge_scale_pager;
    @BindView(R.id.image_scale_text)
     TextView image_scale_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_scale);
        ButterKnife.bind(this);

        //接收数据
        Intent intent = getIntent();
        imageUrls = intent.getStringArrayListExtra("imageUrls");
        position = intent.getIntExtra("position", -1);

        //判断 集合不为空null 集合的长度大于0 position不是-1
        //给viewPager设置适配器
        if (imageUrls != null && imageUrls.size()>0 && position != -1) {

            //1.textView展示第几张图片
            image_scale_text.setText((position+1)+"/"+imageUrls.size());

            //2.设置适配器
            ImageScaleAdapter imageScaleAdapter = new ImageScaleAdapter();
            iamge_scale_pager.setAdapter(imageScaleAdapter);

            //3.设置显示的是点击的那张图片..setCurrentItem()
            //boolean smoothScroll...是否滚动
            iamge_scale_pager.setCurrentItem(position*imageUrls.size(),false);

            //设置监听
            iamge_scale_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    //设置文本显示
                    image_scale_text.setText((position+1)+"/"+imageUrls.size());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
    }
    private class ImageScaleAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public boolean isViewFromObject( View view,  Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem( ViewGroup container, int position) {
            //创建imageView(可以放大缩小的iamgeView...自定义的)
            ZoomImageView zoomImageView = new ZoomImageView(ImageScaleActivity.this);
            //使用glide加载图片
            Glide.with(ImageScaleActivity.this).load(imageUrls.get(position%imageUrls.size())).into(zoomImageView);
            //添加到容器container
            container.addView(zoomImageView);
            //返回这个view
            return zoomImageView;
        }

        @Override
        public void destroyItem( ViewGroup container, int position,  Object object) {
            //从容器中移除这个view
            container.removeView((View) object);
        }
    }
}
