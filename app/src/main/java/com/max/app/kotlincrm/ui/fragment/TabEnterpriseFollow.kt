package com.max.app.kotlincrm.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.max.app.kotlincrm.R

/**
 * 跟进记录
 * Created by Xmg on 2018-4-24.
 */

class TabEnterpriseFollow : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_tab_enterprise_follow, null)
//        mPull2RefreshLayout = v.pull_2_refresh_layout
        initView(v)
//        getData(true)
        return v
    }

    private fun initView(v: View){

    }
}