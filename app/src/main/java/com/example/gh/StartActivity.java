package com.example.gh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.gh.bean.UserInfo;

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
                        String uuid = sharedPreferences.getString("uuid", "");

                        Boolean auto = sharedPreferences.getBoolean("auto", false);
                        int login_at = sharedPreferences.getInt("login_at", 0);

                        app.APP_TOKEN = token;
                        app.UUID = uuid;

                        long timecurrentTimeMillis = System.currentTimeMillis();

                        if(auto && login_at + app.login_times > timecurrentTimeMillis / 1000){


                            sharedPreferences.getInt("id", 0);
                            sharedPreferences.getString("phone", "");
                            sharedPreferences.getString("yz_open_id", "");
                            sharedPreferences.getString("yz_account_id", "");

                            app.userInfo = new UserInfo();
                            app.userInfo.id = sharedPreferences.getInt("id", 0);
                            app.userInfo.phone = sharedPreferences.getString("phone", "");
                            app.userInfo.yz_open_id = sharedPreferences.getString("yz_open_id", "");
                            app.userInfo.yz_account_id = sharedPreferences.getString("yz_account_id", "");
                            
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