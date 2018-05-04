package com.max.app.kotlincrm.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.max.app.kotlincrm.R
import com.max.app.kotlincrm.api.JZBController
import com.max.app.kotlincrm.api.JzbResponseHandler
import com.max.app.kotlincrm.items.ContactItem
import com.max.app.kotlincrm.ui.EnterpriseDetailActivity
import com.max.app.kotlincrm.ui.view.MyRecyclerDivider
import com.max.app.kotlincrm.utils.Utils
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_tab_enterprise_contact_list_item.*
import kotlinx.android.synthetic.main.fragment_tab_enterprise_follow.view.*
import org.json.JSONObject
import java.util.*

/**
 * 联系人
 * Created by Xmg on 2018-5-4.
 */

class TabEnterpriseContact : Fragment(), OnRefreshListener {

    private var mPull2RefreshLayout: SmartRefreshLayout? = null
    private val mContactArray = ArrayList<ContactItem>()
    private var mAdapter: ContactAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.simple_one_recyclerview, null)
        mPull2RefreshLayout = v.pull_2_refresh_layout
        initView(v)
        getData()
        return v
    }

    private fun initView(v: View) {
        mPull2RefreshLayout!!.refreshHeader = MaterialHeader(context)
        mPull2RefreshLayout!!.isEnableOverScrollBounce = false
        mPull2RefreshLayout!!.setEnableHeaderTranslationContent(false)
        mPull2RefreshLayout!!.setOnRefreshListener(this)
        mPull2RefreshLayout!!.isEnableLoadmore = false

        mAdapter = ContactAdapter(R.layout.fragment_tab_enterprise_contact_list_item, mContactArray)
        v.rv_list.layoutManager = LinearLayoutManager(context)
        v.rv_list.addItemDecoration(MyRecyclerDivider(context, R.color.activity_bg_gray, 10))
        v.rv_list.adapter = mAdapter
    }


    override fun onRefresh(refreshlayout: RefreshLayout?) {
        getData()
    }

    private fun getData() {
        JZBController.getInstance(activity).getContactList(
                (activity as EnterpriseDetailActivity).getCId(), object : JzbResponseHandler(activity) {
                    override fun onHttpSuccessStatusOk(jsonObject: JSONObject?) {
                        val jsonArray = jsonObject!!.getJSONArray("data")
                        val type = object : TypeToken<ArrayList<ContactItem>>() {}.type
                        val items = Gson().fromJson<ArrayList<ContactItem>>(jsonArray.toString(), type)
                        mContactArray.clear()
                        mContactArray.addAll(items)
                        mAdapter!!.notifyDataSetChanged()
                        mPull2RefreshLayout!!.finishRefresh(true)
                    }
                }
        )
    }

    inner class ContactAdapter(layoutResId: Int, data: List<ContactItem>) :
            BaseQuickAdapter<ContactItem, BaseViewHolder>(layoutResId, data) {

        override fun convert(holder: BaseViewHolder, items: ContactItem) {
            val position = holder.layoutPosition
            when (position % 4) {
                1 -> holder.setBackgroundRes(R.id.contact_item_icon, R.drawable.round_point_green)
                2 -> holder.setBackgroundRes(R.id.contact_item_icon, R.drawable.round_point_purple)
                3 -> holder.setBackgroundRes(R.id.contact_item_icon, R.drawable.round_point_yellow)
                else -> holder.setBackgroundRes(R.id.contact_item_icon, R.drawable.round_point_blue)
            }
            holder.setGone(R.id.contact_item_edit, true)
            holder.setText(R.id.contact_item_name, items.name)
            holder.setText(R.id.contact_item_icon, items.name.substring(0, 1))
            holder.setText(R.id.contact_item_title, "（${items.title}）")
            if (Utils.isNull(items.phone)) {
                holder.setGone(R.id.contact_item_phone, false)
            } else {
                holder.setGone(R.id.contact_item_phone, true)
                holder.setText(R.id.contact_item_phone,"TEL: ${items.phone}")
            }
            if (Utils.isNull(items.email)) {
                holder.setGone(R.id.contact_item_email, false)
            } else {
                holder.setGone(R.id.contact_item_email, true)
                holder.setText(R.id.contact_item_email,"Email: ${items.email}")
            }
            if (Utils.isNull(items.qq)) {
                holder.setGone(R.id.contact_item_qq, false)
            } else {
                holder.setGone(R.id.contact_item_qq, true)
                holder.setText(R.id.contact_item_qq,"QQ: ${items.qq}")
            }
            holder.getView<TextView>(R.id.contact_item_edit).setOnClickListener({
                val customerId = (activity as EnterpriseDetailActivity).getCId()
//                AddContactActivity.actionActivity(activity, customerId, items.contactId, items)
            })
        }
    }
}