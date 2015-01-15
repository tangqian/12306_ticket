package com.free.app.ticket.util.constants;

public class UrlConstants {
    public final static String GET_BASE_URL = "https://kyfw.12306.cn/otn/";
    // 登录随机参数url
    public final static String GET_LOGIN_INIT_URL = "https://kyfw.12306.cn/otn/login/init";
    // 登录验证码url
    public final static String GET_LOGIN_PASSCODE_URL = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=login&rand=sjrand";
    // 动态js文件url
    public final static String GET_DYNAMIC_JS_URL = "https://kyfw.12306.cn/otn/dynamicJs/";
    // 提交点单验证码url
    public final static String GET_ORDER_PASSCODE_URL = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=passenger&rand=randp";
    // 检查验证码url
    public final static String REQ_CHECK_CODE_URL = "https://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn";
    // 登录验证url
    public final static String REQ_LOGIN_AYSN_SUGGEST_URL = "https://kyfw.12306.cn/otn/login/loginAysnSuggest";
    // 登录url
    public final static String REQ_LOGIN_URL = "https://kyfw.12306.cn/otn/login/userLogin";
    // 初始化我的12306url
    public final static String GET_INIT_MY_12306_URL = "https://kyfw.12306.cn/otn/index/initMy12306";
    // 提交订单随机参数url
    public final static String GET_LEFT_TICKET_INIT_URL = "https://kyfw.12306.cn/otn/leftTicket/init";
    // 余票查询url
    public final static String REQ_TICKET_QUERY_URL = "https://kyfw.12306.cn/otn/leftTicket/queryT";
    public final static String REQ_TICKET_QUERY_URL2 = "https://kyfw.12306.cn/otn/leftTicket/query";
    // 查询日志url
    public final static String GET_TICKET_QUERY_LOG_URL = "https://kyfw.12306.cn/otn/leftTicket/log";
    // 提交订单url
    public final static String REQ_SUBMIT_ORDER_URL = "https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest";
    // 提交订单凭证url
    public final static String REQ_INITDC_URL = "https://kyfw.12306.cn/otn/confirmPassenger/initDc";
    // 检查订单url
    public final static String REQ_CHECK_ORDER_URL = "https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo";
    // 排队情况
    public final static String REQ_QUEUE_COUNT_URL = "https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount";
    // 确认订单url
    public final static String REQ_CONFIRM_SINGLE_URL = "https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue";
    // 排队等待时间url
    public final static String GET_QUERY_ORDER_WAIT_URL = "https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime";

    // 首页url
    public final static String REF_INDEX_INIT_URL = "https://kyfw.12306.cn/otn/index/init";
    
    public static final String REQ_PASSENGERS_INIT_URL = "https://kyfw.12306.cn/otn/passengers/init";
    
    // 联系人
    public static final String REQ_PASSENGERS_QUERY_URL = "https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs";
    
    // 检查是否登录
    public static final String REQ_CHECK_USER_URL = "https://kyfw.12306.cn/otn/login/checkUser";
    
    // 登出
    public static final String GET_LOGOUT_URL = "https://kyfw.12306.cn/otn/login/loginOut";
    
    public final static String FILE_LOGIN_CAPTCHA_URL = "captcha_login.jpg";
    public final static String FILE_SUBMIT_CAPTCHA_URL = "captcha_submit.jpg";
}
