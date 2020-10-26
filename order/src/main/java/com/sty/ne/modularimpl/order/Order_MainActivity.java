package com.sty.ne.modularimpl.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sty.ne.modularimpl.annotation.ARouter;

@ARouter(path = "/personal/Order_MainActivity")
public class Order_MainActivity extends AppCompatActivity {
    private Button btnHome;
    private Button btnPersonal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_order);

        initView();
        if(getIntent() != null) {
            Log.e("sty", getIntent().getStringExtra("name"));
        }
    }

    private void initView() {
        btnPersonal = findViewById(R.id.btn_personal);
        btnHome = findViewById(R.id.btn_home);
        btnPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnPersonalClicked();
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnHomeClicked();
            }
        });
    }

    private void onBtnPersonalClicked() {

    }

    private void onBtnHomeClicked() {

    }
}
