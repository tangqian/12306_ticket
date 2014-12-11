package com.free.app.ticket.model;

import com.alibaba.fastjson.JSONObject;

public class JsonMsg4ConfirmQueue extends JsonMsgSuper {
    
    /**
     * {"repeatSubmitToken":"00085cd34153ef8d769ec2f0afe469d1",
     * "validateMessagesShowId":
     * "_validatorMessage",
     * "status":true,
     * "httpstatus":200,
     * "data":{"errMsg":"排队人数现已超过余票数，请您选择其他席别或车次。","submitStatus":false},
     * "messages":[],
     * "validateMessages":{}}
     * 
     * {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,"messages":[],"validateMessages":{}}
     * 
     * {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,
     * "data":{"errMsg":"非法的订票请求！","submitStatus":false},"messages":[],"validateMessages":{}}
     * 
     * {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,
     * "data":{"submitStatus":true},"messages":[],"validateMessages":{}}
     * 
     * {"repeatSubmitToken":"c7be8608d82534f74e54c0a95bace89e","validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,
     * "data":{"errMsg":"包含未付款订单","submitStatus":false},"messages":[],"validateMessages":{}}
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
        return "JsonMsg4ConfirmQueue [" + data.toJSONString() + "]" + super.toString();
    }


}
