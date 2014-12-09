package com.free.app.ticket.service;

import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.util.ResManager;
import com.free.app.ticket.util.TicketHttpClient;
import com.free.app.ticket.view.CheckCode4OrderDialog;

public class OrderAuthCodeThreadService extends Thread {
    
    private CheckCode4OrderDialog parent;
    
    public OrderAuthCodeThreadService(CheckCode4OrderDialog dialog) {
        parent = dialog;
    }
    
    @Override
    public void run() {
        TicketHttpClient client = HttpClientThreadService.getHttpClient();
        File imageFile = client.buildOrderCodeImage(parent.getCookies());
        if (imageFile != null && imageFile.exists()) {
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            icon.setImage(icon.getImage()
                .getScaledInstance(icon.getIconWidth(), icon.getIconHeight(), Image.SCALE_DEFAULT));
            parent.setAuthCode(icon);
            TicketMainFrame.trace("获取提交订单验证码成功");
        }
        else {
            ImageIcon icon = ResManager.createImageIcon("nocode.jpg");
            parent.setAuthCode(icon);
            TicketMainFrame.remind("获取提交订单验证码失败,请检查网络是否正常");
        }
    }
}
