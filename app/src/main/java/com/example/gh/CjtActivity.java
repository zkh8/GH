package com.example.gh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gh.util.Utils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CjtActivity extends BaseActivity {

    private String Tag = CjtActivity.class.getSimpleName();

    private MainApplication mainApplication;

    private PopupWindow popupWindow_ss = null;
    private WindowManager.LayoutParams params;

    private int fltype = 0;
    private int lucks = 0;

    private int ln = -1;
    private int rc = (int)(3+Math.random()*(6-3+1));

    private int[] imgtype = {R.mipmap.cjb_88, R.mipmap.cjb_lh, R.mipmap.cjb_jb};
    private String[] strtype = {"大礼合", "积分", "元优惠券"};

    private int [] imgArr = {R.mipmap.cjb_88, R.mipmap.cjb_jb, R.mipmap.cjb_lh, R.mipmap.cjb_lh, R.mipmap.cjb_lh, R.mipmap.cjb_jb, R.mipmap.cjb_88, R.mipmap.cjb_88, R.mipmap.cjb4};
    private String[] strArr = {"88元优惠券", "88积分", "88元优惠券", "88元优惠券", "88元优惠券", "88积分", "88元优惠券", "88元优惠券", "立即\n抽奖"};

    private String [] imgStrArr = {"", "", "", "", "", "", "", "", ""};

    private Bitmap[] bitmapArr = {null, null, null, null, null, null, null, null, null};


    private String lmd5 = "";
    private boolean lucking = false;


    private String gzstr = "";

    private TextView tv_cjcs;
    private String lastLucks = "";

    private LinearLayout btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cjt);

        mainApplication = MainApplication.getInstance();

        params = getWindow().getAttributes();


        Bundle bundle = getIntent().getExtras();

        if(bundle != null){

            fltype = bundle.getInt("fltype", 0);
        }

        tv_cjcs = findViewById(R.id.id_cjcs);

        btn_start = findViewById(R.id.id_c8);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });

        findViewById(R.id.id_btn_cjgz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gz_popupWindow_view();
            }
        });
    }

    private void loadData() {

        LoadingTimes(1);
        LoadingDialog(true);

        String url = mainApplication.APP_URL + "/api/app/luck";

        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("fltype", fltype + "")
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
                    Log.d(Tag, e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String resp = response.body().string();

                Log.d(Tag, resp);

                runOnUiThread(()->{

                    LoadingTimes(-1);
                    LoadingDialog(false);

                    if(resp.equals("login")){

                        sessLogout();
                        return;
                    }

                    try {

                        JSONObject jsonObject = new JSONObject(resp);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if(status.equals("success")){

                            JSONObject data = jsonObject.getJSONObject("data");

                            for(int i = 0; i < 8 ; i ++){

                                String imgurl = data.getString("prize" + ( i + 1 ) + "_pic");
                                if(!imgurl.isEmpty()){

                                    if(imgurl.indexOf("://") == -1){

                                        imgurl = mainApplication.APP_URL + imgurl;
                                    }

                                    imgStrArr[i] = imgurl;
                                }


                                imgArr[i] = imgtype[data.getInt("prize" + ( i + 1 ) + "_type")];

                                String str = data.getString("prize" + ( i + 1 ) + "_describe");

                                /*
                                if(data.getInt("prize" + ( i + 1 ) + "_type") == 0){

                                    if(str.isEmpty()){

                                        str = strtype[data.getInt("prize" + ( i + 1 ) + "_type")];
                                    }
                                }else{

                                    if(str.isEmpty()){

                                        str = data.getString("prize" + ( i + 1 ) + "_value") + strtype[data.getInt("prize" + ( i + 1 ) + "_type")];
                                    }
                                }

                                 */

                                strArr[i] = str;

                                ln = data.getInt("ln");
                                rc = data.getInt("rc");
                            }

                            lmd5 = data.getString("lmd5");

                            if(data.has("lucks")){

                                lucks = data.getInt("lucks");
                            }

                            tv_cjcs.setText("剩余抽奖次数：" + lucks + "次");

                            if(data.has("rules")){

                                gzstr = data.getString("rules");
                            }


                            new Thread(runnable).start();
                            //iniView();
                        }else{

                            if(message.isEmpty()){

                                myToast(1002);
                            }else{
                                myToast(message);
                            }

                            finish();
                        }

                    }catch (Exception e){

                        e.printStackTrace();

                        Log.d(Tag, e.getMessage());
                        myToast(1001);
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void save() {

        LoadingTimes(1);
        LoadingDialog(true);

        String url = mainApplication.APP_URL + "/api/app/luck/save";

        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("sid", mainApplication.UUID)
                .add("lmd5", lmd5).build();

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
                    Log.d(Tag, e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String resp = response.body().string();

                Log.d(Tag, resp);

                runOnUiThread(()->{

                    try {

                        LoadingTimes(-1);
                        LoadingDialog(false);

                        if(resp.equals("login")){

                            sessLogout();
                            return;
                        }

                        JSONObject jsonObject = new JSONObject(resp);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if(status.equals("success")){

                            JSONObject data = jsonObject.getJSONObject("data");

                            int ltype = data.getInt("t");
                            String lvluae = data.getString("v");
                            String lmsg = data.getString("m");
                            lucks = data.getInt("lucks");

                            tv_cjcs.setText("剩余抽奖次数：" + lucks + "次");

                            ss_popupWindow_view(ltype, lvluae, lmsg);
                        }else{

                            loadData();

                            if(message.isEmpty()){
                                myToast(1002);
                            }else{
                                myToast(message);
                            }
                        }

                    }catch (Exception e){

                        loadData();

                        e.printStackTrace();

                        Log.d(Tag, e.getMessage());
                        myToast(1001);
                    }
                });
            }
        });

        lmd5 = "";
    }

    private void ss_popupWindow_view(int ltype, String value, String msg) {


        if(isFinishing()){

            Log.d(Tag, "ss_popupWindow_view --activity des");
            return;
        }

        lastLucks = "";

        View popupView = getLayoutInflater().inflate(R.layout.cj_item, null);
        popupWindow_ss = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow_ss.setTouchable(true);
        popupWindow_ss.setFocusable(true);
        popupWindow_ss.setOutsideTouchable(false);
        popupWindow_ss.setBackgroundDrawable(null);

        popupWindow_ss.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                params.alpha=1f;
                getWindow().setAttributes(params);

                if(!lastLucks.isEmpty()){

                    myToast(lastLucks);
                }

                if(lucks < 1){

                    finish();
                }else{

                    loadData();
                }
            }
        });

        popupView.findViewById(R.id.id_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow_ss.dismiss();
            }
        });


        popupView.findViewById(R.id.id_btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow_ss.dismiss();
            }
        });

        if(ltype == 0){

            if(msg.isEmpty()){
                msg = "感谢您参参与";
            }
            ((TextView)popupView.findViewById(R.id.id_tv_1)).setText(msg);
            popupView.findViewById(R.id.id_ll_r).setVisibility(View.GONE);
        }else{

            if(ltype == 2){

                ((TextView)popupView.findViewById(R.id.id_tv_1)).setText("恭喜您获得 现金红包");
                lastLucks = value + "元现金 已入帐";
            }else{

                ((TextView)popupView.findViewById(R.id.id_tv_1)).setText("恭喜您获得 积分");
                lastLucks = value + "积分 已入帐";
            }

            popupView.findViewById(R.id.id_ll_r).setVisibility(View.VISIBLE);
            ((TextView)popupView.findViewById(R.id.id_cjr)).setText("" + value);
            ((TextView)popupView.findViewById(R.id.id_cjrt)).setText(ltype == 1 ? "积分" : "元");
        }

        params.alpha=0.3f;
        getWindow().setAttributes(params);
        popupWindow_ss.showAtLocation(findViewById(R.id.id_cj_main), Gravity.CENTER, 0, 0);
    }

    private void gz_popupWindow_view() {

        int lid = R.layout.qdgz_item;

        int vlpW = ViewGroup.LayoutParams.MATCH_PARENT;
        int vlpH = ViewGroup.LayoutParams.WRAP_CONTENT;


        View popupView = getLayoutInflater().inflate(lid, null);
        popupWindow_ss = new PopupWindow(popupView, vlpW, vlpH);

        popupWindow_ss.setTouchable(true);
        popupWindow_ss.setFocusable(true);
        popupWindow_ss.setOutsideTouchable(false);
        popupWindow_ss.setBackgroundDrawable(null);

        popupWindow_ss.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                params.alpha=1f;
                getWindow().setAttributes(params);
            }
        });


        ((TextView)popupView.findViewById(R.id.id_title)).setText("抽奖规则");

        WebView webView = popupView.findViewById(R.id.id_webview);
        TextView tv_qdgz = popupView.findViewById(R.id.tv_qdgz);

        popupView.findViewById(R.id.id_btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupWindow_ss.dismiss();
            }
        });


        if (gzstr.isEmpty()) {

            webView.setVisibility(View.VISIBLE);
            tv_qdgz.setVisibility(View.GONE);

            //支持javascript
            webView.getSettings().setJavaScriptEnabled(true);
            // 设置可以支持缩放
            webView.getSettings().setSupportZoom(true);
            // 设置出现缩放工具
            webView.getSettings().setBuiltInZoomControls(true);
            //扩大比例的缩放
            webView.getSettings().setUseWideViewPort(true);
            //自适应屏幕
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webView.getSettings().setLoadWithOverviewMode(true);


            //如果不设置WebViewClient，请求会跳转系统浏览器
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if (url.toString().contains("baidu.cn")) {

                        view.loadUrl("https://item.jd.com/100027401298.html");
                        return true;
                    }

                    return false;
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (request.getUrl().toString().contains("baidu.com")) {
                            view.loadUrl("https://item.jd.com/100027401298.html");
                            return true;
                        }
                    }

                    return false;
                }

            });

            webView.loadUrl(mainApplication.APP_URL + "/api/luck/greement");
            //webView.loadUrl("https://item.jd.com/100027401298.html");
            webView.getSettings().setDisplayZoomControls(false);

        }else{

            webView.setVisibility(View.GONE);
            tv_qdgz.setVisibility(View.VISIBLE);
            tv_qdgz.setText(gzstr);
        }


        params.alpha=0.3f;
        getWindow().setAttributes(params);
        popupWindow_ss.showAtLocation(findViewById(R.id.id_cj_main), Gravity.CENTER, 0, 0);
    }

    long waitTime = 2000;
    long touchTime = 0;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Log.d(Tag, "onBackPressed lucking : ");
        long currentTime = System.currentTimeMillis();

        if(valueAnimator != null && valueAnimator.isRunning()){

            if(currentTime - touchTime > waitTime) {

                myToast("正在抽奖中 再按一次退出");
            }else{

                valueAnimator.cancel();
                finish();
            }
            touchTime = currentTime;
        }else{
            finish();
        }
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("mylog","请求结果为-->" + val);

            if(!isDestroyed()){

                iniView();
            }
        }
    };



    private void iniView() {

        if(mViewArr == null){

            LinearLayout v0 = findViewById(R.id.id_c0);
            LinearLayout v1 = findViewById(R.id.id_c1);
            LinearLayout v2 = findViewById(R.id.id_c2);
            LinearLayout v3 = findViewById(R.id.id_c3);
            LinearLayout v4 = findViewById(R.id.id_c4);
            LinearLayout v5 = findViewById(R.id.id_c5);
            LinearLayout v6 = findViewById(R.id.id_c6);
            LinearLayout v7 = findViewById(R.id.id_c7);
            LinearLayout v8 = findViewById(R.id.id_c8);

            mViewArr = new LinearLayout[] {v0, v1, v2, v3, v4, v5, v6, v7, v8};

            ImageView iv0 = findViewById(R.id.id_civ_0);
            ImageView iv1 = findViewById(R.id.id_civ_1);
            ImageView iv2 = findViewById(R.id.id_civ_2);
            ImageView iv3 = findViewById(R.id.id_civ_3);
            ImageView iv4 = findViewById(R.id.id_civ_4);
            ImageView iv5 = findViewById(R.id.id_civ_5);
            ImageView iv6 = findViewById(R.id.id_civ_6);
            ImageView iv7 = findViewById(R.id.id_civ_7);
            //ImageView iv8 = findViewById(R.id.id_civ_8);

            mViewIvArr = new ImageView[] {iv0, iv1, iv2, iv3, iv4, iv5, iv6, iv7};


            LinearLayout ll_cj_v = findViewById(R.id.id_cj_v);

            int mRectSize = Math.min(ll_cj_v.getWidth(), ll_cj_v.getHeight()) / 4 - 20;

            LinearLayout.LayoutParams params;

            for(int i = 0; i < 9; i ++){

                params = (LinearLayout.LayoutParams) mViewArr[i].getLayoutParams();
                params.width = mRectSize;
                params.height = mRectSize;

                mViewArr[i].setLayoutParams(params);
                mViewArr[i].setPadding(10,10,10,25);


                if(i > 7){

                    continue;
                }

                mViewArr[i].setSelected(false);

                mViewIvArr[i].setImageBitmap(bitmapArr[i]);
            }


        }
    }

    Runnable runnable = new Runnable(){
        @Override
        public void run() {

            if(isDestroyed()){

                return;
            }
            for(int i = 0; i < bitmapArr.length; i ++){


                if(i > 7){
                    break;
                }

                String imgurl = imgStrArr[i];

                if(imgurl.isEmpty() || imgurl == null){

                    bitmapArr[i] = BitmapFactory.decodeResource(getResources(),imgArr[i]);
                }else{

                    boolean chk = true;

                    try {
                        bitmapArr[i] = Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(imgurl)
                                .into(200, 200)
                                .get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        Log.i(Tag, "printStackTrace 0000");
                        chk = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.i(Tag, "InterruptedException 1111");
                        chk = false;
                    }


                    if(!chk){

                        bitmapArr[i] = BitmapFactory.decodeResource(getResources(),imgArr[i]);
                    }
                }
            }

            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value","请求结果");
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

    ValueAnimator valueAnimator;
    private int mStartLuckPosition = 0;

    private LinearLayout[] mViewArr;
    private ImageView[] mViewIvArr;


    private void start(){

        if(lmd5.isEmpty()){

            myToast("数据更新中请稍后再试");
            loadData();
            return;
        }

        if(lucking){

            myToast("正在抽奖中");
            return;
        }

        rc = (int)(3+Math.random()*(6-3+1));

        valueAnimator = ValueAnimator.ofInt(mStartLuckPosition, rc * 8 + ln).setDuration(5000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int position = (int) animation.getAnimatedValue();
                chgview(position%8);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mStartLuckPosition = ln;

                btn_start.setEnabled(true);
                lucking = false;
                save();
            }
        });

        btn_start.setEnabled(false);
        lucking = true;
        valueAnimator.start();
    }

    private void chgview(int position) {

        for(int i = 0; i < 8; i ++){

            if(i == position){

                mViewArr[i].setSelected(true);
            }else{
                mViewArr[i].setSelected(false);
            }
        }
    }
}