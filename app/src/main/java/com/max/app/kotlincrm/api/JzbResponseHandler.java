package com.max.app.kotlincrm.api;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.max.app.kotlincrm.R;
import com.max.app.kotlincrm.utils.L;
import com.max.app.kotlincrm.utils.MyToast;

import org.json.JSONObject;

/**
 * 如需使用进度对话框，请使用有参构造。无参构造自动忽略对话框。
 * 对返回信息中status进行了预处理
 */
public abstract class JzbResponseHandler extends StringCallback {

    private static final String TAG = "json";

    private Activity mActivity;

    private String mHintMsg;

    private Dialog mSimpleProgressDialog;
    public JzbResponseHandler() {
    }

    public JzbResponseHandler(Activity activity) {
        mActivity = activity;
    }

    public JzbResponseHandler(Activity activity, String hintMsg) {
        mActivity = activity;
        mHintMsg = hintMsg;
    }

    public JzbResponseHandler(Activity activity, boolean showProgressDialog) {
        mActivity = activity;
        mHintMsg = showProgressDialog ? "" : null;
    }

    public void setShowDialog(boolean isShow){
        mHintMsg = isShow ? "" : null;
    }

    @Override
    public void onStart(Request<String, ? extends Request> request) {
        super.onStart(request);
        showProgressDialog();
    }

    public void showProgressDialog() {
        if (mHintMsg != null && mSimpleProgressDialog == null) {
            mSimpleProgressDialog = showJzbProgressDialog(mActivity, mHintMsg);
        }
    }


    private void hideProgressDialog() {
        try {
            if (mSimpleProgressDialog != null) {
                mSimpleProgressDialog.dismiss();
                mSimpleProgressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHintMsg(String hintMsg) {
        mHintMsg = hintMsg;
    }

    @Override
    public void onSuccess(Response<String> response) {
        hideProgressDialog();
        String responseBody = response.body();
        L.d(TAG, responseBody);
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            if ("ok".equals(jsonObject.optString("status"))
                    || "yes".equals(jsonObject.optString("status"))) {
                onHttpSuccessStatusOk(jsonObject);
            } else {
                onHttpSuccessStatusNoOk(jsonObject);
                if (mActivity != null) {
                    MyToast.showToast(mActivity, jsonObject.optString("msg"));
                }
            }
        } catch (Exception e) {
            L.e(TAG, "解析失败");
            e.printStackTrace();
            onHttpFailure(response.code(), "解析失败");
        }

    }

    @Override
    public void onError(Response<String> response) {
        super.onError(response);
        hideProgressDialog();
        String responseBody = response.body();
        if (responseBody != null && responseBody.length() > 0) {
            try {
                onHttpFailure(response.code(), responseBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mActivity != null) {
                MyToast.showToast(mActivity, "加载失败，请稍后重试");
            }
            L.e(TAG, "网络请求失败");
            if (responseBody.length() > 0) {
                L.e(TAG, "错误码" + response.code() + " responseBody=" + responseBody.toString());
                onHttpFailure(response.code(), responseBody);
            }
        }else{
            onHttpFailure(response.code(), "");
        }
    }

    public Dialog showJzbProgressDialog(Context context, String hint){
        Dialog dialog=new Dialog(context, R.style.DialogFullScreen);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        return dialog;
    }

    protected void onHttpSuccessStatusNoOk(JSONObject jsonObject) {
    }

    public abstract void onHttpSuccessStatusOk(JSONObject jsonObject);

    public void onHttpFailure(int statusCode, String msg) {
    }

}
