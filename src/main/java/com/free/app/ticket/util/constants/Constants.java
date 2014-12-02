package com.free.app.ticket.util.constants;

/**
 * 
 * 系统常量类
 * 
 */
public class Constants {
	public static final String JSESSIONID = "JSESSIONID";
	public static final String BIGIPSERVEROTN = "BIGipServerotn";
	public static final String SYSTEM_SIGN_PLACEHOLDER = "&";

	/******************* 座位席别 ****************************/
	public static String BUSS_SEAT = "9"; // 商务座
	public static String BEST_SEAT = "P";// 特等座(余票)
	public static String ONE_SEAT = "M";// 一等座(余票)
	public static String TWO_SEAT = "O";// 二等座(余票)
	public static String VAG_SLEEPER = "6";// 高级软卧(余票)
	public static String SOFT_SLEEPER = "4";// 软卧(余票)
	public static String HARD_SLEEPER = "3";// 硬卧(余票)
	public static String SOFT_SEAT = "2";// 软座(余票)
	public static String HARD_SEAT = "1";// 硬座(余票)

	// 无票状态的2种显示字符
	public static final String SYS_TICKET_SIGN_1 = "--";
	public static final String SYS_TICKET_SIGN_2 = "无";

	// 指定车次
	public static final String SYS_TRAINCODE = "traincode";
	// 指定座位席别
	public static final String SYS_USERSEAT = "settype";

	// 系统日期输入字符
	public static final String SYS_STRING_DATEFORMAT = "-  -";
	// 配置文件中的用户名
	public static final String SYS_USER_INFO = "userinfo";

	/** 截取token正则表达式 **/
	public static final String REX_GET_TOKEN = "var globalRepeatSubmitToken = '(\\w+)'";

	public static final String REX_GET_KEY_CHECK_ISCHANGE = "'key_check_isChange':'(\\w+)'";

	/** 验证码params **/
	public static final String CHECKCODE_PARAMS_RAND = "rand";
	public static final String CHECKCODE_PARAMS_RANDCODE = "randCode";
	public static final String RANDCODE_VALIDATE = "randCode_validate";

	/** 登录params **/
	public static final String LOGIN_PARAMS_USERNAME = "loginUserDTO.user_name";
	public static final String LOGIN_PARAMS_RANDCODE = "randCode";
	public static final String LOGIN_PARAMS_PASSWORD = "userDTO.password";

	public static final String COMMON_JSON_ATT = "_json_att";

	/** 初始化查询params **/
	public static final String TICKETINIT_BACK_TRAIN_DATE = "back_train_date";
	public static final String TICKETINIT_FLAG = "flag";
	public static final String TICKETINIT_FROM_STATION = "leftTicketDTO.from_station";
	public static final String TICKETINIT_FROM_STATION_NAME = "leftTicketDTO.from_station_name";
	public static final String TICKETINIT_TO_STATION = "leftTicketDTO.to_station";
	public static final String TICKETINIT_TO_STATION_NAME = "leftTicketDTO.to_station_name";
	public static final String TICKETINIT_TRAIN_DATE = "leftTicketDTO.train_date";
	public static final String TICKETINIT_PRE_STEP_FLAG = "pre_step_flag";
	public static final String TICKETINIT_PURPOSE_CODE = "purpose_code";
	public static final String TICKETINIT_PURPOSE_CODES = "purpose_codes";

	/** 提交订单 (back_train_date,purpose_codes引用初始化查询) **/
	public static final String SUBMITORDER_QUERY_FROM_STATION_NAME = "query_from_station_name";
	public static final String SUBMITORDER_QUERY_TO_STATION_NAME = "query_to_station_name";
	public static final String SUBMITORDER_QUERY_TO_SECRETSTR = "secretStr";
	public static final String SUBMITORDER_TOUR_FLAG = "tour_flag";
	public static final String SUBMITORDER_TRAIN_DATE = "train_date";
	public static final String SUBMITORDER_UNDEFINED = "undefined";

	/** 检查订单(_json_att,randCode,tour_flag) **/
	public static final String CHECKORDER_BED_LEVEL_ORDER_NUM = "bed_level_order_num";
	public static final String CHECKORDER_CANCEL_FLAG = "cancel_flag";
	public static final String CHECKORDER_OLDPASSENGERSTR = "oldPassengerStr";
	public static final String CHECKORDER_PASSENGERTICKETSTR = "passengerTicketStr";
	public static final String CHECKORDER_REPEAT_SUBMIT_TOKEN = "REPEAT_SUBMIT_TOKEN";

	/** 获取余票数(_json_att,purpose_codes,train_date,REPEAT_SUBMIT_TOKEN) **/
	public static final String QUEUECOUNT_FROMSTATIONTELECODE = "fromStationTelecode";
	public static final String QUEUECOUNT_LEFTTICKET = "leftTicket";
	public static final String QUEUECOUNT_SEATTYPE = "seatType";
	public static final String QUEUECOUNT_STATIONTRAINCODE = "stationTrainCode";
	public static final String QUEUECOUNT_TOSTATIONTELECODE = "toStationTelecode";
	public static final String QUEUECOUNT_TRAIN_NO = "train_no";

	/**
	 * 确认订单(_json_att,oldPassengerStr,passengerTicketStr,purpose_codes,randCode,
	 * REPEAT_SUBMIT_TOKEN)
	 **/
	public static final String CONFIRMSINGLE_KEY_CHECK_ISCHANGE = "key_check_isChange";
	public static final String CONFIRMSINGLE_LEFTTICKETSTR = "leftTicketStr";
	public static final String CONFIRMSINGLE_TRAIN_LOCATION = "train_location";

	/**
	 * 查询订单等待时间(_json_att,REPEAT_SUBMIT_TOKEN)
	 */
	public static final String QUERYWAIT_RANDOM = "random";
	public static final String QUERYWAIT_tourFlag = "tourFlag";

	/** cookie **/
	public static final String _jc_save_fromDate = "_jc_save_fromDate";
	public static final String _jc_save_fromStation = "_jc_save_fromStation";
	public static final String _jc_save_toDate = "_jc_save_toDate";
	public static final String _jc_save_toStation = "_jc_save_toStation";
	public static final String _jc_save_wfdc_flag = "_jc_save_wfdc_flag";

}
