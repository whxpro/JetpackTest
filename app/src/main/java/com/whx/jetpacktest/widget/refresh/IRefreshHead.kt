package com.whx.jetpacktest.widget.refresh

interface IRefreshHead {
    /**
     * 开始下拉,只调用一次
     */
    fun onStart()

    /**
     * 下拉中 多次被调用
     *
     * @param distance 头布局距离顶部的距离
     */
    fun onPullDown(distance: Int)

    /**
     * 下拉到边界 多次被调用
     */
    fun onBound()

    /**
     * 松手 多次被调用，未触发刷新
     *
     * @param distance 头布局距离顶部的距离
     */
    fun onFingerUpNotTrigger(distance: Int)

    /**
     * 触发刷新
     */
    fun onTriggerRefresh()

    /**
     * 结束
     */
    fun onStop()

    /**
     * 头布局的view高度
     *
     * @return 返回头布局view的高度(px)像素
     */
    fun headViewHeight(): Int
}