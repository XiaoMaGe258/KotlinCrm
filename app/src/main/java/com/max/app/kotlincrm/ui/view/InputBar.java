package com.max.app.kotlincrm.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.max.app.kotlincrm.R;

/**
 * Created by Max on 2017/1/6.
 *
 * 拷贝input_bar.xml布局文件
 *
 * attrs.xml中增加
 * <declare-styleable name="InputBar">
        <attr name="iptMust" format="boolean"/>
        <attr name="iptTitle" format="string"/>
        <attr name="iptContent" format="string"/>
        <attr name="iptHint" format="string"/>
   </declare-styleable>


 xmlns:ipt="http://schemas.android.com/apk/res-auto"

 <com.jianzhibao.ka.enterprise.view.InputBar
     android:id="@+id/ib_create_option_account"
     android:layout_width="match_parent"
     android:layout_height="45dp"
     ipt:iptHint="设置您的账号"
     ipt:iptTitle="管理员账号" />
 */
public class InputBar extends FrameLayout {

    private ImageView mIvMust;
    private TextView mTvTitle;
    private EditText mEdContent;

    public InputBar(Context context) {
        super(context);
        initView(context);

    }

    public InputBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        resolveAttrs(attrs);
    }

    public InputBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        resolveAttrs(attrs);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.input_bar, this);
        mIvMust = (ImageView) findViewById(R.id.iv_must);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mEdContent = (EditText) findViewById(R.id.et_content);
    }

    private void resolveAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.InputBar);
        String title = ta.getString(R.styleable.InputBar_iptTitle);
        String content = ta.getString(R.styleable.InputBar_iptContent);
        String hint = ta.getString(R.styleable.InputBar_iptHint);
        boolean must = ta.getBoolean(R.styleable.InputBar_iptMust, true);
        ta.recycle();

        setTitle(title);
        setContent(content);
        setContentHint(hint);
        setMust(must);
    }

    public void setContent(String content) {
        mEdContent.setText(content);
    }

    public void setContentHint(String hint) {
        mEdContent.setHint(hint);
    }

    public String getContent() {
        return mEdContent.getText().toString().trim();
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setMust(boolean must) {
        mIvMust.setVisibility(must ? VISIBLE : INVISIBLE);
    }

    public void setInputType(int inputType){
        mEdContent.setInputType(inputType);
    }

    public void setTextChangedListener(TextWatcher textChangedListener){
        mEdContent.addTextChangedListener(textChangedListener);
    }

}
