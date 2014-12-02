package com.free.app.ticket.service;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.util.TicketHttpClient;

public class CheckAuthcodeThreadService extends Thread {
    
    //private static final Logger logger = LoggerFactory.getLogger(CheckAuthcodeThreadService.class);
    
    private String authcode;
    
    
    public CheckAuthcodeThreadService(String authcode) {
        this.authcode = authcode;
    }
    
    @Override
    public void run() {
        TicketHttpClient client = HttpClientThreadService.getHttpClient();
        if(client != null){
            boolean result = client.checkLoginAuthcode(authcode);
            if(result){
                TicketMainFrame.trace("验证码正确!");
            }else{
                TicketMainFrame.remind("验证码错误!");                
            }
        }
    }
}
