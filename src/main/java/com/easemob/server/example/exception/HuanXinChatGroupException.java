package com.easemob.server.example.exception;

import com.easemob.server.example.exception.base.HuanXinException;

/**
 * Created by jian on 2016/4/13.
 */
public class HuanXinChatGroupException extends HuanXinException {

    private int code;

    public HuanXinChatGroupException(){
        super();
    }

    public HuanXinChatGroupException(String message){
        super(message);
    }

    public HuanXinChatGroupException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
