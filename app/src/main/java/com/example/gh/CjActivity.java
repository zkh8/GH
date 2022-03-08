package com.example.gh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gh.widget.NineLuckPan;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CjActivity extends BaseActivity {


    private String Tag = CjActivity.class.getSimpleName();

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

    private String lmd5 = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cj);

        mainApplication = MainApplication.getInstance();

        params = getWindow().getAttributes();


        Bundle bundle = getIntent().getExtras();

        if(bundle != null){

            fltype = bundle.getInt("fltype", 0);
        }

        iniView();

        loadData();
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

                                imgArr[i] = imgtype[data.getInt("prize" + ( i + 1 ) + "_type")];

                                String str = data.getString("prize" + ( i + 1 ) + "_describe");

                                if(data.getInt("prize" + ( i + 1 ) + "_type") == 0){

                                    if(str.isEmpty()){

                                        str = strtype[data.getInt("prize" + ( i + 1 ) + "_type")];
                                    }
                                }else{

                                    if(str.isEmpty()){

                                        str = data.getString("prize" + ( i + 1 ) + "_value") + strtype[data.getInt("prize" + ( i + 1 ) + "_type")];
                                    }
                                }

                                strArr[i] = str;

                                ln = data.getInt("ln");
                                rc = data.getInt("rc");
                            }

                            lmd5 = data.getString("lmd5");

                            iniView();
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

    private void iniView() {

        NineLuckPan luckpan = (NineLuckPan) findViewById(R.id.luckpan);

        luckpan.setmImgs(imgArr);
        luckpan.setmLuckStr(strArr);
        luckpan.setmLuckNum(ln);
        luckpan.setmRepeatCount(rc);

        luckpan.setOnLuckPanAnimEndListener(new NineLuckPan.OnLuckPanAnimEndListener() {
            @Override
            public void onAnimEnd(int position, String msg) {

                luckpan.setmLuckNum(-1);

                save();
            }
        });
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

                            int ltype = data.getInt("t");
                            int lvluae = data.getInt("v");
                            String lmsg = data.getString("m");
                            lucks = data.getInt("lucks");

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
    }

    private void ss_popupWindow_view(int ltype, int value, String msg) {

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

            ((TextView)popupView.findViewById(R.id.id_tv_1)).setText("恭喜您获得");
            popupView.findViewById(R.id.id_ll_r).setVisibility(View.VISIBLE);
            ((TextView)popupView.findViewById(R.id.id_cjr)).setText("" + value);
            ((TextView)popupView.findViewById(R.id.id_cjrt)).setText(ltype == 1 ? "积分" : "元");
        }

        params.alpha=0.3f;
        getWindow().setAttributes(params);
        popupWindow_ss.showAtLocation(findViewById(R.id.id_cj_main), Gravity.CENTER, 0, 0);
    }
}