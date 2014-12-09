package com.free.app.ticket.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.model.TicketBuyInfo;
import com.free.app.ticket.model.TicketConfigInfo;
import com.free.app.ticket.model.TrainInfo;
import com.free.app.ticket.util.DateUtils;
import com.free.app.ticket.util.TicketHttpClient;
import com.free.app.ticket.view.CheckCode4OrderDialog;
import com.free.app.ticket.view.CheckCode4OrderDialog.CancelType;

public class AutoBuyThreadService extends Thread {
    
    private static final Logger logger = LoggerFactory.getLogger(AutoBuyThreadService.class);
    
    private TicketBuyInfo buyInfo;
    
    private boolean isStop = false;
    
    private Map<String, String> cookies;
    
    private static final Pattern PATTERN_TOKEN = Pattern.compile("var globalRepeatSubmitToken = '(\\w+)'");
    
    private static final Pattern PATTERN_KEY_CHECK = Pattern.compile("'key_check_isChange':'(\\w+)'");
    
    public AutoBuyThreadService(TicketBuyInfo ticketBuyInfo) {
        this.buyInfo = ticketBuyInfo;
        cookies = getCookie(ticketBuyInfo.getConfigInfo());
    }
    
    @Override
    public void run() {
        int queryCount = 1;
        while (!isStop && !TicketMainFrame.isStop) {
            TicketHttpClient client = HttpClientThreadService.getHttpClient();
            TicketMainFrame.trace("");
            TicketMainFrame.trace("第" + queryCount + "次余票查询");
            List<TrainInfo> trainInfos = client.queryLeftTicket(buyInfo.getConfigInfo(), cookies);
            queryCount++;
            if (trainInfos == null) {
                TicketMainFrame.trace("查询不到余票信息");
                continue;
            }
            
            List<TrainInfo> canBuyTrain = new ArrayList<TrainInfo>();
            List<TrainInfo> notBuyTrain = new ArrayList<TrainInfo>();
            for (TrainInfo train : trainInfos) {
                if (train.getCanWebBuy().equals("Y")) {
                    canBuyTrain.add(train);
                }
                else {
                    notBuyTrain.add(train);
                }
            }
            
            showNotBuyTrains(notBuyTrain);
            showCanBuyTrains(canBuyTrain);
            if (canBuyTrain.isEmpty()) {//全部车次卖完或未开售
                continue;
            }
            
            List<TrainInfo> userPerfers = getUserPerfer(canBuyTrain);
            if (userPerfers.isEmpty()) {
                TicketMainFrame.remind("您中意的车次已售完或未开售，请考虑上述其它车次(先停止刷票，再点车次设置按钮进行修改)");
                continue;
            }
            
            isStop = circularSubmitOrder(userPerfers);
            isStop = true;
        }
        
    }
    
    private boolean circularSubmitOrder(List<TrainInfo> userPerfers) {
        boolean result = false;
        
        TicketHttpClient client = HttpClientThreadService.getHttpClient();
        
        Set<String> failTrainSet = new HashSet<String>();//预订失败列车
        for (TrainInfo trainInfo : userPerfers) {
            boolean submitResult = client.submitOrderRequest(buyInfo.getConfigInfo(), cookies, trainInfo);
            if (!submitResult) {
                failTrainSet.add(trainInfo.getStation_train_code());
                TicketMainFrame.remind("车次[" + trainInfo.getStation_train_code() + "]进入预订页面前检查失败");
                continue;
            }
            OrderToken token = queryOrderToken();
            if (token == null) {
                failTrainSet.add(trainInfo.getStation_train_code());
                TicketMainFrame.remind("车次[" + trainInfo.getStation_train_code() + "]获取预订页面失败");
                continue;
            }
            
            TicketMainFrame.trace("车次[" + trainInfo.getStation_train_code() + "]进入订单提交确认页");
            CancelType cancelType = CheckCode4OrderDialog.showDialog(TicketMainFrame.frame, buyInfo, token, cookies);
            if (cancelType == CancelType.ALLAFTER) {
                isStop = true;
                TicketMainFrame.remind("您取消了全部列车的预订");
                break;
            }
            else if (cancelType == CancelType.ONLYTHIS || cancelType == CancelType.DEFAULT) {//取消预订当前
                TicketMainFrame.remind("您取消了当前车次[" + trainInfo.getStation_train_code() + "]的预订");
                continue;
            }
            else if (cancelType == CancelType.SUCCESS) {
                new TrianQueueThreadService(trainInfo, token, cookies).start();
            }
        }
        
        return result;
    }
    
