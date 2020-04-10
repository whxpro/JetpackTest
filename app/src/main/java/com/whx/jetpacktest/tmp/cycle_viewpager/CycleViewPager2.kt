package com.whx.jetpacktest.tmp.cycle_viewpager

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import androidx.viewpager2.widget.ViewPager2
import com.whx.jetpacktest.R
import com.whx.jetpacktest.tmp.Indicator
import java.lang.ref.WeakReference
import java.util.*

class CycleViewPager2 @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defArr: Int = 0) :
    FrameLayout(context, attrs, defArr) {
    private val mViewPager2: ViewPager2 = ViewPager2(context)

    private var canAutoTurning = false
    private var autoTurningTime: Long = 0
    private var isTurning = false
    private var mAutoTurningRunnable: AutoTurningRunnable? = null

    private var mPendingCurrentItem = NO_POSITION
    private var mIndicator: Indicator? = null

    private val mAdapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            val itemCount = Objects.requireNonNull<RecyclerView.Adapter<*>>(getAdapter()).itemCount
            if (itemCount <= 1) {
                if (isTurning) {
                    stopAutoTurning()
                }
            } else {
                if (!isTurning) {
                    startAutoTurning()
                }
            }
            if (mIndicator != null) {
                mIndicator!!.onChanged(getPagerRealCount(), getRealCurrentItem())
            }
        }
    }

    init {
        initWithAttrs(attrs)

        mViewPager2.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        mViewPager2.offscreenPageLimit = 1

        val mCycleOnPageChangeCallback = CycleOnPageChangeCallback(this)
        mViewPager2.registerOnPageChangeCallback(mCycleOnPageChangeCallback)

        mAutoTurningRunnable = AutoTurningRunnable(this)

        addView(mViewPager2)
    }

    private fun initWithAttrs(attrs: AttributeSet?) {
        val tpa = context.obtainStyledAttributes(attrs, R.styleable.CycleViewPager2)
        canAutoTurning = tpa.getBoolean(R.styleable.CycleViewPager2_canAutoTurning, false)
        autoTurningTime = tpa.getInt(R.styleable.CycleViewPager2_turningTime, 0).toLong()
        tpa.recycle()
    }

    fun setAutoTurning(autoTurningTime: Long) {
        setAutoTurning(true, autoTurningTime)
    }

    fun setAutoTurning(canAutoTurning: Boolean, autoTurningTime: Long) {
        this.canAutoTurning = canAutoTurning
        this.autoTurningTime = autoTurningTime
        stopAutoTurning()
        startAutoTurning()
    }

    fun startAutoTurning() {
        if (!canAutoTurning || autoTurningTime <= 0 || isTurning) return
        isTurning = true
        postDelayed(mAutoTurningRunnable, autoTurningTime)
    }

    fun stopAutoTurning() {
        isTurning = false
        removeCallbacks(mAutoTurningRunnable)
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        if (adapter is BaseCycleAdapter || adapter is BaseCycleFragmentAdapter) {
            if (mViewPager2.adapter == adapter) return
            adapter.registerAdapterDataObserver(mAdapterDataObserver)
            mViewPager2.adapter = adapter
            setCurrentItem(1, false)
            initIndicator()
            return
        }
        throw IllegalArgumentException("adapter must be an instance of CyclePagerAdapter " +
                "or CyclePagerFragmentAdapter")
    }

    fun getAdapter(): RecyclerView.Adapter<*>? {
        return mViewPager2.adapter
    }

    private fun getPagerRealCount(): Int {
        val adapter = getAdapter()
        if (adapter is BaseCycleAdapter) {
            return adapter.getRealItemCount()
        }
        return if (adapter is BaseCycleFragmentAdapter) {
            adapter.getRealItemCount()
        } else 0
    }

    fun setOrientation(@ViewPager2.Orientation orientation: Int) {
        mViewPager2.orientation = orientation
    }

    @ViewPager2.Orientation
    fun getOrientation(): Int {
        return mViewPager2.orientation
    }

    fun setPageTransformer(transformer: ViewPager2.PageTransformer?) {
        mViewPager2.setPageTransformer(transformer)
    }

    fun addItemDecoration(decor: RecyclerView.ItemDecoration) {
        mViewPager2.addItemDecoration(decor)
    }

    fun addItemDecoration(decor: RecyclerView.ItemDecoration, index: Int) {
        mViewPager2.addItemDecoration(decor, index)
    }

    fun setCurrentItem(item: Int) {
        setCurrentItem(item, true)
    }

    fun setCurrentItem(item: Int, smoothScroll: Boolean) {
//        Logger.d("setCurrentItem $item")
        mViewPager2.setCurrentItem(item, smoothScroll)
        if (!smoothScroll && mIndicator != null) {
            mIndicator!!.onPageSelected(getRealCurrentItem())
        }
    }

    fun getCurrentItem(): Int {
        return mViewPager2.currentItem
    }

    fun getRealCurrentItem(): Int {
        return if (getCurrentItem() >= 1) getCurrentItem() - 1 else getCurrentItem()
    }

    fun setOffscreenPageLimit(@ViewPager2.OffscreenPageLimit limit: Int) {
        mViewPager2.offscreenPageLimit = limit
    }

    fun getOffscreenPageLimit(): Int {
        return mViewPager2.offscreenPageLimit
    }

    fun registerOnPageChangeCallback(callback: ViewPager2.OnPageChangeCallback) {
        mViewPager2.registerOnPageChangeCallback(callback)
    }

    fun unregisterOnPageChangeCallback(callback: ViewPager2.OnPageChangeCallback) {
        mViewPager2.unregisterOnPageChangeCallback(callback)
    }

    fun getViewPager2(): ViewPager2 {
        return mViewPager2
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.actionMasked
        if (action == MotionEvent.ACTION_DOWN) {
            if (canAutoTurning && isTurning) {
                stopAutoTurning()
            }
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL ||
            action == MotionEvent.ACTION_OUTSIDE) {
            if (canAutoTurning) startAutoTurning()
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAutoTurning()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAutoTurning()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState ?: return null)
        ss.mCurrentItem = getCurrentItem()
//        Logger.d("onSaveInstanceState: " + ss.mCurrentItem)
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        mPendingCurrentItem = state.mCurrentItem
//        Logger.d("onRestoreInstanceState: $mPendingCurrentItem")
        restorePendingState()
    }

    private fun restorePendingState() {
        if (mPendingCurrentItem == NO_POSITION) {
            // No state to restore, or state is already restored
            return
        }
        val currentItem = 0.coerceAtLeast(mPendingCurrentItem.coerceAtMost(
            ((getAdapter())?.itemCount ?: 0) - 1))
//        Logger.d("restorePendingState: $currentItem")
        mPendingCurrentItem = NO_POSITION
        setCurrentItem(currentItem, false)
    }

    fun setIndicator(indicator: Indicator?) {
        if (mIndicator === indicator) return
        removeIndicatorView()
        mIndicator = indicator
        initIndicator()
    }

    private fun initIndicator() {
        if (mIndicator == null || getAdapter() == null) return
        addView(mIndicator!!.getIndicatorView())
        mIndicator!!.onChanged(getPagerRealCount(), getRealCurrentItem())
    }

    private fun removeIndicatorView() {
        removeView(mIndicator?.getIndicatorView() ?: return)
    }

    //1.normal:
    //onPageScrollStateChanged(state=1) -> onPageScrolled... -> onPageScrollStateChanged(state=2)
    // -> onPageSelected -> onPageScrolled... -> onPageScrollStateChanged(state=0)
    //2.setCurrentItem(,true):
    //onPageScrollStateChanged(state=2) -> onPageSelected -> onPageScrolled... -> onPageScrollStateChanged(state=0)
    //3.other: no call onPageSelected()
    //onPageScrollStateChanged(state=1) -> onPageScrolled... -> onPageScrollStateChanged(state=2)
    // -> onPageScrolled... -> onPageScrollStateChanged(state=0)
    private class CycleOnPageChangeCallback(cycleViewPager2: CycleViewPager2) : ViewPager2.OnPageChangeCallback() {
        private var isBeginPagerChange: Boolean = false
        private var mTempPosition = INVALID_ITEM_POSITION
        private val weakReference = WeakReference<CycleViewPager2>(cycleViewPager2)

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//            Logger.d("onPageScrolled: " + position + " positionOffset: " + positionOffset
//                    + " positionOffsetPixels: " + positionOffsetPixels)

            weakReference.get()?.mIndicator?.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
//            Logger.d("onPageSelected: $position")
            if (isBeginPagerChange) {
                mTempPosition = position
            }

            weakReference.get()?.mIndicator?.onPageSelected(weakReference.get()?.getRealCurrentItem() ?: return)
        }

        override fun onPageScrollStateChanged(state: Int) {
//            Logger.d("onPageScrollStateChanged: state $state")
            val cp = weakReference.get() ?: return
            if (state == ViewPager2.SCROLL_STATE_DRAGGING || cp.isTurning && state ==
                ViewPager2.SCROLL_STATE_SETTLING) {
                isBeginPagerChange = true
            } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                isBeginPagerChange = false
                val fixCurrentItem = getFixCurrentItem(mTempPosition)
                if (fixCurrentItem != INVALID_ITEM_POSITION && fixCurrentItem != mTempPosition) {
                    mTempPosition = INVALID_ITEM_POSITION
//                    Logger.d("onPageScrollStateChanged: fixCurrentItem $fixCurrentItem")
                    cp.setCurrentItem(fixCurrentItem, false)
                }
            }

            cp.mIndicator?.onPageScrollStateChanged(state)
        }

        private fun getFixCurrentItem(position: Int): Int {
            if (position == INVALID_ITEM_POSITION) return INVALID_ITEM_POSITION
            val lastPosition = Objects.requireNonNull<RecyclerView.Adapter<*>>(weakReference
                .get()?.getAdapter()).itemCount - 1
            var fixPosition = INVALID_ITEM_POSITION
            if (position == 0) {
                fixPosition = if (lastPosition == 0) 0 else lastPosition - 1
            } else if (position == lastPosition) {
                fixPosition = 1
            }
            return fixPosition
        }

        companion object {
            private const val INVALID_ITEM_POSITION = -1
        }
    }

    private class AutoTurningRunnable(cycleViewPager2: CycleViewPager2) : Runnable {
        private val reference: WeakReference<CycleViewPager2> = WeakReference(cycleViewPager2)

        override fun run() {
            val cycleViewPager2 = reference.get()
            if (cycleViewPager2 != null && cycleViewPager2.canAutoTurning && cycleViewPager2.isTurning) {
                val itemCount = cycleViewPager2.getAdapter()?.itemCount ?: 0
                if (itemCount == 0) return
                val nextItem = (cycleViewPager2.getCurrentItem() + 1) % itemCount
                cycleViewPager2.setCurrentItem(nextItem, true)
                cycleViewPager2.postDelayed(cycleViewPager2.mAutoTurningRunnable, cycleViewPager2.autoTurningTime)
            }
        }
    }

    private class SavedState : BaseSavedState {
        var mCurrentItem: Int = 0

        constructor(source: Parcel?) : super(source) {
            readValues(source, null)
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        constructor(source: Parcel?, loader: ClassLoader?) : super(source, loader) {
            readValues(source, loader)
        }

        constructor(superState: Parcelable) : super(superState)

        private fun readValues(source: Parcel?, loader: ClassLoader?) {
            mCurrentItem = source?.readInt() ?: 0
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(mCurrentItem)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.ClassLoaderCreator<SavedState> {
            override fun createFromParcel(source: Parcel?, loader: ClassLoader?): SavedState {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    SavedState(source, loader)
                else
                    SavedState(source)
            }
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}