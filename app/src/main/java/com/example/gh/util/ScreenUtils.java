package com.example.gh.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;


import com.example.gh.MainApplication;

import java.lang.reflect.Method;


public class ScreenUtils {

    /**
     * dp转Px
     *
     * @param dp dp
     * @return px
     */
    public static int dpToPx(int dp) {
        DisplayMetrics metrics = getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    /**
     * px 转 dp
     *
     * @param px px
     * @return dp
     */
    public static int pxToDp(int px) {
        DisplayMetrics metrics = getDisplayMetrics();
        return (int) (px / metrics.density);
    }

    /**
     * sp转px
     *
     * @param sp sp
     * @return px
     */
    public static int spToPx(int sp) {
        DisplayMetrics metrics = getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
    }

    /**
     * px转sp
     *
     * @param px px
     * @return sp
     */
    public static int pxToSp(int px) {
        DisplayMetrics metrics = getDisplayMetrics();
        return (int) (px / metrics.scaledDensity);
    }

    /**
     * 获取手机显示App区域的大小（头部导航栏+ActionBar+根布局），不包括虚拟按钮
     *
     * @return 显示大小像素
     */
    public static int[] getAppSize() {
        int[] size = new int[2];
        DisplayMetrics metrics = getDisplayMetrics();
        size[0] = metrics.widthPixels;
        size[1] = metrics.heightPixels;
        return size;
    }

    /**
     * 获取整个手机屏幕的大小(包括虚拟按钮)
     * 必须在onWindowFocus方法之后使用
     *
     * @param activity activity
     * @return 屏幕尺寸
     */
    public static int[] getScreenSize(AppCompatActivity activity) {
        int[] size = new int[2];
        View decorView = activity.getWindow().getDecorView();
        size[0] = decorView.getWidth();
        size[1] = decorView.getHeight();
        return size;
    }

    /**
     * 获取导航栏的高度
     *
     * @return 导航栏的高度
     */
    public static int getStatusBarHeight() {
        Resources resources = MainApplication.getInstance().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 获取虚拟按键的高度
     *
     * @return 虚拟按键的高度
     */
    public static int getNavigationBarHeight() {
        int navigationBarHeight = 0;
        Resources rs = MainApplication.getInstance().getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && hasNavigationBar()) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /**
     * 是否存在虚拟按键
     *
     * @return
     */
    public static boolean hasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = MainApplication.getInstance().getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    /**
     * 返回DisplayMetrics
     *
     * @return DisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics() {
        return MainApplication.getInstance()
                .getResources()
                .getDisplayMetrics();
    }

    public static int getDisplayDpi(){
        DisplayMetrics dm = getDisplayMetrics();
        return dm.densityDpi;
    }

    /**
     * 获取屏幕宽度
     *
     * @param mContext context
     * @return 宽度
     */
    public static int getScreenWidth(Context mContext) {
        if (mContext == null){
            mContext = MainApplication.getInstance();
        }
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕长度
     *
     * @param mContext context
     * @return 长度
     */
    public static int getScreenHeight(Context mContext) {
        if (mContext == null){
            mContext = MainApplication.getInstance();
        }
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        if (activity == null) {
            return;
        }
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bgAlpha;
        window.setAttributes(lp);
    }


    /**
     * 获取是否有虚拟按键的虚拟按键高度
     *
     * @param context
     * @return
     */
    public static int getVirtualBarHeight(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - display.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isMIUI()) {
            if (isFullScreen(context)) {
                vh = 0;
            }
        } else {
            if (!hasDeviceNavigationBar(context)) {
                vh = 0;
            }
        }
        return vh;
    }

    /**
     * 获取是否有虚拟按键
     * 通过判断是否有物理返回键反向判断是否有虚拟按键
     *
     * @param context
     * @return
     */
    public static boolean hasDeviceNavigationBar(Context context) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            boolean result = realSize.y != size.y;
            return realSize.y != size.y;
        } else {

            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !menu && !back;
        }
    }

        public static boolean isFullScreen(Context context){
            // true 是手势，默认是 false
            // https://www.v2ex.com/t/470543
            return Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0) != 0;

        }

        public static boolean isMIUI(){
            String manufacturer = Build.MANUFACTURER;
            // 这个字符串可以自己定义,例如判断华为就填写huawei,魅族就填写meizu
            return "xiaomi".equalsIgnoreCase(manufacturer);
        }
}
