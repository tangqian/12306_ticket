package com.free.app.ticket;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.free.app.ticket.service.HttpClientThreadService;
import com.free.app.ticket.util.ResManager;
import com.free.app.ticket.view.AuthCodeTextField;
import com.free.app.ticket.view.ConsolePane;
import com.free.app.ticket.view.LogPanelManager;
import com.free.app.ticket.view.LoginBtn;

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
	//主窗口
	private static TicketMainFrame frame;

	/************** 登录相关 *********************/


	/************** 联系人 *****************/
	private JTextField linkman1_name;
	private JTextField linkman1_cardNo;
	private JTextField linkman1_mobile;

	private JTextField linkman2_name;
	private JTextField linkman2_cardNo;
	private JTextField linkman2_mobile;

	private JTextField linkman3_name;
	private JTextField linkman3_cardNo;
	private JTextField linkman3_mobile;

	private JTextField linkman4_name;
	private JTextField linkman4_cardNo;
	private JTextField linkman4_mobile;

	private JTextField linkman5_name;
	private JTextField linkman5_cardNo;
	private JTextField linkman5_mobile;

	/************** 配置相关 *****************/
	public JCheckBox boxkTwoSeat;
	public JCheckBox hardSleePer;
	private JFormattedTextField txtStartDate;
	private JTextField formCode;
	private JTextField toCode;

	public JButton startButton;

	/************* 输出相关 ****************/
	//private static JScrollPane scrollPaneLogger;
    private static ConsolePane consolePane;
    
    /**
     * 是否已初始化，即已经获取过登录验证码，在点击登录时需要先判断这个条件
     */
    public static boolean isInited = false;

	/************** 业务逻辑相关的变量 ****************/
	/*// 存放用户信息
	public List<UserInfo> userInfoList = null;
	// 存放查询火车实体
	private TicketSearch req;
	// 项目根路径
	public static String path;
	// 类本身
	public TicketMainFrame mainWin = this;
	// 登录验证码路径
	public String loginUrl;
	// 提交订单验证码路径
	public String submitUrl;
	// 是否登录成功
	public boolean isLogin = false;
	// 线程是否运行
	public boolean isRunThread = false;
	// 是否点击了停止按钮
	public boolean isStopRun = false;*/
	/*// client
	public ClientCore client = new ClientCore();*/

	// 静态构造块
	/*static {
		// 获取当前jar包的目录
		path = System.getProperty("user.dir") + File.separator;
		logger.debug("mainWin path = " + path);
	}*/
	
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
		setTitle(ResManager.getText("ticket.mainframe.title"));
		setIconImage(ResManager.createImageIcon("logo.jpg").getImage());
		setBounds(50, 50, 670, 640);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		ToolTipManager.sharedInstance().setInitialDelay(0);
		getContentPane().setLayout(null);
		// 关闭窗口 保存相关用户信息
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// 保存用户实体
				/*try {
					ToolHelper.getUserInfo(path, "data" + File.separator + "UI.dat", username, password, linkman1_name, linkman1_cardNo, linkman1_mobile, linkman2_name, linkman2_cardNo,
							linkman2_mobile, linkman3_name, linkman3_cardNo, linkman3_mobile, linkman4_name, linkman4_cardNo, linkman4_mobile, linkman5_name, linkman5_cardNo, linkman5_mobile,
							formCode, toCode);
				} catch (Exception ex) {
					logger.error("save user data file Failure!", ex);
				}*/
			}
		});

		/****************** 登录控件相关 **********************/
		LogPanelManager.init(frame);

		/****************** 联系人相关 **********************/
		JPanel panel2 = new JPanel();
		panel2.setBounds(10, 76, 650, 315);
		getContentPane().add(panel2);
		panel2.setLayout(null);
		panel2.setBorder(new TitledBorder(ResManager.getText("ticket.panel.linkmaninfo")));

		JPanel panel3 = new JPanel();
		panel3.setBounds(20, 20, 610, 54);
		panel2.add(panel3);
		panel3.setLayout(null);
		panel3.setBorder(new TitledBorder(ResManager.getText("ticket.panel.linkman1")));

		JLabel label_2 = new JLabel(ResManager.getText("ticket.label.username"));
		label_2.setBounds(55, 26, 30, 15);
		panel3.add(label_2);
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);

		linkman1_name = new JTextField();
		linkman1_name.setName("linkman1_name");
		linkman1_name.setToolTipText(ResManager.getText("ticket.label.username"));
		linkman1_name.setBounds(95, 23, 40, 21);
		panel3.add(linkman1_name);
		linkman1_name.setColumns(10);

		JLabel label_3 = new JLabel(ResManager.getText("ticket.label.cardno"));
		label_3.setBounds(155, 26, 50, 15);
		panel3.add(label_3);
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);

		linkman1_cardNo = new JTextField();
		linkman1_cardNo.setName("linkman1_cardNo");
		linkman1_cardNo.setToolTipText(ResManager.getText("ticket.label.cardno"));
		linkman1_cardNo.setBounds(215, 23, 150, 21);
		panel3.add(linkman1_cardNo);
		linkman1_cardNo.setColumns(10);

		JLabel label_4 = new JLabel(ResManager.getText("ticket.label.mobilephone"));
		label_4.setBounds(375, 26, 40, 15);
		panel3.add(label_4);
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);

		linkman1_mobile = new JTextField();
		linkman1_mobile.setName("linkman1_mobile");
		linkman1_mobile.setToolTipText(ResManager.getText("ticket.label.mobilephone"));
		linkman1_mobile.setBounds(425, 23, 100, 21);
		panel3.add(linkman1_mobile);
		linkman1_mobile.setColumns(10);

		JPanel panel4 = new JPanel();
		panel4.setBounds(20, 80, 610, 54);
		panel2.add(panel4);
		panel4.setLayout(null);
		panel4.setBorder(new TitledBorder(ResManager.getText("ticket.panel.linkman2")));

		JLabel label_5 = new JLabel(ResManager.getText("ticket.label.username"));
		label_5.setBounds(55, 26, 30, 15);
		panel4.add(label_5);
		label_5.setHorizontalAlignment(SwingConstants.RIGHT);

		linkman2_name = new JTextField();
		linkman2_name.setName("linkman2_name");
		linkman2_name.setToolTipText(ResManager.getText("ticket.label.username"));
		linkman2_name.setBounds(95, 23, 40, 21);
		panel4.add(linkman2_name);
		linkman2_name.setColumns(10);

		JLabel label_6 = new JLabel(ResManager.getText("ticket.label.cardno"));
		label_6.setBounds(155, 26, 50, 15);
		panel4.add(label_6);
		label_6.setHorizontalAlignment(SwingConstants.RIGHT);

		linkman2_cardNo = new JTextField();
		linkman2_cardNo.setName("linkman2_cardNo");
		linkman2_cardNo.setToolTipText(ResManager.getText("ticket.label.cardno"));
		linkman2_cardNo.setBounds(215, 23, 150, 21);
		panel4.add(linkman2_cardNo);
		linkman2_cardNo.setColumns(10);

		JLabel label_7 = new JLabel(ResManager.getText("ticket.label.mobilephone"));
		label_7.setBounds(375, 26, 40, 15);
		panel4.add(label_7);
		label_7.setHorizontalAlignment(SwingConstants.RIGHT);

		linkman2_mobile = new JTextField();
		linkman2_mobile.setName("linkman2_mobile");
		linkman2_mobile.setToolTipText(ResManager.getText("ticket.label.mobilephone"));
		linkman2_mobile.setBounds(425, 23, 100, 21);
		panel4.add(linkman2_mobile);
		linkman2_mobile.setColumns(10);

		JPanel panel5 = new JPanel();
		panel5.setBounds(20, 140, 610, 54);
		panel2.add(panel5);
		panel5.setLayout(null);
		panel5.setBorder(new TitledBorder(ResManager.getText("ticket.panel.linkman3")));

		JLabel label_8 = new JLabel(ResManager.getText("ticket.label.username"));
		label_8.setBounds(55, 26, 30, 15);
		panel5.add(label_8);
		label_8.setHorizontalAlignment(SwingConstants.RIGHT);

		linkman3_name = new JTextField();
		linkman3_name.setName("linkman3_name");
		linkman3_name.setToolTipText(ResManager.getText("ticket.label.username"));
		linkman3_name.setBounds(95, 23, 40, 21);
		panel5.add(linkman3_name);
		linkman3_name.setColumns(10);

		JLabel label_9 = new JLabel(ResManager.getText("ticket.label.cardno"));
		label_9.setBounds(155, 26, 50, 15);
		panel5.add(label_9);
		label_9.setHorizontalAlignment(SwingConstants.RIGHT);

		linkman3_cardNo = new JTextField();
		linkman3_cardNo.setName("linkman3_cardNo");
		linkman3_cardNo.setToolTipText(ResManager.getText("ticket.label.cardno"));
		linkman3_cardNo.setBounds(215, 23, 150, 21);
		panel5.add(linkman3_cardNo);
		linkman3_cardNo.setColumns(10);

		JLabel label_10 = new JLabel(ResManager.getText("ticket.label.mobilephone"));
		label_10.setBounds(375, 26, 40, 15);
		panel5.add(label_10);
		label_10.setHorizontalAlignment(SwingConstants.RIGHT);

		linkman3_mobile = new JTextField();
		linkman3_mobile.setName("linkman3_mobile");
		linkman3_mobile.setToolTipText(ResManager.getText("ticket.label.mobilephone"));
		linkman3_mobile.setBounds(425, 23, 100, 21);
		panel5.add(linkman3_mobile);
		linkman3_mobile.setColumns(10);

		JPanel panel7 = new JPanel();
		panel7.setLayout(null);
		panel7.setBorder(new TitledBorder(ResManager.getText("ticket.panel.linkman4")));
		panel7.setBounds(20, 193, 610, 54);
		panel2.add(panel7);

		JLabel label = new JLabel(ResManager.getText("ticket.label.username"));
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(55, 26, 30, 15);
		panel7.add(label);

		linkman4_name = new JTextField();
		linkman4_name.setToolTipText(ResManager.getText("ticket.label.username"));
		linkman4_name.setName("linkman4_name");
		linkman4_name.setColumns(10);
		linkman4_name.setBounds(95, 23, 40, 21);
		panel7.add(linkman4_name);

		JLabel label_1 = new JLabel(ResManager.getText("ticket.label.cardno"));
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(155, 26, 50, 15);
		panel7.add(label_1);

		linkman4_cardNo = new JTextField();
		linkman4_cardNo.setToolTipText(ResManager.getText("ticket.label.cardno"));
		linkman4_cardNo.setName("linkman4_cardNo");
		linkman4_cardNo.setColumns(10);
		linkman4_cardNo.setBounds(215, 23, 150, 21);
		panel7.add(linkman4_cardNo);

		JLabel label_11 = new JLabel(ResManager.getText("ticket.label.mobilephone"));
		label_11.setHorizontalAlignment(SwingConstants.RIGHT);
		label_11.setBounds(375, 26, 40, 15);
		panel7.add(label_11);

		linkman4_mobile = new JTextField();
		linkman4_mobile.setName("linkman4_mobile");
		linkman4_mobile.setBounds(425, 23, 100, 21);
		linkman4_mobile.setToolTipText(ResManager.getText("ticket.label.mobilephone"));
		linkman4_mobile.setColumns(10);
		panel7.add(linkman4_mobile);

		JPanel panel8 = new JPanel();
		panel8.setLayout(null);
		panel8.setBorder(new TitledBorder(ResManager.getText("ticket.panel.linkman5")));
		panel8.setBounds(20, 251, 610, 54);
		panel2.add(panel8);

		JLabel label_12 = new JLabel(ResManager.getText("ticket.label.username"));
		label_12.setHorizontalAlignment(SwingConstants.RIGHT);
		label_12.setBounds(55, 26, 30, 15);
		panel8.add(label_12);

		linkman5_name = new JTextField();
		linkman5_name.setToolTipText(ResManager.getText("ticket.label.username"));
		linkman5_name.setName("linkman5_name");
		linkman5_name.setColumns(10);
		linkman5_name.setBounds(95, 23, 40, 21);
		panel8.add(linkman5_name);

		JLabel label_13 = new JLabel(ResManager.getText("ticket.label.cardno"));
		label_13.setHorizontalAlignment(SwingConstants.RIGHT);
		label_13.setBounds(155, 26, 50, 15);
		panel8.add(label_13);

		linkman5_cardNo = new JTextField();
		linkman5_cardNo.setToolTipText(ResManager.getText("ticket.label.cardno"));
		linkman5_cardNo.setName("linkman5_cardNo");
		linkman5_cardNo.setColumns(10);
		linkman5_cardNo.setBounds(215, 23, 150, 21);
		panel8.add(linkman5_cardNo);

		JLabel label_14 = new JLabel(ResManager.getText("ticket.label.mobilephone"));
		label_14.setHorizontalAlignment(SwingConstants.RIGHT);
		label_14.setBounds(375, 26, 40, 15);
		panel8.add(label_14);

		linkman5_mobile = new JTextField();
		linkman5_mobile.setToolTipText(ResManager.getText("ticket.label.mobilephone"));
		linkman5_mobile.setName("linkman5_mobile");
		linkman5_mobile.setColumns(10);
		linkman5_mobile.setBounds(425, 23, 100, 21);
		panel8.add(linkman5_mobile);

		/****************** 配置相关 **********************/
		JPanel panel6 = new JPanel();
		panel6.setBounds(10, 394, 650, 63);
		getContentPane().add(panel6);
		panel6.setLayout(null);
		panel6.setBorder(new TitledBorder(ResManager.getText("ticket.panel.configuration")));

		boxkTwoSeat = new JCheckBox(ResManager.getText("ticket.label.boxkTwoSeat"));
		boxkTwoSeat.setBounds(82, 29, 74, 15);
		panel6.add(boxkTwoSeat);
		boxkTwoSeat.setHorizontalAlignment(SwingConstants.RIGHT);

		hardSleePer = new JCheckBox(ResManager.getText("ticket.label.hardSleePer"));
		hardSleePer.setBounds(6, 29, 74, 15);
		panel6.add(hardSleePer);
		hardSleePer.setHorizontalAlignment(SwingConstants.RIGHT);

		JLabel label_15 = new JLabel(ResManager.getText("ticket.label.txtStartDate"));
		label_15.setBounds(162, 30, 60, 13);
		panel6.add(label_15);
		label_15.setHorizontalAlignment(SwingConstants.RIGHT);

		MaskFormatter mf = null;
		try {
			mf = new MaskFormatter("####-##-##");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		txtStartDate = new JFormattedTextField(mf);
		txtStartDate.setToolTipText(ResManager.getText("ticket.label.txtStartDate"));
		txtStartDate.setBounds(225, 26, 84, 21);
		panel6.add(txtStartDate);
		txtStartDate.setColumns(10);

		JLabel label_16 = new JLabel(ResManager.getText("ticket.label.formCode"));
		label_16.setBounds(307, 26, 40, 17);
		panel6.add(label_16);
		label_16.setHorizontalAlignment(SwingConstants.RIGHT);

		formCode = new JTextField();
		formCode.setToolTipText(ResManager.getText("ticket.label.formCode"));
		formCode.setName("formCode");
		formCode.setBounds(357, 26, 60, 21);
		panel6.add(formCode);
		formCode.setColumns(10);

		JLabel label_17 = new JLabel(ResManager.getText("ticket.label.toCode"));
		label_17.setBounds(405, 26, 60, 17);
		panel6.add(label_17);
		label_17.setHorizontalAlignment(SwingConstants.RIGHT);

		toCode = new JTextField();
		toCode.setToolTipText(ResManager.getText("ticket.label.toCode"));
		toCode.setName("toCode");
		toCode.setBounds(475, 26, 60, 21);
		panel6.add(toCode);
		toCode.setColumns(10);

		startButton = new JButton();
		startButton.setText(ResManager.getText("ticket.btn.start"));
		startButton.setBounds(559, 22, 70, 28);
		panel6.add(startButton);
		startButton.addActionListener(new StartButton());

		/****************** 信息输出相关 **********************/
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 460, 640, 145);
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(scrollPane);

		/*consolePane = new JTextArea();
		scrollPane.setViewportView(consoleArea);
		DefaultCaret caret = (DefaultCaret) consoleArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		consolePane.setText(ResManager.getText("ticket.textarea.messageOut"));
		consolePane.setEditable(false);
		consolePane.setLineWrap(true);*/
		
		consolePane = new ConsolePane();
		scrollPane.setViewportView(consolePane);
		consolePane.appendNorm(ResManager.getText("ticket.textarea.messageOut"));
		consolePane.setEditable(false);
	}

	// 开始按钮onclick事件监听
	class StartButton implements ActionListener {
		@SuppressWarnings("rawtypes")
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
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
				List<String> msglist = ToolHelper.validateWidget(txtStartDate, formCode, toCode);
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
	}
	
    /**
     * 追加显示主窗口追踪信息
     * @param message
     */
    public static synchronized void trace(String message) {
        consolePane.appendNorm(message);
    }
    
    public static synchronized void remind(String message) {
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

	/**
	 * 获取乘车人
	 * 
	 * @return List<UserInfo>
	 */
	/*public List<UserInfo> getUserInfo() {
		List<UserInfo> list = new ArrayList<UserInfo>();
		if (StringHelper.isEmptyString(linkman1_cardNo.getText().trim()) || !StringHelper.isEmptyString(linkman1_name.getText().trim())) {
			if (!StringHelper.isEmptyString(linkman1_mobile.getText().trim())) {
				UserInfo userInfo1 = new UserInfo(linkman1_cardNo.getText().trim(), linkman1_name.getText().trim(), linkman1_mobile.getText().trim());
				list.add(userInfo1);
			} else {
				UserInfo userInfo1 = new UserInfo(linkman1_cardNo.getText().trim(), linkman1_name.getText().trim());
				list.add(userInfo1);
			}
		}
		if (!StringHelper.isEmptyString(linkman2_cardNo.getText().trim()) || !StringHelper.isEmptyString(linkman2_name.getText().trim())) {
			if (!StringHelper.isEmptyString(linkman2_mobile.getText().trim())) {
				UserInfo userInfo2 = new UserInfo(linkman2_cardNo.getText().trim(), linkman2_name.getText().trim(), linkman2_mobile.getText().trim());
				list.add(userInfo2);
			} else {
				UserInfo userInfo2 = new UserInfo(linkman2_cardNo.getText().trim(), linkman2_name.getText().trim());
				list.add(userInfo2);
			}
		}
		if (!StringHelper.isEmptyString(linkman3_cardNo.getText().trim()) || !StringHelper.isEmptyString(linkman3_name.getText().trim())) {
			if (!StringHelper.isEmptyString(linkman3_name.getText().trim())) {
				UserInfo userInfo3 = new UserInfo(linkman3_cardNo.getText().trim(), linkman3_name.getText().trim(), linkman3_mobile.getText().trim());
				list.add(userInfo3);
			} else {
				UserInfo userInfo3 = new UserInfo(linkman3_cardNo.getText().trim(), linkman3_name.getText().trim());
				list.add(userInfo3);
			}
		}
		if (!StringHelper.isEmptyString(linkman4_cardNo.getText().trim()) || !StringHelper.isEmptyString(linkman4_name.getText().trim())) {
			if (!StringHelper.isEmptyString(linkman4_name.getText().trim())) {
				UserInfo userInfo4 = new UserInfo(linkman4_cardNo.getText().trim(), linkman4_name.getText().trim(), linkman4_mobile.getText().trim());
				list.add(userInfo4);
			} else {
				UserInfo userInfo4 = new UserInfo(linkman4_cardNo.getText().trim(), linkman4_name.getText().trim());
				list.add(userInfo4);
			}
		}
		if (!StringHelper.isEmptyString(linkman5_cardNo.getText().trim()) || !StringHelper.isEmptyString(linkman5_name.getText().trim())) {
			if (!StringHelper.isEmptyString(linkman5_name.getText().trim())) {
				UserInfo userInfo5 = new UserInfo(linkman5_cardNo.getText().trim(), linkman5_name.getText().trim(), linkman5_mobile.getText().trim());
				list.add(userInfo5);
			} else {
				UserInfo userInfo5 = new UserInfo(linkman5_cardNo.getText().trim(), linkman5_name.getText().trim());
				list.add(userInfo5);
			}
		}
		return list;
	}*/

	/**
	 * 获取列车查询
	 * 
	 * @return
	 */
	/*private TicketSearch getOrderRequest() {
		req = new TicketSearch();
		req.setFrom_station_name(formCode.getText().trim());
		req.setTo_station_name(toCode.getText().trim());
		req.setTrain_date(txtStartDate.getText().trim());
		req.setTo_date(DateHelper.getCurDate());
		return req;
	}*/
}