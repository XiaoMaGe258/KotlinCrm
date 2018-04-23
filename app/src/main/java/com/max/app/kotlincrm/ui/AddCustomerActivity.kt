package com.max.app.kotlincrm.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.max.app.kotlincrm.R
import com.max.app.kotlincrm.adapter.TBaseAdapter
import com.max.app.kotlincrm.api.JZBController
import com.max.app.kotlincrm.api.JzbResponseHandler
import com.max.app.kotlincrm.utils.L
import kotlinx.android.synthetic.main.activity_add_customer.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * 添加客户
 * Created by Xmg on 2018-4-21.
 */
class AddCustomerActivity : BaseActivity() {

    private val mSearchResultInformations = ArrayList<SearchResultInformation>()
    private var mSearchResultAdapter: SearchResultAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer)
        initActionbar()
        initView()
    }

    private fun initActionbar(){
        setAbTitle("添加客户")
        setAbBack({
            finish()
        })
    }

    private fun initView(){
        mSearchResultAdapter = SearchResultAdapter(mContext, mSearchResultInformations)
        lv_customer_list.adapter = mSearchResultAdapter

        et_search_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable) {
                mSearchResultInformations.clear()
                demandSearchResult()
            }
        })
    }
    private var mEnterpriseName: String? = ""
    //查询搜索结果
    private fun demandSearchResult() {
        mEnterpriseName = et_search_text.text.toString()
        if ("" == mEnterpriseName) {
            lv_customer_list.visibility = View.GONE
            return
        }
        requestEnterpriseFromServer(mEnterpriseName!!)
    }

    private fun requestEnterpriseFromServer(enterpriseName: String) {
        JZBController.getInstance(mContext).requestEnterpriseInformation(enterpriseName,
                object : JzbResponseHandler() {
            override fun onHttpSuccessStatusOk(data: JSONObject?) {
                try {
                    val array = data!!.getJSONArray("data")
                    val length = array.length()
                    for (i in 0 until length) {
                        val jsonObject = array.getJSONObject(i)
                        val id = jsonObject.optString("id")
                        val type = jsonObject.optInt("type")
                        val customerName = jsonObject.optString("customerName")
                        val searchResultInformation = SearchResultInformation()
                        if (type == 3) {
                            val deadline = jsonObject.optString("deadline")
                            searchResultInformation.enterpriseTime = deadline
                        }
                        if (type == 5) {
                            val storeName = jsonObject.optString("storeName")
                            searchResultInformation.storeName = storeName
                        }
                        if (type == 6) {
                            val auditStatus = jsonObject.optString("auditStatusText")
                            searchResultInformation.auditStatus = auditStatus
                        }
                        searchResultInformation.customerId = id
                        searchResultInformation.enterpriseStatus = type
                        searchResultInformation.enterpriseName = customerName
                        mSearchResultInformations.add(searchResultInformation)
                    }
                    L.md("mSearchResultInformations.size=${mSearchResultInformations.size}")
                    mSearchResultAdapter!!.notifyDataSetChanged()
                    if (mSearchResultInformations.size > 0) {
                        showSearchResultAndHideNoAboutEnterprise()
                    } else {
                        showNoAboutEnterpriseAndHideSearchResult()
                    }
                } catch (e: JSONException) {
                    showNoAboutEnterpriseAndHideSearchResult()
                }
            }
        })
    }

    //显示无数据时的布局并且隐藏有数据时的布局
    private fun showNoAboutEnterpriseAndHideSearchResult() {
        lv_customer_list.visibility = View.GONE
        rl_empty_layout.visibility = View.VISIBLE
    }

    //显示有数据时的布局并且隐藏无数据时的布局
    private fun showSearchResultAndHideNoAboutEnterprise() {
        lv_customer_list.visibility = View.VISIBLE
        rl_empty_layout.visibility = View.GONE
    }

    private inner class SearchResultAboutEnterpriseHolder {
        internal var enterpriseName: TextView? = null
        internal var enterpriseTime: TextView? = null
        internal var enterpriseNameStatus2: TextView? = null
        internal var enterpriseNameStatus: ImageView? = null
        internal var enterpriseDetail: ImageView? = null

    }

    private inner class SearchResultAdapter(context: Activity, list: List<SearchResultInformation>):
            TBaseAdapter<SearchResultInformation>(context, list) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view: View? = null
            if (convertView != null) {
                view = convertView
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.search_result_about_enterprise_item, parent, false)
            }
            var holder: SearchResultAboutEnterpriseHolder? = null
            if(view!!.tag != null){
                holder = view!!.tag as SearchResultAboutEnterpriseHolder
            }
            if (holder == null) {
                holder = SearchResultAboutEnterpriseHolder()
                holder!!.enterpriseName = view!!.findViewById(R.id.search_result_about_enterprise_item_enterprise_name) as TextView
                holder!!.enterpriseTime = view!!.findViewById(R.id.search_result_about_enterprise_item_enterprise_time) as TextView
                holder!!.enterpriseNameStatus2 = view!!.findViewById(R.id.search_result_about_enterprise_item_enterprise_status2) as TextView
                holder!!.enterpriseNameStatus = view!!.findViewById(R.id.search_result_about_enterprise_item_enterprise_status) as ImageView
                holder!!.enterpriseDetail = view!!.findViewById(R.id.search_result_about_enterprise_item_enterprise_detail) as ImageView
                view!!.tag = holder
            }
            val mSearchResultInformation = getItem(position) as SearchResultInformation
            val enterpriseName = mSearchResultInformation.enterpriseName
            val enterpriseTime = mSearchResultInformation.enterpriseTime
            holder!!.enterpriseName!!.text = enterpriseName
            holder!!.enterpriseTime!!.text = enterpriseTime
            when (mSearchResultInformation.enterpriseStatus) {
                1 -> {
                    //"团体公海"企业
                    holder!!.enterpriseNameStatus!!.visibility = View.VISIBLE
                    holder!!.enterpriseNameStatus2!!.visibility = View.INVISIBLE
                    holder!!.enterpriseNameStatus!!.setImageResource(R.drawable.team_public_sea)
                    holder!!.enterpriseDetail!!.visibility = View.VISIBLE
                    holder!!.enterpriseTime!!.visibility = View.GONE
                }
                2 -> {
                    //"冲突"企业
                    holder!!.enterpriseNameStatus!!.visibility = View.VISIBLE
                    holder!!.enterpriseNameStatus2!!.visibility = View.INVISIBLE
                    holder!!.enterpriseNameStatus!!.setImageResource(R.drawable.conflict)
                    holder!!.enterpriseDetail!!.visibility = View.INVISIBLE
                    holder!!.enterpriseTime!!.visibility = View.GONE
                }
                3 -> {
                    //"锁定"企业
                    holder!!.enterpriseNameStatus!!.visibility = View.VISIBLE
                    holder!!.enterpriseNameStatus2!!.visibility = View.INVISIBLE
                    holder!!.enterpriseNameStatus!!.setImageResource(R.drawable.locked_up)
                    holder!!.enterpriseDetail!!.visibility = View.GONE
                    holder!!.enterpriseTime!!.visibility = View.VISIBLE
                }
                4 -> {
                    //"公海"企业
                    holder!!.enterpriseNameStatus!!.visibility = View.VISIBLE
                    holder!!.enterpriseNameStatus2!!.visibility = View.INVISIBLE
                    holder!!.enterpriseNameStatus!!.setImageResource(R.drawable.public_sea)
                    holder!!.enterpriseDetail!!.visibility = View.VISIBLE
                    holder!!.enterpriseTime!!.visibility = View.GONE
                }
                5 -> {
                    //自己的库中
                    holder!!.enterpriseNameStatus!!.visibility = View.INVISIBLE
                    holder!!.enterpriseNameStatus2!!.visibility = View.VISIBLE
                    holder!!.enterpriseNameStatus2!!.text = "已存在(" + mSearchResultInformation.storeName + ")"
                    holder!!.enterpriseNameStatus2!!.setBackgroundResource(R.drawable.round_purple_bg)
                    holder!!.enterpriseDetail!!.visibility = View.INVISIBLE
                    holder!!.enterpriseTime!!.visibility = View.GONE
                }
                6 -> {
                    //审核中
                    holder!!.enterpriseNameStatus!!.visibility = View.INVISIBLE
                    holder!!.enterpriseNameStatus2!!.visibility = View.VISIBLE
                    holder!!.enterpriseNameStatus2!!.text = (
                            if (TextUtils.isEmpty(mSearchResultInformation.auditStatus))
                                "审核中"
                            else
                                mSearchResultInformation.auditStatus
                    )
                    holder!!.enterpriseNameStatus2!!.setBackgroundResource(R.drawable.round_orange_bg)
                    holder!!.enterpriseDetail!!.visibility = View.INVISIBLE
                    holder!!.enterpriseTime!!.visibility = View.GONE
                }
            }
            return view!!
        }
    }

