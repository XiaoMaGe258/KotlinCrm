package com.max.app.kotlincrm.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.max.app.kotlincrm.R

/**
 * 自定义RecyclerDivider
 * Created by Xmg on 2018-4-25.
 *
 * 使用：
 * recycler.addItemDecoration(MyRecyclerDivider(context))
 */
class MyRecyclerDivider : RecyclerView.ItemDecoration {

    private var dividerHeight: Int = 0
    private var dividerPaint: Paint? = null

    constructor(context: Context) {
        this.dividerPaint = Paint()
        this.dividerPaint!!.color = context.resources.getColor(R.color.list_divider)
        this.dividerHeight = dip2px(context, 1f)
    }

    constructor(context: Context, colorId: Int) {
        this.dividerPaint = Paint()
        this.dividerPaint!!.color = colorId
        this.dividerHeight = dip2px(context, 1f)
    }

    constructor(context: Context, colorId: Int, dividerHeight: Int) {
        this.dividerPaint = Paint()
        this.dividerPaint!!.color = context.resources.getColor(colorId)
        this.dividerHeight = dip2px(context, dividerHeight.toFloat())
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val childCount = parent.childCount
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        for (i in 0 until childCount - 1) {
            val view = parent.getChildAt(i)
            val top = view.bottom.toFloat()
            val bottom = (view.bottom + dividerHeight).toFloat()
            c.drawRect(left.toFloat(), top, right.toFloat(), bottom, dividerPaint!!)
        }
    }

    override fun getItemOffsets
            (outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        // 在底部空出1dp的空间，画divider线
        outRect.bottom = dividerHeight
    }

    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}