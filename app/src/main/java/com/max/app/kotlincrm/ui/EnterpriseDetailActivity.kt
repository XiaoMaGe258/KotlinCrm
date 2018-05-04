package com.max.app.kotlincrm.ui

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import com.max.app.kotlincrm.R
import com.max.app.kotlincrm.api.JZBConstants
import com.max.app.kotlincrm.ui.fragment.TabEnterpriseContact
import com.max.app.kotlincrm.ui.fragment.TabEnterpriseFollow
import com.max.app.kotlincrm.ui.fragment.TabEnterpriseInfo
import com.max.app.kotlincrm.utils.Sp
import com.max.app.kotlincrm.utils.Utils
import kotlinx.android.synthetic.main.activity_enterprise_detail_layout.*
import java.util.ArrayList

/**
 * 企业详情
 * Created by Xmg on 2018-4-24.
 */

class EnterpriseDetailActivity : BaseActivity() {

    private var mRoleNumber: Int = 0
    private val mFragments = ArrayList<Fragment>()
    private val mTabs = arrayOf("跟进记录", "联系人", "客户资料")
    private var mPopupWindow: PopupWindow? = null

    private var name: String = ""
    private var levelDec: String = ""
    private var flag: String = ""
    private var customerId: String = ""
    private var isMineLib: Boolean = true
    private var fromPage: Int = 0

    companion object {
        fun actionActivity(context: Activity, name: String?, levelDec: String?, flag: String?,
                           customerId: String?, isMineLib: Boolean, fromPage: Int) {
            val intent = Intent(context, EnterpriseDetailActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("levelDec", levelDec)
            intent.putExtra("flag", flag)
            intent.putExtra("customerId", customerId)
            intent.putExtra("isMineLib", isMineLib)
            intent.putExtra("fromPage", fromPage)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enterprise_detail_layout)
        initActionbar()
        handlerIntent()
        initView()
    }

    override fun onPause() {
        super.onPause()
        if (mPopupWindow != null) {
            mPopupWindow!!.dismiss()
        }
    }

    private fun handlerIntent(){
        name = intent.getStringExtra("name")
        levelDec = intent.getStringExtra("levelDec")
        flag = intent.getStringExtra("flag")
        customerId = intent.getStringExtra("customerId")
        isMineLib = intent.getBooleanExtra("isMineLib", true)
        fromPage = intent.getIntExtra("fromPage", 0)
    }

    private fun initActionbar(){
        setAbTitle("企业详情")
        showAbBack()

        if (fromPage == JZBConstants.PUBLIC_LIBRARY) {
            setAbNext("", null)
        } else {
            var nextText = if (isMineLib) "跟进" else  "陪访"
            Sp.init_SP_Instance(mContext, JZBConstants.SP_USERINFO)
            mRoleNumber = Sp.get_Int(JZBConstants.ROLENUMBER, 0)
            if ((mRoleNumber == 5 || mRoleNumber == 4)) {//只有销售 并且 只有账号模式才有收款操作  && "2" == mFilterChargeType
                nextText = "操作"
            }
            setAbNext(nextText, {
                if ((mRoleNumber == 5 || mRoleNumber == 4)) {// && "2" == mFilterChargeType
                    showPopupWindowMenu()
                } else {
                    FollowUpActivity.actionActivity(mContext, customerId, name, isMineLib)
                }
            })
        }
    }

    private fun initView(){
        tv_customer_name.text = name
        tv_customer_level.text = levelDec
        if(Utils.isNull(flag)){
            tv_customer_flag.visibility = View.GONE
        }else{
            tv_customer_flag.text = flag
            when (flag) {
                "试用中" -> tv_customer_flag.setBackgroundResource(R.drawable.round_orange_bg)
                "试用过" -> tv_customer_flag.setBackgroundResource(R.drawable.round_gray_bg)
                else -> tv_customer_flag.setBackgroundResource(R.drawable.round_green_bg)
            }
        }

        mFragments.add(TabEnterpriseFollow())
        mFragments.add(TabEnterpriseContact())
        mFragments.add(TabEnterpriseInfo())

        vp_tab_item_pager.adapter = MyPagerAdapter(supportFragmentManager)
        tl_sliding_tab.setViewPager(vp_tab_item_pager)
    }

    private fun showPopupWindowMenu() {
        val contentView = View.inflate(mContext, R.layout.ab_follow_popup, null)
        mPopupWindow = PopupWindow(contentView, RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, true)
        mPopupWindow!!.isTouchable = true
        mPopupWindow!!.isFocusable = true
        mPopupWindow!!.setBackgroundDrawable(BitmapDrawable())
        val tvFollowUp = contentView.findViewById(R.id.tv_follow_up) as TextView
        val tvCollection = contentView.findViewById(R.id.tv_collection) as TextView
        val tvAddContact = contentView.findViewById(R.id.tv_add_contact) as TextView
//        val tvSchedule = contentView.findViewById(R.id.tv_schedule) as TextView
        tvFollowUp.setOnClickListener({
            FollowUpActivity.actionActivity(mContext, customerId, name, isMineLib)
        })
        tvCollection.setOnClickListener(this)
        tvAddContact.setOnClickListener(this)
//        tvSchedule.setOnClickListener(this)
        mPopupWindow!!.showAsDropDown(mActionNextLayout, 0, 0)
    }

    fun getCId(): String {
        return customerId
    }
    fun getFrom() : Int{
        return fromPage
    }
    private inner class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }

        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mTabs[position]
        }
    }
}