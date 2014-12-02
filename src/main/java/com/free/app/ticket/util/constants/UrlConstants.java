package com.free.app.ticket.util.constants;

/**
 * 
 * 请求url常量类
 * 
 */
public class UrlConstants {
	// 获取cookieurl
	public final static String REQ_LOGININIT_URL = "https://kyfw.12306.cn/otn/login/init";//https://kyfw.12306.cn/otn/
	// 获取登录验证码url
	public final static String REQ_GETLOGINPASSCODE_URL = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=login&rand=sjrand";
	// 获取登录验证码url
	public final static String REQ_GETSUBPASSCODE_URL = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=passenger&rand=randp";
	// 检查验证码url
	public final static String REQ_CHECKCODE_URL = "https://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn";
	// 登录验证url
	public final static String REQ_LOGINAYSNSUGGEST_URL = "https://kyfw.12306.cn/otn/login/loginAysnSuggest";
	// 登录url
	public final static String REQ_LOGIN_URL = "https://kyfw.12306.cn/otn/login/userLogin";
	// 查询余票initurl
	public final static String REQ_TIKETINIT_URL = "https://kyfw.12306.cn/otn/leftTicket/init";
	// 查询余票url
	public final static String REQ_TIKETSEARCH_URL = "https://kyfw.12306.cn/otn/leftTicket/query";
	// 提交车票url
	public final static String REQ_SUBMITORDER_URL = "https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest";
	// 获取tokenurl
	public final static String REQ_INITDC_URL = "https://kyfw.12306.cn/otn/confirmPassenger/initDc";
	// 检查订单url
	public final static String REQ_CHECKORDER_URL = "https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo";
	// 查询余票
	public final static String REQ_QUEUECOUNT_URL = "https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount";
	// 提交订单url
	public final static String REQ_CONFIRMSINGLE_URL = "https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue";
	// 查询等待时间url
	public final static String REQ_QUERYORDERWAIT_URL = "https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime";

	// 登录请求相关ref
	public final static String REF_LOGINPASSCODE_URL = "https://kyfw.12306.cn/otn/login/init";
	// 查询余票init相关ref
	public final static String REF_INITTICKET_URL = "https://kyfw.12306.cn/otn/index/init";
	// 查询余票相关ref
	public final static String REF_TICKET_URL = "https://kyfw.12306.cn/otn/leftTicket/init";
}
