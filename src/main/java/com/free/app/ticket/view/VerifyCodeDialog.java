package com.free.app.ticket.view;

import com.free.app.ticket.service.HttpClientThreadService;
import com.free.app.ticket.util.ResManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VerifyCodeDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(VerifyCodeDialog.class);

    private JButton okButton;

    private JButton cancelButton;

    private JLabel code;

    private  final MyModel model = new MyModel();

    private boolean[][] selects = new boolean[2][4];

    private static final int unit = 71;

    private static final int baseX = 530;
    private static final int baseY = 300;

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

        final JTable table = new JTable(model);
        table.setBounds(43, 63, 284, 142);
        table.setRowHeight(71);
        table.setShowGrid(false);
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //得到选中的行列的索引值
                int r = table.getSelectedRow();
                int c = table.getSelectedColumn();
                if (model.isSelected(r, c)) {
                    model.setValueAt(false, r, c);
                } else {
                    model.setValueAt(true, r, c);
                }

            }
        });

        table.setOpaque(false);
        MyTableCellRenderer render = new MyTableCellRenderer(model);
        render.setOpaque(false); //将渲染器设置为透明
        //将这个渲染器设置到fileTable里。这个设置在没有另外专门对column设置的情况下有效
        //若你对某个column特殊指定了渲染器，则对于这个column，它将不调用render渲染器
        //因此为了保证透明，如果你对column额外指定了渲染器，那么在额外的渲染器里也应该设置透明
        table.setDefaultRenderer(Object.class, render);
        centerPanel.add(table, BorderLayout.CENTER);

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
                dispose();
            }
        });
        operatePanel.add(okButton);
        operatePanel.add(cancelButton);
        c.add(operatePanel, BorderLayout.SOUTH);
    }

    public void showCode(String text, ImageIcon icon){
        code.setText(text);
        code.setIcon(icon);
        model.clearValues();
    }

    public static VerifyCodeDialog getInstance() {
        if (instance == null) {
            instance = new VerifyCodeDialog();
        }
        return instance;
        //d.setVisible(true);
    }
}

class MyTableCellRenderer extends DefaultTableCellRenderer {

    private MyModel myModel;

    public MyTableCellRenderer(MyModel myModel) {
        this.myModel = myModel;
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        if (myModel.isSelected(row, column)) {
            return new JLabel(ResManager.createImageIcon("selected.jpg"), SwingConstants.CENTER);
        } else {
            return new JLabel("");
        }
    }
}

class MyModel extends AbstractTableModel {

    private boolean[][] selects = new boolean[2][4];

    @Override
    public int getRowCount() {
        return selects.length;
    }

    @Override
    public int getColumnCount() {
        return selects[0].length;
    }

    public boolean isSelected(int rowIndex, int columnIndex) {
        return selects[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        selects[rowIndex][columnIndex] = (Boolean) aValue;
        fireTableCellUpdated(rowIndex, columnIndex);

        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < selects.length; r++) {
            for (int c = 0; c < selects[0].length; c++) {
                if (selects[r][c]) {
                    sb.append((c * 2 + 1) * 35).append(",").append((r * 2 + 1) * 35).append(",");
                }
            }
        }
        String authCode = sb.length() != 0 ? sb.substring(0, sb.length() - 1) : null;
        LoginPanelManager.setAuthCode(authCode);
    }

    public void clearValues() {
        for (int r = 0; r < selects.length; r++) {
            for (int c = 0; c < selects[0].length; c++) {
                selects[r][c] = false;
                fireTableCellUpdated(r, c);
            }
        }
        LoginPanelManager.setAuthCode(null);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
}
