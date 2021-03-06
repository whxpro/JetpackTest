package com.whx.jetpacktest.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;

import com.whx.jetpacktest.R;
import com.whx.jetpacktest.utils.ExtUtilsKt;

public class DefaultFootView extends FrameLayout implements ILoadMoreFoot {

    private TextView mTvStatus;
    private LayoutParams mLayoutParams =
            new LayoutParams(LayoutParams.MATCH_PARENT, (int) ExtUtilsKt.dp2px(60));

    public DefaultFootView(Context context) {
        this(context, null);
    }

    public DefaultFootView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultFootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.default_headview, null);
        mTvStatus = view.findViewById(R.id.tv_status);
        this.addView(view, mLayoutParams);
    }

    /**
     * 设置刷新头的状态颜色
     *
     * @param textColor
     */
    public void setTextColor(@ColorRes int textColor) {
        mTvStatus.setTextColor(getResources().getColor(textColor));
    }

    /**
     * 设置刷新头的状态文字大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        mTvStatus.setTextSize(textSize);
    }

    @Override
    public void onStart() {
        mTvStatus.setText("上拉");
    }

    @Override
    public void onPullUp(int distance) {
        mTvStatus.setText("上拉加载更多");
    }


    @Override
    public void onBound() {
        mTvStatus.setText("松开加载更多");
    }

    @Override
    public void onFingerUp(int distance) {
        mTvStatus.setText("加载更多...");
    }

    @Override
    public void onStop() {

    }

    @Override
    public int footViewHeight() {
        return (int) ExtUtilsKt.dp2px(60);
    }
}
