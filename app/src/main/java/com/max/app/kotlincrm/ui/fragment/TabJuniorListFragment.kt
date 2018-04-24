package com.max.app.kotlincrm.ui.fragment;

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.max.app.kotlincrm.R
import com.max.app.kotlincrm.api.JZBConstants
import com.max.app.kotlincrm.api.JZBController
import com.max.app.kotlincrm.api.JzbResponseHandler
import com.max.app.kotlincrm.ui.EnterpriseDetailActivity
import com.max.app.kotlincrm.utils.L
import com.max.app.kotlincrm.utils.Utils
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_tab_list.*
import kotlinx.android.synthetic.main.fragment_tab_list.view.*
import org.json.JSONObject
import java.util.*

/**
 * 下级客户
 * Created by Xmg on 2018-4-24.
 */
class TabJuniorListFragment: Fragment(), OnRefreshListener, OnLoadmoreListener, View.OnClickListener, TextWatcher {

    private var mPull2RefreshLayout: SmartRefreshLayout? = null
    private val mItems = ArrayList<PrivateLibraryListItem>()
    private var mAdapter: LibraryAdapter? = null
    private var mStartIndex = 0
    private val mPageSize = 10

    private val mFilterItems = ArrayList<ChooseItem>()
    private var mFilterAdapter: FilterAdapter? = null
    private var mIsShowedTopSelectLayout = false//是否在显示过滤窗口
    private var mCurTopListIndex = 0//当前显示的是哪个过滤窗口
    private val mLevelArray = arrayOf("全类客户", "A类客户", "B类客户", "C类客户", "D类客户")
    private val mLevelValue = arrayOf("", "A", "B", "C", "D")
    private val mOrderArray = arrayOf("综合排序", "剩余时间由高到低", "剩余时间由低到高")
    private val mOrderValue = arrayOf("", "desc", "asc")
    private var mBdArray = arrayOf("全部销售")
    private var mBdValue = arrayOf("")
    private var mFilterKeyWord = ""
    private var mFilterLevel = ""
    private var mFilterOrder = ""
    private var mFilterBd = ""

