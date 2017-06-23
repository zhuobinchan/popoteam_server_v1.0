package com.easemob.server.example.exception;

import com.easemob.server.example.exception.base.HuanXinException;

/**
 * Created by jian on 2016/4/13.
 */
public class HuanXinMessageException extends HuanXinException {

    public HuanXinMessageException() {
        super();
    }

    public HuanXinMessageException(String message) {
        super(message);
    }
}
