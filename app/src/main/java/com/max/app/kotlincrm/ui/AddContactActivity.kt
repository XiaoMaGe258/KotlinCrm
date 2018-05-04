package com.max.app.kotlincrm.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.max.app.kotlincrm.R
import com.max.app.kotlincrm.api.JZBController
import com.max.app.kotlincrm.api.JzbResponseHandler
import com.max.app.kotlincrm.items.ContactItem
import com.max.app.kotlincrm.utils.L
import com.max.app.kotlincrm.utils.MyToast
import kotlinx.android.synthetic.main.activity_add_contact.*
import org.json.JSONObject
import java.util.*

/**
 * 添加联系人
 * Created by Xmg on 2018-5-4.
 */
class AddContactActivity : BaseActivity() {

    private var mCustomerId: String? = null
    private var mContactItem: ContactItem? = null

    companion object {
        fun actionActivity(context: Activity, customerId: String, item: ContactItem?) {
            val intent = Intent(context, AddContactActivity::class.java)
            intent.putExtra("customerId", customerId)
            intent.putExtra("contactItem", item)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        parserIntent()
        initActionbar()
        initView()
    }

    private fun parserIntent(){
        mCustomerId = intent.getStringExtra("customerId")
        mContactItem = intent.getSerializableExtra("contactItem") as ContactItem?
    }

    private fun initActionbar(){
        if (mContactItem != null) {
            setAbTitle("编辑联系人")
        }else {
            setAbTitle("添加联系人")
        }
        showAbBack()
    }

    private fun initView(){
        if (mContactItem != null) {
            initData()
        }
        btn_login_button.setOnClickListener({
            commit(mContactItem != null)
        })
    }

    private fun initData(){
        ib_contact_name.content = mContactItem!!.name
        ib_contact_phone.content = mContactItem!!.phone
        ib_contact_telephone.content = mContactItem!!.telephone
        ib_contact_email.content = mContactItem!!.email
        ib_contact_qq.content = mContactItem!!.qq
        ib_contact_job.content = mContactItem!!.title
        ib_contact_address.content = mContactItem!!.address
        rb_contact_yes.isChecked = mContactItem!!.isKP
        rb_contact_no.isChecked = !mContactItem!!.isKP
    }

    private fun commit(isEdit: Boolean) {
        val emailRegular = Regex("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$")
        if (TextUtils.isEmpty(ib_contact_name.content)) {
            MyToast.makeText(mContext, "联系人不能为空")
            return
        } else if (TextUtils.isEmpty(ib_contact_phone.content)) {
            MyToast.makeText(mContext, "手机不能为空")
            return
        } else if (!ib_contact_phone.content.startsWith("1") || ib_contact_phone.content.length !== 11) {
            MyToast.makeText(mContext, "手机号格式不正确")
            return
        } else if (TextUtils.isEmpty(ib_contact_email.content)) {
            MyToast.makeText(mContext, "邮箱不能为空")
            return
        } else if (!ib_contact_email.content.matches(emailRegular)) {
            MyToast.makeText(mContext, "邮箱格式不正确")
            return
        } else if (TextUtils.isEmpty(ib_contact_job.content)) {
            MyToast.makeText(mContext, "职位不能为空")
            return
        } else if (TextUtils.isEmpty(mCustomerId)) {
            MyToast.makeText(mContext, "系统异常，请稍后重试")
            return
        }

        val params = HashMap<String, Any?>()
        params["c_customerId"] = mCustomerId
        params["c_name"] = ib_contact_name.content
        params["c_phone"] = ib_contact_phone.content
        params["c_title"] = ib_contact_job.content
        params["c_email"] = ib_contact_email.content
        params["c_qq"] = ib_contact_qq.content
        params["c_telephone"] = ib_contact_telephone.content
        params["c_address"] = ib_contact_address.content
        params["c_kp"] = rb_contact_yes.isChecked

        if (isEdit) {
            params["c_id"] = mContactItem!!.contactId
            JZBController.getInstance(mContext).postEditContact(params, object : JzbResponseHandler(mContext, true) {
                override fun onHttpSuccessStatusOk(jsonObject: JSONObject) {
                    L.d("xmg", "编辑联系人成功： jsonObject:$jsonObject")
                    MyToast.makeText(mContext, "编辑成功")
                    setResult(RESULT_OK)
                    finish()
                }
            })
        } else {
            JZBController.getInstance(mContext).postAddContact(params, object : JzbResponseHandler(mContext, true) {
                override fun onHttpSuccessStatusOk(jsonObject: JSONObject) {
                    L.d("xmg", "添加联系人成功： jsonObject:$jsonObject")
                    MyToast.makeText(mContext, "添加成功")
                    setResult(RESULT_OK)
                    finish()
                }
            })
        }

    }
}