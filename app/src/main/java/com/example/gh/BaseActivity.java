package com.example.gh;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    private String Tag = BaseActivity.class.getSimpleName();

    Toast mToast;

    private CountDownTimer cd_timer;
    private boolean isPause = false;

    private AlertDialog alertDialog;
    private int loadingTimes = 0;
    private int waitTimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    public void myToast(String msg){

        if(isFinishing()){
            return;
        }

        if(mToast != null){

            mToast.cancel();
        }

        mToast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.TOP, 0,0);
        mToast.show();
    }

    public void myToast(int errno){

        String msg = "";

        switch (errno){
            case 1000:
                msg = getResources().getString(R.string.err_neterr);
                break;
            case 1001:
                msg = getResources().getString(R.string.err_syserr);
                break;
            case 1002:
                msg = getResources().getString(R.string.err_operr) + " " + errno;
                break;
            case 1003:
                msg = getResources().getString(R.string.err_roles);
                break;
            case 1100:
                msg = getResources().getString(R.string.err_yhsx);
                break;
            case 1101:
                msg = getResources().getString(R.string.err_sesserr);
                break;
        }

        if(msg.isEmpty()){

            msg = getResources().getString(R.string.err_syserr);
        }

        myToast(msg);
    }



    public void LoadingTimes(int opt){

        if(opt == 0){

            loadingTimes = 0;
        }else{

            loadingTimes = loadingTimes + opt;

            if(loadingTimes < 0){

                loadingTimes = 0;
            }
        }
    }

    public void LoadingDialog(boolean show) {
        if(!show){

            if (null != alertDialog && alertDialog.isShowing()) {

                alertDialog.dismiss();

                if(cd_timer != null){

                    cd_timer.cancel();
                }
            }
            return;
        }
        if(null != alertDialog && alertDialog.isShowing()){

            return;
        }

        Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {

                    Log.d(Tag, "LoadingDialog" + loadingTimes);

                    sleep(2000);

                    runOnUiThread(()->{

                        if(!isDestroyed()){

                            LoadingDialogShow();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
    private void LoadingDialogShow(){

        if(isFinishing()){

            return;
        }

        Log.d(Tag, "LoadingDialogShow:" + loadingTimes);

        if(loadingTimes == 0){

            return;
        }

        if(alertDialog != null && alertDialog.isShowing()){

            return;
        }

        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.setContentView(R.layout.loading);
        alertDialog.setCanceledOnTouchOutside(false);

        Log.d(Tag, "LoadingDialogShow:----s--" + loadingTimes);

        cdt();
    }


    public void sessLogout(){

        sessLogout(1101);
    }

    public void sessLogout(int ecode){

        myToast(ecode);

        Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    sleep(2000);

                    if(isPause){
                        return;
                    }

                    SharedPreferences sharedPreferences = getSharedPreferences("share_login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("auto", false);
                    editor.commit();

                    Intent it = new Intent(getApplicationContext(), LoginActivity.class);

                    startActivity(it);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }

    private void cdt(){

        if(cd_timer == null){

            cd_timer = new CountDownTimer(30000, 10) {
                @Override
                public void onTick(long millisUntilFinished) {

                    //Log.d(Tag, millisUntilFinished + "");
                }

                @Override
                public void onFinish() {

                    if (!isDestroyed() && null != alertDialog && alertDialog.isShowing()) {

                        Log.d(Tag, "cd_timer : onFinish : " + loadingTimes);
                        loadingTimes = 0;
                        alertDialog.dismiss();
                    }
                }
            };
        }else{

            cd_timer.cancel();
        }


        cd_timer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        LoadingDialog(false);
    }

    @Override
    protected void onPause() {
        super.onPause();

        isPause = true;
        LoadingDialog(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(alertDialog != null && alertDialog.isShowing()){

            alertDialog.dismiss();
        }

        if(cd_timer != null){

            cd_timer.onFinish();
        }
    }
}