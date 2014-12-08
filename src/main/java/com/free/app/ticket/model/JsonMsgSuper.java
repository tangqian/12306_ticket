package com.free.app.ticket.model;

import java.util.Arrays;

/**
 * 
 * JSON消息共有父类
 * 
 */
public class JsonMsgSuper {
    
    private String validateMessagesShowId;
    
    private boolean status;
    
    private int httpstatus;
    
    private Object validateMessages;
    
    private String[] messages;
    
    public void setMessages(String[] messages) {
        this.messages = messages;
    }
    
    public String[] getMessages() {
        return messages;
    }
    
    public String getValidateMessagesShowId() {
        return validateMessagesShowId;
    }
    
    public void setValidateMessagesShowId(String validateMessagesShowId) {
        this.validateMessagesShowId = validateMessagesShowId;
    }
    
    public boolean getStatus() {
        return status;
    }
    
    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public int getHttpstatus() {
        return httpstatus;
    }
    
    public void setHttpstatus(int httpstatus) {
        this.httpstatus = httpstatus;
    }
    
    public Object getValidateMessages() {
        return validateMessages;
    }
    
    public void setValidateMessages(Object validateMessages) {
        this.validateMessages = validateMessages;
    }
    
    @Override
    public String toString() {
        return "JsonMsgSuper [httpstatus=" + httpstatus + ", status=" + status + ", messages="
            + Arrays.toString(messages) + ", validateMessages=" + validateMessages + ", validateMessagesShowId="
            + validateMessagesShowId + "]";
    }
}
