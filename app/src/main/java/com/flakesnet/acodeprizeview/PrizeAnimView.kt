package com.flakesnet.acodeprizeview

import android.animation.*
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast


class PrizeAnimView : ViewGroup {
    var scaleOffset = 1.5
    var marginTop = 100
    var asAlpha = false
    var asStart = true
    var data = mutableListOf<PrizeModel>()
    var childIndex = 0
    var delay = 3000L

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        /**
         * SpecMode
         * 1,UNSPECIFIED 父控件不对你有任何限制，你想要多大给你多大，想上天就上天。这种情况一般用于系统内部，表示一种测量状态。（这个模式主要用于系统内部多次Measure的情形，并不是真的说你想要多大最后就真有多大）
         * 2,EXACTLY 父控件已经知道你所需的精确大小，你的最终大小应该就是这么大。
         * 3,AT_MOST 你的大小不能大于父控件给你指定的size，但具体是多少，得看你自己的实现。
         */
        Log.e("星系view", "------开始测量------")
        //父控件传进来的宽度和高度以及对应的测量模式
        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val modeWidth = MeasureSpec.getMode(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)
        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)

        //如果当前ViewGroup的宽高为wrap_content的情况
        var width = 0 //自己测量的 宽度
        var height = 0 //自己测量的高度
        //计算高度
        val child = getChildAt(0)
        measureChild(child, widthMeasureSpec, heightMeasureSpec)
        //得到LayoutParams
        val lp = child.layoutParams as MarginLayoutParams
        //子View占据的宽度
        val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
        //子View占据的高度
        val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin
        width = childWidth
        height += childHeight
        height += (scaleOffset * childHeight).toInt()
        height += marginTop

        for (index in 1 until childCount) {
            val child = getChildAt(index)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
        }
        Log.e("星系view", "容器高度：$height")
        //wrap_content
        setMeasuredDimension(
            if (modeWidth == MeasureSpec.EXACTLY) sizeWidth else width,
            if (modeHeight == MeasureSpec.EXACTLY) sizeHeight else height
        )

    }

    override fun onLayout(p0: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        Log.e("星系view", "------开始布局------   $childCount")
        for (index in 0 until childCount) {
            val child = getChildAt(index)
            val lp = child.layoutParams as MarginLayoutParams
            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            Log.e(
                "星系view",
                "$index    view's width:${child.width}  view's height:${child.height}"
            )
            child.layout(
                0,
                height - childHeight,
                childWidth,
                height
            )
        }
        if (asStart) {
            asStart = false
            startAnim()
        }
    }


    val han = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val index = msg.what
            Log.e("lllllll", "index:$index")
            val child = getChildAt(index)
            createAnim(child)
        }
    }

    //执行动画
    fun startAnim() {
        han.sendEmptyMessage(childCount - 1)
    }

    //执行动画
    fun startAnim(index: Int) {
        han.sendEmptyMessage(index)
    }


    //创建动画
    private fun createAnim(childeView: View) {
        addChildView()
        Log.e("星系view", "childCount:$childCount  容器高：$height   item高：${childeView.height}")
        val translationY: ObjectAnimator =
            ObjectAnimator.ofFloat(
                childeView,
                "translationY",
                0f,
                -(height - (childeView.height * scaleOffset)).toFloat()
            )
        val translationX: ObjectAnimator =
            ObjectAnimator.ofFloat(
                childeView,
                "translationX",
                0f,
                ((childeView.width * scaleOffset) - childeView.width).toFloat() / 2
            )
        Log.e(
            "dddd",
            "ddddd::${childeView.width}    ${((childeView.width * scaleOffset) - childeView.width).toFloat()}"
        )
        // 2.缩放动画
        val scaleX = ObjectAnimator.ofFloat(childeView, "scaleX", 1f, scaleOffset.toFloat())
        val scaleY = ObjectAnimator.ofFloat(childeView, "scaleY", 1f, scaleOffset.toFloat())
        val animatorSet = AnimatorSet()
        animatorSet.duration = delay
        if (asAlpha) {
            val alpha = ObjectAnimator.ofFloat(getChildAt(childCount - 1), "alpha", 1f, 0f)
            animatorSet.playTogether(alpha, translationX, translationY, scaleX, scaleY)
        } else {
            animatorSet.playTogether(translationX, translationY, scaleX, scaleY)
        }
        animatorSet.start()
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // TODO Auto-generated method stub
                super.onAnimationEnd(animation)
                if (asAlpha) {
                    Log.e("星系view", "动画结束  childCount:$childCount  删除:${childCount - 1}")
                    removeViewAt(childCount - 1)
                    startAnim(childCount - 2)
                } else {
                    Log.e("星系view", "动画结束  childCount:$childCount  没有remove  执行倒数第二个view的动画")
                    startAnim(childCount - 2)
                }
                asAlpha = true
            }
        })
    }


    fun create() {
        if (data.isNullOrEmpty()) {
            Toast.makeText(context, "最少输入一条数据", Toast.LENGTH_LONG).show()
            return
        }
        addChildView()
//        startAnim()
    }


    //添加子view
    fun addChildView() {
        val view = View.inflate(context, R.layout.prize_view_item, null)
        view.layoutParams = MarginLayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        val name = view.findViewById<TextView>(R.id.tvUserName)
        name.text = data[childIndex % data.size].name
        addView(view, 0)
        childIndex++
    }

    /**
     * 与当前ViewGroup对应的LayoutParams
     */
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams? {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams? {
        return MarginLayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams? {
        return MarginLayoutParams(p)
    }
}