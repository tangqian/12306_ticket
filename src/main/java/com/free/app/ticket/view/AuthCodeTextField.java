package com.free.app.ticket.view;

import java.awt.EventQueue;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.free.app.ticket.service.CheckAuthcodeThreadService;

public class AuthCodeTextField extends JTextField {
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 5291297639654513239L;
    
    private int maxLength = 4;
    
    private String oldValue = null;
    
    public int getMaxLength() {
        return this.maxLength;
    }
    
    public AuthCodeTextField() {
        this.getDocument().addDocumentListener(new DocumentListener() {
            
            @Override
            public void removeUpdate(DocumentEvent e) {
            }
            
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (e.getDocument().getLength() > 4) {
                    
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            AuthCodeTextField.this.setText(oldValue);
                        }
                    });
                }
                else if (e.getDocument().getLength() == 4) {
                    String newValue = AuthCodeTextField.this.getText();
                    if( !newValue.equals(oldValue)){
                        oldValue = newValue;
                        new CheckAuthcodeThreadService(newValue).start();
                    }
                }
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }
    
}
