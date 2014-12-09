package com.free.app.ticket.model;

import com.alibaba.fastjson.JSONObject;

public class JsonMsg4QueueCount extends JsonMsgSuper {
    
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
    
    public class QueueCount {
        /** 座位类型 **/
        private String count;
        /** 票字符串 **/
        private String ticket;
        /** op_2为true的话表示目前排队人数已经超过余票张数 **/
        private boolean op_2;
        private boolean op_1;
        /** countT表示目前排队人数 **/
        private String countT;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public boolean getOp_2() {
            return op_2;
        }

        public void setOp_2(boolean op_2) {
            this.op_2 = op_2;
        }

        public boolean getOp_1() {
            return op_1;
        }

        public void setOp_1(boolean op_1) {
            this.op_1 = op_1;
        }

        public String getCountT() {
            return countT;
        }

        public void setCountT(String countT) {
            this.countT = countT;
        }

    }
}