    private var mRlOptionLayout: ViewGroup? = null
    private var mTvOptionLevel: TextView? = null
    private var mTvOptionOrder: TextView? = null
    private var mTvOptionBd: TextView? = null
    private var mEtOptionSearch: EditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_tab_list, null)
        mPull2RefreshLayout = v.pull_2_refresh_layout
        initView(v)
        getData(true)
        return v
    }

    private fun initView(v: View){
        mPull2RefreshLayout!!.refreshHeader = MaterialHeader(context)
        mPull2RefreshLayout!!.refreshFooter = ClassicsFooter(context)
        mPull2RefreshLayout!!.isEnableOverScrollBounce = false
        mPull2RefreshLayout!!.setEnableHeaderTranslationContent(false)
        mPull2RefreshLayout!!.setOnRefreshListener(this)
        mPull2RefreshLayout!!.setOnLoadmoreListener(this)

        mAdapter = LibraryAdapter(R.layout.private_library_list_view_item, mItems)
        v.rv_list.layoutManager = LinearLayoutManager(context)
        v.rv_list.adapter = mAdapter
        mAdapter!!.onItemClickListener = mOnLibraryItemClickListener

        //过滤器
        mFilterAdapter = FilterAdapter(R.layout.simple_one_text_item, mFilterItems)
        v.rv_option.layoutManager = LinearLayoutManager(context)
        v.rv_option.addItemDecoration(MySimpleDivider(context))
        v.rv_option.adapter = mFilterAdapter
        v.ll_filter_customer.setOnClickListener(this)
        v.ll_filter_order.setOnClickListener(this)
        v.ll_filter_bd.visibility = View.VISIBLE
        v.ll_filter_bd.setOnClickListener(this)
        v.v_shade_view.setOnClickListener(this)
        v.iv_clear_search.setOnClickListener(this)
        mRlOptionLayout = v.rl_option_layout
        mTvOptionLevel = v.tv_filter_customer
        mTvOptionOrder = v.tv_filter_order
        mTvOptionBd = v.tv_filter_bd
        mEtOptionSearch = v.et_search_customer
        mFilterAdapter!!.onItemClickListener = mOnFilterItemClickListener
        v.et_search_customer.addTextChangedListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ll_filter_customer -> {
                toggleTopList(0)
                initCustomerLevel()
            }
            R.id.ll_filter_order -> {
                toggleTopList(1)
                initCustomerOrder()
            }
            R.id.ll_filter_bd -> {
                toggleTopList(2)
                initCustomerBd()
            }
            R.id.v_shade_view -> {
                hideTopList()
            }
            R.id.iv_clear_search -> {
                mFilterKeyWord = ""
                mEtOptionSearch!!.setText("")
                getData(true)
            }
        }
    }

    private fun getData(isRefreshing: Boolean) {
        JZBController.getInstance(activity).getJuniorLibraryList(mFilterLevel, mFilterOrder, mFilterBd, mFilterKeyWord,
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
    private var mOnLibraryItemClickListener = BaseQuickAdapter.OnItemClickListener {
        adapter: BaseQuickAdapter<Any, BaseViewHolder>, view: View, position: Int ->
        val item = adapter.getItem(position) as TabMyListFragment.PrivateLibraryListItem
        var flag = ""
        when {
            item.applyContact -> flag = "已申请签约"
            item.applyTryout -> flag = "已申请试用"
            item.tryouted -> flag = "已试用"
        }
        EnterpriseDetailActivity.actionActivity(activity, item.name, item.levelDec, flag,
                item.customerId, false, JZBConstants.PRIVATE_LIBRARY)
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

            holder.setGone(R.id.private_library_item_bd_layout, true)
            holder.setText(R.id.tv_bd_name, "所属销售：${item.bdName}")
            holder.setText(R.id.tv_bd_cn, "创建人：${if (Utils.isNull(item.creatorName)) "-" else item.creatorName}")
            holder.setText(R.id.tv_bd_ct, "创建时间：${item.ct}")

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


    private var mOnFilterItemClickListener = BaseQuickAdapter.OnItemClickListener {
        adapter: BaseQuickAdapter<Any, BaseViewHolder>, view: View, position: Int ->
        val item = adapter.getItem(position) as ChooseItem
        L.md("select name: ${item.itemName}")
        when(mCurTopListIndex){
            0 -> {
                //全类客户
                mFilterLevel = mLevelValue[position]
                mTvOptionLevel!!.text = mLevelArray[position]
            }
            1 -> {
                //综合排序
                mFilterOrder = mOrderValue[position]
                mTvOptionOrder!!.text = mLevelArray[position]
            }
            2 -> {
                //销售
                mFilterBd = mBdValue[position]
                mTvOptionBd!!.text = mLevelArray[position]
            }
        }
        hideTopList()
        getData(true)
    }

    override fun afterTextChanged(s: Editable?) {
        //搜索企业
        mFilterKeyWord = s.toString()
        getData(true)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    private fun initCustomerLevel() {
        mCurTopListIndex = 0
        mFilterItems.clear()
        for (i in mLevelArray.indices) {
            val item = ChooseItem()
            if (mLevelValue[i] == mFilterLevel) {
                item.isSelect = true
                tv_filter_customer.text = mLevelArray[i]
            } else {
                item.isSelect = false
            }
            item.itemID = mLevelValue[i]
            item.itemName = mLevelArray[i]
            mFilterItems.add(item)
        }
        mFilterAdapter!!.notifyDataSetChanged()
    }

    private fun initCustomerOrder() {
        mCurTopListIndex = 1
        mFilterItems.clear()
        for (i in mOrderArray.indices) {
            val item = ChooseItem()
            if (mOrderValue[i] == mFilterOrder) {
                item.isSelect = true
                tv_filter_order.text = mOrderArray[i]
            } else {
                item.isSelect = false
            }
            item.itemID = mOrderValue[i]
            item.itemName = mOrderArray[i]
            mFilterItems.add(item)
        }
        mFilterAdapter!!.notifyDataSetChanged()
    }

    private fun initCustomerBd() {
        if (mBdArray == null) {
            return
        }
        L.md("mFilterBd=$mFilterBd")
        mCurTopListIndex = 2
        mFilterItems.clear()
        for (i in mBdArray.indices) {
            val item = ChooseItem()
            if (mBdValue[i] == mFilterBd) {
                L.md("mBdValue[i]=" + mBdValue[i])
                item.isSelect = true
                tv_filter_bd.text = mBdArray[i]
            } else {
                item.isSelect = false
            }

            item.itemID = mBdValue[i]
            item.itemName = mBdArray[i]
            mFilterItems.add(item)
        }
        mFilterAdapter!!.notifyDataSetChanged()
    }

    private fun toggleTopList(index: Int) {
        if (index == mCurTopListIndex && mIsShowedTopSelectLayout) {
            hideTopList()
        } else {
            showTopList()
        }
    }

    private fun showTopList() {
        mIsShowedTopSelectLayout = true
        mRlOptionLayout!!.visibility = View.VISIBLE
    }

    private fun hideTopList() {
        mIsShowedTopSelectLayout = false
        mRlOptionLayout!!.visibility = View.GONE
    }

    inner class ChooseItem {
        internal var itemName: String? = null
        internal var itemID: String? = null
        internal var isSelect = false
        internal var subordinates: List<ChooseItem> = ArrayList()
    }

    inner class FilterAdapter(layoutResId: Int, data: List<ChooseItem>) :
            BaseQuickAdapter<ChooseItem, BaseViewHolder>(layoutResId, data) {

        override fun convert(holder: BaseViewHolder, item: ChooseItem) {
            holder.setText(R.id.tv_one_text, item.itemName)
        }
    }
    internal inner class MySimpleDivider : RecyclerView.ItemDecoration {

        private var dividerHeight: Int = 0
        private var dividerPaint: Paint? = null

        constructor(context: Context) {
            this.dividerPaint = Paint()
            this.dividerPaint!!.color = context.resources.getColor(R.color.list_divider)
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

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
            super.getItemOffsets(outRect, view, parent, state)
            // 在底部空出1dp的空间，画divider线
            outRect.bottom = dividerHeight
        }

        fun dip2px(context: Context, dipValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dipValue * scale + 0.5f).toInt()
        }
    }
}