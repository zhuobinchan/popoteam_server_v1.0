package com.easemob.server.example.exception;

import com.easemob.server.example.exception.base.HuanXinException;

/**
 * Created by jian on 2016/4/13.
 */
public class HuanXinUserException extends HuanXinException {

    public HuanXinUserException(){
        super();
    }

    public HuanXinUserException(String message){
        super(message);
    }
}
