package com.free.app.ticket.view;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.free.app.ticket.TicketMainFrame;

/**
 * 
 * <登录退出面板管理类>
 */
public class LogPanelManager  {
    
    private static LoginPanel login_panel = null;
    
    private static LogoutPanel logout_panel = null;
    
    /**
     * <一句话功能简述>
     *
     */
    public static void init(TicketMainFrame frame){
        if(login_panel == null)
            login_panel = new LoginPanel();
        
        if(logout_panel == null){
            logout_panel = new LogoutPanel();
        }
        frame.getContentPane().add(login_panel);
        frame.getContentPane().add(logout_panel);
    }
    
    /**
     * <更新授权码>
     * @param icon
     */
    public static void setIcon(ImageIcon icon){
        login_panel.code.setIcon(icon);
    }
    
    /*public static JPanel getLoginPanel(){
        return login_panel;
    }
    
    public static JPanel getLogoutPanel(){
        return login_panel;
    }*/
    
}
