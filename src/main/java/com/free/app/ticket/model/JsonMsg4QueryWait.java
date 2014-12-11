package com.free.app.ticket.model;

import com.alibaba.fastjson.JSONObject;

public class JsonMsg4QueryWait extends JsonMsgSuper {
    
    /**
     * {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,
     * "data":{"queryOrderWaitTimeStatus":true,"count":0,
     * "waitTime":4,"requestId":5948697475069901366,"waitCount":1,
     * "tourFlag":"dc","orderId":null},
     * "messages":[],"validateMessages":{}}
     * 
     * {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,
     * "data":{"queryOrderWaitTimeStatus":true,"count":0,
     * "waitTime":-1,"requestId":5948697475069901366,"waitCount":0,
     * "tourFlag":"dc","orderId":"EC07607883"},
     * "messages":[],"validateMessages":{}}
     * 
     * {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,
     * "data":{"queryOrderWaitTimeStatus":true,"count":0,
     * "waitTime":-1,"requestId":5948722653829134902,"waitCount":0,
     * "tourFlag":"dc","orderId":"EC30914652"},
     * "messages":[],"validateMessages":{}}
     * 
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
        return "JsonMsg4QueryWait [" + data.toJSONString() + "]" + super.toString();
    }


}
