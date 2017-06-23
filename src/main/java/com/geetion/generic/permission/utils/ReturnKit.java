package com.geetion.generic.permission.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by virgilyan on 1/21/15.
 */
public class ReturnKit {
    private static String dataTypeName = "returnType";

    public ReturnKit() {
    }

    public static String getDataTypeName() {
        return dataTypeName;
    }

    public static void setDataTypeName(String dataTypeＮame) {
        dataTypeName = dataTypeＮame;
    }

    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    public static boolean isJson(HttpServletRequest request, String dataTypeName) {
        return isAjax(request) && !"default".equalsIgnoreCase(request.getParameter(dataTypeName)) || "json".equalsIgnoreCase(request.getParameter(dataTypeName));
    }

    public static boolean isJson(HttpServletRequest request) {
        return isJson(request, dataTypeName);
    }

    public static enum ReturnType {
        DFAULT(0),
        JSON(1);

        private final int value;

        private ReturnType(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }
}
