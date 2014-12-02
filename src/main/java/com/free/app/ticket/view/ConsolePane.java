package com.free.app.ticket.view;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsolePane extends JTextPane {
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(ConsolePane.class);
    
    private static final SimpleAttributeSet NORM_ATTR;
    
    private static final SimpleAttributeSet RED_ATTR;
    
    static{
        NORM_ATTR = new SimpleAttributeSet();
        StyleConstants.setFontSize(NORM_ATTR, 14);
        
        RED_ATTR = new SimpleAttributeSet();
        StyleConstants.setForeground(RED_ATTR, Color.RED);
        StyleConstants.setFontSize(RED_ATTR, 14);
        //StyleConstants.setBold(RED_ATTR, true);
    }
    
    public void appendNorm(String str) {
        insert(str, NORM_ATTR);
    }
    
    public void appendRed(String str) {
        insert(str, RED_ATTR);
    }

    private void insert(String str, AttributeSet attrSet) {
        Document doc = this.getDocument();
        try {
            doc.insertString(doc.getLength(),  str + "\n", attrSet);
        }
        catch (BadLocationException e) {
            logger.error("BadLocationException:", e);
        }
        this.setCaretPosition(doc.getLength());
    }
}
