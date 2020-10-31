package com.sty.ne.modularimpl;

import android.os.Bundle;

import com.sty.ne.modularimpl.annotation.ARouter;
import com.sty.ne.modularimpl.annotation.Parameter;

import androidx.appcompat.app.AppCompatActivity;

@ARouter(path = "/app/Main3Activity")
public class Main3Activity extends AppCompatActivity {
    @Parameter
    String password;

    @Parameter
    int gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}