package com.free.app.ticket.model;

import com.alibaba.fastjson.JSONObject;

public class JsonMsg4QueueCount extends JsonMsgSuper {
    
    /**
     * eg . 返回json内容
     * {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,
     * "data":{"count":"5","ticket":"1012303263403840000010123000003024800000","op_2":"false","countT":"0","op_1":"true"},
     * "messages":[],"validateMessages":{}}
     * 
     * {"validateMessagesShowId":"_validatorMessage","status":true,"httpstatus":200,
     * "data":{"count":"0","ticket":"1014853349404155002410148503783026350278","op_2":"false","countT":"0","op_1":"false"},
     * "messages":[],"validateMessages":{}}
     * 
     * op_2为true的话表示目前排队人数已经超过余票张数 
     * countT表示目前排队人数
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
        return "JsonMsg4QueueCount [" + data.toJSONString() + "]" + super.toString();
    }
    
}
