package com.sty.ne.modularimpl.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sty.ne.modularimpl.annotation.ARouter;
import com.sty.ne.modularimpl.annotation.Parameter;
import com.sty.ne.modularimpl.api.core.ParameterLoad;

@ARouter(path = "/order/Order_MainActivity")
public class Order_MainActivity extends AppCompatActivity {
    private Button btnHome;
    private Button btnPersonal;

    @Parameter
    String name;

    @Parameter(name = "ageX")
    int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_order);

        initView();

        ParameterLoad parameterLoad = new Order_MainActivity$$Parameter();
        parameterLoad.loadParameter(this);

        if(getIntent() != null) {
            //Log.e("sty", getIntent().getStringExtra("name"));
            Log.e("sty", "name >>> " + name + "\n age --> " + age);
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
