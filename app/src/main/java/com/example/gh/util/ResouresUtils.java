package com.example.gh.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.example.gh.MainApplication;


public class ResouresUtils {
    /**
     * 获取颜色
     *
     * @param colorId 颜色ID
     * @return 颜色
     */
    public static int getColor(int colorId) {
        Context context = MainApplication.getInstance().getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(colorId);
        } else {
            return context.getResources().getColor(colorId);
        }
    }

    /**
     * 获取文字
     *
     * @param stringID 文字Id
     * @return 文字
     */
    public static String getString(int stringID) {
        return getString(stringID, (String) null);
    }

    /**
     * 获取文字
     *
     * @param stringID 文字Id
     * @return 文字
     */
    public static String getString(int stringID, String... string) {
        Context context = MainApplication.getInstance().getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getString(stringID, string[0]);
        } else {
            return context.getResources().getString(stringID,
                    string[0]);
        }
    }


    /**
     * 获取drawable
     */
    public static Drawable getDrawable(int resId) {
        Resources resources = MainApplication.getInstance().getApplicationContext().getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return resources.getDrawable(resId, null);
        } else {
            return resources.getDrawable(resId);
        }
    }

    public static String getAppVersion() {
        try {
            PackageInfo packageInfo = MainApplication.getInstance().getApplicationContext().getPackageManager().getPackageInfo(MainApplication.getInstance().getApplicationContext().getPackageName(), PackageManager.GET_CONFIGURATIONS);
            if (packageInfo != null) {
                return packageInfo.versionName;
            }
            return "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int sp2px(float spValue) {
        final float fontScale = MainApplication.getInstance().getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕
     *
     * @param resId resId
     * @return dimen
     */
    public static int getDimens(int resId) {
        Resources resources = MainApplication.getInstance().getApplicationContext().getResources();
        return resources.getDimensionPixelSize(resId);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
