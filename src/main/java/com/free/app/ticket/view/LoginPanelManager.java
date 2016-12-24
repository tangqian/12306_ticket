package com.free.app.ticket.view;

import javax.swing.ImageIcon;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.model.MixData4UI;

/**
 * 
 * <登录登出面板UI管理类>
 */
public class LoginPanelManager {
    
    private static LoginPanel login_panel = null;
    
    private static LogoutPanel logout_panel = null;
    
    private static boolean isInited = false;
    
    /**
     * <界面初始化调用>
     *
     */
    public static void init(TicketMainFrame frame) {
        if (!isInited) {
            isInited = true;
            login_panel = new LoginPanel();
            logout_panel = new LogoutPanel(frame);
            frame.getContentPane().add(login_panel);
            frame.getContentPane().add(logout_panel);
        }
    }
    
    /**
     * <切换登录\退出面板>
     *
     */
    public static void toggle() {
        if (login_panel.isVisible()) {
            login_panel.setVisible(false);
            logout_panel.setVisible(true);
        }
        else {
            logout_panel.setVisible(false);
            login_panel.setVisible(true);
            login_panel.authCode = null;
        }
    }
    
    /**
     * <是否已登录>
     * @return
     */
    public static boolean isLogged(){
        return logout_panel.isVisible();
    }
    
    public static void requestFocus2Authcode() {
        login_panel.authCode = null;
        VerifyCodeDialog.getInstance().setVisible(true);
    }
    
    public static void requestFocus2Password() {
        login_panel.password.setText("");
        login_panel.password.requestFocus();
    }

    public static void setAuthCode(String authCode){
        login_panel.authCode = authCode;
    }
    

    public static void requestFocus2Username() {
        login_panel.username.requestFocus();
    }
    
    /**
     * <设置登录按钮有效>
     *
     */
    public static void setLoginEnabled() {
        login_panel.loginBtn.setLoginEnabled();
    }

	public static void bindUItoModel(MixData4UI mixData) {
		mixData.setUserName(login_panel.username.getText());
	}

	public static void bindModeltoUI(MixData4UI mixData) {
		login_panel.username.setText(mixData.getUserName());
		login_panel.password.requestFocus();
	}



}
