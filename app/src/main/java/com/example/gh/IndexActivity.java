package com.example.gh;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gh.adapter.IndexFragmentAdapter;
import com.example.gh.util.DateUtil;
import com.example.gh.util.DownloadUtil;

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

public class IndexActivity extends BaseActivity implements View.OnClickListener{

    private String Tag = IndexActivity.class.getSimpleName();

    private MainApplication mainApplication;

    ViewPager2 viewPager2;
    List<LinearLayout> tabs_linearLayout = new ArrayList<>();
    private ArrayList<Fragment> fragments;

    ImageView tabs_imageView_current;
    TextView tabs_textView_current;
    private int cureent = 0;

    GhscFragment fr_gh;
    GrzxFragment fr_wd;


    private String downloadUrl = "";
    private String targetVersion = "";

    private int lloadtime = 0;
    private int lloadDataInterval = 3600 * 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        mainApplication = MainApplication.getInstance();
        iniView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
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

    private void iniView() {

        Log.d(Tag, "iniTab");

        tabs_linearLayout.add(findViewById(R.id.id_tab_1));
        tabs_linearLayout.add(findViewById(R.id.id_tab_2));
        tabs_linearLayout.add(findViewById(R.id.id_tab_3));

        for (LinearLayout linearLayout : tabs_linearLayout) {

            linearLayout.setOnClickListener(this);
        }

        tabs_imageView_current = findViewById(R.id.id_tab_img_1);
        tabs_textView_current = findViewById(R.id.id_tab_text_1);



        viewPager2 = findViewById(R.id.id_main_viewpager);
        fragments = new ArrayList<>();

        fr_gh = new GhscFragment();
        fr_wd = new GrzxFragment();

        fragments.add(fr_gh);
        fragments.add(new QdjlFragment());
        fragments.add(fr_wd);

        //禁止左右滑动
        viewPager2.setUserInputEnabled(false);

        IndexFragmentAdapter myFragmentPagerAdapter = new IndexFragmentAdapter(getSupportFragmentManager(),getLifecycle(),fragments);
        viewPager2.setAdapter(myFragmentPagerAdapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                fragment_chg(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        tabs_imageView_current.setSelected(true);
        tabs_textView_current.setTextColor(getResources().getColor(R.color.bg_r));
    }

    private void fragment_chg(int position) {


        if(tabs_imageView_current != null){

            tabs_imageView_current.setSelected(false);
            tabs_textView_current.setTextColor(getResources().getColor(R.color.black));
        }

        switch (position) {
            case R.id.id_tab_1:
                mainApplication.YZ_url_type = 0;
            case R.id.id_btn_ljdh:

                if(position == R.id.id_btn_ljdh){
                    mainApplication.YZ_url_type = 1;
                }
                viewPager2.setCurrentItem(0,false);
            case 0:

                tabs_imageView_current = findViewById(R.id.id_tab_img_1);
                tabs_textView_current = findViewById(R.id.id_tab_text_1);
                cureent = 0;
                break;
            case R.id.id_tab_2:

                viewPager2.setCurrentItem(1,false);
            case 1:

                tabs_imageView_current = findViewById(R.id.id_tab_img_2);
                tabs_textView_current = findViewById(R.id.id_tab_text_2);
                cureent = 1;
                break;
            case R.id.id_tab_3:
                mainApplication.YZ_url_type = 0;
            case R.id.id_wdjf:

                if(position == R.id.id_wdjf){
                    mainApplication.YZ_url_type = 1;
                }

                viewPager2.setCurrentItem(2,false);
            case 2:

                tabs_imageView_current = findViewById(R.id.id_tab_img_3);
                tabs_textView_current = findViewById(R.id.id_tab_text_3);
                cureent = 2;
                break;
        }

        tabs_imageView_current.setSelected(true);
        tabs_textView_current.setTextColor(getResources().getColor(R.color.bg_r));
    }

    @Override
    public void onClick(View v) {

        fragment_chg(v.getId());
    }

    long waitTime = 2000;
    long touchTime = 0;

    @Override
    public void onBackPressed() {

        if(cureent == 0){

            if(fr_gh.onBack()){
                return;
            }
        }
        else if(cureent == 2){

            if(fr_wd.onBack()){
                return;
            }
        }

        long currentTime = System.currentTimeMillis();
        if((currentTime-touchTime)>=waitTime) {

            Toast.makeText(this, "再按一次退出", (int)waitTime).show();

            touchTime = currentTime;
        }else {
            finish();
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
            data = FileProvider.getUriForFile(this, "com.yunlu.bxgh.provider", file);
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

        if(lloadtime > DateUtil.getTimes()){

            return;
        }
        lloadtime = DateUtil.getTimes() + lloadDataInterval;


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

                        JSONObject jsonObject = new JSONObject(resp);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        int code = jsonObject.getInt("code");

                        if(status.equals("success")){

                            JSONObject data = jsonObject.getJSONObject("data");

                            targetVersion = data.getString("app_version");
                            downloadUrl = data.getString("app_url");


                            if(targetVersion.isEmpty() || downloadUrl.isEmpty()){

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