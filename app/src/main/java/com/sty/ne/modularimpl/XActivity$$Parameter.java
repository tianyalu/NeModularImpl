package com.sty.ne.modularimpl;

import com.sty.ne.modularimpl.api.core.ParameterLoad;

/**
 * @Author: tian
 * @UpdateDate: 2020/10/30 9:30 PM
 */
public class XActivity$$Parameter implements ParameterLoad {
    @Override
    public void loadParameter(Object target) {
        //一次
        MainActivity t = (MainActivity) target;

        //循环
        t.name = t.getIntent().getStringExtra("name");
        t.age = t.getIntent().getIntExtra("age", t.age);
    }
}
