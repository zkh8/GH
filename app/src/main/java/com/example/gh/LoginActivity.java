package com.example.gh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gh.bean.UserInfo;
import com.example.gh.util.TextUtil;
import com.example.gh.util.ViewUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    private MainApplication mainApplication;
    private final String Tag = LoginActivity.class.getSimpleName();

    private EditText et_phone, et_yzm;
    private TextView btn_hqyzm;
    private ImageView btn_phone_del;

    private String form_client;
    private String form_sid;

    private UserInfo userInfo;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mainApplication = MainApplication.getInstance();

        et_phone = findViewById(R.id.id_phone);
        et_phone.addTextChangedListener(new LoginActivity.MyTextWatcher(et_phone));


        et_yzm = findViewById(R.id.id_yzm);
        btn_hqyzm = findViewById(R.id.id_btn_yzm);

        btn_hqyzm.setOnClickListener((View)->{

            String phone = et_phone.getText().toString().trim();

            if(phone.equals("") || phone.length() != 11){

                myToast("请输入正确的电话号码");
            }else{

                loadVC(phone);
            }
        });

        btn_phone_del = findViewById(R.id.id_phone_del);
        btn_phone_del.setOnClickListener(this);
        btn_phone_del.setVisibility(View.GONE);

        findViewById(R.id.id_cb).setOnClickListener(this);
        findViewById(R.id.id_save).setOnClickListener(this);
        findViewById(R.id.id_btn_xy).setOnClickListener(this);


        SharedPreferences sharedPreferences = getSharedPreferences("share_login", MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        String token = sharedPreferences.getString("token", "");

        Boolean auto = sharedPreferences.getBoolean("auto", false);
        int login_at = sharedPreferences.getInt("login_at", 0);
        String uuid = sharedPreferences.getString("uuid", "");

        long timecurrentTimeMillis = System.currentTimeMillis();

        if(auto && login_at + mainApplication.login_times > timecurrentTimeMillis / 1000){

            mainApplication.APP_TOKEN = token;
            mainApplication.UUID = uuid;

            Thread myThread = new Thread() {//创建子线程
                @Override
                public void run() {
                    try {
                        sleep(1000);

                        if(!isFinishing()){

                            loginOk();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            myThread.start();
        }else{

            form_sid = UUID.randomUUID().toString();
            mainApplication.UUID = form_sid;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.id_cb:


                if(((CheckBox)v).isChecked()){

                    findViewById(R.id.id_save).setEnabled(true);
                }else{

                    findViewById(R.id.id_save).setEnabled(false);
                }
                break;
            case R.id.id_save:

                save();
                break;
            case R.id.id_phone_del:

                et_phone.setText("");
                et_phone.requestFocus();
                break;
            case R.id.id_btn_xy:

                Intent intent = new Intent();
                intent.setClass(this, XyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
        }
    }

    private void save() {

        String sjhm = et_phone.getText().toString().trim();
        String yzm = et_yzm.getText().toString().trim();

        if(sjhm.equals("") || sjhm.length() != 11){

            myToast("请输入正确的电话号码");
            et_phone.requestFocus();
            return;
        }

        if(yzm.equals("") || form_sid == null){

            myToast("验证码错误请再次输入");
            et_yzm.requestFocus();
            return;
        }

        LoadingTimes(1);
        LoadingDialog(true);

        String url_chkVC = mainApplication.APP_URL + "/api/app/login";

        FormBody body = new FormBody.Builder()
                .add("sid", form_sid)
                .add("code", yzm)
                .add("phone", sjhm).build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().post(body).url(url_chkVC).build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                runOnUiThread(()->{

                    LoadingTimes(-1);
                    LoadingDialog(false);
                    Log.d(Tag, e.getMessage());

                    myToast(1000);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {


                String resp = response.body().string();

                Log.d(Tag, resp);

                runOnUiThread(()->{

                    try{
                        LoadingTimes(-1);
                        LoadingDialog(false);

                        JSONObject jsonObject = new JSONObject(resp);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        Log.d(Tag, status);

                        if(status.equals("success")){

                            JSONObject data = new JSONObject(jsonObject.getString("data"));
                            token = data.getString("token");

                            JSONObject customers = new JSONObject(data.getString("customer"));

                            mainApplication.APP_TOKEN = "Bearer " + token;

                            UserInfo userInfo = new UserInfo();
                            userInfo.id = customers.getInt("id");
                            userInfo.phone = customers.getString("phone");
                            userInfo.login_at = customers.getInt("login_at");
                            userInfo.yz_open_id = customers.getString("yz_open_id");
                            userInfo.yz_account_id = customers.getString("yz_account_id");

                            mainApplication.userInfo = userInfo;

                            SharedPreferences sharedPreferences = getSharedPreferences("share_login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putInt("id", userInfo.id);
                            editor.putString("phone", userInfo.phone);
                            editor.putInt("login_at", userInfo.login_at);
                            editor.putString("yz_open_id", userInfo.yz_open_id);
                            editor.putString("yz_account_id", userInfo.yz_account_id);
                            editor.putBoolean("auto", true);
                            editor.putString("token", mainApplication.APP_TOKEN);
                            editor.putString("uuid", mainApplication.UUID);
                            editor.commit();

                            loginOk();
                        }else{

                            if(!message.equals("")){
                                myToast(message);
                            }else{
                                myToast(1002);
                            }
                        }
                    }catch (Exception e){

                        e.printStackTrace();
                        myToast(1001);

                        Log.d(Tag, e.getMessage());
                    }
                });
            }
        });
    }

    private void loginOk() {

        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), IndexActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void loadVC(String phone) {

        LoadingTimes(1);
        LoadingDialog(true);

        String url_getVC = mainApplication.APP_URL + "/api/app/getVerificationCode";

        FormBody body = new FormBody.Builder()
                .add("sid", form_sid)
                .add("phone", phone).build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().post(body).url(url_getVC).build();

        Call call =  client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                runOnUiThread(()->{

                    LoadingTimes(-1);
                    LoadingDialog(false);
                    Log.d(Tag, e.getMessage());

                    myToast(1000);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String resp = response.body().string();

                Log.d(Tag, resp);

                runOnUiThread(()->{

                    LoadingTimes(-1);
                    LoadingDialog(false);
                    try{

                        JSONObject jsonObject = new JSONObject(resp);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        Log.d(Tag, status);

                        if(status.equals("success")){

                            if(!message.equals("")){

                                myToast(message);

                                et_yzm.setText(message);
                            }else{

                                myToast("验证码已发送，10分钟内有效");
                            }

                            LoginActivity.CountDownTimerUtils mCountDownTimerUtils = new LoginActivity.CountDownTimerUtils(btn_hqyzm, 60000, 1000);
                            mCountDownTimerUtils.start();
                        }else{

                            if(!message.equals("")){

                                myToast(message);
                            }else{

                                myToast(1002);
                            }
                        }
                    }catch (Exception e){

                        Log.d(Tag, e.getMessage());
                        myToast(1001);
                    }
                });
            }
        });
    }

    private class MyTextWatcher implements TextWatcher {
        private EditText mView; // 声明一个编辑框对象
        private int mMinLength = 6; // 声明一个最小长度变量
        private int mMaxLength = 16; // 声明一个最大长度变量

        public MyTextWatcher(EditText v, int minLength, int maxLength) {
            super();
            mView = v;
            mMinLength = minLength;
            mMaxLength = maxLength;
        }

        public MyTextWatcher(EditText v) {
            super();
            mView = v;
        }

        // 在编辑框的输入文本变化前触发
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        // 在编辑框的输入文本变化时触发
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        // 在编辑框的输入文本变化后触发
        public void afterTextChanged(Editable s) {
            String str = s.toString();

            if(mView.getId() == R.id.id_phone){

                if(str.length() > 0){

                    TextUtil.setTypeB(mView);
                    chgV(btn_phone_del, null, true);

                    if(str.length() >= 11){

                        hideOneInputMethod();
                    }
                }else{

                    TextUtil.setTypeN(mView);
                    chgV(btn_phone_del, null, false);
                }
            }
            else if(mView.getId() == R.id.id_yzm){

                if(str.length() > 0){

                    TextUtil.setTypeB(mView);
                }else{

                    TextUtil.setTypeN(mView);
                }
            }
        }

        public void chgV(View v1, View v2, boolean visible){


            if(visible){

                v1.setVisibility(View.VISIBLE);

                if(v2 != null){

                    v2.setVisibility(View.VISIBLE);
                }
            }else{

                v1.setVisibility(View.GONE);

                if(v2 != null){

                    v2.setVisibility(View.GONE);
                }
            }
        }

        public void hideOneInputMethod(){

            ViewUtil.hideOneInputMethod(LoginActivity.this, mView);
        }
    }

    public class CountDownTimerUtils extends CountDownTimer {

        private TextView mTextView;

        public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.mTextView = textView;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTextView.setClickable(false); //设置不可点击
            mTextView.setSelected(false);
            mTextView.setTextColor(getResources().getColor(R.color.bg_r_1));

            mTextView.setText(millisUntilFinished / 1000 + "秒后重发");
        }

        @Override
        public void onFinish() {
            mTextView.setText("重新获取验证码");
            mTextView.setClickable(true);
            mTextView.setSelected(true);
            mTextView.setTextColor(getResources().getColor(R.color.bg_b_50));
        }
    }
}