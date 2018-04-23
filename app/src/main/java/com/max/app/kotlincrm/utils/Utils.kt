package com.max.app.kotlincrm.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils

/**
 * 工具类
 * Created by Xmg on 2018-4-23.
 */
object Utils {

    //判空
    fun isNull(str: String?): Boolean {
        return TextUtils.isEmpty(str) || "null".equals(str, ignoreCase = true)
    }

    //打电话
    fun actionCall(context: Context, callNumber: String?){
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$callNumber"))
        context.startActivity(intent)
    }
}