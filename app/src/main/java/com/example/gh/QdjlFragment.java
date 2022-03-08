package com.example.gh;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.gh.util.DateUtil;
import com.youzan.androidsdk.YouzanSDK;
import com.youzan.androidsdkx5.YouZanSDKX5Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QdjlFragment extends BaseFragment implements View.OnClickListener {

    private String Tag = QdjlFragment.class.getSimpleName();

    private MainApplication mainApplication;
    private View rootView;


    private PopupWindow popupWindow_ss = null;
    private WindowManager.LayoutParams params;


    private List<Boolean> qd_list = new ArrayList<Boolean>();

    private List<LinearLayout> qd_layoutList = new ArrayList<LinearLayout>();
    private List<TextView> qd_tvlist = new ArrayList<TextView>();
    private List<ImageView> qd_ivlist = new ArrayList<ImageView>();
    private LinearLayout btn_tx;

    private TextView tv_wdjf;
    private TextView tv_xjsy;
    private TextView tv_gg;

    private TextView tv_jf1, tv_jf2;

    private int lucks = 0;


    public QdjlFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView == null){

            rootActivity = (AppCompatActivity) getActivity();

            rootView = inflater.inflate(R.layout.fragment_qdjl, container, false);

            mainApplication = MainApplication.getInstance();

            params = getActivity().getWindow().getAttributes();

            iniBtn();
        }
        return rootView;
    }

    private void iniBtn() {

        for(int i = 0; i < 8; i ++){

            qd_list.add(false);
        }

        btn_tx = rootView.findViewById(R.id.id_btn_tx);
        btn_tx.setOnClickListener(this);


        tv_wdjf = rootView.findViewById(R.id.id_wdjf);
        tv_xjsy = rootView.findViewById(R.id.id_xjsy);
        tv_gg = rootView.findViewById(R.id.id_btn_gg);


        qd_layoutList.add(rootView.findViewById(R.id.id_qd_ll_1));
        qd_layoutList.add(rootView.findViewById(R.id.id_qd_ll_2));
        qd_layoutList.add(rootView.findViewById(R.id.id_qd_ll_3));
        qd_layoutList.add(rootView.findViewById(R.id.id_qd_ll_4));
        qd_layoutList.add(rootView.findViewById(R.id.id_qd_ll_5));
        qd_layoutList.add(rootView.findViewById(R.id.id_qd_ll_6));
        qd_layoutList.add(rootView.findViewById(R.id.id_qd_ll_7));

        qd_tvlist.add(rootView.findViewById(R.id.id_tv_1));
        qd_tvlist.add(rootView.findViewById(R.id.id_tv_2));
        qd_tvlist.add(rootView.findViewById(R.id.id_tv_3));
        qd_tvlist.add(rootView.findViewById(R.id.id_tv_4));
        qd_tvlist.add(rootView.findViewById(R.id.id_tv_5));
        qd_tvlist.add(rootView.findViewById(R.id.id_tv_6));
        qd_tvlist.add(rootView.findViewById(R.id.id_tv_7));

        qd_ivlist.add(rootView.findViewById(R.id.id_iv_1));
        qd_ivlist.add(rootView.findViewById(R.id.id_iv_2));
        qd_ivlist.add(rootView.findViewById(R.id.id_iv_3));
        qd_ivlist.add(rootView.findViewById(R.id.id_iv_4));
        qd_ivlist.add(rootView.findViewById(R.id.id_iv_5));
        qd_ivlist.add(rootView.findViewById(R.id.id_iv_6));
        qd_ivlist.add(rootView.findViewById(R.id.id_iv_7));

        rootView.findViewById(R.id.id_btn_gg).setOnClickListener(this);
        rootView.findViewById(R.id.id_btn_cj).setOnClickListener(this);
        rootView.findViewById(R.id.id_btn_ljdh).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()){
            case R.id.id_btn_gg:
                break;
            case R.id.id_btn_tx:

                intent = new Intent();
                intent.setClass(rootActivity, TxActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.id_btn_cj:

                if(lucks < 1){

                    myToast("观看 视频广告 可获取抽奖机会");
                    return;
                }
                intent = new Intent();
                intent.setClass(rootActivity, CjActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.id_btn_ljdh:

                mainApplication.YZ_url_type = 1;

                IndexActivity indexActivity = (IndexActivity) getActivity();
                indexActivity.onClick(v);
                break;
        }
    }

    private void loadData(){

        LoadingTimes(1);
        LoadingDialog(true);

        String url = mainApplication.APP_URL + "/api/app/sign";

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

                rootActivity.runOnUiThread(()->{

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

                rootActivity.runOnUiThread(()->{

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
                        int code = jsonObject.getInt("code");

                        if(code == 1000){
                            sessLogout(code);
                            return;
                        }

                        if(status.equals("success")){

                            JSONObject data = jsonObject.getJSONObject("data");

                            JSONArray qd_arr = data.getJSONArray("qd_list");

                            for(int i = 0; i < qd_arr.length(); i ++){

                                if(qd_arr.getInt(i) == 1){

                                    qd_layoutList.get(i).setSelected(true);
                                    qd_ivlist.get(i).setSelected(true);
                                    qd_tvlist.get(i).setTextColor(rootActivity.getResources().getColor(R.color.white));
                                }else{

                                    qd_layoutList.get(i).setSelected(false);
                                    qd_ivlist.get(i).setSelected(false);
                                    qd_tvlist.get(i).setTextColor(rootActivity.getResources().getColor(R.color.black));
                                }
                            }

                            lucks = data.getInt("lucks");


                            tv_wdjf.setText("" + data.getInt("point"));
                            tv_xjsy.setText("" + data.getInt("money"));
                            tv_gg.setText("(" + data.getInt("gg") + "/" + data.getInt("gg_max") + ")");

                            if(data.getInt("qd_today") == 1){

                                ss_popupWindow_view(data.getInt("gd_point"), data.getInt("gg_point"));
                            }
                            else if(data.getInt("qd_today") == 7){

                                rootView.findViewById(R.id.id_btn_cj).callOnClick();

                                Intent intent;
                                intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putInt("fltype", 1);
                                intent.putExtras(bundle);
                                intent.setClass(rootActivity, CjActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
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

                        Log.d(Tag, "onResponse " + e.getMessage());

                        myToast(1001);
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void ss_popupWindow_view(int jf1, int jf2) {

        View popupView = getLayoutInflater().inflate(R.layout.qd_item, null);
        popupWindow_ss = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow_ss.setTouchable(true);
        popupWindow_ss.setFocusable(true);
        popupWindow_ss.setOutsideTouchable(false);
        popupWindow_ss.setBackgroundDrawable(null);

        popupWindow_ss.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                params.alpha=1f;
                getActivity().getWindow().setAttributes(params);
            }
        });

        popupView.findViewById(R.id.id_btn_ksp).setOnClickListener(new View.OnClickListener() {
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

        tv_jf1 = popupView.findViewById(R.id.id_jf1);
        tv_jf2 = popupView.findViewById(R.id.id_jf2);

        tv_jf1.setText("" + jf1);
        tv_jf2.setText("" + jf2);

        params.alpha=0.3f;
        getActivity().getWindow().setAttributes(params);
        popupWindow_ss.showAtLocation(rootView.findViewById(R.id.id_qd_main), Gravity.CENTER, 0, 0);
    }
}