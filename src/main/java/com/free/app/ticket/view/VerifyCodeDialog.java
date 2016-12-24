package com.free.app.ticket.view;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.service.HttpClientThreadService;
import com.free.app.ticket.util.ResManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VerifyCodeDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(VerifyCodeDialog.class);

    private JButton okButton;

    private JButton cancelButton;

    public JLabel code;

    private static VerifyCodeDialog instance;

    private VerifyCodeDialog() {
        super();
        init();
        setModal(true);
        setResizable(false);
        setTitle("验证码");
        setSize(400, 300);
    }

    private void init() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(null);
        code = new JLabel();
        code.setBounds(20, 18, 330, 200);
        code.setText("正在获取验证码......");
        code.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(code);
        c.add(centerPanel, BorderLayout.CENTER);

        JPanel operatePanel = new JPanel();
        FlowLayout fl = new FlowLayout();
        fl.setHgap(15);
        operatePanel.setLayout(fl);

        cancelButton = new JButton("刷新");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HttpClientThreadService().start();
            }
        });

        okButton = new JButton("确定");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginPanelManager.setAuthCode("dddddddd");
                dispose();
            }
        });
        operatePanel.add(okButton);
        operatePanel.add(cancelButton);
        c.add(operatePanel, BorderLayout.SOUTH);
    }

    public static VerifyCodeDialog getInstance() {
        if(instance == null){
            instance = new VerifyCodeDialog();
        }
        return instance;
        //d.setVisible(true);
    }
}
