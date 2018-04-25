package com.max.app.kotlincrm.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import com.max.app.kotlincrm.JZBApplication
import com.max.app.kotlincrm.R
import com.max.app.kotlincrm.api.JZBConstants
import com.max.app.kotlincrm.utils.MyToast
import com.max.app.kotlincrm.utils.Sp
import kotlinx.android.synthetic.main.activity_settting.*

/**
 * 设置
 * Created by Xmg on 2018-4-21.
 */
class SettingActivity : BaseActivity() {

    companion object {
        fun actionActivity(context: Activity) {
            val intent = Intent(context, SettingActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settting)
        initActionbar()
        initView()
        getVersion()
    }

    private fun initActionbar(){
        setAbTitle("设置")
        showAbBack()
    }

    private fun initView(){
        rl_update.setOnClickListener {
            checkVersion()
        }
        rl_login_out.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        val dialog = Dialog(mContext, R.style.DialogFullScreen)
        dialog.setContentView(R.layout.logout_dialog)
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val enter = dialog.findViewById(R.id.logout_dialog_enter) as Button
        val cancel = dialog.findViewById(R.id.logout_dialog_cancle) as Button
        enter.setOnClickListener {
            JZBApplication.recycleAllActivity()
            Sp.delete_SP(mContext, JZBConstants.SP_USERINFO)
            mContext.finish()
        }
        cancel.setOnClickListener { dialog.dismiss() }
    }

    private fun checkVersion(){
        MyToast.makeText(mContext, "已经是最新版本")
    }

    private fun getVersion(){
        try {
            val manager = mContext.packageManager
            val info = manager.getPackageInfo(mContext.packageName, 0)
            val version = info.versionName
            version_text.text = "当前版本：V$version"
        } catch (e: Exception) {
            e.printStackTrace()
            version_text.text = "当前版本：V1.0"
        }

    }
}