//    private fun getLocation(){
//        mProgressDialog = DialogUtils.createSimpleProgressDialog(mContext)
//        LocationUtil.getInstance(mContext).getLocation { bdLocation ->
//            mProgressDialog!!.dismiss()
//            mLongitude = bdLocation.longitude.toString() + ""
//            mLatitude = bdLocation.latitude.toString() + ""
//            if ("4.9E-324" == mLongitude || "4.9E-324" == mLatitude) {
//                mLongitude = "0"
//                mLatitude = "0"
//            }
//            mAddress = if (TextUtils.isEmpty(bdLocation.addrStr)) "" else bdLocation.addrStr
//            Sp.init_SP_Instance(mContext, JZBConstants.SP_USERINFO)
//            Sp.put_String(JZBConstants.BD_LONGITUDE, mLongitude)
//            Sp.put_String(JZBConstants.BD_LATITUDE, mLatitude)
//            Sp.put_String(JZBConstants.BD_ADDRESS, mAddress)
//            L.md("经纬度：longitude=$mLongitude  latitude=$mLatitude  address=$mAddress")
//            if ("0" == mLatitude || "0" == mLongitude) {
//                DialogUtils.showNoticeDialog(mContext,
//                        "定位失败",
//                        "由于公司需要你在指定地点签到，为了获取你的位置请开启定位服务",
//                        "不签了", "去设置", null, View.OnClickListener {
//                    startActivity(getAppDetailSettingIntent(mContext))
//                })
//            } else {
//                tv_bottom_location.text = mAddress
//            }
//        }
//    }
//    private fun getAppDetailSettingIntent(context: Context): Intent {
//        val localIntent = Intent()
//        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
//        localIntent.data = Uri.fromParts("package", context.packageName, null)
//        return localIntent
//    }

    private inner class SearchResultInformation {
        var customerId: String? = null
        var enterpriseName: String? = null
        var enterpriseTime: String? = null
        var enterpriseStatus: Int = 0
        var storeName: String? = null
        var auditStatus: String? = null

    }
}