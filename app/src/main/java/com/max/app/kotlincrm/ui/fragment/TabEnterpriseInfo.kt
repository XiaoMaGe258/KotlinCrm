package com.max.app.kotlincrm.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.gson.Gson
import com.max.app.kotlincrm.R
import com.max.app.kotlincrm.api.JZBController
import com.max.app.kotlincrm.api.JzbResponseHandler
import com.max.app.kotlincrm.items.EnterpriseModel
import com.max.app.kotlincrm.ui.EnterpriseDetailActivity
import com.max.app.kotlincrm.utils.Utils
import kotlinx.android.synthetic.main.fragment_tab_enterprise_info.view.*
import org.json.JSONObject

/**
 * 客户资料
 * Created by Xmg on 2018-5-4.
 */

class TabEnterpriseInfo : Fragment() {

    var v: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_tab_enterprise_info, null)
        initView(v)
        initData()
        return v
    }

    private fun initView(v: View) {
        this.v = v
    }

    private fun initData() {
        JZBController.getInstance(activity).getEnterpriseModelInfo(
                (activity as EnterpriseDetailActivity).getCId(), object : JzbResponseHandler(activity) {
                    override fun onHttpSuccessStatusOk(jsonObject: JSONObject?) {
                        val json = jsonObject!!.getJSONObject("data")
                        val item = Gson().fromJson(json.toString(), EnterpriseModel::class.java)
                        refreshUI(item as EnterpriseModel)
                    }
                }
        )
    }

    private fun refreshUI (info: EnterpriseModel){
        if (!Utils.isNull(info.name)) {
            v!!.customer_info_name_layout.visibility = View.VISIBLE
            v!!.customer_info_name.text = info.name
        }
        if (!Utils.isNull(info.district)) {
            v!!.customer_info_area_layout.visibility = View.VISIBLE
            v!!.customer_info_area.text = info.district
        }
        if (!Utils.isNull(info.location)) {
            v!!.customer_info_address_layout.visibility = View.VISIBLE
            v!!.customer_info_address.text = info.location
        }
        if (!Utils.isNull(info.contact)) {
            v!!.customer_info_contact_layout.visibility = View.VISIBLE
            v!!.customer_info_contact.text = info.contact
        }
        if (!Utils.isNull(info.title)) {
            v!!.customer_info_job_title_layout.visibility = View.VISIBLE
            v!!.customer_info_job_title.text = info.title
        }
        if (!Utils.isNull(info.kp)) {
            v!!.customer_info_policymakers_layout.visibility = View.VISIBLE
            v!!.customer_info_policymakers.text = info.kp
        }
        if (!Utils.isNull(info.tel)) {
            v!!.customer_info_phone_layout.visibility = View.VISIBLE
            v!!.customer_info_phone.text = info.tel
            v!!.customer_info_phone.setOnClickListener({
                if (info.tel.contains(",") && !info.tel.startsWith(",") && !info.tel.endsWith(",")) {
                    showPhoneListDialog(info.tel.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray())
                } else {
                    Utils.actionCall(activity, info.tel.replace(",", ""))
                }
            })
        }
        if (!Utils.isNull(info.qq)) {
            v!!.customer_info_qq_layout.visibility = View.VISIBLE
            v!!.customer_info_qq.text = info.qq
        }
        if (!Utils.isNull(info.email)) {
            v!!.customer_info_email_layout.visibility = View.VISIBLE
            v!!.customer_info_email.text = info.email
        }
        if (!Utils.isNull(info.license)) {
            v!!.customer_info_business_license_layout.visibility = View.VISIBLE
            v!!.customer_info_business_license.text = "点击查看"
            v!!.customer_info_business_license.setOnClickListener({
//                ImageViewActivity.actionImageViewActivity(mContext, info.businessLicense)
            })
        }
        if (!Utils.isNull(info.scale)) {
            v!!.customer_info_size_layout.visibility = View.VISIBLE
            v!!.customer_info_size.text = info.scale
        }
        if (!Utils.isNull(info.property)) {
            v!!.customer_info_character_layout.visibility = View.VISIBLE
            v!!.customer_info_character.text = info.property
        }
        if (!Utils.isNull(info.about)) {
            v!!.customer_info_introduce_layout.visibility = View.VISIBLE
            v!!.customer_info_introduce.text = info.about
        }
        if (!Utils.isNull(info.website)) {
            v!!.customer_info_website_layout.visibility = View.VISIBLE
            v!!.customer_info_website.text = info.website
        }
        if (!Utils.isNull(info.ct)) {
            v!!.customer_info_createtime_layout.visibility = View.VISIBLE
            v!!.customer_info_createtime.text = info.ct
        }
        if (!Utils.isNull(info.note)) {
            v!!.customer_info_comments_layout.visibility = View.VISIBLE
            v!!.customer_info_comments.text = info.note
        }
        if (info.isHideContactInfo) {
            v!!.customer_info_phone_layout.visibility = View.GONE
        } else {
            v!!.customer_info_phone_layout.visibility = View.VISIBLE
        }

//        mFromPage = (activity as CustomerLibraryDetailActivity).getFromPage()
//        if (mFromPage == JZBConstants.PUBLIC_LIBRARY) {
//            mCustomerInfoPhoneLayout.setVisibility(View.GONE)
//            mCustomerInfoQqLayout.setVisibility(View.GONE)
//            mCustomerInfoEmailLayout.setVisibility(View.GONE)
//        }
    }

    private fun showPhoneListDialog(phones: Array<String>) {

        val dialog = Dialog(activity, R.style.DialogFullScreen)
        dialog.setContentView(R.layout.dialog_call_listview)
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.setCanceledOnTouchOutside(true)
        val listView = dialog.findViewById(R.id.dialog_listview) as ListView
        val arrayAdapter = ArrayAdapter(activity, R.layout.simple_one_text_item, phones)
        listView.adapter = arrayAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val phone = parent.getItemAtPosition(position) as String
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            startActivity(intent)
            dialog.dismiss()
        }
        dialog.show()
    }
}