package com.max.app.kotlincrm

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.max.app.kotlincrm.api.JZBConstants
import com.max.app.kotlincrm.ui.BaseActivity
import com.max.app.kotlincrm.utils.GlideCircleTransform
import com.max.app.kotlincrm.utils.Sp
import kotlinx.android.synthetic.main.activity_main_manager.*
import java.util.ArrayList
import android.support.v7.widget.GridLayoutManager
import android.view.View


/**
 * 首页
 * Created by Xmg on 2018-4-18.
 */
class MainManagerActivity : BaseActivity(), BaseQuickAdapter.OnItemClickListener {

    private val mGridViewList = ArrayList<GridViewItem>()
//    var mAdapter = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_manager)
        initView()
    }

    private fun initView(){
        Sp.init_SP_Instance(mContext, JZBConstants.SP_USERINFO)
        Glide.with(mContext).load(Sp.get_String(JZBConstants.AVATAR, ""))
                .placeholder(R.drawable.ab_icon).transform(GlideCircleTransform(mContext))
                .into(iv_user_logo)
        tv_user_name.text = Sp.get_String(JZBConstants.TRUENAME, "")
        tv_user_role.text = Sp.get_String(JZBConstants.ROLENAME, "")

        initGridView()
        gv_grid.layoutManager = GridLayoutManager(this, 4)
        val gridAdapter = GridViewAdapter(R.layout.home_gridview_item, mGridViewList)
        gv_grid.adapter = gridAdapter
        gridAdapter.onItemClickListener = this

    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val item = adapter!!.getItem(position) as GridViewItem
        handlerGridViewItemOnclick(item.name)
    }

    private fun handlerGridViewItemOnclick(name: String) {
        when(name){
            "个人客户池" -> {
//            PrivateLibraryActivity.actionPrivateLibraryActivity(mContext)
            }
            "公共客户池" -> {
//            PublicLibraryActivity.actionPublicLibraryActivity(mContext)
            }
            "付费客户池" -> {
//            PayMoneyActivity.actionPayMoneyActivity(mContext)
            }
            "添加客户" -> {
//            UnfamiliarVisitedActivity.actionUnfamiliarVisitedActivity(mContext, false)
            }
        }
    }

    private fun initGridView(){
        mGridViewList.add(GridViewItem("个人客户池", R.drawable.ic_private_library))
        mGridViewList.add(GridViewItem("公共客户池", R.drawable.ic_public_library))
        mGridViewList.add(GridViewItem("付费客户池", R.drawable.ic_pay_library))
        mGridViewList.add(GridViewItem("添加客户", R.drawable.ic_unfamiliar_visit))
    }

    inner class GridViewItem
    @JvmOverloads constructor(
            internal var name: String,
            internal var icon: Int
    )

    inner class GridViewAdapter(layoutResId: Int, data: List<GridViewItem>) : BaseQuickAdapter<GridViewItem, BaseViewHolder>(layoutResId, data) {

        override fun convert(helper: BaseViewHolder, item: GridViewItem) {
            helper.setText(R.id.grid_view_item_name, item.name)
            helper.setImageResource(R.id.grid_view_item_image, item.icon)
        }
    }


}