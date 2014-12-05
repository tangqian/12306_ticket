package com.free.app.ticket.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.model.ContacterInfo;
import com.free.app.ticket.service.HttpClientThreadService;
import com.free.app.ticket.util.ResManager;
import com.free.app.ticket.util.TicketHttpClient;

/**
 * 
 * <登出面板UI类>
 */
class LogoutPanel extends JPanel {
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -7232772496936978217L;
    
    public JButton logoutBtn;
    
    public JButton addBtn;
    
    public LogoutPanel(final JFrame frame) {
        this.setBounds(10, 12, 650, 54);
        this.setLayout(null);
        this.setBorder(new TitledBorder(ResManager.getText("ticket.panel.userinfo")));
        
        JLabel label_o = new JLabel(ResManager.getText("ticket.panel.logged"));
        label_o.setFont(new Font("Courier", Font.PLAIN, 14));
        label_o.setBounds(10, 26, 480, 15);
        this.add(label_o);
        label_o.setHorizontalAlignment(SwingConstants.CENTER);
        
        initAddBtn(frame);
        initLogoutBtn(frame);
        this.setVisible(false);
    }
    
    /**
     * <初始化增加乘客按钮>
     * @param frame
     */
    private void initAddBtn(final JFrame frame) {
        if (addBtn != null)
            return;
        addBtn = new JButton("增加");
        addBtn.setToolTipText("增加购票乘客");
        addBtn.setBounds(490, 18, 65, 28);
        addBtn.setVisible(true);
        addBtn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                TicketHttpClient client = HttpClientThreadService.getHttpClient();
                ContacterInfo[] contacters = client.getPassengers();
                ContacterInfo[] selected = SelContacterDialog.showDialog(frame, contacters);//弹出乘客选择框，并取得被选中乘客
                PassengerPanelManager.addPassenger(selected);//加入待购票乘客表格中
                System.out.println("------end--------");
            }
            
        });
        this.add(addBtn);
    }
    
    /**
     * <初始化退出按钮>
     * @param frame
     */
    private void initLogoutBtn(final JFrame frame) {
        if (logoutBtn != null)
            return;
        logoutBtn = new JButton("退出");
        logoutBtn.setBounds(560, 18, 65, 28);
        logoutBtn.setVisible(true);
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(frame, "确认退出登录吗？", "请选择", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    new Thread(new Runnable() {
                        
                        @Override
                        public void run() {
                            TicketHttpClient client = HttpClientThreadService.getHttpClient();
                            if (client != null) {
                                client.loginOut();
                            }
                            TicketMainFrame.isInited = false;
                            HttpClientThreadService.restart();//点退出时，要重新获取初始sessionid,即重新得到一个httpclient
                        }
                    }).start();
                    LogPanelManager.toggle();
                }
            }
        });
        this.add(logoutBtn);
    }
    
}
