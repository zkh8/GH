package com.example.gh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gh.util.TextUtil;
import com.example.gh.util.ViewUtil;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TxActivity extends BaseActivity implements View.OnClickListener {

    private String Tag = TxActivity.class.getSimpleName();

    private MainApplication mainApplication;


    private EditText et_name, et_card, et_money;
    private TextView tv_ktxje, btn_save;

    private int money_max = 0;
    private Double money = 0.0;
    private String card_name = "";
    private String card_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx);

        mainApplication = MainApplication.getInstance();

        iniBtn();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void iniBtn() {

        et_name = findViewById(R.id.id_name);
        et_card = findViewById(R.id.id_card);
        et_money = findViewById(R.id.id_txje);
        et_money.addTextChangedListener(new TxActivity.MyTextWatcher(et_money));

        tv_ktxje = findViewById(R.id.id_ktxje);

        findViewById(R.id.id_btn_back).setOnClickListener(this);
        findViewById(R.id.id_btn_qbtx).setOnClickListener(this);
        btn_save = findViewById(R.id.id_btn_save);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.id_btn_back:
                finish();
                break;
            case R.id.id_btn_qbtx:

                et_money.setText(money_max + "");
                break;
            case R.id.id_btn_save:
                save();
                break;
        }
    }

    private void save() {

        String str_name = et_name.getText().toString().trim();
        String str_card = et_card.getText().toString().trim();
        String str_money = et_money.getText().toString().trim();

        if(str_name.isEmpty()){

            myToast(et_name.getHint().toString());
            return;
        }

        if(str_card.isEmpty()){

            myToast(et_card.getHint().toString());
            return;
        }

        int txje = Integer.parseInt(str_money, 10);

        if(str_money.isEmpty() || txje < 1){

            myToast("提现金额不能小于1");
            return;
        }

        if(txje > money){

            myToast("提现金额不能不能大小" + money);
            return;
        }


        LoadingTimes(1);
        LoadingDialog(true);

        String url = mainApplication.APP_URL + "/api/app/withdrawal/save";

        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("money", "" + txje)
                .add("cardname", "" + str_name)
                .add("cardid", "" + str_card)
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


                    try {

                        LoadingTimes(-1);
                        LoadingDialog(false);

                        if(resp.equals("login")){

                            sessLogout();
                            return;
                        }

                        et_money.setText("");

                        JSONObject jsonObject = new JSONObject(resp);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if(status.equals("success")){

                            JSONObject data = jsonObject.getJSONObject("data");

                            money = data.getDouble("money");
                            card_name = data.getString("card_name");
                            card_id = data.getString("card_id");

                            et_name.setText(card_name);
                            et_card.setText(card_id);
                            tv_ktxje.setText(money + "");

                            myToast("已提交提现审核");
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

    private void loadData(){

        LoadingTimes(1);
        LoadingDialog(true);

        String url = mainApplication.APP_URL + "/api/app/withdrawal";

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

                            money = data.getDouble("money");
                            money_max = Integer.parseInt(Math.round(Math.floor(money)) + "", 10);
                            card_name = data.getString("card_name");
                            card_id = data.getString("card_id");

                            et_name.setText(card_name);
                            et_card.setText(card_id);
                            tv_ktxje.setText(money + "");
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

            if(mView.getId() == R.id.id_txje){

                if(str.length() > 0){

                    if(Integer.parseInt(str, 10) > money_max){

                        mView.setText(money_max + "");
                    }
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

            ViewUtil.hideOneInputMethod(TxActivity.this, mView);
        }
    }
}