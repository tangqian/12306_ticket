package com.free.app.ticket.model;

import com.alibaba.fastjson.JSONObject;

public class JsonMsg4CheckOrder extends JsonMsgSuper {
    
    private JSONObject data;

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject getData() {
        return data;
    }
    
    @Override
    public String toString() {
        return "JsonMsg4CheckOrder [" + data.toJSONString() + "]" + super.toString();
    }


}
