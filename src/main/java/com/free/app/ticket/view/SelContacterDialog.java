package com.free.app.ticket.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.free.app.ticket.model.ContacterInfo;
import com.free.app.ticket.model.PassengerData;

public class SelContacterDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private JButton okButton;
    
    private JButton cancelButton;
    
    private ContacterInfo[] contacters = null;
    
    private JCheckBox[] boxs = null;
    
    private ContacterInfo[] selectContacters = null;
    
    private List<PassengerData> selectPassengers = null;
    
    private static int MAX_PASSENGERS = 5;
    
    private SelContacterDialog(ContacterInfo[] contacters, List<PassengerData> selectPassengers) {
        super();
        this.contacters = contacters;
        this.selectPassengers = selectPassengers;
        init();
        setModal(true);
        setResizable(false);
        setSize(400, 300);
    }
    
    private void init() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        
        JPanel contacterPanel = new JPanel();
        contacterPanel.setLayout(new FlowLayout());
        
        ContacterItemListener itemListener = new ContacterItemListener();
        
        if(contacters != null){
            boxs = new JCheckBox[contacters.length];
            for (int i = 0; i < contacters.length; i++) {
                JCheckBox cb1 = new JCheckBox(contacters[i].getPassenger_name(), false);
				PassengerData passenger = new PassengerData(
						contacters[i].getPassenger_name(),
						contacters[i].getPassenger_id_no(),
						contacters[i].getMobile_no());
				if (selectPassengers.contains(passenger)) {
					cb1.setSelected(true);
				}
                boxs[i] = cb1;
                cb1.addItemListener(itemListener);
                contacterPanel.add(cb1);
            }
        }
        /*contacterPanel.setBounds(10, 10, 350, 200);
        JScrollPane scrollPane = new JScrollPane(contacterPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);*/
        c.add(contacterPanel);
        
        JPanel operatePanel = new JPanel();
        FlowLayout fl = new FlowLayout();
        fl.setHgap(15);
        operatePanel.setLayout(fl);
        okButton = new JButton("确定");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (boxs != null) {
                    selectContacters = new ContacterInfo[MAX_PASSENGERS];
                    int index = 0;
                    for (int i = 0; i < boxs.length; i++) {
                        if(boxs[i].isSelected()){
                            selectContacters[index++] = contacters[i];
                            if(index == MAX_PASSENGERS)
                                break;
                        }
                    }
                }
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
    
    public ContacterInfo[] getSelected(){
        return selectContacters;
    }
    
    class ContacterItemListener implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
        }
        
    }
    
    public static ContacterInfo[] showDialog(Component relativeTo, ContacterInfo[] contacters, List<PassengerData> selectPassengers) {
        SelContacterDialog d = new SelContacterDialog(contacters, selectPassengers);
        d.setLocationRelativeTo(relativeTo);
        d.setVisible(true);
        return d.getSelected();
    }
    
}
