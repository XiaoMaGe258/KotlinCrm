package com.max.app.kotlincrm.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.max.app.kotlincrm.R;

/**
 *
 * <style name="Dialog_Fullscreen1">
	 <item name="android:windowNoTitle">true</item>
	 <item name="android:windowFrame">@null</item>
	 <item name="android:windowIsFloating">true</item>
	 <item name="android:windowContentOverlay">@null</item>
	 <item name="android:windowBackground">@color/transparent</item>
 */
public class DialogUtils {
	
	public static ProgressDialog createSimpleProgressDialog(Activity activity) {
		return createSimpleProgressDialog(activity, true);
	}

	public static ProgressDialog createSimpleProgressDialog(Activity activity,
                                                            boolean cancelable) {
		if (!activity.isFinishing()) {
			ProgressDialog progressDialog = new ProgressDialog(activity, R.style.DialogFullScreen);
			progressDialog.setCancelable(cancelable);
			progressDialog.show();
			progressDialog.setContentView(R.layout.dialog_progress);
			return progressDialog;
		}
		return null;
    }

//	public static void showImageDialog(final Context context, final String url){
//		final Dialog dialog=new Dialog(context, R.style.DialogFullScreen);
//		dialog.setContentView(R.layout.image_dialog);
//		dialog.getWindow().setGravity(Gravity.CENTER);
//		dialog.setCanceledOnTouchOutside(true);
//		dialog.show();
//		final ImageView image = (ImageView) dialog.findViewById(R.id.iv_dialog_image);
//		TextView save = (TextView) dialog.findViewById(R.id.tv_dialog_save);
////		Glide.with(context).load(url).placeholder(R.drawable.bg).into(image);
//		Glide.with(context).load(url).into(image);
//		save.setVisibility(View.GONE);
//		image.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
////		save.setOnClickListener(new View.OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				//GlideBitmapDrawable    TransitionDrawable
////				Bitmap bitmap = ImageUtil.Drawable2Bitmap(image.getDrawable());
////				if (bitmap != null) {
////					ImageUtil.saveImageToGallery(context, bitmap);
////				} else {
////					MyToast.showToast(context, "请稍候");
////				}
////			}
////		});
//	}

	/**
	 * 创建一个提示对话框
	 * @param context		上下文
	 * @param titleText		主标题（必选）
	 * @param subTitleText	副标题（可选）
	 * @param leftBtnText	左侧按钮文字（可选）
	 * @param rightBtnText	右侧按钮文字（必选）
	 * @param leftListener	左侧按钮事件（可选）
	 * @param rightListener	右侧按钮事件（可选）
	 */
	public static void showNoticeDialog(@NonNull Context context, @NonNull String titleText, String subTitleText,
                                        String leftBtnText, @NonNull String rightBtnText,
                                        final View.OnClickListener leftListener, final View.OnClickListener rightListener){
		showNoticeDialog(context, titleText, subTitleText, leftBtnText, rightBtnText,
				context.getResources().getColor(R.color.text_gray_light),
				context.getResources().getColor(R.color.app_green),
		leftListener, rightListener, true);
	}

	/**
	 * 创建一个提示对话框
	 * @param context		上下文
	 * @param titleText		主标题（必选）
	 * @param subTitleText	副标题（可选）
	 * @param leftBtnText	左侧按钮文字（可选）
	 * @param rightBtnText	右侧按钮文字（必选）
	 * @param leftListener	左侧按钮事件（可选）
	 * @param rightListener	右侧按钮事件（可选）
	 * @param canCancel	是否可取消对话框
	 */
	public static void showNoticeDialog(@NonNull Context context, @NonNull String titleText, String subTitleText,
                                        String leftBtnText, @NonNull String rightBtnText,
                                        final View.OnClickListener leftListener, final View.OnClickListener rightListener,
                                        boolean canCancel){
		showNoticeDialog(context, titleText, subTitleText, leftBtnText, rightBtnText,
				context.getResources().getColor(R.color.text_gray_light),
				context.getResources().getColor(R.color.app_green),
		leftListener, rightListener, canCancel);
	}

	/**
	 *
	 * @param context		上下文
	 * @param titleText		主标题（必选）
	 * @param subTitleText	副标题（可选）
	 * @param leftBtnText	左侧按钮文字（可选）
	 * @param rightBtnText	右侧按钮文字（必选）
	 * @param leftBtnTextColor  左侧按钮文字颜色（必选）
	 * @param rightBtnTextColor 右侧按钮文字颜色（必选）
	 * @param leftListener	左侧按钮事件（可选）
	 * @param rightListener	右侧按钮事件（可选）
	 * @param canCancel	是否可取消对话框
	 */
	public static void showNoticeDialog(@NonNull Context context, @NonNull String titleText, String subTitleText,
                                        String leftBtnText, @NonNull String rightBtnText, int leftBtnTextColor, int rightBtnTextColor,
                                        final View.OnClickListener leftListener, final View.OnClickListener rightListener,
                                        boolean canCancel){
		final Dialog dialog=new Dialog(context, R.style.DialogFullScreen);
		dialog.setContentView(R.layout.gengral_notice_dialog);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.setCanceledOnTouchOutside(canCancel);
		dialog.show();
		TextView title = (TextView) dialog.findViewById(R.id.notice_dialog_title);
		TextView subTitle = (TextView) dialog.findViewById(R.id.notice_dialog_subtitle);
		Button leftBtn = (Button) dialog.findViewById(R.id.notice_dialog_left_btn);
		Button rightBtn = (Button) dialog.findViewById(R.id.notice_dialog_right_btn);
		View vLine = dialog.findViewById(R.id.vertical_line);

		title.setText(titleText);
		leftBtn.setTextColor(leftBtnTextColor);
		rightBtn.setTextColor(rightBtnTextColor);

		if(TextUtils.isEmpty(leftBtnText)){
			leftBtn.setVisibility(View.GONE);
			vLine.setVisibility(View.GONE);
			rightBtn.setBackgroundResource(R.drawable.dialog_center_button_bg);
		}else{
			leftBtn.setText(leftBtnText);
		}
		if(TextUtils.isEmpty(rightBtnText)){
			rightBtn.setVisibility(View.GONE);
			vLine.setVisibility(View.GONE);
			leftBtn.setBackgroundResource(R.drawable.dialog_center_button_bg);
		}else{
			rightBtn.setText(rightBtnText);
		}
		if(TextUtils.isEmpty(subTitleText)){
			subTitle.setVisibility(View.GONE);
		}else{
			subTitle.setText(subTitleText);
		}

		leftBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(leftListener != null) {
					leftListener.onClick(v);
				}
			}
		});

		rightBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(rightListener != null) {
					rightListener.onClick(v);
				}
			}
		});
	}

	public static Dialog showJzbProgressDialog(Context context, String hint){
		Dialog dialog=new Dialog(context, R.style.DialogFullScreen);
		dialog.setContentView(R.layout.dialog_progress);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		return dialog;
	}
}
