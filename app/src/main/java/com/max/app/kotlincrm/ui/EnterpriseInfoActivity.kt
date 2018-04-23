package com.max.app.kotlincrm.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.max.app.kotlincrm.R
import com.max.app.kotlincrm.api.JZBController
import com.max.app.kotlincrm.api.JzbResponseHandler
import com.max.app.kotlincrm.utils.MyToast
import kotlinx.android.synthetic.main.activity_enterprise_info.*
import org.json.JSONObject

/**
 * 企业信息（捡回）
 * Created by Xmg on 2018-4-23.
 */
class EnterpriseInfoActivity : BaseActivity() {

    private var customerId: String? = null
    private var enterpriseStatus: Int? = null

    companion object {
        fun actionActivity(context: Activity, customerId: String, enterpriseStatus: Int) {
            val intent = Intent(context, EnterpriseInfoActivity::class.java)
            intent.putExtra("customerId", customerId)
            intent.putExtra("enterpriseStatus", enterpriseStatus)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enterprise_info)
        enterpriseStatus = intent.getIntExtra("enterpriseStatus", 0)
        customerId = intent.getStringExtra("customerId")
        initActionbar()
        initView()
        initData()
    }

    private fun initActionbar(){
        setAbTitle("企业信息")
        showAbBack()
    }

    private fun initView(){
        if (enterpriseStatus == 1) {
            btn_pickup.text = "申请捡回"
        } else if (enterpriseStatus == 4) {
            btn_pickup.text = "捡回"
        }
        btn_pickup.setOnClickListener {
            pickup()
        }
    }

    private fun initData(){
        JZBController.getInstance(mContext).getEnterpriseInfo(
                customerId, object : JzbResponseHandler(mContext){
            override fun onHttpSuccessStatusOk(jsonObject: JSONObject?) {
                val json = jsonObject!!.getJSONObject("data")
                val name = json.optString("name")
                val city = json.optString("district")
                val property = json.optString("property")
                val about = json.optString("about")
                tv_enterprise_name.text = if(isNull(name)) "未知" else name
                tv_enterprise_city.text = if(isNull(city)) "未知" else city
                tv_enterprise_property.text = if(isNull(property)) "未知" else property
                tv_enterprise_introduce.text = if(isNull(about)) "未知" else about
            }
        })
    }

    private fun pickup(){
        JZBController.getInstance(mContext).postPickupEnterprise(
                customerId, object : JzbResponseHandler(mContext){
            override fun onHttpSuccessStatusOk(jsonObject: JSONObject?) {
                val msg = jsonObject!!.optString("msg")
                MyToast.makeText(mContext, msg)
                finish()
            }
        })
    }

}