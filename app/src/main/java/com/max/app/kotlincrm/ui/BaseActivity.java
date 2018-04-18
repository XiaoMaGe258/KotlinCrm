package com.max.app.kotlincrm.ui;

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

import com.max.app.kotlincrm.R;


public class BaseActivity extends AppCompatActivity implements View.OnClickListener, OnTouchListener {

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

	/**黑屏提示时，actionbar的遮挡图片*/
	public ImageView mBlackAlertCoverImage;
	public Toolbar mToolbar;
	/**自定义ActionBar左侧按键的Id*/
	public final static int mCustomActionBarActionBackId = R.id.action_back_layout;
	/**自定义ActionBar右侧按键的Id*/
	public final static int mCustomActionBarActionNextId = R.id.action_next_layout;
	
	/**默认顶部系统栏的颜色*/
	private final int DEFAULT_SYSTEM_BAR_COLOR = R.color.app_default_green;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		JZBApplication.ALLACTIVITY.add(this);
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
		mToolbar = (Toolbar) findViewById(R.id.toolbar_root);
		mActionBarTitle = (TextView) mToolbar.findViewById(R.id.ab_title);
		mActionNextLayout = (RelativeLayout) mToolbar.findViewById(R.id.action_next_layout);
		mActionBackLayout = (RelativeLayout) mToolbar.findViewById(R.id.action_back_layout);
		mActionBarNextText = (TextView) mToolbar.findViewById(R.id.action_next_text);
		mActionBarBackText = (TextView) mToolbar.findViewById(R.id.action_back_text);
		mActionBarNextFlag = (ImageView) mToolbar.findViewById(R.id.action_next_flag);
		mActionBarBackFlag = (ImageView) mToolbar.findViewById(R.id.action_back_flag);
		mBlackAlertCoverImage = (ImageView) mToolbar.findViewById(R.id.black_alert_cover_image);
		mActionBackLayout.setOnClickListener(this);
		mActionBackLayout.setOnTouchListener(this);
		mActionNextLayout.setOnTouchListener(this);
	}
	

	LinearLayout contentView;
	public void setContentView(int layoutSourceId){
		if (layoutSourceId == R.layout.custom_actionbar_general) {
			super.setContentView(R.layout.custom_actionbar_general);
			contentView = (LinearLayout) findViewById(R.id.layout_center);
			if (contentView != null) {
				contentView.removeAllViews();
			}

		} else {
			View addView = LayoutInflater.from(this).inflate(layoutSourceId, null);
			contentView.addView(addView, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		JZBApplication.ALLACTIVITY.remove(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case mCustomActionBarActionBackId:
				finish();
				break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			v.setAlpha(0.5f);
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			v.setAlpha(1f);
		}
		return false;
	}
}
