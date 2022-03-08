package com.example.gh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gh.adapter.IndexFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class IndexActivity extends BaseActivity implements View.OnClickListener{

    private String Tag = MainActivity.class.getSimpleName();

    private MainApplication mainApplication;

    ViewPager2 viewPager2;
    List<LinearLayout> tabs_linearLayout = new ArrayList<>();
    private ArrayList<Fragment> fragments;

    ImageView tabs_imageView_current;
    TextView tabs_textView_current;
    private int cureent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);


        mainApplication = MainApplication.getInstance();

        iniView();
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

        fragments.add(new GhscFragment());
        fragments.add(new QdjlFragment());
        fragments.add(new GrzxFragment());

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

                viewPager2.setCurrentItem(2,false);
            case 2:

                tabs_imageView_current = findViewById(R.id.id_tab_img_3);
                tabs_textView_current = findViewById(R.id.id_tab_text_3);
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

        long currentTime = System.currentTimeMillis();
        if((currentTime-touchTime)>=waitTime) {

            Toast.makeText(this, "再按一次退出", (int)waitTime).show();

            touchTime = currentTime;
        }else {
            finish();
        }
    }
}