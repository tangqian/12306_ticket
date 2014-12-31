package com.free.app.ticket.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.model.JsonMsg4ConfirmQueue;
import com.free.app.ticket.model.JsonMsg4QueryWait;
import com.free.app.ticket.model.JsonMsg4QueueCount;
import com.free.app.ticket.model.TicketBuyInfo;
import com.free.app.ticket.model.TrainInfo;
import com.free.app.ticket.model.PassengerData.SeatType;
import com.free.app.ticket.service.AutoBuyThreadService.OrderToken;
import com.free.app.ticket.util.TicketHttpClient;

public class TrianQueueThreadService extends Thread {
    
    private TrainInfo train;
    
    private OrderToken orderToken;
    
    private Map<String, String> cookies;
    
    private TicketBuyInfo buyInfo;
    
    private String authcode;
    
    private String prefix4Log;
    
    public TrianQueueThreadService(TrainInfo train, OrderToken token, TicketBuyInfo buyInfo, String authcode,
        Map<String, String> cookieMap) {
        this.train = train;
        this.buyInfo = buyInfo;
        this.authcode = authcode;
        this.orderToken = token;
        this.cookies = cookieMap;
    }
    
    @Override
    public void run() {
        synchronized (AutoBuyThreadService.obj) {
            prefix4Log = "[Thread-" + currentThread().getId() + "]正在排队买" + train.getStation_train_code() + ":";
            TicketHttpClient client = HttpClientThreadService.getHttpClient();
            String seatType = buyInfo.getPassengers().get(0).getSeatTypeValue();
            JsonMsg4QueueCount msg =
                client.getQueueCount(train, buyInfo.getConfigInfo().getTrainDateAlias(), seatType, orderToken.getToken(), cookies);
            if (msg == null || msg.getData().getBooleanValue("op_2")) {
                TicketMainFrame.remind(prefix4Log + "排队人数超过余票数!将预订其它车次");
            }
            else {
                TicketMainFrame.trace(prefix4Log + "有余票：" + getLeftTicketDetail(msg.getData().getString("ticket")));
                confirmQueue();
            }
            
            AutoBuyThreadService.waitFlag = false;
            AutoBuyThreadService.obj.notifyAll();
        }
    }
    
    private void confirmQueue() {
        TicketHttpClient client = HttpClientThreadService.getHttpClient();
        
        int queueCount = 0;
        boolean enterQueue = false;
        while (queueCount < 3) {
            JsonMsg4ConfirmQueue confirmResult =
                client.confirmQueue(train, orderToken, buyInfo.getPassengers(), authcode, cookies);
            if (confirmResult == null || confirmResult.getData() == null) {//发生异常，重复请求最多3次
                queueCount++;
            }
            else if (confirmResult.getHttpstatus() == 200 && confirmResult.getStatus()
                && confirmResult.getData().getBooleanValue("submitStatus")) {
                TicketMainFrame.remind(prefix4Log + "成功进入排队队伍中，排队中...(可同时排队买其它车次,可增加命中率)");
                enterQueue = true;
                break;
            }
            else {
                String errMsg = confirmResult.getData().getString("errMsg");
                if (errMsg != null) {
                    TicketMainFrame.remind(prefix4Log + errMsg);
                }
                break;
            }
        }
        
        if (enterQueue) {
            int waitCount = 0;
            while (waitCount < 10) {
                JsonMsg4QueryWait waitMsg = client.queryOrderWaitTime(orderToken.getToken(), cookies);
                if (waitMsg == null) {
                    TicketMainFrame.remind(prefix4Log + "查询排队信息出错");
                }
                else {
                    if (waitMsg.getData() == null) {
                        String errorMsg = "查询排队信息出错";
                        if (waitMsg.getMessages() != null && waitMsg.getMessages().length != 0)
                            errorMsg += ":" + waitMsg.getMessages()[0];
                        TicketMainFrame.remind(prefix4Log + errorMsg);
                        break;
                    }
                    else {
                        TicketMainFrame.trace(prefix4Log + "当前等待人数:" + waitMsg.getData().getIntValue("waitCount")
                            + "人，等待时间约为：" + waitMsg.getData().getIntValue("waitTime") + "秒");
                        
                        String orderId = waitMsg.getData().getString("orderId");
                        if (!StringUtils.isEmpty(orderId) && !orderId.equals("null")) {
                            TicketMainFrame.remind(prefix4Log + "恭喜你买到票了，可以回家了，哦耶!(请尽快登录12306付款)");
                            break;
                        }
                    }
                }
                try {
                    Thread.sleep(1000l);
                }
                catch (InterruptedException e) {
                }
                waitCount++;
            }
        }
        
    }
    
    /**
     * <一句话功能简述>
     * @param ticket
     * @return
     */
    private String getLeftTicketDetail(String ticket) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < ticket.length()) {
            String s = ticket.substring(i, i + 10);
            SeatType type = SeatType.getTypeByValue(s.substring(0, 1));
            if (type != null) {
                int count = Integer.valueOf(s.substring(6, 10));
                if (count == 0) {//无票不打印
                    sb.append(type.getLabel()).append('(').append(count).append("张),");
                }
                else if (count < 3000) {
                    sb.append(type.getLabel()).append('(').append(count).append("张),");
                }
                else {
                    sb.append("无座").append('(').append(count - 3000).append("张),");
                }
            }
            i = i + 10;
        }
        if (sb.length() > 0)
            return sb.substring(0, sb.length() - 1);
        return sb.toString();
    }
    
    public static void main(String[] args) {
        //String s = "1012303263403840000010123000003024800000";
        //String s = "O031850198M048850030P057950008";
        //String s = "O031850172M0488500939096350014";
        
    }
}
