package com.free.app.ticket.service;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.util.TicketHttpClient;
import com.free.app.ticket.view.LoginPanel;

public class LoginThreadService extends Thread {
    
    //private static final Logger logger = LoggerFactory.getLogger(CheckAuthcodeThreadService.class);
    
    private LoginPanel panel;
    
    private String username;
    
    private String password;
    
    private String authcode;
    
    public LoginThreadService(LoginPanel panel, String username, String password, String authcode) {
        this.panel = panel;
        this.username = username;
        this.password = password;
        this.authcode = authcode;
    }
    
    @Override
    public void run() {
        TicketHttpClient client = HttpClientThreadService.getHttpClient();
        if (client != null) {
            String result = client.checkLogin(username, password, authcode);
            if (result == null) {
                TicketMainFrame.trace("登录成功!");
                panel.loginBtn.setVisible(false);
                //panel.logoutBtn.setVisible(true);
            }
            else {
                if ("randCodeError".equals(result) ) {//验证码错误时不重新获取验证码
                    result = "验证码不正确！";
                }
                new HttpClientThreadService().start();//重新获取验证码
                TicketMainFrame.alert(result);
            }
        }
        panel.loginBtn.dsLogging();
    }
}
