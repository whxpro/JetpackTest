package com.whx.jetpacktest.widget.refresh;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.whx.jetpacktest.R;

public class DWRefreshLayout extends FrameLayout {
    private static final String TAG = "DWRefreshLayout";
    private static final String HEAD_TAG = "head";
    private static final String FOOT_TAG = "foot";

    @Nullable
    private View mHeadView;
    private View mContentView;
    @Nullable
    private View mFootView;

    @Nullable
    private IRefreshHead mIRefreshHead;
    @Nullable
    private ILoadMoreFoot mILoadMoreFoot;

    private int mHeadViewHeight;
    private int mFootViewHeight;

    @Nullable
    private ValueAnimator mValueAnimator;

    //view需要移动的的距离
    private int moveDistance = 0;

    //灵敏度，moveDistance * mTact = 实际 需要移动的距离
    private float mHeadViewTact = 0.6f;
    private float mFootViewTact = 0.5f;

    /*当前的刷新布局状态*/
    private int mStatus = STATUS_NONE;
    //状态: 无
    private static final int STATUS_NONE = 0;
    //状态：刷新
    private static final int STATUS_REFRESH = 1;
    //状态：加载更多
    private static final int STATUS_LOAD_MORE = 2;

    //方向
    private int mDirection = DIRECTION_NONE;
    private static final int DIRECTION_NONE = 0;
    //划动方向: 向下划（刷新动作）
    public static final int DIRECTION_UP = 1;
    //划动方向：向上划（加载更多动作）
    public static final int DIRECTION_DOWN = 2;

    //动画的执行时间,默认是200ms
    int mAnimatorDuration = ANIMATOR_DEFAULT_DURATION;
    //动画的默认执行时间
    private static int ANIMATOR_DEFAULT_DURATION = 200;
    //自动刷新的动画默认执行时间
    private static int ANIMATOR_AUTO_REFRESH_DURATION = 500;

    //是否拦截事件
    private boolean interception;
    //是否为自动刷新
    private boolean isAutoRefresh = false;
    //自动刷新动画监听器
    private AutoRefreshAnimatorListener mAutoRefreshAnimatorListener = new AutoRefreshAnimatorListener();

    //刷新布局的排放方式
    private int mRefreshStyle;
    //刷新头布局在刷新内容之下
    private static final int STYLE_BELOW = 1;
    //刷新头布局与刷新内容并排
    private static final int STYLE_DEFAULT = 2;
    //刷新头布局在刷新内容之上
    private static final int STYLE_MATERIAL = 3;

    //是否禁止上拉加载更多默认是支持的
    private boolean mEnableLoadMore = true;
    private boolean mCanPullRefresh = true;

    private OnRefreshListener mOnRefreshListener;


    public DWRefreshLayout(Context context) {
        this(context, null);
    }

    public DWRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DWRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
//        init();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.DWRefreshLayout);
        mRefreshStyle = ta.getInt(R.styleable.DWRefreshLayout_refresh_style, STYLE_DEFAULT);
        Log.d(TAG, "RefreshStyle: " + mRefreshStyle);
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 后续支持多个子view
        if (getChildCount() > 1) {
            throw new IllegalArgumentException(">>>>---- do refresh direct view count must equal 1 ----<<<<");
        }

//        LayoutParams headParams = new LayoutParams(LayoutParams.MATCH_PARENT, mHeadViewHeight);
//        this.addView(mHeadView, 0, headParams);

