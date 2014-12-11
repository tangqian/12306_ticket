package com.free.app.ticket.service;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.model.JsonMsg4CheckOrder;
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
        JsonMsg4CheckOrder msg =
            client.checkOrderAuthcode(authcode, dialog.getToken().getToken(), dialog.getBuyInfo().getPassengers(), dialog.getCookies());
        
        if(msg != null){
            if (msg.getHttpstatus() == 200 && msg.getStatus() && msg.getData().getBooleanValue("submitStatus")) {
                TicketMainFrame.trace("订单验证码输入正确!");
                dialog.setSuccess(authcode);
                dialog.dispose();
            }else{
                String errMsg = msg.getData().getString("errMsg");
                if(errMsg == null)
                    errMsg = "未知错误";
                TicketMainFrame.remind(errMsg);
                dialog.dispose();
                
            }
        }else{
            TicketMainFrame.remind("订单验证码输入错误!");
        }
    }
}
