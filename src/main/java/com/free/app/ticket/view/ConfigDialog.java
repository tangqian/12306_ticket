package com.free.app.ticket.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.free.app.ticket.TicketMainFrame;

public class ConfigDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private JButton okButton;
    
    private JButton cancelButton;
    
    private JCheckBox preciseModel;
    
    private JCheckBox fuzzyModel;
    
    private ConfigDialog() {
        super();
        init();
        setModal(true);
        setResizable(false);
        setTitle("车次设置");
        setLocation(300, 0);
        setSize(400, 300);
    }
    
    private void init() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(null);
        initCenterPanel(centerPanel);
        c.add(centerPanel);
        
        JPanel operatePanel = new JPanel();
        FlowLayout fl = new FlowLayout();
        fl.setHgap(15);
        operatePanel.setLayout(fl);
        okButton = new JButton("确定");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        cancelButton = new JButton("取消");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        operatePanel.add(okButton);
        operatePanel.add(cancelButton);
        
        c.add(operatePanel, BorderLayout.SOUTH);
    }
    
    private void initCenterPanel(JPanel centerPanel) {
        JPanel modelChoose = new JPanel();
        modelChoose.setBounds(10, 0, 380, 50);
        FlowLayout fl = new FlowLayout();
        fl.setVgap(1);
        modelChoose.setLayout(fl);
        modelChoose.setBorder(new TitledBorder("第一步：选择刷票模式"));
        preciseModel = new JCheckBox("精确买票", false);
        fuzzyModel = new JCheckBox("范围买票", false);
        modelChoose.add(preciseModel);
        modelChoose.add(fuzzyModel);
        centerPanel.add(modelChoose);
        
        PrecisePanel precisePanel = new PrecisePanel();
        centerPanel.add(precisePanel);
        
        FuzzyPanel fuzzyPanel = new FuzzyPanel();
        centerPanel.add(fuzzyPanel);
    }
    
    public static void showDialog() {
        ConfigDialog d = new ConfigDialog();
        d.setLocationRelativeTo(TicketMainFrame.frame);
        d.setVisible(true);
    }
    
    class PrecisePanel extends JPanel{
        
        /**
         * 注释内容
         */
        private static final long serialVersionUID = 1L;
        
        private JTextField[] trains;
      
        public PrecisePanel() {
            this.setBounds(10, 50, 380, 180);
            this.setLayout(null);
            this.setBorder(new TitledBorder("第二步：具体设置"));
            
            trains = new JTextField[5];
            int heightOneRow = (180 - 20) / trains.length;
            for (int i = 0; i < trains.length; i++) {
                JLabel label = new JLabel( "车次" + (i + 1));
                label.setBounds(10, 25 + heightOneRow * i, 50, 15);
                label.setHorizontalAlignment(SwingConstants.RIGHT);
                this.add(label);
                
                JTextField field = new JTextField();
                field.setBounds(65, 21 + heightOneRow * i, 50, 21);
                field.setColumns(12);
                trains[i] = field;
                this.add(field);
                
                JComboBox comboBox=new JComboBox();
                comboBox.addItem("二等座");
                comboBox.addItem("一等座");
                comboBox.addItem("商务座/特等座");
                comboBox.setSelectedItem("二等座");
                comboBox.setBounds(130, 21 + heightOneRow * i, 80, 21);
                this.add(comboBox);
                
            }
        }
    }
    
    class FuzzyPanel extends JPanel{
        
        /**
         * 注释内容
         */
        private static final long serialVersionUID = 1L;

        public FuzzyPanel() {
            this.setBounds(10, 50, 380, 180);
            this.setBorder(new TitledBorder("第二步：具体设置"));
            this.setVisible(false);
        }
    }
}
