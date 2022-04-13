package com.example.gh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gh.adapter.OnLoadMoreListener;
import com.example.gh.adapter.SpaceItemDecoration;
import com.example.gh.adapter.SymxAdapter;
import com.example.gh.bean.SymxInfo;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SyActivity extends BaseActivity implements View.OnClickListener {

    private String Tag = TxActivity.class.getSimpleName();

    private MainApplication mainApplication;

    RecyclerView recyclerView = null;
    SymxAdapter myAdapter = null;
    private OnLoadMoreListener mOnLoadMoreListener;
    private Handler handler;

    private SwipeRefreshLayout swipeRefreshLayout;
    private List<SymxInfo> symxinfoList = new ArrayList<SymxInfo>();


    private TextView btn_tx, btn_tx_t, btn_sz_t, tv_ktxye, tv_jrsy;
    private View btn_tx_l, btn_sz_l;
    private int ltype = 0;

    private int page = 1, perPage = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sy);

        mainApplication = MainApplication.getInstance();

        findViewById(R.id.id_btn_back).setOnClickListener(this);


        tv_ktxye = findViewById(R.id.id_tv_ktxye);
        tv_jrsy = findViewById(R.id.id_tv_jrsy);

        btn_tx = findViewById(R.id.btn_tx);
        btn_tx.setOnClickListener(this);

        btn_tx_t = findViewById(R.id.btn_tx_t);
        btn_tx_t.setOnClickListener(this);
        btn_sz_t = findViewById(R.id.btn_sz_t);
        btn_sz_t.setOnClickListener(this);

        btn_tx_l = findViewById(R.id.btn_tx_l);
        btn_sz_l = findViewById(R.id.btn_sz_l);

        iniRecyclerView();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.id_btn_back:
            case R.id.btn_tx:
                finish();
                break;
            case R.id.btn_tx_t:
                chgltype(1);
                break;
            case R.id.btn_sz_t:
                chgltype(0);
                break;
        }
    }

    private void chgltype(int i) {

        if(i != ltype){

            ltype = i;

            if(ltype == 0){

                btn_sz_l.setVisibility(View.VISIBLE);
                btn_tx_l.setVisibility(View.GONE);

                btn_sz_t.setTextAppearance(R.style.text_style_selected);
                btn_tx_t.setTextAppearance(R.style.text_style_noselected);
            }else{

                btn_sz_l.setVisibility(View.GONE);
                btn_tx_l.setVisibility(View.VISIBLE);

                btn_sz_t.setTextAppearance(R.style.text_style_noselected);
                btn_tx_t.setTextAppearance(R.style.text_style_selected);
            }

            loadData(true);
        }
    }


    private void iniRecyclerView() {

        Log.d(Tag, Tag + " --  iniRecyclerView");

        recyclerView = findViewById(R.id.id_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        myAdapter = new SymxAdapter(symxinfoList, this, 0);

        recyclerView.addItemDecoration(new SpaceItemDecoration(5));
        recyclerView.setAdapter(myAdapter);

        swipeRefreshLayout = findViewById(R.id.id_list_swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Log.d(Tag, Tag + " --  onRefresh");

                loadData(true);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);

        handler = new Handler();

        mOnLoadMoreListener=new OnLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Log.d(Tag, Tag + " --  onLoading");

                        loadData(false);
                    }
                }, 1000);
            }
        };

        recyclerView.addOnScrollListener(mOnLoadMoreListener);

        myAdapter.setOnItemClickListener(new SymxAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, SymxAdapter.ViewName practise, int position) {

                Log.d(Tag, Tag + " --  onClick--" + v.getId() + " position :" + position);

                Intent intent = new Intent();

                switch (v.getId()){

                    default:

                        break;
                }
            }
        });
    }

    private void loadData(boolean reload) {

        if(reload){

            page = 1;
        }

        String url = mainApplication.APP_URL + "/api/app/bills";

        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("sid", mainApplication.UUID)
                .add("ltype", "" + ltype)
                .add("uid", "" + mainApplication.userInfo.id)
                .add("page", "" + page)
                .add("perPage", "" + perPage).build();

        Request request = new Request.Builder()
                .post(body)
                .header("Authorization", mainApplication.APP_TOKEN)
                .url(url).build();

        Log.d(Tag, "loadData " + url);

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                runOnUiThread(()->{

                    if(swipeRefreshLayout.isRefreshing()){

                        swipeRefreshLayout.setRefreshing(false);
                    }
                    myToast(1000);

                    Log.d(Tag, "  onFailure  " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String resp = response.body().string();

                Log.d(Tag, resp);

                runOnUiThread(()->{


                    if(swipeRefreshLayout.isRefreshing()){

                        swipeRefreshLayout.setRefreshing(false);
                    }
                    try {

                        if(resp.equals("login")){

                            sessLogout();
                            return;
                        }

                        JSONObject jsonObject = new JSONObject(resp);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if(status.equals("success")){

                            JSONObject data = jsonObject.getJSONObject("data");
                            int ltype = data.getInt("ltype");

                            boolean loadmore = false;
                            if(ltype == 0){

                                symxinfoList = SymxInfo.getDefaultListSz(data.getJSONArray("list"));
                            }else{

                                symxinfoList = SymxInfo.getDefaultListTx(data.getJSONArray("list"));
                            }

                            Log.d(Tag, "symxinfoList" + symxinfoList.size());
                            if(symxinfoList.size() >= perPage){

                                page ++;
                                loadmore = true;
                            }

                            if(reload){

                                myAdapter.clearData();
                                myAdapter.reloadData(symxinfoList, loadmore);
                            }else{

                                myAdapter.addData(symxinfoList, myAdapter.getItemCount(), loadmore);
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

                        Log.d(Tag, e.getMessage());
                        myToast(1001);
                    }
                });
            }
        });
    }

    private void loadIni(){

        LoadingTimes(1);
        LoadingDialog(true);

        String url = mainApplication.APP_URL + "/api/app/bills/ini";

        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("sid", mainApplication.UUID)
                .add("uid", "" + mainApplication.userInfo.id).build();

        Request request = new Request.Builder()
                .post(body)
                .header("Authorization", mainApplication.APP_TOKEN)
                .url(url).build();

        Log.d(Tag, "loadData " + url + " storeid : " + mainApplication.userInfo.id);

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                runOnUiThread(()->{

                    LoadingTimes(-1);
                    LoadingDialog(false);
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
                    try {

                        JSONObject jsonObject = new JSONObject(resp);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if(status.equals("success")){

                            JSONObject data = jsonObject.getJSONObject("data");


                            tv_ktxye.setText(data.getString("money"));
                            tv_jrsy.setText(data.getString("tmoney") + "元");

                        }else if(!message.isEmpty()){

                            Toast mToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                            mToast.setGravity(Gravity.TOP, 0,0);
                            mToast.show();
                        }

                    }catch (Exception e){

                        e.printStackTrace();

                        Log.d(Tag, e.getMessage());
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        loadIni();
        loadData(true);
    }
}