    private OrderToken queryOrderToken() {
        OrderToken token = null;
        
        TicketHttpClient client = HttpClientThreadService.getHttpClient();
        
        int sum = 0;
        while (token == null && sum < 3) {
            try {
                String msg = client.getInitDcPage(cookies);
                String token_str = null;
                String key_check_isChange_str = null;
                
                Matcher m_token = PATTERN_TOKEN.matcher(msg);
                Matcher m_key_check_isChange = PATTERN_KEY_CHECK.matcher(msg);
                if (m_token.find()) {
                    token_str = m_token.group(1);
                }
                if (m_key_check_isChange.find()) {
                    key_check_isChange_str = m_key_check_isChange.group(1);
                }
                if (token_str != null && key_check_isChange_str != null) {
                    token = new OrderToken(token_str, key_check_isChange_str);
                }
                else {
                    logger.error("initDc get token fail for unknow reason, check it!");
                }
            }
            catch (Exception e) {
                logger.error("initDC page error : ", e);
            }
            sum++;
        }
        
        return token;
    }
    
    /**
     * <获取用户优选车次列表>
     * @param all
     * @return
     */
    private List<TrainInfo> getUserPerfer(List<TrainInfo> all) {
        List<TrainInfo> perfers = new ArrayList<TrainInfo>();
        for (TrainInfo trainInfo : all) {
            perfers.add(trainInfo);
        }
        return perfers;
    }
    
    /**
     * <向控制台打印可买车次>
     * @param trains
     */
    private void showCanBuyTrains(List<TrainInfo> trains) {
        if (trains.isEmpty()) {
            TicketMainFrame.remind("全部车票已售完或未开售");
            return;
        }
        
        TicketMainFrame.trace("-----下列车次可买，余票详情如下------");
        String line = "";
        for (TrainInfo train : trains) {
            line = train.getStation_train_code() + ":" + train.getLeftTicketInfo();
            TicketMainFrame.trace(line);
        }
    }
    
    /**
     * <向控制台打印不可买车次>
     * @param trains
     */
    private void showNotBuyTrains(List<TrainInfo> trains) {
        if (trains.isEmpty())
            return;
        
        String msg = "";
        for (TrainInfo train : trains) {
            msg += train.getStation_train_code() + ",";
        }
        msg = msg.substring(0, msg.length() - 1);
        TicketMainFrame.trace("已售完或未开售车次有【" + msg + "】");
    }
    
    private Map<String, String> getCookie(TicketConfigInfo config) {
        Map<String, String> cookies = new HashMap<String, String>();
        cookies.put("_jc_save_fromStation", getUnicode4Cookie(config.getFrom_station_name(), config.getFrom_station()));
        cookies.put("_jc_save_toStation", getUnicode4Cookie(config.getTo_station_name(), config.getTo_station()));
        cookies.put("_jc_save_fromDate", config.getTrain_date());
        cookies.put("_jc_save_toDate", DateUtils.formatDate(new Date()));
        cookies.put("_jc_save_wfdc_flag", "dc");
        return cookies;
    }
    
    public static String getUnicode4Cookie(String cityName, String cityCode) {
        String result = "";
        for (int i = 0; i < cityName.length(); i++) {
            int chr1 = (char)cityName.charAt(i);
            if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
                result += "%u" + Integer.toHexString(chr1).toUpperCase();
            }
            else {
                result += cityName.charAt(i);
            }
        }
        result += "%2C" + cityCode;
        return result;
    }
    
    public class OrderToken {
        
        private String token;
        
        private String key_check_isChange;
        
        public OrderToken(String token, String keyCheckIsChange) {
            super();
            this.token = token;
            key_check_isChange = keyCheckIsChange;
        }
        
        public String getKey_check_isChange() {
            return key_check_isChange;
        }
        
        public String getToken() {
            return token;
        }
        
        @Override
        public String toString() {
            return "OrderToken [token=" + token + ", key_check_isChange=" + key_check_isChange + "]";
        }
        
    }
}
