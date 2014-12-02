package com.free.app.ticket.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.free.app.ticket.util.ResManager;

/**
 * 
 * <登出面板UI类>
 */
public class LogoutPanel extends JPanel {
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -7232772496936978217L;
    
    public JButton logoutBtn;
    
    public LogoutPanel() {
        this.setBounds(10, 12, 650, 54);
        this.setLayout(null);
        this.setBorder(new TitledBorder(ResManager.getText("ticket.panel.userinfo")));
        
        JLabel label_o = new JLabel(ResManager.getText("欢迎使用本软件，有问题请反映至管理员xxx"));
        label_o.setBounds(10, 26, 585, 15);
        this.add(label_o);
        label_o.setHorizontalAlignment(SwingConstants.RIGHT);
        
        logoutBtn = new JButton("退出");
        logoutBtn.setBounds(560, 18, 65, 28);
        logoutBtn.setVisible(false);
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        this.add(logoutBtn);
    }
    
}
