package com.free.app.ticket.model;

import java.util.Arrays;

import com.alibaba.fastjson.JSONObject;

public class JsonMsg4Login extends JsonMsgSuper {
    
    private String[] messages;
    
    private JSONObject data;
    
    public void setMessages(String[] messages) {
        this.messages = messages;
    }
    
    public String[] getMessages() {
        return messages;
    }
    
    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject getData() {
        return data;
    }
    
    @Override
    public String toString() {
        return "JsonMsg4Login [messages=" + Arrays.toString(messages) + "]" + super.toString();
    }


}
