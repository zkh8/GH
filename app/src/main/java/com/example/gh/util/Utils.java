package com.example.gh.util;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Utils {
    // 根据手机的分辨率从 dp 的单位 转成为 px(像素)
    public static int dip2px(Context context, float dpValue) {
        // 获取当前手机的像素密度（1个dp对应几个px）
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f); // 四舍五入取整
    }

    // 根据手机的分辨率从 px(像素) 的单位 转成为 dp
    public static int px2dip(Context context, float pxValue) {
        // 获取当前手机的像素密度（1个dp对应几个px）
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f); // 四舍五入取整
    }

    // 根据手机的分辨率从 sp 的单位 转成为 px(像素)
    public static int sp2px(Context context, float spValue) {
        // 获取当前手机的像素密度（1个dp对应几个px）
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f); // 四舍五入取整
    }

    // 根据手机的分辨率从 px(像素) 的单位 转成为 sp
    public static int px2sp(Context context, float pxValue) {
        // 获取当前手机的像素密度（1个dp对应几个px）
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f); // 四舍五入取整
    }

    // 获得屏幕的宽度
    public static int getScreenWidth(Context ctx) {
        // 从系统服务中获取窗口管理器
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        // 从默认显示器中获取显示参数保存到dm对象中
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels; // 返回屏幕的宽度数值
    }

    // 获得屏幕的高度
    public static int getScreenHeight(Context ctx) {
        // 从系统服务中获取窗口管理器
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        // 从默认显示器中获取显示参数保存到dm对象中
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels; // 返回屏幕的高度数值
    }

    // 获得屏幕的像素密度
    public static float getScreenDensity(Context ctx) {
        // 从系统服务中获取窗口管理器
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        // 从默认显示器中获取显示参数保存到dm对象中
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.density; // 返回屏幕的像素密度数值
    }

    public static void copyToClipboard(Context context, CharSequence content) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText(null, content));//参数一：标签，可为空，参数二：要复制到剪贴板的文本
            if (clipboard.hasPrimaryClip()) {
                clipboard.getPrimaryClip().getItemAt(0).getText();
            }
        }
    }


    public static Point getSize(Context ctx) {
        Point size = new Point();
        // 从系统服务中获取窗口管理器
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 获取当前屏幕的四周边界
            Rect rect = wm.getCurrentWindowMetrics().getBounds();
            size.x = rect.width();
            size.y = rect.height();
        } else {
            DisplayMetrics dm = new DisplayMetrics();
            // 从默认显示器中获取显示参数保存到dm对象中
            wm.getDefaultDisplay().getMetrics(dm);
            size.x = dm.widthPixels;
            size.y = dm.heightPixels;
        }
        return size;
    }
}

