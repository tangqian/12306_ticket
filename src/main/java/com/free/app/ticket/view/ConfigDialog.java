package com.free.app.ticket.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.model.TrainData4UI;
import com.free.app.ticket.model.TrainData4UI.BuyModel;
import com.free.app.ticket.model.TrainData4UI.SeatOptionType;
import com.free.app.ticket.model.TrainData4UI.UserTrainInfo;

public class ConfigDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(ConfigDialog.class);
    
    private JButton okButton;
    
    private JButton cancelButton;
    
    private JRadioButton preciseModelBtn;
    
    private JRadioButton fuzzyModelBtn;
    
    private PrecisePanel precisePanel;
    
    private FuzzyPanel fuzzyPanel;
    
    private TrainData4UI trainData;
    
    private ConfigDialog(TrainData4UI data) {
        super();
        this.trainData = data;
        init();
        bindModeltoUI();
        setModal(true);
        setResizable(false);
        setTitle("车次设置");
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
                bindUItoModel();
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
        
        ButtonGroup bg = new ButtonGroup();
        preciseModelBtn = new JRadioButton("精确买票");
        preciseModelBtn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                precisePanel.setVisible(true);
                fuzzyPanel.setVisible(false);
            }
        });
        bg.add(preciseModelBtn);
        
        fuzzyModelBtn = new JRadioButton("范围买票");
        fuzzyModelBtn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                precisePanel.setVisible(false);
                fuzzyPanel.setVisible(true);
            }
        });
        bg.add(fuzzyModelBtn);
        
        modelChoose.add(preciseModelBtn);
        modelChoose.add(fuzzyModelBtn);
        centerPanel.add(modelChoose);
        
        precisePanel = new PrecisePanel();
        precisePanel.setVisible(false);
        centerPanel.add(precisePanel);
        
        fuzzyPanel = new FuzzyPanel();
        fuzzyPanel.setVisible(false);
        centerPanel.add(fuzzyPanel);
    }
    
    public void bindUItoModel() {
        if (trainData == null){
            logger.debug(" train config bindUItoModel, trainData is null");
            trainData = new TrainData4UI();
        }
        
        if (preciseModelBtn.isSelected())
            trainData.setModel(BuyModel.Precise);
        else if (fuzzyModelBtn.isSelected())
            trainData.setModel(BuyModel.Fuzzy);
        
        precisePanel.bindUItoModel();
    }
    
    public void bindModeltoUI() {
        if (trainData == null){
            logger.debug(" train config bindModeltoUI, trainData is null");
            return;
        }
        if (trainData.getModel() == BuyModel.Precise) {
            precisePanel.setVisible(true);
            fuzzyPanel.setVisible(false);
            preciseModelBtn.setSelected(true);
        }else if (trainData.getModel() == BuyModel.Fuzzy) {
            precisePanel.setVisible(false);
            fuzzyPanel.setVisible(true);
            fuzzyModelBtn.setSelected(true);
        }
        
        precisePanel.bindModeltoUI();
    }
    
    public static TrainData4UI showDialog(TrainData4UI trainData) {
        ConfigDialog d = new ConfigDialog(trainData);
        d.setLocationRelativeTo(TicketMainFrame.frame);
        d.setVisible(true);
        return d.trainData;
    }
    
    public class PrecisePanel extends JPanel {
        
        /**
         * 注释内容
         */
        private static final long serialVersionUID = 1L;
        
        private TrainTextField[] trainTexts;
        
        private JComboBox[] boxBest;
        
        private JComboBox[] boxWorst;
        
        public PrecisePanel() {
            this.setBounds(10, 50, 380, 180);
            this.setLayout(null);
            this.setBorder(new TitledBorder("第二步：具体设置"));
            
            trainTexts = new TrainTextField[5];
            boxBest = new JComboBox[5];
            boxWorst = new JComboBox[5];
            int heightOneRow = (180 - 20) / trainTexts.length;
            for (int i = 0; i < trainTexts.length; i++) {
                JLabel label = new JLabel("车次" + (i + 1));
                label.setBounds(10, 25 + heightOneRow * i, 50, 15);
                label.setHorizontalAlignment(SwingConstants.RIGHT);
                this.add(label);
                
                TrainTextField field = new TrainTextField(i);
                field.setBounds(65, 21 + heightOneRow * i, 60, 21);
                field.setColumns(12);
                trainTexts[i] = field;
                this.add(field);
                
                JComboBox comboBox = new JComboBox();
                comboBox.setToolTipText("最希望买到的席别");
                comboBox.setBounds(140, 21 + heightOneRow * i, 105, 21);
                boxBest[i] = comboBox;
                this.add(comboBox);
                
                label = new JLabel("--");
                label.setToolTipText("席别范围");
                label.setBounds(250, 25 + heightOneRow * i, 20, 15);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                this.add(label);
                
                comboBox = new JComboBox();
                comboBox.setToolTipText("实在买不到还能接受的席别，两者之间的席别也会进行购买");
                boxWorst[i] = comboBox;
                comboBox.setBounds(275, 21 + heightOneRow * i, 105, 21);
                this.add(comboBox);
                
            }
        }
        
        public void bindModeltoUI() {
            UserTrainInfo[] trains = trainData.getUserTrains();
            if (trains == null)
                return;
            for (int i = 0; i < trains.length; i++) {
                UserTrainInfo train = trains[i];
                if (train != null) {
                    SeatOptionType optionType = SeatOptionType.getType(train.getTrainCode().charAt(0));
                    if (optionType != null) {
                        trainTexts[i].setText(train.getTrainCode());
                        optionType.initItem(boxBest[i], train.getBestSeatType());
                        optionType.initItem(boxWorst[i], train.getWorstSeatType());
                    }
                }
            }
        }
        
        public void bindUItoModel() {
            List<UserTrainInfo> list = new ArrayList<UserTrainInfo>();
            for (int i = 0; i < trainTexts.length; i++) {
                String trainCode = trainTexts[i].getText();
                if (StringUtils.isEmpty(trainCode))
                    continue;
                
                trainCode = trainCode.toUpperCase();//车次统一转成大写
                String bestSeat = (String)boxBest[i].getSelectedItem();
                String worstSeat = (String)boxWorst[i].getSelectedItem();
                if (!StringUtils.isEmpty(bestSeat) && !StringUtils.isEmpty(worstSeat)) {
                    UserTrainInfo info = new UserTrainInfo(trainCode, bestSeat, worstSeat);
                    list.add(info);
                }
            }
            if (!list.isEmpty()) {
                UserTrainInfo[] array = new UserTrainInfo[list.size()];
                trainData.setUserTrains(list.toArray(array));
            }else{
                trainData.setUserTrains(null);
            }
        }
        
        class TrainTextField extends JTextField {
            
            /**
             * 注释内容
             */
            private static final long serialVersionUID = 5291297639654513239L;
            
            private SeatOptionType oldType = null;
            
            private final int index;
            
            public TrainTextField(int index) {
                this.index = index;
                this.getDocument().addDocumentListener(new DocumentListener() {
                    
                    @Override
                    public void removeUpdate(DocumentEvent e) {
                    }
                    
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        String value = TrainTextField.this.getText().toUpperCase();
                        SeatOptionType newType = SeatOptionType.getType(value.charAt(0));
                        if (newType != null) {
                            if (oldType != newType) {
                                newType.initItem(boxBest[TrainTextField.this.index], null);
                                newType.initItem(boxWorst[TrainTextField.this.index], null);
                                oldType = newType;
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
    
    class FuzzyPanel extends JPanel {
        
        private JFormattedTextField startTime;
        
        private JFormattedTextField endTime;
        
        private JCheckBox gaotieCheck;
        
        private JCheckBox otherCheck;
        
        private JComboBox gaotieBoxBest;
        
        private JComboBox gaotieBoxWorst;
        
        private JComboBox otherBest;
        
        private JComboBox otherWorst;
        
        /**
         * 注释内容
         */
        private static final long serialVersionUID = 1L;
        
        public FuzzyPanel() {
            this.setBounds(10, 50, 380, 180);
            this.setLayout(null);
            this.setBorder(new TitledBorder("第二步：具体设置"));
            
            int heightOneRow = (180 - 20) / 5;
            JLabel label = new JLabel("乘车时间范围");
            label.setBounds(10, 25, 110, 15);
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            this.add(label);
            
            MaskFormatter mf = null;
            try {
                mf = new MaskFormatter("##-##");
            }
            catch (ParseException e1) {
                e1.printStackTrace();
            }
            startTime = new JFormattedTextField(mf);
            startTime.setBounds(130, 21, 60, 21);
            this.add(startTime);
            startTime.setColumns(8);
            
            endTime = new JFormattedTextField(mf);
            endTime.setBounds(195, 21, 60, 21);
            this.add(endTime);
            endTime.setColumns(8);
            
            gaotieBoxBest = new JComboBox();
            gaotieBoxBest.setToolTipText("最希望买到的席别");
            gaotieBoxBest.setBounds(130, 21 + heightOneRow * 1, 100, 21);
            SeatOptionType.GAOTIE.initItem(gaotieBoxBest, null);
            this.add(gaotieBoxBest);
            
            label = new JLabel("--");
            label.setToolTipText("席别范围");
            label.setBounds(235, 25 + heightOneRow * 1, 20, 15);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(label);
            
            gaotieBoxWorst = new JComboBox();
            gaotieBoxWorst.setToolTipText("实在买不到还能接受的席别，两者之间的席别也会进行购买");
            gaotieBoxWorst.setBounds(260, 21 + heightOneRow * 1, 100, 21);
            SeatOptionType.GAOTIE.initItem(gaotieBoxWorst, null);
            this.add(gaotieBoxWorst);
            
        }
    }
    
}
