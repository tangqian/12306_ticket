package com.free.app.ticket.model;

import com.alibaba.fastjson.JSONObject;

public class JsonMsg4CheckOrder extends JsonMsgSuper {
    
    /**
     * {"validateMessagesShowId":"_validatorMessage",
     * "status":true,
     * "httpstatus":200,
     * "data":{"submitStatus":true},
     * "messages":[],"validateMessages":{}}
     * 
     * {"validateMessagesShowId":"_validatorMessage",
     * "status":true,"httpstatus":200,
     * "data":{"errMsg":"对不起，由于您取消次数过多，今日将不能继续受理您的订票请求。12月12日您可继续使用订票功能。","submitStatus":false},
     * "messages":[],"validateMessages":{}}
     * 
     * {"validateMessagesShowId":"_validatorMessage",
     * "status":true,"httpstatus":200,
     * "data":{"errMsg":"randCodeError","submitStatus":false},
     * "messages":[],"validateMessages":{}}
     */
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
