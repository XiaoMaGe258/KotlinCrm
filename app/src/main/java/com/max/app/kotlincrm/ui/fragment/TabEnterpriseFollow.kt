package com.max.app.kotlincrm.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.max.app.kotlincrm.R
import com.max.app.kotlincrm.api.JZBConstants
import com.max.app.kotlincrm.api.JZBController
import com.max.app.kotlincrm.api.JzbResponseHandler
import com.max.app.kotlincrm.items.FollowItem
import com.max.app.kotlincrm.ui.EnterpriseDetailActivity
import com.max.app.kotlincrm.utils.DialogUtils
import com.max.app.kotlincrm.utils.MyToast
import com.max.app.kotlincrm.utils.SpannableStringUtil
import com.max.app.kotlincrm.utils.Utils
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_tab_enterprise_follow.*
import kotlinx.android.synthetic.main.fragment_tab_enterprise_follow.view.*
import org.json.JSONObject
import java.util.*

/**
 * 跟进记录
 * Created by Xmg on 2018-4-24.
 */

class TabEnterpriseFollow : Fragment(), OnRefreshListener {

    private var mStartIndex = 0
    private val mPageSize = 10
    private var mPull2RefreshLayout: SmartRefreshLayout? = null
    private val mFollowItemsList = ArrayList<FollowItem>()
    private var mAdapter: FollowLogsAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_tab_enterprise_follow, null)
        mPull2RefreshLayout = v.pull_2_refresh_layout
        initActionbar()
        initView(v)
        getData()
        return v
    }

    private fun initActionbar() {

    }

    private fun initView(v: View) {
        mPull2RefreshLayout!!.refreshHeader = MaterialHeader(context)
        mPull2RefreshLayout!!.isEnableOverScrollBounce = false
        mPull2RefreshLayout!!.setEnableHeaderTranslationContent(false)
        mPull2RefreshLayout!!.setOnRefreshListener(this)
        mPull2RefreshLayout!!.isEnableLoadmore = false

        mAdapter = FollowLogsAdapter(R.layout.fragment_tab_follow_list_item, mFollowItemsList)
        v.rv_list.layoutManager = LinearLayoutManager(context)
        v.rv_list.adapter = mAdapter
    }


    override fun onRefresh(refreshlayout: RefreshLayout?) {
        getData()
    }

    private fun getData() {
        JZBController.getInstance(activity).getFollowLogs(
                (activity as EnterpriseDetailActivity).getCId(), mStartIndex * mPageSize, mPageSize,
                object : JzbResponseHandler(activity) {
                    override fun onHttpSuccessStatusOk(jsonObject: JSONObject?) {
                        //顶部信息
                        val json2Array = jsonObject!!.getJSONArray("data2")
                        val topJson = json2Array.getJSONObject(0)
                        val visitCount = topJson.optInt("baifang", 0)
                        val callCount = topJson.optInt("dianhua", 0)
                        refreshTopLayout(visitCount, callCount)

                        val json1Array = jsonObject!!.getJSONArray("data1")
                        val type = object : TypeToken<ArrayList<FollowItem>>() {}.type
                        val items = Gson().fromJson<ArrayList<FollowItem>>(json1Array.toString(), type)
                        mFollowItemsList.clear()
                        mFollowItemsList.addAll(items)
                        mAdapter!!.notifyDataSetChanged()
                        mPull2RefreshLayout!!.finishRefresh(true)
                    }
                }
        )
    }

    private fun refreshTopLayout(visitCount: Int, callCount: Int) {
        tv_visit_count.text = SpannableStringUtil.getForeColorSpan("拜访次数：$visitCount",
                0, 5, Color.parseColor("#b2b2b2"))
        tv_call_count.text = SpannableStringUtil.getForeColorSpan("电话数量：$callCount",
                0, 5, Color.parseColor("#b2b2b2"))
    }

    inner class FollowLogsAdapter(layoutResId: Int, data: List<FollowItem>) :
            BaseQuickAdapter<FollowItem, BaseViewHolder>(layoutResId, data) {

        override fun convert(holder: BaseViewHolder, items: FollowItem) {
            if ("拜访" == items.followupType) {
                holder.setBackgroundRes(R.id.follow_item_type_flag, R.drawable.round_purple_bg)
            } else {
                holder.setBackgroundRes(R.id.follow_item_type_flag, R.drawable.round_yellow_green_bg)
            }
            holder.setText(R.id.follow_item_type_flag, items.followupType)
            holder.setText(R.id.follow_item_person_from, items.followuper)
            holder.setText(R.id.follow_item_date, items.followupDate
                    + if (Utils.isNull(items.peifang)) "" else " [" + items.peifang + "]")
            holder.setText(R.id.follow_item_person, if (Utils.isNull(items.followupObject)) "" else items.followupObject)
            holder.setText(R.id.follow_item_address, if (Utils.isNull(items.location)) "" else items.location)
            holder.setText(R.id.follow_item_content, if (Utils.isNull(items.content)) "" else items.content)
            if (!Utils.isNull(items.label)) {
                holder.setGone(R.id.follow_item_tags, true)
                holder.setText(R.id.follow_item_tags, items.label.replace(",", " | "))
            } else {
                holder.setGone(R.id.follow_item_tags, false)
            }
            if ((activity as EnterpriseDetailActivity).getFrom() == JZBConstants.PUBLIC_LIBRARY) {
                holder.setGone(R.id.follow_item_call_layout, false)
                holder.setGone(R.id.follow_up_leave_btn, false)
            } else {
                holder.setGone(R.id.follow_item_call_layout, true)
                holder.setGone(R.id.follow_up_leave_btn, true)
            }
            if (Utils.isNull(items.followupObject)) {//如果没有姓名信息，就不显示姓名
                holder.setGone(R.id.follow_item_person, false)
            } else {
                holder.setGone(R.id.follow_item_person, true)
            }
            if (Utils.isNull(items.contactInformation)) {//如果没有电话信息，就不显示电话按钮
                holder.setGone(R.id.follow_item_call_layout, false)
            } else {
                holder.setGone(R.id.follow_item_call_layout, true)
            }
            //离岗按钮
            if (Utils.isNull(items.endDate) && -1 == items.status) {
                holder.setGone(R.id.follow_up_leave_btn, false)
            } else {
                holder.setGone(R.id.follow_up_leave_btn, true)
            }
            //待完善区域
            if (items.status == -1) {
                //可点击
                holder.setGone(R.id.follow_item_status_wait, false)
                holder.setGone(R.id.follow_item_content_tag, true)
                holder.setGone(R.id.follow_item_content_flag, true)
                holder.setGone(R.id.follow_item_content, false)
                holder.getView<ViewGroup>(R.id.follow_item_content_layout).isClickable = true
            } else {
                if (items.status == 0) {
                    holder.setGone(R.id.follow_item_status_wait, true)
                } else {
                    holder.setGone(R.id.follow_item_status_wait, false)
                }
                //不可点击
                holder.setGone(R.id.follow_item_content_tag, false)
                holder.setGone(R.id.follow_item_content_flag, false)
                holder.setGone(R.id.follow_item_content, true)
                holder.getView<ViewGroup>(R.id.follow_item_content_layout).isClickable = false
            }
            holder.getView<ViewGroup>(R.id.follow_item_call_layout).setOnClickListener {
                Utils.actionCall(activity, items.contactInformation)
            }
            holder.getView<ViewGroup>(R.id.follow_item_content_layout).setOnClickListener {
                //跟进

            }
            holder.getView<ViewGroup>(R.id.follow_up_leave_btn).setOnClickListener {
                DialogUtils.showNoticeDialog(activity, "确定记录离访时间吗？", "", "取消",
                        "确定", null, View.OnClickListener {
                    JZBController.getInstance(activity).postLeaveFollowup("" + items.id,
                            object : JzbResponseHandler(activity) {
                                override fun onHttpSuccessStatusOk(jsonObject: JSONObject?) {
                                    try {
                                        //{"msg":"离访成功!","endDate":"2016-08-11 16:44:58","status":"ok"}
                                        val status = jsonObject!!.getString("status")
                                        if ("ok" == status) {
                                            MyToast.makeText(mContext, "离访成功")
                                            holder.setGone(R.id.follow_up_leave_btn, false)
                                        } else {
                                            val msg = jsonObject!!.getString("msg")
                                            MyToast.makeText(mContext, msg)
                                        }
                                    } catch (e: Exception) {
                                        MyToast.makeText(mContext, "离访失败")
                                    }
                                }
                            }
                    )
                })
            }
        }
    }
}