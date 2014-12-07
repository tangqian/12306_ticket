package com.free.app.ticket.service;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.util.TicketHttpClient;
import com.free.app.ticket.view.LoginPanelManager;

public class LoginThreadService extends Thread {
    
    //private static final Logger logger = LoggerFactory.getLogger(CheckAuthcodeThreadService.class);
    
    private String username;
    
    private String password;
    
    private String authcode;
    
    public LoginThreadService(String username, String password, String authcode) {
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
                LoginPanelManager.toggle();
            }
            else {
                if ("randCodeError".equals(result)) {//验证码错误时不重新获取验证码
                    result = "验证码不正确！";
                }
                if (result.startsWith("密码输入错误")) {
                    LoginPanelManager.requestFocus2Password();
                }
                else if (result.startsWith("登录名不存在")) {
                    LoginPanelManager.requestFocus2Username();
                }
                else if (result.startsWith("验证码")) {
                    LoginPanelManager.requestFocus2Authcode();
                }
                TicketMainFrame.alert(result);
                new HttpClientThreadService().start();//重新获取验证码
            }
        }
        LoginPanelManager.setLoginEnabled();
    }
}
