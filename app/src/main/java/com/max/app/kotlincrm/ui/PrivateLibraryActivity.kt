package com.max.app.kotlincrm.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.max.app.kotlincrm.R
import com.max.app.kotlincrm.ui.fragment.TabListFragment
import kotlinx.android.synthetic.main.fragment_private_lib_tabs.*
import java.util.ArrayList

/**
 * 个人客户池
 * Created by Xmg on 2018-4-23.
 */

class PrivateLibraryActivity : BaseActivity() {

    val mFragments = ArrayList<Fragment>()
    private val mTabs = arrayOf("我的客户", "下级客户")

    companion object {
        fun actionActivity(context: Activity) {
            val intent = Intent(context, PrivateLibraryActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_private_lib_tabs)
        initView()
    }

    private fun initView(){
        for (tab in mTabs) {
            mFragments.add(TabListFragment())
        }

        vp_tab_item_pager.adapter = MyPagerAdapter(supportFragmentManager)
        tl_sliding_tab.setViewPager(vp_tab_item_pager)
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