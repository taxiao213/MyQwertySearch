package com.hr.han.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Han on 2017/4/25.
 */

public class ColorTextView extends android.support.v7.widget.AppCompatTextView {


    public ColorTextView(Context context) {
        this(context, null);
    }

    public ColorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 将颜色设置成蓝色
     *
     * @param text           全部的文字
     * @param specifiedTexts 搜索的文字匹配
     */
    public void setSpecifiedTextsColor(String text, String specifiedTexts) {
        //将颜色设置成蓝色
        int color = Color.parseColor("#38ADFF");
        setSpecifiedTextsColor(text, specifiedTexts, color);
    }

    /**
     * @param text           全部的文字
     * @param specifiedTexts 搜索的文字匹配
     * @param color
     */
    public void setSpecifiedTextsColor(String text, String specifiedTexts, int color) {

        if (specifiedTexts != null && specifiedTexts.length() > 0) {

            List<Integer> sTextsStartList = new ArrayList<>();
            int sTextLength = specifiedTexts.length();
            String temp = text;
            int lengthFront = 0;//记录被找出后前面的字段的长度
            int start = -1;
            do {
                start = temp.indexOf(specifiedTexts);

                if (start != -1) {
                    start = start + lengthFront;
                    sTextsStartList.add(start);
                    lengthFront = start + sTextLength;
                    temp = text.substring(lengthFront);
                }

            } while (start != -1);

            SpannableStringBuilder styledText = new SpannableStringBuilder(text);
            for (Integer i : sTextsStartList) {
                styledText.setSpan(
                        new ForegroundColorSpan(color),
                        i,
                        i + sTextLength,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            setText(styledText);
        } else {
            setText(text);
        }
    }


    /**
     * 使字体变色
     * @param baseText
     * @param highlightText if the string of highlightText is a subset of the string of baseText,highlight the string of highlightText.
     */
    public void showTextHighlight(String baseText, String highlightText) {

        if (baseText != null && highlightText != null && highlightText.length() > 0) {

            int index = baseText.indexOf(highlightText);
            if (index < 0) {
                this.setText(baseText);
                return;
            }

            int len = highlightText.length();
            /**
             *  "<u><font color=#FF8C00 >"+str+"</font></u>"; 	//with underline
             *  "<font color=#FF8C00 >"+str+"</font>";			//without underline
             *  //将颜色设置成蓝色
             *  <color name="dark_orange">#FF8C00</color>
             */
            Spanned spanned = Html.fromHtml(baseText.substring(0, index) + "<font color=#38ADFF >"
                    + baseText.substring(index, index + len) + "</font>"
                    + baseText.substring(index + len, baseText.length()));

            this.setText(spanned);
        } else {
            this.setText(baseText);
        }

    }

}
