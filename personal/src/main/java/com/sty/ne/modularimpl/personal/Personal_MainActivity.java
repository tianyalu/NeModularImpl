package com.sty.ne.modularimpl.personal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sty.ne.modularimpl.annotation.ARouter;

@ARouter(path = "/personal/Personal_MainActivity")
public class Personal_MainActivity extends AppCompatActivity {
    private Button btnHome;
    private Button btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_personal);
        initView();
        if(getIntent() != null) {
            Log.e("sty", getIntent().getStringExtra("name"));
        }
    }

    private void initView() {
        btnOrder = findViewById(R.id.btn_order);
        btnHome = findViewById(R.id.btn_home);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnOrderClicked();
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnHomeClicked();
            }
        });
    }

    private void onBtnOrderClicked() {

    }

    private void onBtnHomeClicked() {

    }
}
