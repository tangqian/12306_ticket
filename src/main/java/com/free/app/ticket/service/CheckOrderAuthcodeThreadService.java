package com.free.app.ticket.service;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.util.TicketHttpClient;
import com.free.app.ticket.view.CheckCode4OrderDialog;

public class CheckOrderAuthcodeThreadService extends Thread {
    
    //private static final Logger logger = LoggerFactory.getLogger(CheckAuthcodeThreadService.class);
    
    private String authcode;
    
    private CheckCode4OrderDialog dialog;
    
    public CheckOrderAuthcodeThreadService(String authcode, CheckCode4OrderDialog dialog) {
        this.authcode = authcode;
        this.dialog = dialog;
    }
    
    @Override
    public void run() {
        TicketHttpClient client = HttpClientThreadService.getHttpClient();
        boolean result =
            client.checkOrderAuthcode(authcode, dialog.getToken().getToken(), dialog.getBuyInfo().getPassengers(), dialog.getCookies());
        if (result) {
            TicketMainFrame.trace("订单验证码输入正确!");
            dialog.setSuccess();
            dialog.dispose();
        }
        else {
            TicketMainFrame.remind("订单验证码输入错误!");
        }
    }
}
