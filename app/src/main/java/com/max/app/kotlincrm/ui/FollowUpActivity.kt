package com.max.app.kotlincrm.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.widget.RadioGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.max.app.kotlincrm.R
import com.max.app.kotlincrm.api.JZBController
import com.max.app.kotlincrm.api.JzbResponseHandler
import com.max.app.kotlincrm.items.FollowUpModel
import com.max.app.kotlincrm.ui.view.MyRecyclerDivider
import com.max.app.kotlincrm.utils.L
import com.max.app.kotlincrm.utils.MyToast
import kotlinx.android.synthetic.main.activity_follow_up_layout.*
import org.json.JSONObject
import java.util.*

/**
 * 跟进
 * Created by Xmg on 2018-4-24.
 */

class FollowUpActivity : BaseActivity(), OnClickListener, RadioGroup.OnCheckedChangeListener {

    private var mFollowUpModel: FollowUpModel? = null

    private var method = 0// 拜访方式 0 电话   1 拜访  3 微信
    private var category = 0 // 沟通分类 1 主动回访 2 客户呼入

    private var longitude = ""
    private var latitude = ""
    private var address = ""
    private var customerId = ""
    private var followUpId = ""
    private var enterpriseName = ""
    private var contact = ""
    private var contactID = ""
    private var startDate = ""
    private var endDate = ""
    private var content = ""
    private var label = ""
    private var mIsPrivateLibrary = true// true跟进，false陪访
    private var mIsFillInfo = false

    private val mContactList = ArrayList<Contact>()//联系人列表
    private var mSelectContact: Contact? = null
    companion object {
        fun actionActivity(context: Activity, customerId: String,
                           enterpriseName: String, isMineLib: Boolean) {
            val followUpModel = FollowUpModel()
            followUpModel.customerId = customerId
            followUpModel.enterpriseName = enterpriseName
            followUpModel.isMineLib = isMineLib
            followUpModel.isFillInfo = false
            actionActivity(context, followUpModel)
        }

        fun actionActivity(context: Activity, model: FollowUpModel) {
            val intent = Intent(context, FollowUpActivity::class.java)
            intent.putExtra("followUpModel", model)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_up_layout)
        initActionbar()
        parseIntent()
        initView()
        L.md("customerId=$customerId")
    }

    private fun parseIntent() {
        mFollowUpModel = intent.getSerializableExtra("followUpModel") as FollowUpModel
        customerId = mFollowUpModel!!.customerId
        followUpId = mFollowUpModel!!.followUpId
        enterpriseName = mFollowUpModel!!.enterpriseName
        address = mFollowUpModel!!.address
        startDate = mFollowUpModel!!.startDate
        endDate = mFollowUpModel!!.endDate
        mIsPrivateLibrary = mFollowUpModel!!.isMineLib
        mIsFillInfo = mFollowUpModel!!.isFillInfo
        contact = mFollowUpModel!!.contactName
        contactID = mFollowUpModel!!.contactId
        content = mFollowUpModel!!.content
        category = mFollowUpModel!!.category
        label = mFollowUpModel!!.label
    }

    private fun initActionbar(){
        setAbTitle("跟进")
    }

    private fun initView(){
        tv_enterprise_name.text = enterpriseName
        rl_follow_object_layout.setOnClickListener(this)
        vg_follow_method.setOnCheckedChangeListener(this)
        vg_follow_communication.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId){
            R.id.follow_up_type_wechat ->{
                L.md("微信")
                method = 3
            }
            R.id.follow_up_type_phone ->{
                L.md("电话")
                method = 0
            }
            R.id.follow_up_type_visit ->{
                L.md("拜访")
                method = 1
            }
            R.id.follow_up_communication_out ->{
                L.md("主动回访")
            }
            R.id.follow_up_communication_in ->{
                L.md("客户呼入")
            }
        }
    }

    override fun onClick(v: View?) {
        when(v){
            rl_follow_object_layout -> {
                JZBController.getInstance(mContext).getFollowObject(customerId,
                        object : JzbResponseHandler(mContext) {
                            override fun onHttpSuccessStatusOk(jsonObject: JSONObject?) {
                                val jsonArray = jsonObject!!.getJSONArray("data")
                                val type = object : TypeToken<ArrayList<Contact>>() {}.type
                                val items = Gson().fromJson<ArrayList<Contact>>(jsonArray.toString(), type)
                                mContactList.clear()
                                mContactList.addAll(items)

                                if (mContactList.size == 0) {
                                    MyToast.makeText(mContext, "暂时没有需要拜访的联系人")
                                } else {
                                    L.md("mContactList.size=${mContactList.size}")
                                    alertContactDialog()
                                }
                            }
                        }
                )
            }
        }
    }

    private fun alertContactDialog() {
        val dialog = Dialog(mContext, R.style.DialogFullScreen)
        dialog.setContentView(R.layout.dialog_simple_list)
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val recyclerView = dialog.findViewById(R.id.rv_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.addItemDecoration(MyRecyclerDivider(mContext))
        val adapter = FollowLogsAdapter(R.layout.simple_one_text_fill_item, mContactList)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { adapter: BaseQuickAdapter<Any, BaseViewHolder>, view: View, position: Int ->
            dialog.dismiss()
            mSelectContact = adapter.getItem(position) as Contact
            tv_follow_object.text = mSelectContact!!.name
        }
    }

    inner class FollowLogsAdapter(layoutResId: Int, data: List<Contact>) :
            BaseQuickAdapter<Contact, BaseViewHolder>(layoutResId, data) {

        override fun convert(holder: BaseViewHolder?, item: Contact?) {
            holder!!.setText(R.id.tv_one_text, item!!.name)
        }

    }

    inner class Contact {
        internal var contactId: String? = null
        internal var name: String? = null
    }
}