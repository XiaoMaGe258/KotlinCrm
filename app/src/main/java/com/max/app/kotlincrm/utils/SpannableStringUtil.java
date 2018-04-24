package com.max.app.kotlincrm.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

public class SpannableStringUtil {

    /**
     * 超链接
     * @param targetText    目标文字
     * @param start 起始位置
     * @param end   结束位置
     * @param url   地址 #tel:13xxxxxxxxxx
     * @return
     */
    public static CharSequence getUrlSpan(String targetText, int start, int end, String url) {
        SpannableString spanString = new SpannableString(targetText);
        URLSpan span = new URLSpan(url);//"tel:0123456789"
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    /**
     * 文字背景颜色
     * @param targetText    目标文字
     * @param start 起始位置
     * @param end   结束位置
     * @param color   颜色#Color.YELLOW
     */
    public static CharSequence getBackColorSpan(String targetText, int start, int end, int color) {
        SpannableString spanString = new SpannableString(targetText);
        BackgroundColorSpan span = new BackgroundColorSpan(color);
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    /**
     * 文字颜色
     * @param targetText    目标文字
     * @param start 起始位置
     * @param end   结束位置
     * @param color   颜色#Color.BLUE
     */
    public static CharSequence getForeColorSpan(String targetText, int start, int end, int color) {
        SpannableString spanString = new SpannableString(targetText);
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    /**
     * 字体大小
     * @param targetText    目标文字
     * @param start 起始位置
     * @param end   结束位置
     * @param textSize 字体大小
     */
    public static CharSequence getFontSpan(String targetText, int start, int end, int textSize) {
        SpannableString spanString = new SpannableString(targetText);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(textSize);
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    /**
     * 粗体，斜体
     * @param targetText    目标文字
     * @param start 起始位置
     * @param end   结束位置
     * @param style 字体样式：#Typeface.BOLD_ITALIC
     * @return
     */
    public static CharSequence getStyleSpan(String targetText, int start, int end, int style) {
        SpannableString spanString = new SpannableString(targetText);
        StyleSpan span = new StyleSpan(style);
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    /**
     * 删除线
     * @param targetText    目标文字
     * @param start 起始位置
     * @param end   结束位置
     * @return
     */
    public static CharSequence getStrikeSpan(String targetText, int start, int end) {
        SpannableString spanString = new SpannableString(targetText);
        StrikethroughSpan span = new StrikethroughSpan();
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 下划线
     * @param targetText    目标文字
     * @param start 起始位置
     * @param end   结束位置
     */
    public static CharSequence getUnderLineSpan(String targetText, int start, int end) {
        SpannableString spanString = new SpannableString(targetText);
        UnderlineSpan span = new UnderlineSpan();
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    /**
     * 图片
     * @param drawable  图片
     */
    public static CharSequence getImageSpan(Drawable drawable) {
        SpannableString spanString = new SpannableString(" ");
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     *
     * @param targetText
     * @param start
     * @param end
     * 使用注意：
     *  textView.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
        textView.append(spStr);
        textView.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
        事件监听：
        new ClickableSpan(){
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.WHITE);       //设置文件颜色
                ds.setUnderlineText(true);      //设置下划线
            }

            @Override
            public void onClick(View widget) {
                Log.d("", "onTextClick........");
            }
        }
     */
    public static void getClickSpan(String targetText, int start, int end, ClickableSpan clickableSpanListener){
        SpannableString spanString = new SpannableString(targetText);
        spanString.setSpan(clickableSpanListener, 0, targetText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


    }
}
