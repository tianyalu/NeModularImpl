package com.sty.ne.modularimpl;

import android.os.Bundle;

import com.sty.ne.modularimpl.annotation.ARouter;
import com.sty.ne.modularimpl.annotation.Parameter;

import androidx.appcompat.app.AppCompatActivity;

@ARouter(path = "/app/Main2Activity")
public class Main2Activity extends AppCompatActivity {
    @Parameter
    String name;

    @Parameter(name = "ageX")
    int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}