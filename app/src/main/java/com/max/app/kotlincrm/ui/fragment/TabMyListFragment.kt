package com.max.app.kotlincrm.ui.fragment;

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.max.app.kotlincrm.R
import com.max.app.kotlincrm.api.JZBController
import com.max.app.kotlincrm.api.JzbResponseHandler
import com.max.app.kotlincrm.utils.Utils
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_tab_list.view.*
import org.json.JSONObject
import java.util.*

class TabListFragment: Fragment(), OnRefreshListener, OnLoadmoreListener {

    private var mPull2RefreshLayout: SmartRefreshLayout? = null
    private var mListView: RecyclerView? = null
    private val mItems = ArrayList<PrivateLibraryListItem>()
    private var mAdapter: LibraryAdapter? = null
    private var mStartIndex = 0
    private val mPageSize = 10

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_tab_list, null)
        mListView = v.rv_list
        mPull2RefreshLayout = v.pull_2_refresh_layout
        initView()
        getData(true)
        return v
    }

    private fun initView(){
        mPull2RefreshLayout!!.refreshHeader = MaterialHeader(context)
        mPull2RefreshLayout!!.refreshFooter = ClassicsFooter(context)
        mPull2RefreshLayout!!.isEnableOverScrollBounce = false
        mPull2RefreshLayout!!.setEnableHeaderTranslationContent(false)
        mPull2RefreshLayout!!.setOnRefreshListener(this)
        mPull2RefreshLayout!!.setOnLoadmoreListener(this)

        mListView!!.layoutManager = LinearLayoutManager(context)
        mAdapter = LibraryAdapter(R.layout.private_library_list_view_item, mItems)
        mListView!!.adapter = mAdapter
    }

    private fun getData(isRefreshing: Boolean) {
        JZBController.getInstance(activity).getPrivateLibraryList(
                mStartIndex * mPageSize, mPageSize, object : JzbResponseHandler(activity){
            override fun onHttpSuccessStatusOk(jsonObject: JSONObject?) {
                if (isRefreshing) {
                    mItems.clear()
                }
                val data = jsonObject!!.getJSONArray("data")
                val type = object : TypeToken<ArrayList<PrivateLibraryListItem>>() {}.type
                val items = Gson().fromJson<ArrayList<PrivateLibraryListItem>>(data.toString(), type)

                mItems.addAll(items)
                mAdapter!!.notifyDataSetChanged()
                mPull2RefreshLayout!!.finishRefresh(true)
                mPull2RefreshLayout!!.finishLoadmore(true)
                mPull2RefreshLayout!!.isEnableLoadmore = items.size == mPageSize
            }
        })
    }

    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        getData(false)
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        getData(true)
    }

    inner class LibraryAdapter(layoutResId: Int, data: List<PrivateLibraryListItem>) :
            BaseQuickAdapter<PrivateLibraryListItem, BaseViewHolder>(layoutResId, data) {

        override fun convert(holder: BaseViewHolder, item: PrivateLibraryListItem) {
            holder.setText(R.id.private_library_item_name, item.name)
            holder.setText(R.id.private_library_item_customer_level, item.level)
            holder.setText(R.id.private_library_item_customer_source, item.source)
            holder.setText(R.id.private_library_item_customer_date, item.surplusDay)
            var type = "其他跟进："
            if ("拜访" == item.followupType) {
                type = "拜访跟进："
            } else if ("电话" == item.followupType) {
                type = "电话跟进："
            }
            holder.setText(R.id.transaction_item_follow_name, if (Utils.isNull(item.followupObject)) "" else type + item.followupObject)
            holder.setText(R.id.private_library_item_follow_date, if (Utils.isNull(item.followupDate)) "" else item.followupDate)
            holder.setText(R.id.private_library_item_follow_content, if (Utils.isNull(item.followupContent)) "" else item.followupContent)

            holder.setGone(R.id.private_library_item_bd_layout, false)

            //如果跟进信息都是空的，就隐藏跟进区域
            if (Utils.isNull(item.followupObject) && Utils.isNull(item.followupDate)
                    && Utils.isNull(item.followupContent) && Utils.isNull(item.contactInformation)) {
                holder.setGone(R.id.private_library_item_follow_layout, false)
            } else {
                holder.setGone(R.id.private_library_item_follow_layout, true)
            }

            holder.getView<ViewGroup>(R.id.private_library_item_call_layout).setOnClickListener {
                if (!TextUtils.isEmpty(item.contactInformation)) {
                    Utils.actionCall(activity, item.contactInformation)
                }
            }
        }
    }

    inner class PrivateLibraryListItem {
        var customerId: String? = null
        var name: String? = null
        var level: String? = null
        var levelDec: String? = null//分类详情： B类客户（可挖掘客户）
        var source: String? = null
        var followupTimes: String? = null//跟进次数
        var followupType: String? = null//跟进类型（电话/拜访）
        var followupObject: String? = null//跟进对象
        var followupContent: String? = null//跟进内容
        var followupDate: String? = null//跟进时间
        var contactInformation: String? = null//电话
        var applyContact: Boolean = false//已申请签约
        var applyTryout: Boolean = false//已申请试用
        var tryouted: Boolean = false//已试用
        var surplusDay: String? = null
        var bdName: String? = null
        var ct: String? = null//下级—— 创建时间
        var creatorName: String? = null//下级—— 创建人
    }

}