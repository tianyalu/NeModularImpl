package com.sty.ne.modularimpl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sty.ne.modularimpl.annotation.ARouter;
import com.sty.ne.modularimpl.annotation.model.RouterBean;
import com.sty.ne.modularimpl.api.ARouterLoadGroup;
import com.sty.ne.modularimpl.api.ARouterLoadPath;
import com.sty.ne.modularimpl.test.ARouter$$Group$$order;

import java.util.Map;

@ARouter(path = "/app/MainActivity")
public class MainActivity extends AppCompatActivity {
    private Button btnOrder;
    private Button btnPersonal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        btnOrder = findViewById(R.id.btn_order);
        btnPersonal = findViewById(R.id.btn_personal);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnOrderClicked();
            }
        });
        btnPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnPersonalClicked();
            }
        });
    }

    private void onBtnOrderClicked() {
        //最终集成化模式，所有子模块APT生成的类文件都会打包到apk中
        ARouterLoadGroup loadGroup = new ARouter$$Group$$order();
        Map<String, Class<? extends ARouterLoadPath>> groupMap = loadGroup.loadGroup();
        // app - order
        Class<? extends ARouterLoadPath> clazz = groupMap.get("order");

        //类加载技术
        ARouterLoadPath path = null;
        try {
            path = clazz.newInstance();
            Map<String, RouterBean> pathMap = path.loadPath();
            //获取/order/Order_MainActivity
            RouterBean routerBean = pathMap.get("/order/Order_MainActivity");
            if(routerBean != null) {
                Intent intent = new Intent(this, routerBean.getClazz());
                intent.putExtra("name", "sty");
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onBtnPersonalClicked() {
        //最终集成化模式，所有子模块APT生成的类文件都会打包到apk中
        ARouterLoadGroup loadGroup = new ARouter$$Group$$order();
        Map<String, Class<? extends ARouterLoadPath>> groupMap = loadGroup.loadGroup();
        // app - personal
        Class<? extends ARouterLoadPath> clazz = groupMap.get("personal");

        //类加载技术
        ARouterLoadPath path = null;
        try {
            path = clazz.newInstance();
            Map<String, RouterBean> pathMap = path.loadPath();
            //获取/personal/Personal_MainActivity
            RouterBean routerBean = pathMap.get("/personal/Personal_MainActivity");
            if(routerBean != null) {
                Intent intent = new Intent(this, routerBean.getClazz());
                intent.putExtra("name", "sty");
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}