        mContentView = this.getChildAt(0);

//        addHeadView(mHeadView);

//        addFootView(mFootView);
//        LayoutParams footParams = new LayoutParams(LayoutParams.MATCH_PARENT, mFootViewHeight);
//        this.addView(mFootView, 2, footParams);

    }

    /**
     * 添加刷新头,添加的位置要根据{@link #mRefreshStyle}的值来添加
     * 如果{@link #mRefreshStyle}的值为{@link #STYLE_BELOW} headView
     * 在当前刷新布局的位置是0.也就是addView(headView,0);
     * 如果{@link #mRefreshStyle}的值为{@link #STYLE_DEFAULT}或是
     * 为{@link #STYLE_MATERIAL} headView在当前刷新布局的位置是1
     *
     * @param headView 头布局
     */
    private void addHeadView(View headView) {
        this.removeView(findViewWithTag(HEAD_TAG));

        //重新添加头布局
        LayoutParams headParams = new LayoutParams(LayoutParams.MATCH_PARENT, mHeadViewHeight);
        if (mRefreshStyle == STYLE_BELOW) {
            this.addView(headView, 0, headParams);
            Log.d(TAG, "add headView below");
        } else {
            this.addView(headView, 1, headParams);
            Log.d(TAG, "add headView other > default or material");
        }
        headView.setTag(HEAD_TAG);
    }

    /**
     * 添加脚布局
     *
     * @param footView 脚布局
     */
    private void addFootView(View footView) {
        this.removeView(findViewWithTag(FOOT_TAG));

        LayoutParams footParams = new LayoutParams(LayoutParams.MATCH_PARENT, mFootViewHeight);
        this.addView(footView, 2, footParams);
        footView.setTag(FOOT_TAG);
    }


    /**
     * 设置自定义的头布局
     *
     * @param headView 头布局
     */
    public void setHeadView(View headView) {
        if (!(headView instanceof IRefreshHead)) {
            throw new IllegalArgumentException("headView must implement IRefreshHead");
        }
        mHeadView = headView;
        mIRefreshHead = (IRefreshHead) headView;
        mHeadViewHeight = mIRefreshHead.headViewHeight();

        addHeadView(headView);
    }

    /**
     * 设置自定义的脚布局
     *
     * @param footView 脚布局
     */
    public void setFootView(View footView) {
        if (!(footView instanceof ILoadMoreFoot)) {
            throw new IllegalArgumentException("footView must implement ILoadMoreFoot");
        }
        mFootView = footView;
        mILoadMoreFoot = (ILoadMoreFoot) footView;
        mFootViewHeight = mILoadMoreFoot.footViewHeight();

        addFootView(mFootView);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        b = getHeight();
        if (mRefreshStyle == STYLE_BELOW) {
            //below
            if (mHeadView != null) {
                mHeadView.layout(0, 0, r, mHeadViewHeight);
            }
            mContentView.layout(0, moveDistance, r, b + moveDistance);
            if (mFootView != null) {
                mFootView.layout(0, b + moveDistance, r, b + mFootViewHeight + moveDistance);
            }

        } else if (mRefreshStyle == STYLE_DEFAULT) {
            //default
            if (mHeadView != null) {
                mHeadView.layout(0, Math.min(-mHeadViewHeight + moveDistance, 0), r, moveDistance);
            }
            mContentView.layout(0, Math.min(moveDistance, mHeadViewHeight), r, b + moveDistance);
            if (mFootView != null) {
                mFootView.layout(0, b + moveDistance, r, b + mFootViewHeight + moveDistance);
            }
        } else {
            //material
            if (mHeadView != null) {
                mHeadView.layout(0, -mHeadViewHeight + moveDistance, r, moveDistance);
            }
            if (mDirection == DIRECTION_DOWN) {
                mContentView.layout(0, 0, r, b);
            } else if (mDirection == DIRECTION_UP) {
                mContentView.layout(0, moveDistance, r, b + moveDistance);
            } else {
                mContentView.layout(0, moveDistance, r, b + moveDistance);
            }
            if (mFootView != null) {
                mFootView.layout(0, b + moveDistance, r, b + mFootViewHeight + moveDistance);
            }
        }

    }

    private int downY;
    private int downX;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //如果此时是在刷新中或是在加载更多中就不要对事件进行自定义的拦截
        if (mStatus == STATUS_REFRESH || mStatus == STATUS_LOAD_MORE) {
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                downX = (int) ev.getX();
                mDirection = DIRECTION_NONE;
                interception = false;
//                Log.d(TAG, "dispatchTouchEvent down");
                break;
            case MotionEvent.ACTION_MOVE:
                int diffY = (int) (downY - ev.getY());
                int diffX = (int) (downX - ev.getX());
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    return super.dispatchTouchEvent(ev);
                }
                if (diffY < 0) {
                    boolean contentViewCanScrollDown = mContentView.canScrollVertically(-1);
                    if (!contentViewCanScrollDown) {
//                        Log.d(TAG, "下拉 ");
                        mDirection = DIRECTION_DOWN;
                        if (!interception && mIRefreshHead != null) {
                            mIRefreshHead.onStart();
                        }
                        interception = mHeadView != null;
                    }
                } else if (diffY > 0) {
                    boolean contentViewCanScrollUp = mContentView.canScrollVertically(1);
                    if (!contentViewCanScrollUp) {
//                        Log.d(TAG, "上拉 ");
                        mDirection = DIRECTION_UP;
                        if (!interception && mILoadMoreFoot != null) {
                            mILoadMoreFoot.onStart();
                        }
                        interception = mFootView != null;
                    }
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //如果此时是在刷新中或是在加载更多中就不要对事件进行自定义的拦截
        if (mStatus == STATUS_REFRESH || mStatus == STATUS_LOAD_MORE) {
            return super.onInterceptTouchEvent(ev);
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.d(TAG, "onInterceptTouchEvent down:");
                break;
            case MotionEvent.ACTION_MOVE:
//                if (mDirection == DIRECTION_DOWN) {
//                    mIRefreshHead.onStart();
//                } else if (mDirection == DIRECTION_UP) {
//                    mILoadMoreFoot.onStart();
//                }
//                Log.d(TAG, "onInterceptTouchEvent move:" + interception);
                return interception;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果此时是在刷新中或是在加载更多中就不要对事件进行自定义的拦截
        if (mStatus == STATUS_REFRESH || mStatus == STATUS_LOAD_MORE) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent down:");
                return true;
            case MotionEvent.ACTION_MOVE:
                move((int) event.getY());
                Log.d(TAG, "onTouchEvent move:" + interception);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int upY = (int) event.getY();
                if (mDirection == DIRECTION_DOWN) {
                    if (!mCanPullRefresh) {
                        //禁止下拉刷新
                        return super.onTouchEvent(event);
                    }
                    if (moveDistance >= mHeadViewHeight) {
                        //触发刷新
                        if (mIRefreshHead != null) {
                            mIRefreshHead.onTriggerRefresh();
                        }
                        reset((int) ((upY - downY) * mHeadViewTact), mHeadViewHeight);
                        mStatus = STATUS_REFRESH;
                        if (null != mOnRefreshListener) {
                            mOnRefreshListener.onRefresh();
                        }
                    } else {
                        //回弹
                        if (mIRefreshHead != null) {
                            mIRefreshHead.onFingerUpNotTrigger(moveDistance);
                        }
                        mStatus = STATUS_NONE;
                        reset((int) ((upY - downY) * mHeadViewTact), 0);
                    }
                } else if (mDirection == DIRECTION_UP) {
                    if (!mEnableLoadMore) {
                        //禁止加载更多
                        return super.onTouchEvent(event);
                    }
                    if (mILoadMoreFoot != null) {
                        mILoadMoreFoot.onFingerUp(Math.abs(moveDistance));
                    }
                    if (Math.abs(moveDistance) >= mFootViewHeight) {
                        //触发加载
                        reset((int) ((upY - downY) * mFootViewTact), -mFootViewHeight);
                        mStatus = STATUS_LOAD_MORE;
                        if (null != mOnRefreshListener) {
                            mOnRefreshListener.onLoadMore();
                        }
                    } else {
                        //回弹
                        mStatus = STATUS_NONE;
                        reset((int) ((upY - downY) * mFootViewTact), 0);
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
    }

    /**
     * 移动布局
     *
     * @param moveY 手指移动的Y坐标
     */
    private void move(int moveY) {
        if (mDirection == DIRECTION_DOWN) {
            //下移
            if (!mCanPullRefresh) {
                //禁止下拉刷新
                return;
            }
            moveDistance = (int) ((moveY - downY) * mHeadViewTact);
            if (moveDistance >= mHeadViewHeight && mIRefreshHead != null) {
                mIRefreshHead.onBound();
            } else {
                if (mIRefreshHead != null) {
                    mIRefreshHead.onPullDown(moveDistance);
                }
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.onPull(DIRECTION_DOWN, moveDistance);
                }
            }
        } else {
            //上移
            if (!mEnableLoadMore) {
                //禁止加载更多
                return;
            }
            moveDistance = (int) ((moveY - downY) * mFootViewTact);
            if (moveDistance <= -mFootViewHeight && mILoadMoreFoot != null) {
                mILoadMoreFoot.onBound();
            } else {
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.onPull(DIRECTION_UP, moveDistance);
                }
                if (mILoadMoreFoot != null) {
                    mILoadMoreFoot.onPullUp(Math.abs(moveDistance));
                }
            }
        }
        Log.d(TAG, "moveDistance: " + moveDistance);

        requestLayout();
    }

    /**
     * 重置
     *
     * @param startY 起始Y
     * @param endY   结束Y
     */
    private void reset(int startY, int endY) {
        Log.d(TAG, "startY: " + startY + " endY: " + endY);
        mValueAnimator = ValueAnimator.ofInt(startY, endY);
        mValueAnimator.setDuration(mAnimatorDuration);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                Log.d(TAG, "value: " + value);
                moveDistance = value;
                requestLayout();
            }
        });
        mValueAnimator.addListener(mAutoRefreshAnimatorListener);
        mValueAnimator.start();

    }

    private class AutoRefreshAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
            if (isAutoRefresh && mIRefreshHead != null) {
                mIRefreshHead.onStart();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            ValueAnimator valueAnimator = (ValueAnimator) animation;
            int value = (int) valueAnimator.getAnimatedValue();
            if (mIRefreshHead != null) {
                mIRefreshHead.onFingerUpNotTrigger(value);
            }
            if (isAutoRefresh && null != mOnRefreshListener) {
                mOnRefreshListener.onRefresh();
            }
            isAutoRefresh = false;
            mAnimatorDuration = ANIMATOR_DEFAULT_DURATION;
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }


    /**
     * 设置刷新
     *
     * @param refresh true为刷新,false为关闭刷新
     */
    private void setRefresh(boolean refresh) {
        if (refresh) {
            mDirection = DIRECTION_DOWN;
            mStatus = STATUS_REFRESH;
            isAutoRefresh = true;
            mAnimatorDuration = ANIMATOR_AUTO_REFRESH_DURATION;
            reset(0, mHeadViewHeight);
        } else {
            if (mStatus == STATUS_NONE) {
                return;
            }
            if (mDirection == DIRECTION_DOWN) {
                reset(mHeadViewHeight, 0);
                if (mIRefreshHead != null) {
                    mIRefreshHead.onStop();
                }
            } else if (mDirection == DIRECTION_UP) {
                reset(-mFootViewHeight, 0);
                if (mILoadMoreFoot != null) {
                    mILoadMoreFoot.onStop();
                }
            }
            mStatus = STATUS_NONE;
        }
    }

    public void manualRefresh() {
        setRefresh(true);
    }

    public void refreshComplete() {
        setRefresh(false);
    }

    /**
     * 当前是否在加载中或是刷新中
     */
    public boolean isRefresh() {
        return mStatus != STATUS_NONE;
    }

    /**
     * 开关上拉加载更多
     */
    public void setEnableLoadMore(boolean enableLoadMore) {
        this.mEnableLoadMore = enableLoadMore;
    }

    public void setCanPullRefresh(boolean canPullRefresh) {
        this.mCanPullRefresh = canPullRefresh;
    }

    /**
     * 设置刷新回调接口
     *
     * @param onRefreshListener 刷新回调接口
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    /**
     * 刷新回调接口
     */
    public interface OnRefreshListener {
        /**
         * 当刷新被触发时会调用此方法
         */
        void onRefresh();

        /**
         * 上拉 或 刷新时调用
         */
        void onPull(int direction, int distance);

        /**
         * 当加载更多时会调用此方法
         */
        void onLoadMore();
    }
}
