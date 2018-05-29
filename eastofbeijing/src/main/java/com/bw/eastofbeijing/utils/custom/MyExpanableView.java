package com.bw.eastofbeijing.utils.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by Dash on 2018/1/30.
 *
 * 自定义的二姐列表....解决的是scrollView嵌套的时候 事件的冲突和显示不全
 */
public class MyExpanableView extends ExpandableListView {
    public MyExpanableView(Context context) {
        super(context);
    }

    public MyExpanableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyExpanableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //Integer.MAX_VALUE >>2 移动两位
        int hight = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >>2, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, hight);
    }
}
