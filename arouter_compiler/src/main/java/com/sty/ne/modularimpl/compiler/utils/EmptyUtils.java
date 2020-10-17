package com.sty.ne.modularimpl.compiler.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @Author: tian
 * @UpdateDate: 2020/10/16 9:11 PM
 */
public class EmptyUtils {
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
