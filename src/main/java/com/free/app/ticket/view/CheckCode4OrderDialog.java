package com.free.app.ticket.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.free.app.ticket.model.TicketBuyInfo;
import com.free.app.ticket.service.CheckOrderAuthcodeThreadService;
import com.free.app.ticket.service.OrderAuthCodeThreadService;
import com.free.app.ticket.service.AutoBuyThreadService.OrderToken;
import com.free.app.ticket.util.ResManager;

public class CheckCode4OrderDialog extends JDialog {
    
    private static final long serialVersionUID = 1L;
    
    //private static final Logger logger = LoggerFactory.getLogger(CheckCode4OrderDialog.class);
    
    /*private JButton cancelButton;*/

    private JLabel code;
    
    private TicketBuyInfo buyInfo;
    
    private OrderToken token;
    
    private Map<String, String> cookies;
    
    private DialogResult callbackResult = new DialogResult(ChooseType.DEFAULT, null);;
    
    private CheckCode4OrderDialog(TicketBuyInfo buyInfo, OrderToken token, Map<String, String> cookies) {
        super();
        this.buyInfo = buyInfo;
        this.token = token;
        this.cookies = cookies;
        init();
        setModal(true);
        setResizable(false);
        setTitle("请输入验证码");
        setSize(200, 150);
        setLocation(300, 100);
    }
    
    public static DialogResult showDialog(Component relativeTo, TicketBuyInfo buyInfo, OrderToken token,
        Map<String, String> cookies) {
        CheckCode4OrderDialog d = new CheckCode4OrderDialog(buyInfo, token, cookies);
        d.setLocationRelativeTo(relativeTo);
        d.setVisible(true);
        return d.callbackResult;
    }
    
    public void setSuccess(String authcode) {
        callbackResult.setChooseType(ChooseType.SUCCESS);
        callbackResult.setAuthcode(authcode);
    }
    
    public void setAuthCode(ImageIcon icon) {
        code.setIcon(icon);
    }
    
    private void init() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        
        JPanel contacterPanel = new JPanel();
        contacterPanel.setLayout(null);
        
        code = new JLabel();
        code.setBounds(22, 21, 78, 28);
        code.setToolTipText("点我刷新验证码！");
        code.setIcon(ResManager.createImageIcon("nocode.jpg"));
        contacterPanel.add(code);
        code.setHorizontalAlignment(SwingConstants.RIGHT);
        // 增加鼠标单击验证码重新获取验证码
        code.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new OrderAuthCodeThreadService(CheckCode4OrderDialog.this).start();
            }
        });
        
        AuthCodeTextField4Order authcode = new AuthCodeTextField4Order();
        authcode.setToolTipText(ResManager.getText("ticket.label.code.tipinfo"));
        authcode.setBounds(110, 21, 40, 21);
        contacterPanel.add(authcode);
        authcode.setColumns(10);
        
        c.add(contacterPanel);
        
        JPanel operatePanel = new JPanel();
        FlowLayout fl = new FlowLayout();
        fl.setHgap(15);
        operatePanel.setLayout(fl);
        
        JButton cancelAllButton = new JButton("取消全部");
        cancelAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                callbackResult.setChooseType(ChooseType.CANCELALL);
                dispose();
            }
        });
        
        JButton cancelCurButton = new JButton("取消当前");
        cancelCurButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                callbackResult.setChooseType(ChooseType.CANCELTHIS);
                dispose();
            }
        });
        operatePanel.add(cancelAllButton);
        operatePanel.add(cancelCurButton);
        
        c.add(operatePanel, BorderLayout.SOUTH);
    }
    
    public TicketBuyInfo getBuyInfo() {
        return buyInfo;
    }
    
    public OrderToken getToken() {
        return token;
    }
    
    public Map<String, String> getCookies() {
        return cookies;
    }
    
    public static class DialogResult {
        
        private ChooseType chooseType;
        
        private String authcode;
        
        public DialogResult(ChooseType chooseType, String authcode) {
            super();
            this.chooseType = chooseType;
            this.authcode = authcode;
        }
        
        public void setChooseType(ChooseType chooseType) {
            this.chooseType = chooseType;
        }
        
        public void setAuthcode(String authcode) {
            this.authcode = authcode;
        }
        
        public ChooseType getChooseType() {
            return chooseType;
        }
        
        public String getAuthcode() {
            return authcode;
        }
        
    }
    
    public static enum ChooseType {
        
        CANCELALL("全部取消预订"), CANCELTHIS("只取消预订当前车次"), DEFAULT("默认"), SUCCESS("授权码成功通过验证");
        
        private String label;
        
        private ChooseType(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
    }
    
    class AuthCodeTextField4Order extends JTextField {
        
        /**
         * 注释内容
         */
        private static final long serialVersionUID = 5291297639654513239L;
        
        private int maxLength = 4;
        
        private String oldValue = null;
        
        public int getMaxLength() {
            return this.maxLength;
        }
        
        public AuthCodeTextField4Order() {
            this.getDocument().addDocumentListener(new DocumentListener() {
                
                @Override
                public void removeUpdate(DocumentEvent e) {
                }
                
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if (e.getDocument().getLength() > 4) {
                        
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                AuthCodeTextField4Order.this.setText(oldValue);
                            }
                        });
                    }
                    else if (e.getDocument().getLength() == 4) {
                        String newValue = AuthCodeTextField4Order.this.getText();
                        if (!newValue.equals(oldValue)) {
                            oldValue = newValue;
                            new CheckOrderAuthcodeThreadService(newValue, CheckCode4OrderDialog.this).start();
                        }
                    }
                }
                
                @Override
                public void changedUpdate(DocumentEvent e) {
                }
            });
        }
        
    }
    
}
