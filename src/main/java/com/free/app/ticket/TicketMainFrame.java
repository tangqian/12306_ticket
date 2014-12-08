package com.free.app.ticket;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.free.app.ticket.model.MixData4UI;
import com.free.app.ticket.service.HttpClientThreadService;
import com.free.app.ticket.util.DateUtils;
import com.free.app.ticket.util.ResManager;
import com.free.app.ticket.view.ConfigPanelManager;
import com.free.app.ticket.view.ConsolePane;
import com.free.app.ticket.view.LoginPanelManager;
import com.free.app.ticket.view.PassengerPanelManager;
import com.free.app.ticket.view.RefreshPanelManager;

/**
 * 主窗口类
 * 
 */
public class TicketMainFrame extends JFrame {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4187140706289820520L;
    
    /**************** 日志对象 ****************/
    private static final Logger logger = LoggerFactory.getLogger(TicketMainFrame.class);
    
    // 主窗口
    public static TicketMainFrame frame;
    
    /************** 登录相关 *********************/
    
    /************** 联系人 *****************/
    
    /************** 配置相关 *****************/
    public JCheckBox boxkTwoSeat;
    
    public JCheckBox hardSleePer;
    
    private JFormattedTextField txtStartDate;
    
    private JTextField formCode;
    
    private JTextField toCode;
    
    public JButton startButton;
    
    /************* 输出相关 ****************/
    // private static JScrollPane scrollPaneLogger;
    private static ConsolePane consolePane;
    
    /**
     * 是否已初始化，即已经获取过登录验证码，在点击登录时需要先判断这个条件
     */
    public static boolean isInited = false;
    
    /**
     * 是否停止刷票
     */
    public static boolean isStop = false;
    
    /************** 业务逻辑相关的变量 ****************/
    /*
     * // 存放用户信息 public List<UserInfo> userInfoList = null; // 存放查询火车实体 private
     * TicketSearch req; // 项目根路径 public static String path; // 类本身 public
     * TicketMainFrame mainWin = this; // 登录验证码路径 public String loginUrl; //
     * 提交订单验证码路径 public String submitUrl; // 是否登录成功 public boolean isLogin =
     * false; // 线程是否运行 public boolean isRunThread = false; // 是否点击了停止按钮 public
     * boolean isStopRun = false;
     */
    /*
     * // client public ClientCore client = new ClientCore();
     */

    // 静态构造块
    /*
     * static { // 获取当前jar包的目录 path = System.getProperty("user.dir") +
     * File.separator; logger.debug("mainWin path = " + path); }
     */

    public static void main(String[] arg0) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    frame = new TicketMainFrame();
                    frame.setVisible(true);
                }
                catch (Exception e) {
                    logger.error("init mainframe error : ", e);
                }
            }
        });
    }
    
    public TicketMainFrame() {
        initView();
        new HttpClientThreadService().start();
    }
    
    /**
     * 初始化界面
     */
    public void initView() {
        setTitle("2015，我要回家");
        setIconImage(ResManager.createImageIcon("logo.jpg").getImage());
        setBounds(50, 50, 800, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        ToolTipManager.sharedInstance().setInitialDelay(0);
        getContentPane().setLayout(null);
        final String filePath = System.getProperty("user.dir") + File.separator + "12306.dat";
        // 关闭窗口 保存相关用户信息
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // 保存用户实体
                try {
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
                    out.writeObject(frame.bindUItoModel());
                    out.close();
                    logger.debug("Saved UI data to file: {}", filePath);
                }
                catch (Exception ex) {
                    logger.error("save UI data file Failure!", ex);
                }
            }
            
            @Override
            public void windowOpened(WindowEvent event) {
                ObjectInputStream in = null;
                try {
                    // 基于上次保存的dat数据文件恢复UI组件初始值
                    File inFile = new File(filePath);
                    MixData4UI mixData;
                    if (!inFile.exists()) {
                        mixData = new MixData4UI();
                    }
                    else {
                        in = new ObjectInputStream(new FileInputStream(inFile));
                        mixData = (MixData4UI)in.readObject();
                    }
                    ConfigPanelManager.bindModeltoUI(mixData);
                    LoginPanelManager.bindModeltoUI(mixData);
                    PassengerPanelManager.bindModeltoUI(mixData);
                    logger.debug("Loaded UI data from file: {}", filePath);
                }
                catch (Exception e) {
                    logger.error("Load UI data from file error", e);
                }
                finally{
                    if(in != null){
                        try {
                            in.close();
                        }
                        catch (IOException e2) {
                        }
                    }
                }
            }
        });
        
        /****************** 配置相关 **********************/
        ConfigPanelManager.init(this);
        
        /****************** 登录控件相关 **********************/
        LoginPanelManager.init(this);
        
        /****************** 联系人相关 **********************/
        PassengerPanelManager.init(this);
        
        /****************** 刷票控制面板 **********************/
        RefreshPanelManager.init(this);
        
        /****************** 信息输出相关 **********************/
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 420, 780, 245);
        scrollPane.setBorder(new TitledBorder("运行记录"));
        scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().add(scrollPane);
        
        /*
         * consolePane = new JTextArea();
         * scrollPane.setViewportView(consoleArea); DefaultCaret caret =
         * (DefaultCaret) consoleArea.getCaret();
         * caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
         * consolePane.setText
         * (ResManager.getText("ticket.textarea.messageOut"));
         * consolePane.setEditable(false); consolePane.setLineWrap(true);
         */

        consolePane = new ConsolePane();
        scrollPane.setViewportView(consolePane);
        consolePane.setEditable(false);
    }
    
    private MixData4UI bindUItoModel() {
        MixData4UI mixData = new MixData4UI();
        ConfigPanelManager.bindUItoModel(mixData);
        LoginPanelManager.bindUItoModel(mixData);
        PassengerPanelManager.bindUItoModel(mixData);
        return mixData;
    }
    
    // 开始按钮onclick事件监听
    
    /**
     * 追加显示主窗口追踪信息
     * 
     * @param message
     */
    public static synchronized void trace(String message) {
        message = DateUtils.formatDate(new Date(), "HH:mm:ss.SSS ") + message;
        consolePane.appendNorm(message);
    }
    
    public static synchronized void remind(String message) {
        message = DateUtils.formatDate(new Date(), "HH:mm:ss.SSS ") + message;
        consolePane.appendRed(message);
    }
    
    /**
     * 显示警告消息
     * 
     * @param msg
     */
    public static void alert(String msg) {
        JOptionPane.showMessageDialog(frame, msg);
    }
    
}