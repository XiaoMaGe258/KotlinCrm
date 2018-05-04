package com.max.app.kotlincrm.ui

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.max.app.kotlincrm.MainManagerActivity
import com.max.app.kotlincrm.R
import com.max.app.kotlincrm.api.JZBController
import com.max.app.kotlincrm.api.JzbResponseHandler
import com.max.app.kotlincrm.items.FollowUpModel
import com.max.app.kotlincrm.items.NormalKVItem
import com.max.app.kotlincrm.ui.view.MyRecyclerDecoration
import com.max.app.kotlincrm.ui.view.MyRecyclerDivider
import com.max.app.kotlincrm.utils.*
import kotlinx.android.synthetic.main.activity_follow_up_layout.*
import org.json.JSONObject
import java.io.File
import java.util.*

/**
 * 跟进
 * Created by Xmg on 2018-4-24.
 */

class FollowUpActivity : BaseActivity(), OnClickListener, RadioGroup.OnCheckedChangeListener, BaseQuickAdapter.OnItemClickListener {

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

    private var mSmallBitmap: Bitmap? = null
    private var mPictureFile: File? = null

    private var mProgressDialog : ProgressDialog? = null
    private val mContactList = ArrayList<Contact>()//联系人列表
    private var mSelectContact: Contact? = null
    private val mTagList = ArrayList<NormalKVItem>()
    private val mSelectTagList = ArrayList<NormalKVItem>()
    private var mGridAdapter: GridViewAdapter? = null
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
        initLocation()
        initTagList()
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
        showAbBack()
    }

    private fun initView(){
        tv_enterprise_name.text = enterpriseName
        rl_follow_object_layout.setOnClickListener(this)
        vg_follow_method.setOnCheckedChangeListener(this)
        vg_follow_communication.setOnCheckedChangeListener(this)

        rv_tag_grid.layoutManager = GridLayoutManager(this, 3)
        mGridAdapter = GridViewAdapter(R.layout.tag_gridview_item, mTagList)
        rv_tag_grid.addItemDecoration(MyRecyclerDecoration(mContext, 5))
        rv_tag_grid.adapter = mGridAdapter
        mGridAdapter!!.onItemClickListener = this

        follow_up_location_btn.setOnClickListener(this)
        follow_up_sign_btn.setOnClickListener(this)
        follow_up_look_image_layout.setOnClickListener(this)
    }

    private fun initLocation(){
        mProgressDialog = DialogUtils.createSimpleProgressDialog(mContext)
        LocationUtil.getInstance(mContext).getLocation {
            mProgressDialog!!.dismiss()
            longitude = it.longitude.toString()
            latitude = it.latitude.toString()
            address = it.addrStr
            follow_up_location_name.text = address
        }
    }

    private fun initTagList(){
        JZBController.getInstance(mContext).getFollowTags(object: JzbResponseHandler(mContext){
            override fun onHttpSuccessStatusOk(jsonObject: JSONObject?) {
                val dataArray = jsonObject!!.optJSONArray("data")
                if (dataArray != null) {
                    mTagList.clear()
                    for (i in 0 until dataArray.length()) {
                        mTagList.add(NormalKVItem("", dataArray.optString(i)))
                    }
                    mGridAdapter!!.notifyDataSetChanged()
                    setTagList(label)
                }
            }
        })
    }

    private fun setTagList(listStr: String) {
        if (listStr.isEmpty() || Utils.isNull(listStr) || mTagList.size == 0) {
            return
        }
        val strArray = listStr.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        mSelectTagList.clear()
        for (str in strArray) {
            mSelectTagList.add(NormalKVItem("", str))
            for (i in mTagList.indices) {
                if (mTagList[i].getName() == str) {
                    mTagList[i].isSelect = true
                }
            }
        }
        mGridAdapter!!.notifyDataSetChanged()
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId){
            R.id.follow_up_type_wechat ->{
                L.md("微信")
                method = 3
                follow_up_location_layout.visibility = GONE
            }
            R.id.follow_up_type_phone ->{
                L.md("电话")
                method = 0
                follow_up_location_layout.visibility = GONE
            }
            R.id.follow_up_type_visit ->{
                L.md("拜访")
                method = 1
                follow_up_location_layout.visibility = VISIBLE
            }
            R.id.follow_up_communication_out ->{
                L.md("主动回访")
                category = 1
            }
            R.id.follow_up_communication_in ->{
                L.md("客户呼入")
                category = 2
            }
        }
    }

    override fun onClick(v: View?) {
        when(v){
            rl_follow_object_layout -> {
                //跟进对象
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
            follow_up_location_btn -> {
                //重新定位
                initLocation()
            }
            follow_up_sign_btn -> {
                //到访拍照
                PickImageUtil.getInstance(mContext).camera(object : PickImageUtil.IGetSelectBitmap {

                    override fun getSelImageBitmap(bitmap: Bitmap, id: Int) {
                    }

                    override fun getSelImageFile(file: File) {
                        mSmallBitmap = ImageUtils.getSmallBitMap(file.absolutePath, 480, 800)
                        L.d("xmg", "file size=" + file.length() + "  path=" + file.absolutePath)
                        val sPath = ImageUtils.saveImage2SD("jianzhibao", "tempFollowPicture.jpg", mSmallBitmap)
                        try {
                            file.delete()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        mPictureFile = File(sPath)
                        follow_up_look_image_layout.visibility = View.VISIBLE
                    }
                })
            }
            follow_up_look_image_layout -> {
                if (mSmallBitmap == null)
                    return
                showImageDialog(mSmallBitmap!!)
            }
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val type = adapter!!.getItem(position) as NormalKVItem
        if (containsItem(type)) {
            removeItem(type)
        } else {
            mSelectTagList.add(type)
        }

        mTagList[position].isSelect = !mTagList[position].isSelect
        mGridAdapter!!.notifyDataSetChanged()
    }

    private fun containsItem(category: NormalKVItem): Boolean {
        for (type in mSelectTagList) {
            if (type.name == category.name) {
                return true
            }
        }
        return false
    }

    private fun removeItem(category: NormalKVItem) {
        var tempArray: ArrayList<NormalKVItem>? = ArrayList()
        for (type in mSelectTagList) {
            if (type.name != category.name) {
                tempArray!!.add(type)
            }
        }
        mSelectTagList.clear()
        mSelectTagList.addAll(tempArray!!)
        tempArray!!.clear()
        tempArray = null
    }

    private fun getSelectTagStr(): String {
        val sb = StringBuilder()
        for (item in mSelectTagList) {
            if (sb.isNotEmpty()) {
                sb.append(",")
            }
            sb.append(item.getName())
        }
        return sb.toString()
    }

    private fun showImageDialog(bitmap: Bitmap) {
        val dialog = Dialog(mContext, R.style.DialogFullScreen)
        dialog.setContentView(R.layout.imageview_layout)
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val image = dialog.findViewById(R.id.image_view) as ImageView
        image.setImageBitmap(bitmap)
        image.setOnClickListener { dialog.dismiss() }
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

    inner class GridViewAdapter(layoutResId: Int, data: List<NormalKVItem>) :
            BaseQuickAdapter<NormalKVItem, BaseViewHolder>(layoutResId, data) {

        override fun convert(helper: BaseViewHolder, item: NormalKVItem) {
            helper.setText(R.id.item_name, item.name)

            if (item.isSelect) {
                helper.setTextColor(R.id.item_name, resources.getColor(R.color.white))
                helper.getView<ViewGroup>(R.id.bank_handwrite_layout)
                        .setBackgroundResource(R.drawable.button_round_green)
            } else {
                helper.setTextColor(R.id.item_name, resources.getColor(R.color.text_gray_dark_lf))
                helper.getView<ViewGroup>(R.id.bank_handwrite_layout)
                        .setBackgroundResource(R.drawable.button_round_gray_xxxlight)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        PickImageUtil.getInstance(mContext).onActivityResult(requestCode, resultCode, data)
    }
}