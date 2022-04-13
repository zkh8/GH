package com.example.gh.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadUtil {

    private static String Tag = "DownloadUtil";

    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;

    public static DownloadUtil get() {
        if (downloadUtil == null) {

            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    /**
     * @param url 下载连接
     * @param filePath 储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public void download(final String url, final String filePath, final OnDownloadListener listener) {

        Request request = new Request.Builder().url(url).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(Tag, "DownloadUtil onFailure e:" + e.getMessage());
                listener.onDownloadFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(Tag, "DownloadUtil onResponse start");
                boolean intercept = false;
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                //String savePath = isExistDir(saveDir);
                Log.d(Tag, "DownloadUtil filePath:" + filePath);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    //File file = new File(savePath, getNameFromUrl(url));
                    File file = new File(filePath);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        intercept = listener.onDownloading(progress);

                        if(intercept){
                            break;
                        }
                    }
                    fos.flush();
                    // 下载完成
                    if(!intercept){

                        listener.onDownloadSuccess();
                    }
                } catch (Exception e) {
                    Log.d(Tag, "DownloadUtil onResponse e1:" + e.getMessage());
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        Log.d(Tag, "DownloadUtil onResponse e2:" + e.getMessage());
                    }
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        Log.d(Tag, "DownloadUtil onResponse e3:" + e.getMessage());
                    }
                }
            }

        });
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * @param url
     * @return
     * 从下载连接中解析出文件名
     */
    public static String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress
         * 下载进度
         */
        boolean onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
}
