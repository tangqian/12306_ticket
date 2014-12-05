package com.free.app.ticket.service;

import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.util.ResManager;
import com.free.app.ticket.util.TicketHttpClient;
import com.free.app.ticket.view.LogPanelManager;

public class HttpClientThreadService extends Thread {
    
    private static TicketHttpClient httpClient = null;
    
    @Override
    public void run() {
        synchronized (HttpClientThreadService.class) {
            if (httpClient == null) {
                httpClient = TicketHttpClient.getInstance();
                if (httpClient != null) {
                    TicketMainFrame.trace("初始获取cookie成功");
                }
                else {
                    TicketMainFrame.remind("初始获取cookie失败!请检查网络是否正常");
                }
            }
            if (httpClient != null) {
                File imageFile = httpClient.buildLoginCodeImage();
                if (imageFile != null && imageFile.exists()) {
                    ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
                    icon.setImage(icon.getImage()
                        .getScaledInstance(icon.getIconWidth(), icon.getIconHeight(), Image.SCALE_DEFAULT));
                    LogPanelManager.setIcon(icon);
                    TicketMainFrame.isInited = true;
                    TicketMainFrame.trace("获取验证码成功");
                    
                }
                else {
                    ImageIcon icon = ResManager.createImageIcon("nocode.jpg");
                    LogPanelManager.setIcon(icon);
                    TicketMainFrame.remind("获取验证码失败,请检查网络是否正常");
                }
            }
        }
    }
    
    
    
    public static TicketHttpClient getHttpClient() {
        return httpClient;
    }
    
    /**
     * <重启httpClient>
     *
     */
    public static void restart() {
        httpClient = null;
        new HttpClientThreadService().run();
    }
}
