package com.max.app.kotlincrm

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.max.app.kotlincrm.adapter.TBaseAdapter
import com.max.app.kotlincrm.api.JZBConstants
import com.max.app.kotlincrm.ui.BaseActivity
import com.max.app.kotlincrm.utils.GlideCircleTransform
import com.max.app.kotlincrm.utils.Sp
import kotlinx.android.synthetic.main.activity_main_manager.*
import java.util.ArrayList

/**
 * 首页
 * Created by Xmg on 2018-4-18.
 */
class MainManagerActivity : BaseActivity() {

    private val mGridViewList = ArrayList<GridViewItem>()

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
//        gv_grid.adapter = GridViewAdapter(mGridViewList, mContext)
//        gv_grid.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//            val item = parent.getItemAtPosition(position) as GridViewItem
////            handlerGridViewItemOnclick(item.name)
//        }
    }

    private fun initGridView(){
        mGridViewList.add(GridViewItem("个人客户池", R.drawable.ic_private_library))
        mGridViewList.add(GridViewItem("公共客户池", R.drawable.ic_public_library))
        mGridViewList.add(GridViewItem("付费客户池", R.drawable.ic_pay_library))
        mGridViewList.add(GridViewItem("添加客户", R.drawable.ic_unfamiliar_visit))
    }

    private inner class GridViewItem
    @JvmOverloads constructor(
            internal var name: String,
            internal var icon: Int,
            internal var showRedPoint: Boolean = false
    )

    private inner class GridViewAdapter(list: List<GridViewItem>, context: Activity) : TBaseAdapter<GridViewItem>(context, list) {
        override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
            var convertView = convertView
            convertView = mLayoutInflater.inflate(R.layout.home_gridview_item, parent, false)
            val nameText = convertView.findViewById(R.id.grid_view_item_name) as TextView
            val iconImage = convertView.findViewById(R.id.grid_view_item_image) as ImageView
            val item = getItem(position) as GridViewItem
            nameText.text = item.name
            iconImage.setImageResource(item.icon)

            if ("" != item.name) {
                convertView.setBackgroundResource(R.drawable.gridview_select)
            } else {
                convertView.setBackgroundResource(R.drawable.gridview_select_no_use)
            }
            return convertView
        }
    }
}