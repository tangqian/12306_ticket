package com.free.app.ticket.model;

import java.util.Arrays;

public class JsonMsg4Contacter extends JsonMsgSuper {
    
    private Data data;
    
    public void setData(Data data) {
        this.data = data;
    }
    
    public Data getData() {
        return data;
    }
    
    @Override
    public String toString() {
        return "JsonMsg4Authcode [data=" + data + "]" + super.toString();
    }
    
    public class Data {
        
        private boolean isExist;
        
        private ContacterInfo[] normal_passengers;
        
        public void setNormal_passengers(ContacterInfo[] normal_passengers) {
            this.normal_passengers = normal_passengers;
        }
        
        public ContacterInfo[] getNormal_passengers() {
            return normal_passengers;
        }
        
        public void setIsExist(boolean isExist) {
            this.isExist = isExist;
        }
        
        public boolean isExist() {
            return isExist;
        }
        
        @Override
        public String toString() {
            return "Data [isExist=" + isExist + ", normal_passengers=" + Arrays.toString(normal_passengers) + "]";
        }
        
    }
    
}
