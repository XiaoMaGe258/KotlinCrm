package com.max.app.kotlincrm.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import com.max.app.kotlincrm.R
import com.max.app.kotlincrm.ui.fragment.TabEnterpriseFollow
import com.max.app.kotlincrm.utils.Utils
import kotlinx.android.synthetic.main.activity_enterprise_detail_layout.*
import java.util.ArrayList

/**
 * 企业详情
 * Created by Xmg on 2018-4-24.
 */

class EnterpriseDetailActivity : BaseActivity() {

    val mFragments = ArrayList<Fragment>()
    private val mTabs = arrayOf("跟进记录", "联系人", "客户资料")

    var name: String = ""
    var levelDec: String = ""
    var flag: String = ""
    var customerId: String = ""
    var isMineLib: Boolean = true
    var fromPage: Int = 0

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
        mFragments.add(TabEnterpriseFollow())
        mFragments.add(TabEnterpriseFollow())

        vp_tab_item_pager.adapter = MyPagerAdapter(supportFragmentManager)
        tl_sliding_tab.setViewPager(vp_tab_item_pager)
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