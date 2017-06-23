package com.easemob.server.example.exception;

import com.easemob.server.example.exception.base.HuanXinException;

/**
 * Created by quanjianan on 2017/5/5.
 */
public class HuanxinResponseException extends HuanXinException {

    private int code;

    public HuanxinResponseException() {
        super();
    }

    public HuanxinResponseException(String message){
        super(message);
    }

    public HuanxinResponseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
