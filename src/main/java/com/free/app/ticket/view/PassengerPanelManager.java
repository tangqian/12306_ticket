package com.free.app.ticket.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.commons.lang3.StringUtils;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.model.ContacterInfo;
import com.free.app.ticket.model.MixData4UI;
import com.free.app.ticket.model.PassengerData;

public class PassengerPanelManager {
    
    private static JPanel parent = null;
    
    private static JTable table = null;
    
    private static JTextField passengerName;
    
    private static JTextField cardNo;
    
    private static JTextField mobile;
    
    private static DefaultTableModel tableModel;
    
    private static boolean isInited = false;
    
    public static void init(JFrame frame) {
        if (!isInited) {
            isInited = true;
            parent = new JPanel();
            parent.setLayout(null);
            parent.setBounds(10, 130, 780, 250);
            parent.setBorder(new TitledBorder("第三步：乘车人信息"));
            frame.getContentPane().add(parent);
            
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setBounds(10, 20, 760, 185);
            parent.add(scrollPane);
            
            String[][] row = new String[0][6];
            String[] column =
            {"姓名", "身份证号", "手机号码"};
            tableModel = new DefaultTableModel(row, column);
            
            table = new JTable(tableModel);
            table.setDefaultRenderer(Integer.class, new MyRenderer());
            adjustView(table);
            scrollPane.setViewportView(table);
            
            parent.add(getOperatePanel(tableModel));
            
            table.addMouseListener(new MouseAdapter() { // 鼠标事件
                public void mouseClicked(MouseEvent e) {
                    int selectedRow = table.getSelectedRow(); // 获得选中行索引
                    Object oa = tableModel.getValueAt(selectedRow, 0);
                    Object ob = tableModel.getValueAt(selectedRow, 1);
                    Object oc = tableModel.getValueAt(selectedRow, 2);
                    passengerName.setText(oa.toString()); // 给文本框赋值
                    cardNo.setText(ob.toString());
                    mobile.setText(oc.toString());
                }
            });
        }
    }
    
    public static void addPassenger(ContacterInfo[] selected) {
        if (isInited && selected != null) {
            for (ContacterInfo contacterInfo : selected) {
                if (table.getRowCount() >= 5) {
                    TicketMainFrame.remind("最多只能购买5张票!");
                    break;
                }
                else {
                    if (contacterInfo == null)
                        continue;
                    String[] rowValues =
                        {contacterInfo.getPassenger_name(), contacterInfo.getPassenger_id_no(),
                            contacterInfo.getMobile_no()};
                    tableModel.addRow(rowValues); // 添加一行
                }
            }
        }
    }
    
    public static List<PassengerData> getPassenger() {
        int rowCount = tableModel.getRowCount();
        List<PassengerData> lists = new ArrayList<PassengerData>();
        if (rowCount > 0) {
            for (int i = 0; i < rowCount; i++) {
                String name = (String)tableModel.getValueAt(i, 0);
                String cardNo = (String)tableModel.getValueAt(i, 1);
                String mobile = (String)tableModel.getValueAt(i, 2);
                
                if (StringUtils.isBlank(name)) {
                    TicketMainFrame.remind("第" + i + 1 + "行，用户姓名未填，请检查");
                    continue;
                }
                else if (StringUtils.isBlank(cardNo)) {
                    TicketMainFrame.remind("第" + i + 1 + "行，身份证号未填，请检查");
                    continue;
                }
                else {
                    PassengerData info = new PassengerData(name, cardNo, mobile);
                    lists.add(info);
                }
            }
        }
        return lists;
    }
    
    /**
     * <获取操作面板>
     * 
     * @param tableModel
     */
    private static JPanel getOperatePanel(final DefaultTableModel tableModel) {
        final JPanel panel = new JPanel();
        panel.setBounds(10, 210, 770, 34);
        
        panel.add(new JLabel("姓名"));
        passengerName = new JTextField("", 10);
        panel.add(passengerName);
        
        panel.add(new JLabel("身份证号"));
        cardNo = new JTextField("", 20);
        panel.add(cardNo);
        
        panel.add(new JLabel("手机号码"));
        mobile = new JTextField("", 13);
        panel.add(mobile);
        
        final JButton addButton = new JButton("新增"); // 添加按钮
        addButton.setToolTipText("新增购票乘客信息");
        addButton.addActionListener(new ActionListener() {// 添加事件
            public void actionPerformed(ActionEvent e) {
                if (table.getRowCount() >= 5) {
                    TicketMainFrame.remind("最多只能购买5张票!");
                }
                else {
                    String[] rowValues =
                    {passengerName.getText(), cardNo.getText(), mobile.getText()};
                    tableModel.addRow(rowValues); // 添加一行
                }
                
            }
        });
        panel.add(addButton);
        
        final JButton updateButton = new JButton("修改"); // 修改按钮
        updateButton.setToolTipText("修改乘客信息，请先选择一行记录");
        updateButton.addActionListener(new ActionListener() {// 添加事件
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();// 获得选中行的索引
                if (selectedRow != -1) // 是否存在选中行
                {
                    // 修改指定的值：
                    tableModel.setValueAt(passengerName.getText(), selectedRow, 0);
                    tableModel.setValueAt(cardNo.getText(), selectedRow, 1);
                    tableModel.setValueAt(mobile.getText(), selectedRow, 2);
                }
                else {
                    TicketMainFrame.remind("请先选中一行乘客信息!");
                }
            }
        });
        panel.add(updateButton);
        
        final JButton delButton = new JButton("删除");
        delButton.setToolTipText("删除乘客信息，请先选择一行记录");
        delButton.addActionListener(new ActionListener() {// 添加事件
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();// 获得选中行的索引
                if (selectedRow != -1) // 存在选中行
                {
                    tableModel.removeRow(selectedRow); // 删除行
                }
                else {
                    TicketMainFrame.remind("请先选中一行乘客信息!");
                }
            }
        });
        panel.add(delButton);
        return panel;
    }
    
    private static void adjustView(JTable table) {
        table.getTableHeader().setPreferredSize(new Dimension(0, 30));
        // 3设置表内容行高
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 单选
        table.setRowHeight(25);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(100);
                    break;
                
                case 1:
                    column.setPreferredWidth(150);
                    break;
                
                case 2:
                    column.setPreferredWidth(110);
                    break;
                
                default:
                    break;
            }
        }
    }
    
    static class MyRenderer implements TableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
            JLabel jl = new JLabel();
            if ((Integer)value > 0) {
                jl.setForeground(Color.RED);
            }
            jl.setBackground(Color.WHITE);
            jl.setOpaque(true);
            jl.setText(value.toString());
            System.out.println("row:" + row + ", column" + column);
            return jl;
        }
    }
    
    public static void bindUItoModel(MixData4UI mixData) {
        mixData.setPassengerDatas(getPassenger());
    }
    
    public static void bindModeltoUI(MixData4UI mixData) {
        List<PassengerData> passengers = mixData.getPassengerDatas();
        if (passengers != null && !passengers.isEmpty()) {
            for (PassengerData passenger : passengers) {
                if (table.getRowCount() >= 5) {
                    //TicketMainFrame.remind("最多只能购买5张票!");
                    break;
                }
                else {
                    String[] rowValues =
                    {passenger.getName(), passenger.getCardNo(), passenger.getMobile()};
                    tableModel.addRow(rowValues); // 添加一行
                }
            }
        }
    }
}
