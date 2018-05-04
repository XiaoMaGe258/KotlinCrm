package com.max.app.kotlincrm.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.max.app.kotlincrm.R

/**
 * 自定义Recycler间隔
 * Created by Xmg on 2018-5-4.
 *
 * 使用：
 * recycler.addItemDecoration(MyRecyclerDecoration(context, 5))
 */
class MyRecyclerDecoration : RecyclerView.ItemDecoration {

    //间距（gridView子项间距）
    private var space: Int = 0

    constructor(context: Context, space: Int) {
        this.space = dip2px(context, space)
    }

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State?) {
        outRect.top = space
        outRect.left = space
        outRect.right = space
        outRect.bottom = space

        // Add top margin only for the first item to avoid double space between items
//        if (parent.getChildPosition(view) == 0)
//          outRect.top = space
    }

    fun dip2px(context: Context, dipValue: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}