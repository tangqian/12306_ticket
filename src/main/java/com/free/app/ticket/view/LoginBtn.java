package com.free.app.ticket.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.apache.commons.lang3.StringUtils;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.service.LoginThreadService;
import com.free.app.ticket.util.ResManager;

public class LoginBtn extends JButton {
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 是否正在登录
     */
    private boolean isLogging = false;
    
    public void dsLogging(){
        isLogging = false;
    }
    

    public LoginBtn(final LoginPanel panel, String text) {
        super(text);
        
        //this.frame = frame;
        this.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = panel.username.getText().trim();
                String pwd = String.valueOf(panel.password.getPassword()).trim();
                String authcode = panel.authcode.getText().trim();
                
                String alertMsg = null;
                if (StringUtils.isEmpty(username)) {
                    alertMsg = ResManager.getText("ticket.label.user_name");
                    panel.username.requestFocus();
                }
                else if (StringUtils.isEmpty(pwd)) {
                    alertMsg = ResManager.getText("ticket.label.password");
                    panel.password.requestFocus();
                }
                else if (StringUtils.isEmpty(authcode)) {
                    alertMsg = ResManager.getText("ticket.label.codename");
                    panel.authcode.requestFocus();
                }
                
                if(alertMsg != null){
                    TicketMainFrame.remind( "请输入" + alertMsg + "!");
                    return;
                }
                
                if(!TicketMainFrame.isInited){
                    TicketMainFrame.remind( "请先获取授权码!");
                    return;
                }
                
                if(!isLogging){
                    isLogging = true;
                    new LoginThreadService(panel, username, pwd, authcode).start();
                }
                
                
                
                /*System.out.println(userName);
                System.out.println(pwd);
                System.out.println(authCode);*/
                
                /*List<String> list = ToolHelper.validateWidget(username, password, authcode);
                if (list.size() > 0) {
                    String msg = "";
                    for (int i = 0; i < list.size(); i++) {
                        msg += (i == list.size() - 1 ? list.get(i) : list.get(i) + ",");
                    }
                    k(msg + "不能为空！");
                    return;
                }
                LoginUserInfo user = new LoginUserInfo(username.getText(), password.getText(), authcode.getText());
                LoginThread loginThread = new LoginThread(user, mainWin);
                loginThread.start();*/
            }
        });
    }
    
}
