package com.example.gh.util;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.widget.EditText;
import android.widget.TextView;

public class TextUtil {


    public static void setHintS(EditText v, int size){

        SpannableString ss = new SpannableString(v.getHint());//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 附加属性到文本
        v.setHint(new SpannedString(ss));
    }
    public static void setHintS(EditText v){

        SpannableString ss = new SpannableString(v.getHint());//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 附加属性到文本
        v.setHint(new SpannedString(ss));
    }
    public static void setTypeB(EditText v){

        v.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }
    public static void setTypeN(EditText v){

        v.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }
    public static void setTypeB(TextView v){

        v.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }
    public static void setTypeN(TextView v){

        v.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    public static double convertToDouble(String number, double defaultValue){

        if (number.isEmpty()) {

            return defaultValue;
        }
        try {
            return Double.parseDouble(number);
        } catch (Exception e) {

            return defaultValue;
        }
    }
}
