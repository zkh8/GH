package com.example.gh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.gh.util.DateUtil;
import com.example.gh.util.DownloadUtil;
import com.youzan.androidsdk.YouzanSDK;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SetActivity extends BaseActivity implements View.OnClickListener {

    private String Tag = SetActivity.class.getSimpleName();

    private MainApplication mainApplication;

    private String downloadUrl = "";
    private String targetVersion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);


        mainApplication = MainApplication.getInstance();


        findViewById(R.id.id_btn_back).setOnClickListener(this);
        findViewById(R.id.id_btn_about).setOnClickListener(this);
        findViewById(R.id.id_btn_xy).setOnClickListener(this);
        findViewById(R.id.id_btn_out).setOnClickListener(this);
        findViewById(R.id.id_btn_vc).setOnClickListener(this);

        ((TextView)findViewById(R.id.id_tv_phone)).setText(mainApplication.userInfo.phone);

        ((TextView)findViewById(R.id.id_tv_vc)).setText(getVersionName());
    }


    @Override
    public void onClick(View v) {

        Intent intent = new Intent();

        switch (v.getId()){

            case R.id.id_btn_back:
                finish();
                break;
            case R.id.id_btn_about:
                intent.setClass(this, AboutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.id_btn_xy:

                intent.setClass(this, XyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.id_btn_out:

                YouzanSDK.userLogout(this);

                intent.setClass(this, LoginActivity.class);

                SharedPreferences sharedPreferences = getSharedPreferences("share_login", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.putBoolean("auto", false);
                editor.commit();

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.id_btn_vc:
                loadData();
                break;

        }
    }


    private void chkup() {
        //检查更新
        try {
            //6.0才用动态权限
            if (Build.VERSION.SDK_INT >= 23) {
                String[] permissions = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.INTERNET};
                List<String> permissionList = new ArrayList<>();
                for (int i = 0; i < permissions.length; i++) {
                    if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                        permissionList.add(permissions[i]);
                    }
                }
                if (permissionList.size() <= 0) {
                    //说明权限都已经通过，可以做你想做的事情去
                    //自动更新

                    if(versionCheck(targetVersion)) {
                        showTip(this);
                    }else{
                        myToast("已经是最新版本");
                    }

                } else {
                    //存在未允许的权限
                    ActivityCompat.requestPermissions(this, permissions, 100);
                }
            }
        } catch (Exception ex) {
            myToast("自动更新异常：" + ex.getMessage());
        }
    }


    private File file;
    private ProgressBar mProgress;
    private TextView txtStatus;
    private boolean intercept = false;

    private int progress;// 当前进度

    //新版本检测
    private boolean versionCheck(String targetVersion) {

        if(getVersionName().contentEquals(targetVersion)) {

            return false;
        } else {

            return true;
        }
    }

    //升级弹窗
    private void showTip(Context context) {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle("升级提示").setMessage("检测到新版本，请升级")
                .setNeutralButton("升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShowDownloadDialog(context);
                        //doDownload();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
        dialog.setCanceledOnTouchOutside(false);//可选
        dialog.setCancelable(false);//可选
    }

    private void ShowDownloadDialog(Context context) {
        intercept = false;
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("软件版本更新");
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        txtStatus = v.findViewById(R.id.txtStatus);
        dialog.setView(v);
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intercept = true;
            }
        });
        dialog.show();
        doDownload();
    }

    //下载升级包
    private void doDownload() {

        if(downloadUrl.isEmpty()){

            downloadUrl = mainApplication.APP_URL + "/static/down/gh.apk";
        }

        String parentPath = "";
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                parentPath = this.getExternalFilesDir(null).getPath();
            } else {
                parentPath = this.getFilesDir().getPath();
            }
        } catch (Exception e) {
            Log.d(Tag, e.getMessage());
        }

        file = new File(parentPath, "gh.apk");
        final String filePath = file.getAbsolutePath();

        if (file.exists()) {

            file.delete();
        }

        try {
            DownloadUtil.get().download(downloadUrl, filePath, new DownloadUtil.OnDownloadListener() {
                @Override
                public void onDownloadSuccess() {
                    //成功
                    installApk();
                }

                @Override
                public boolean onDownloading(int progressTemp) {
                    //进度
                    Log.d(Tag, "doDownload download:" + progressTemp +"%");

                    progress = progressTemp;
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    //mProgress.setProgress(progress);
                    //txtStatus.setText(progress);
                    return intercept;
                }

                @Override
                public void onDownloadFailed() {
                    //失败
                    Log.d(Tag, "doDownload download fail");
                }
            });
        } catch (Exception e) {
            Log.d(Tag, "doDownload e2:" + e.getMessage());
        }
    }

    //安装app
    private void installApk() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;

        //7.0以上安卓系统安装app需要通过fileProvider（需要在AndroidManifest.xml声明）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(this, "com.example.gh.provider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Log.d(Tag,"installApk 7.0data:" + data);
        } else {
            data = Uri.fromFile(file);
            Log.d(Tag,"installApk data:" + data);
        }

        intent.setDataAndType(data, "application/vnd.android.package-archive");
        this.startActivity(intent);
    }

    private String getVersionName() {
        String versionName = "";
        try {
            versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int SHOWDOWN = 3;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    Log.d(Tag, "progress : ----- " + progress);
                    txtStatus.setText(progress + "%");
                    mProgress.setProgress(progress);
                    break;
                default:
                    break;
            }
        }

    };


    private void loadData() {

        LoadingTimes(1);
        LoadingDialog(true);


        String url = mainApplication.APP_URL + "/api/app/version";

        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("sid", mainApplication.UUID).build();

        Request request = new Request.Builder()
                .post(body)
                .header("Authorization", mainApplication.APP_TOKEN)
                .url(url).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                runOnUiThread(()->{

                    LoadingTimes(-1);
                    LoadingDialog(false);
                    myToast(1000);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String resp = response.body().string();

                Log.d(Tag, url);
                Log.d(Tag, resp);

                runOnUiThread(()->{

                    try {
                        LoadingTimes(-1);
                        LoadingDialog(false);


                        JSONObject jsonObject = new JSONObject(resp);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        int code = jsonObject.getInt("code");

                        if(status.equals("success")){

                            JSONObject data = jsonObject.getJSONObject("data");

                            targetVersion = data.getString("app_version");
                            downloadUrl = data.getString("app_url");

                            if(targetVersion.isEmpty() || downloadUrl.isEmpty()){

                                myToast("已经是最新版本");
                            }else{

                                if(downloadUrl.indexOf("://") == -1){

                                    downloadUrl = mainApplication.APP_URL + downloadUrl;
                                }

                                chkup();
                            }
                        }else{

                            if(message.isEmpty()){
                                myToast(1002);
                            }else{
                                myToast(message);
                            }
                        }
                    }catch (Exception e){

                        e.printStackTrace();
                        myToast(1001);
                    }
                });
            }
        });
    }
}