package com.bw.eastofbeijing.utils.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Dash on 2018/3/1.
 */
public class ObservableScrollView extends ScrollView {
    private static final String TAG = "ObservableScrollView";

    public interface IScrollViewListener{
        void  onScrollChanged(int l, int t, int oldl, int oldt);
    }
    IScrollViewListener mScrollViewListener;
    public void setScrollViewListener(IScrollViewListener listener){
        this.mScrollViewListener=listener;
    }

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(mScrollViewListener!=null){
            mScrollViewListener.onScrollChanged(x, y, oldx, oldy);
        }
    }
}
