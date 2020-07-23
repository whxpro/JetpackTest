package com.whx.jetpacktest.widget.refresh

interface ILoadMoreFoot {
    /**
     * 开始上拉 调用一次
     */
    fun onStart()

    /**
     * 上拉中 多次被调用
     *
     * @param distance 脚布局距离底部的距离
     */
    fun onPullUp(distance: Int)

    /**
     * 下拉到边界 多次被调用
     */
    fun onBound()

    /**
     * 松手 多次被调用
     *
     * @param distance 脚布局距离底部的距离
     */
    fun onFingerUp(distance: Int)

    /**
     * 结束
     */
    fun onStop()

    /**
     * 脚布局的view高度
     *
     * @return 返回脚布局view的高度(px)像素.
     */
    fun footViewHeight(): Int
}