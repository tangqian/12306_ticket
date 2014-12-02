package com.free.app.ticket.model;

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
        return "JsonMsgSuper [httpstatus=" + httpstatus + ", status=" + status + ", validateMessages="
            + validateMessages + ", validateMessagesShowId=" + validateMessagesShowId + "]";
    }
}
