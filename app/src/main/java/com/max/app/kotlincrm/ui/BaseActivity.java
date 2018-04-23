package com.max.app.kotlincrm.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.max.app.kotlincrm.JZBApplication;
import com.max.app.kotlincrm.R;


public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

	public Activity mContext;

	/**自定义ActionBar的居中标题*/
	public TextView mActionBarTitle;

	/**自定义ActionBar的左侧整体布局*/
	public RelativeLayout mActionBackLayout;
	/**自定义ActionBar的右侧整体布局*/
	public RelativeLayout mActionNextLayout;
	
	/**自定义ActionBar的左侧文字*/
	public TextView mActionBarBackText;
	/**自定义ActionBar的右侧文字*/
	public TextView mActionBarNextText;
	
	/**自定义ActionBar的左侧箭头*/
	public ImageView mActionBarBackFlag;
	/**自定义ActionBar的右侧箭头*/
	public ImageView mActionBarNextFlag;

	/**自定义Actionbar的右侧设置图片*/
	public ImageView mActionBarSetting;

	public Toolbar mToolbar;
	/**自定义ActionBar左侧按键的Id*/
	public final static int mCustomActionBarActionBackId = R.id.action_back_layout;
	/**自定义ActionBar右侧按键的Id*/
	public final static int mCustomActionBarActionNextId = R.id.action_next_layout;
	
	//ToolBar下方，页面内容
	private LinearLayout mContentView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		JZBApplication.AllActivity.add(this);
		mContext = this;
		setContentView(R.layout.custom_actionbar_general);
		initCustomActionBar();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mContext = this;
	}

	private void initCustomActionBar(){
		mToolbar = findViewById(R.id.toolbar_root);
		mActionBarTitle = mToolbar.findViewById(R.id.ab_title);
		mActionNextLayout = mToolbar.findViewById(R.id.action_next_layout);
		mActionBackLayout = mToolbar.findViewById(R.id.action_back_layout);
		mActionBarNextText = mToolbar.findViewById(R.id.action_next_text);
		mActionBarBackText = mToolbar.findViewById(R.id.action_back_text);
		mActionBarNextFlag = mToolbar.findViewById(R.id.action_next_flag);
		mActionBarBackFlag = mToolbar.findViewById(R.id.action_back_flag);
		mActionBarSetting = mToolbar.findViewById(R.id.action_right_setting);
		mActionBackLayout.setOnClickListener(this);
		mActionBackLayout.setOnTouchListener(mOnTouchListener);
		mActionNextLayout.setOnTouchListener(mOnTouchListener);
	}
	

	public void setContentView(int layoutSourceId){
		if (layoutSourceId == R.layout.custom_actionbar_general) {
			super.setContentView(R.layout.custom_actionbar_general);
			mContentView = findViewById(R.id.layout_center);
			if (mContentView != null) {
				mContentView.removeAllViews();
			}

		} else {
			View addView = LayoutInflater.from(this).inflate(layoutSourceId, null);
			mContentView.addView(addView, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case mCustomActionBarActionBackId:
				finish();
				break;
		}
	}

	public OnTouchListener mOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				v.setAlpha(0.5f);
			}else if(event.getAction() == MotionEvent.ACTION_UP){
				v.setAlpha(1f);
			}
			return false;
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		JZBApplication.AllActivity.remove(this);
	}

	protected void setAbTitle(String title){
		mActionBarTitle.setText(title);
	}

	protected void setAbBack(View.OnClickListener onClickListener){
		mActionBackLayout.setVisibility(View.VISIBLE);
		mActionBarBackText.setVisibility(View.INVISIBLE);
		mActionBackLayout.setOnTouchListener(mOnTouchListener);
		if(onClickListener != null){
			mActionBackLayout.setOnClickListener(onClickListener);
		}
	}

	protected void setAbSetting(View.OnClickListener onClickListener){
		mActionBarSetting.setVisibility(View.VISIBLE);
		mActionBarSetting.setOnTouchListener(mOnTouchListener);
		if(onClickListener != null){
			mActionBarSetting.setOnClickListener(onClickListener);
		}
	}

}
