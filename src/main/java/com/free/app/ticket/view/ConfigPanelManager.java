package com.free.app.ticket.view;

import java.awt.FlowLayout;
import java.text.ParseException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.model.MixData4UI;
import com.free.app.ticket.model.TicketConfigInfo;
import com.free.app.ticket.util.DateUtils;
import com.free.app.ticket.util.StationNameUtils;

public class ConfigPanelManager {
    
    private static JPanel search_panel = null;
    
    private static JFormattedTextField trainDate;
    
    private static JTextField fromStation;
    
    private static JTextField toStation;
    
    private static JLabel trainConfigTips;
    
    private static boolean isInited = false;
    
    /**
     * <界面初始化调用>
     * 
     */
    public static void init(TicketMainFrame frame) {
        if (!isInited) {
            isInited = true;
            
            search_panel = new JPanel();
            // search_panel.setBounds(10, 394, 650, 63);
            search_panel.setBounds(10, 6, 780, 54);
            search_panel.setLayout(new FlowLayout());
            frame.getContentPane().add(search_panel);
            search_panel.setBorder(new TitledBorder("第一步：输入乘车信息"));
            
            JLabel label_16 = new JLabel("出发地");
            label_16.setBounds(257, 26, 40, 17);
            search_panel.add(label_16);
            label_16.setHorizontalAlignment(SwingConstants.RIGHT);
            
            fromStation = new JTextField();
            fromStation.setName("fromStation");
            fromStation.setBounds(307, 26, 60, 21);
            search_panel.add(fromStation);
            fromStation.setColumns(15);
            
            JLabel label_17 = new JLabel("目的地");
            label_17.setBounds(355, 26, 60, 17);
            search_panel.add(label_17);
            label_17.setHorizontalAlignment(SwingConstants.RIGHT);
            
            toStation = new JTextField();
            toStation.setName("toStation");
            toStation.setBounds(425, 26, 60, 21);
            search_panel.add(toStation);
            toStation.setColumns(15);
            
            JLabel label_15 = new JLabel("出发日");
            label_15.setBounds(112, 30, 60, 13);
            search_panel.add(label_15);
            label_15.setHorizontalAlignment(SwingConstants.RIGHT);
            
            MaskFormatter mf = null;
            try {
                mf = new MaskFormatter("####-##-##");
            }
            catch (ParseException e1) {
                e1.printStackTrace();
            }
            trainDate = new JFormattedTextField(mf);
            trainDate.setBounds(175, 26, 84, 21);
            search_panel.add(trainDate);
            trainDate.setColumns(11);
            
            JButton configButton = new JButton();
            configButton.setText("车次设置");
            configButton.setBounds(6, 22, 70, 28);
            search_panel.add(configButton);
            
            trainConfigTips = new JLabel("未设置车次");
            search_panel.add(trainConfigTips);
        }
    }
    
    public static TicketConfigInfo getValidConfigInfo() {
        String fromStationName = fromStation.getText();
        String fromCode = StationNameUtils.getCityCode(fromStationName);
        String toStationName = toStation.getText();
        String toCode = StationNameUtils.getCityCode(toStationName);
        if (fromCode == null) {
            TicketMainFrame.remind("出发地输入错误，请检查!");
            fromStation.requestFocus();
            return null;
        }
        
        if (toCode == null) {
            TicketMainFrame.remind("目的地输入错误，请检查!");
            toStation.requestFocus();
            return null;
        }
        
        String sTrainDate = trainDate.getText();
        if ("-  -".equals(sTrainDate.trim())) {
            TicketMainFrame.remind("请输入出发日!");
            trainDate.requestFocus();
            return null;
        }
        
        return new TicketConfigInfo(fromStationName, fromCode, toStationName, toCode, sTrainDate);
    }
    
    public static void bindUItoModel(MixData4UI mixData) {
        mixData.setTrainFrom(fromStation.getText());
        mixData.setTrainTo(toStation.getText());
        String sTrainDate = trainDate.getText().trim();
        if ("-  -".equals(sTrainDate)) {
            mixData.setTrainDate(null);
        }
        else {
            mixData.setTrainDate(sTrainDate);
            mixData.setInitDateFlag(DateUtils.formatDate(new Date()) + ":1");
        }
    }
    
    public static void bindModeltoUI(MixData4UI mixData) {
        fromStation.setText(mixData.getTrainFrom());
        toStation.setText(mixData.getTrainTo());
        
        String trainDayStr = DateUtils.formatDate(DateUtils.addDays(new Date(), 59), "yyyy-MM-dd");
        if (mixData.getTrainDate() != null) {
            if (mixData.getInitDateFlag() != null
                && mixData.getInitDateFlag().equals(DateUtils.formatDate(new Date()) + ":1")) {
                trainDate.setText(mixData.getTrainDate());
            }
            else {
                trainDate.setText(trainDayStr);
            }
        }
        else {
            trainDate.setText(trainDayStr);
        }
    }
    
}
