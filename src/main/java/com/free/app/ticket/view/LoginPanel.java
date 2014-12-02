package com.free.app.ticket.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.free.app.ticket.service.HttpClientThreadService;
import com.free.app.ticket.util.ResManager;

/**
 * 
 * <登录面板UI类>
 */
public class LoginPanel extends JPanel {
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -5160355345190643159L;
    
    public JTextField username;
    
    public JPasswordField password;
    
    public static JLabel code;
    
    public LoginBtn loginBtn;
    
    public JTextField authcode;
    
    public LoginPanel() {
        this.setBounds(10, 12, 650, 54);
        this.setLayout(null);
        this.setBorder(new TitledBorder(ResManager.getText("ticket.panel.userinfo")));
        
        JLabel label_o = new JLabel(ResManager.getText("ticket.label.user_name"));
        label_o.setBounds(10, 26, 40, 15);
        this.add(label_o);
        label_o.setHorizontalAlignment(SwingConstants.RIGHT);
        
        username = new JTextField();
        username.setName("username");
        username.setBounds(60, 23, 100, 21);
        this.add(username);
        username.setColumns(10);
        
        JLabel label_o1 = new JLabel(ResManager.getText("ticket.label.password"));
        label_o1.setBounds(170, 26, 40, 15);
        this.add(label_o1);
        label_o1.setHorizontalAlignment(SwingConstants.RIGHT);
        
        password = new JPasswordField();
        password.setName("password");
        password.setBounds(220, 23, 100, 21);
        this.add(password);
        password.setColumns(10);
        
        code = new JLabel();
        code.setBounds(341, 18, 78, 28);
        code.setToolTipText("点我刷新验证码！");
        code.setIcon(ResManager.createImageIcon("nocode.jpg"));
        this.add(code);
        code.setHorizontalAlignment(SwingConstants.RIGHT);
        // 增加鼠标单击验证码重新获取验证码
        code.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new HttpClientThreadService().start();
            }
        });
        
        authcode = new AuthCodeTextField();
        authcode.setToolTipText(ResManager.getText("ticket.label.code.tipinfo"));
        authcode.setBounds(440, 23, 40, 21);
        this.add(authcode);
        authcode.setColumns(7);
        
        loginBtn = new LoginBtn(this, ResManager.getText("ticket.btn.login"));
        loginBtn.setBounds(560, 18, 65, 28);
        this.add(loginBtn);
    }
    
}
