package com.free.app.ticket.model;

import com.alibaba.fastjson.JSONObject;

public class JsonMsg4SubmitOrder extends JsonMsgSuper {
    
    /**
     * {"validateMessagesShowId":"_validatorMessage",
     * "status":false,"httpstatus":200,
     * "messages":["您还有未处理的订单，请您到<a href=\"../queryOrder/initNoComplete\">[未完成订单]</a>进行处理!"]
     * ,"validateMessages":{}}
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
        return "JsonMsg4SubmitOrder [" + data.toJSONString() + "]" + super.toString();
    }


}
