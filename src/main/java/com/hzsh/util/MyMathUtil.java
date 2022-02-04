package com.hzsh.util;

/**
 * 自定义算术运算
 */
public final class MyMathUtil {

    public static Double 加(Double a, Double b, Boolean... 空值当作零) {
        if (空值当作零[0] == false && a == null || b == null) {
            return null;
        }

        a = a == null ? 0 : a;
        b = b == null ? 0 : b;

        return a * b;
    }


    public static Double 减(Double a, Double b, Boolean... 空值当作零) {
        if (空值当作零[0] == false && a == null || b == null) {
            return null;
        }

        a = a == null ? 0 : a;
        b = b == null ? 0 : b;

        return a - b;
    }


    public static Double 乘(Double a, Double b, Boolean... 空值当作零) {
        if (空值当作零[0] == false && a == null || b == null) {
            return null;
        }

        a = a == null ? 0 : a;
        b = b == null ? 0 : b;

        return a * b;
    }


    public static Double 除(Double a, Double b, Boolean... 空值当作零) {
        if (空值当作零[0] == false && a == null || b == null || b == 0) {
            return null;
        }

        a = a == null ? 0 : a;

        return a / b;
    }
}
