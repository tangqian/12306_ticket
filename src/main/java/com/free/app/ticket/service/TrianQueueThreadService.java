package com.free.app.ticket.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.model.JsonMsg4QueueCount;
import com.free.app.ticket.model.TrainInfo;
import com.free.app.ticket.service.AutoBuyThreadService.OrderToken;
import com.free.app.ticket.util.TicketHttpClient;

public class TrianQueueThreadService extends Thread {
    
    private static final Logger logger = LoggerFactory.getLogger(CheckAuthcodeThreadService.class);
    
    private TrainInfo train;
    
    private OrderToken orderToken;
    
    private Map<String, String> cookies;
    
    public TrianQueueThreadService(TrainInfo train, OrderToken token, Map<String, String> cookieMap) {
        this.train = train;
        this.orderToken = token;
        this.cookies = cookieMap;
    }
    
    @Override
    public void run() {
        String prefix4Log = "[Thread-" + currentThread().getId() + "]正在排队买" + train.getTrain_no() + ":";
        logger.info(prefix4Log);
        
        TicketHttpClient client = HttpClientThreadService.getHttpClient();
        JsonMsg4QueueCount msg = client.getQueueCount(train, orderToken.getToken(), cookies);
        if (msg == null || msg.getData().getBooleanValue("op_2")) {
            TicketMainFrame.remind(prefix4Log + "排队人数超过余票数!将预订其它车次");
        }
        else {
            System.out.println(prefix4Log + "-----in---success-------");
        }
        
    }
}
