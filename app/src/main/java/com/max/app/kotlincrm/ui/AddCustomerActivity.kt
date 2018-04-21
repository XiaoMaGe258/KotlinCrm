package com.max.app.kotlincrm.ui

import android.os.Bundle
import com.max.app.kotlincrm.R

/**
 * 添加客户
 * Created by Xmg on 2018-4-21.
 */
class AddCustomerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer)
        initActionbar()

    }

    private fun initActionbar(){
        setAbTitle("添加客户")
        setAbBack({
            finish()
        })
    }
}