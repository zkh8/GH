package com.example.gh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sharedPreferences = getSharedPreferences("share_ini", MODE_PRIVATE);
        Boolean launch = sharedPreferences.getBoolean("launch", false);


        Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    sleep(1000);

                    Intent it;
                    if(launch){

                        it = new Intent(getApplicationContext(), LaunchActivity.class);
                    }else{

                        MainApplication app = MainApplication.getInstance();

                        SharedPreferences sharedPreferences = getSharedPreferences("share_login", MODE_PRIVATE);
                        String token = sharedPreferences.getString("token", "");

                        Boolean auto = sharedPreferences.getBoolean("auto", false);
                        int login_at = sharedPreferences.getInt("login_at", 0);

                        app.APP_TOKEN = token;

                        long timecurrentTimeMillis = System.currentTimeMillis();

                        if(auto && login_at + app.login_times > timecurrentTimeMillis / 1000){

                            it = new Intent(getApplicationContext(), IndexActivity.class);
                        }else{

                            it = new Intent(getApplicationContext(), LoginActivity.class);
                        }
                    }

                    startActivity(it);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }



}