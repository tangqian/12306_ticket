package com.free.app.ticket.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.model.ContacterInfo;
import com.free.app.ticket.util.DateUtils;
import com.free.app.ticket.util.ResManager;

public class ConfigPanelManager {
    
    private static JPanel search_panel = null;
    
    private static JFormattedTextField trainDate;
    
    private static JTextField fromStation;
    
    private static JTextField toStation;
    
    private static JButton startButton;
    
    private static boolean isInited = false;
    
    /**
     * <界面初始化调用>
     *
     */
    public static void init(TicketMainFrame frame) {
        if (!isInited) {
            isInited = true;
            
            search_panel = new JPanel();
            search_panel.setBounds(10, 394, 650, 63);
            frame.getContentPane().add(search_panel);
            search_panel.setLayout(null);
            search_panel.setBorder(new TitledBorder(ResManager.getText("ticket.panel.configuration")));
            
            JButton configButton = new JButton();
            configButton.setText("设置");
            configButton.setBounds(6, 22, 70, 28);
            search_panel.add(configButton);
            
            JLabel label_15 = new JLabel(ResManager.getText("ticket.label.trainDate"));
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
            trainDate.setColumns(10);
            
            JLabel label_16 = new JLabel(ResManager.getText("ticket.label.fromStation"));
            label_16.setBounds(257, 26, 40, 17);
            search_panel.add(label_16);
            label_16.setHorizontalAlignment(SwingConstants.RIGHT);
            
            fromStation = new JTextField();
            fromStation.setName("fromStation");
            fromStation.setBounds(307, 26, 60, 21);
            search_panel.add(fromStation);
            fromStation.setColumns(10);
            
            JLabel label_17 = new JLabel(ResManager.getText("ticket.label.toStation"));
            label_17.setBounds(355, 26, 60, 17);
            search_panel.add(label_17);
            label_17.setHorizontalAlignment(SwingConstants.RIGHT);
            
            toStation = new JTextField();
            toStation.setName("toStation");
            toStation.setBounds(425, 26, 60, 21);
            search_panel.add(toStation);
            toStation.setColumns(10);
            
            startButton = new StartBtn(ResManager.getText("ticket.btn.start"));
            startButton.setBounds(509, 22, 80, 28);
            search_panel.add(startButton);
            //startButton.addActionListener(new StartButton());
            initValue();
        }
    }
    
    private static void initValue() {
        //trainDate.setText(DateUtils.formatDate(new Date()));
        trainDate.setText("2014-12-18");
        fromStation.setText("HYQ");
        toStation.setText("SZQ");
    }
    
    static class StartBtn extends JButton {
        
        /**
         * 注释内容
         */
        private static final long serialVersionUID = 1L;
        
        /**
         * 是否正在刷票
         */
        private boolean isWorking = false;
        
        public StartBtn(String text) {
            super(text);
            
            this.addActionListener(new ActionListener() {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isWorking) {
                        if (JOptionPane.showConfirmDialog(TicketMainFrame.frame, "确认停止刷票吗？", "请选择", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            setText("开始刷票");
                            isWorking = false;
                        }
                    }
                    else {
                        if (!LogPanelManager.isLogged()) {
                            TicketMainFrame.remind("请先登录!");
                            return;
                        }
                        
                        List<ContacterInfo> passengers = PassengerPanelManager.getPassenger();
                        if(passengers.isEmpty()){
                            TicketMainFrame.remind("请至少填写一个购票乘客的信息!");
                            return;
                        }else{
                            String tipsMsg = "===开始为乘客[";
                            for (ContacterInfo contacterInfo : passengers) {
                                tipsMsg += contacterInfo.getPassenger_name()+",";
                            }
                            tipsMsg = tipsMsg.substring(0, tipsMsg.length() - 1);
                            tipsMsg += "]刷票，请耐心等待===";
                            TicketMainFrame.trace(tipsMsg);
                            
                            
                            
                        }
                    }
                    
                    /*if (ResManager.getText("ticket.btn.start").equals(btn.getText())) {
                        List list = getUserInfo();
                        // 未输入联系人
                        if (list.size() == 0) {
                            showMsg("请至少输入1位联系人信息!");
                            return;
                        }
                        // 未登录
                        if (!isLogin) {
                            showMsg("请登录!");
                            return;
                        }
                        // 验证控件是否输入
                        List<String> msglist = ToolHelper.validateWidget(trainDate, fromStation, toStation);
                        if (msglist.size() > 0) {
                            String msg = "";
                            for (int i = 0; i < msglist.size(); i++) {
                                msg += (i == msglist.size() - 1 ? msglist.get(i) : msglist.get(i) + ",");
                            }
                            showMsg(msg + "不能为空！");
                            return;
                        }

                        // 获取列车查询实体
                        getOrderRequest();
                        if (isRunThread) {
                            showMsg("订票线程已启动!");
                            return;
                        }
                        userInfoList = getUserInfo();
                        // TODO 启动订票线程
                        new TicketThread(req, userInfoList, mainWin).start();
                        mainWin.startButton.setText(ResManager.getText("ticket.btn.stop"));
                    } else if (ResManager.getText("ticket.btn.stop").equals(btn.getText())) {
                        mainWin.isStopRun = true;
                        btn.setText(ResManager.getText("ticket.btn.start"));
                    }*/
                }
                
            });
        }
        
    }
    
}
