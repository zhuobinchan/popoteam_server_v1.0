package com.geetion.generic.serverfile.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by alexcai on 15/2/2.
 */
public class JSONObjectWrapper {
    private JSONObject jsonObject;

    public JSONObjectWrapper(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJSONObject() {
        return jsonObject;
    }
}
