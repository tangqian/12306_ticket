package com.free.app.ticket.service;

import java.util.ArrayList;
import java.util.Collections;
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
import com.free.app.ticket.model.PassengerData;
import com.free.app.ticket.model.TicketBuyInfo;
import com.free.app.ticket.model.TicketConfigInfo;
import com.free.app.ticket.model.TrainConfigInfo;
import com.free.app.ticket.model.TrainInfo;
import com.free.app.ticket.model.PassengerData.SeatType;
import com.free.app.ticket.model.TrainConfigInfo.SpecificTrainInfo;
import com.free.app.ticket.util.ComparatorTrain;
import com.free.app.ticket.util.DateUtils;
import com.free.app.ticket.util.TicketHttpClient;
import com.free.app.ticket.view.CheckCode4OrderDialog;
import com.free.app.ticket.view.CheckCode4OrderDialog.ChooseType;
import com.free.app.ticket.view.CheckCode4OrderDialog.DialogResult;
import com.free.app.ticket.view.RefreshPanelManager;

public class AutoBuyThreadService extends Thread {
    
    private static final Logger logger = LoggerFactory.getLogger(AutoBuyThreadService.class);
    
    private TicketBuyInfo buyInfo;
    
    private boolean isStop = false;
    
    private Map<String, String> cookies;
    
    /**
     * 同步代码块用
     */
    public static final Object obj = new Object();
    
    /**
     * 同步代码块用
     */
    public static boolean waitFlag = true;
    
    private static final Pattern PATTERN_TOKEN = Pattern.compile("var globalRepeatSubmitToken = '(\\w+)'");
    
    private static final Pattern PATTERN_KEY_CHECK = Pattern.compile("'key_check_isChange':'(\\w+)'");
    
    public AutoBuyThreadService(TicketBuyInfo ticketBuyInfo) {
        this.buyInfo = ticketBuyInfo;
        cookies = getCookie(ticketBuyInfo.getTicketConfigInfo());
    }
    
    @Override
    public void run() {
        TicketHttpClient client = HttpClientThreadService.getHttpClient();
        // 获取提交订单动态参数
        client.leftTicketInit(cookies);
        
        int queryCount = 1;
        while (!isStop && !TicketMainFrame.isStop) {
            
            TicketMainFrame.trace("");
            TicketMainFrame.trace("第" + queryCount + "次余票查询");
            List<TrainInfo> trainInfos = client.queryLeftTicket(buyInfo.getTicketConfigInfo(), cookies);
            queryCount++;
            if (trainInfos == null || trainInfos.isEmpty()) {
                TicketMainFrame.trace("查询不到余票信息,3秒后开始下一轮查询");
                try {
                    Thread.sleep(3000L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
            
            Collections.sort(canBuyTrain, new ComparatorTrain());
            showCanBuyTrains(canBuyTrain);
            if (canBuyTrain.isEmpty()) {//全部车次卖完或未开售
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            
            List<TrainInfo> userPerfers = getUserPerfer(canBuyTrain);
            if (userPerfers.isEmpty()) {
                TicketMainFrame.remind("您中意的车次已售完或未开售，请考虑上述其它车次或更改席别(先停止刷票，再点车次设置按钮进行修改)");
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            
            if (!client.checkUserLogin()) {
                TicketMainFrame.trace("检查是否登录未通过");
                continue;
            }
            
            circularSubmitOrder(userPerfers);
            isStop = true;
            RefreshPanelManager.stop();
        }
        
    }
    
    private void restart() {
        new AutoBuyThreadService(buyInfo).run();
    }
    
    private boolean circularSubmitOrder(List<TrainInfo> userPerfers) {
        boolean result = false;
        
        TicketHttpClient client = HttpClientThreadService.getHttpClient();
        
        Set<String> failTrainSet = new HashSet<String>();//预订失败列车
        for (TrainInfo trainInfo : userPerfers) {
            if (TicketMainFrame.isStop) {
                break;
            }
            
            String updateRet = updateTicket(trainInfo);//根据列车余票信息，确定用户所坐席位
            if (updateRet != null) {
                TicketMainFrame.remind(updateRet);
                continue;
            }
            
            String submitResult = client.submitOrderRequest(buyInfo.getTicketConfigInfo(), cookies, trainInfo);
            if (submitResult != null) {
                failTrainSet.add(trainInfo.getStation_train_code());
                TicketMainFrame.remind("车次[" + trainInfo.getStation_train_code() + "]进入预订页面前检查失败");
                if (submitResult.contains("未完成订单")) {
                    TicketMainFrame.remind("恭喜您已经成功预订到列车，可以回家了，哦耶!(友情提示，请尽快登录12306进行支付)");
                    break;
                }
                else if (submitResult.contains("网络繁忙")) {
                    break;
                }
                else {
                    continue;
                }
            }
            OrderToken token = queryOrderToken();
            if (token == null) {
                failTrainSet.add(trainInfo.getStation_train_code());
                TicketMainFrame.remind("车次[" + trainInfo.getStation_train_code() + "]获取预订页面失败");
                continue;
            }
            
            TicketMainFrame.trace("车次[" + trainInfo.getStation_train_code() + "]进入订单提交确认页");
            DialogResult checkResult = CheckCode4OrderDialog.showDialog(trainInfo, buyInfo, token, cookies);
            ChooseType chooseType = checkResult.getChooseType();
            if (chooseType == ChooseType.CANCELALL) {
                TicketMainFrame.remind("您取消了全部列车的预订");
                break;
            }
            else if (chooseType == ChooseType.CANCELTHIS || chooseType == ChooseType.DEFAULT) {//取消预订当前
                TicketMainFrame.remind("您取消了当前车次[" + trainInfo.getStation_train_code() + "]的预订");
                
                restart();
                break;
            }
            else if (chooseType == ChooseType.SUCCESS) {
                synchronized (obj) {
                    new TrianQueueThreadService(trainInfo, token, buyInfo, checkResult.getAuthcode(), cookies).start();
                    while (waitFlag) {
                        try {
                            obj.wait();
                        }
                        catch (InterruptedException e) {
                        }
                    }
                    waitFlag = true;
                    
                    restart();
                    break;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 根据当前车次的余票更新乘客所买席位信息
     * @param train
     * @return
     */
    private String updateTicket(TrainInfo train) {
        String updateResult = null;
        
        SeatType seatType = null;
        if (!TrainInfo.isSellOut(train.getZe_num())) {//二等座
            seatType = SeatType.TWO_SEAT;
        }
        else if (!TrainInfo.isSellOut(train.getYz_num())) {//硬座
            seatType = SeatType.HARD_SEAT;
        }
        else if (!TrainInfo.isSellOut(train.getYw_num())) {//硬卧
            seatType = SeatType.HARD_SLEEPER;
        }
        else if (!TrainInfo.isSellOut(train.getWz_num())) {//无座
            seatType = SeatType.NONE_SEAT;
        }
        
        if (seatType != null) {
            List<PassengerData> pass = buyInfo.getPassengers();
            for (PassengerData passengerData : pass) {
                passengerData.setSeatType(seatType);
            }
            buyInfo.setCurrentBuySeat(seatType.getLabel());
        }
        else {
            updateResult = "车次[" + train.getStation_train_code() + "]没有你想要的席位";
        }
        return updateResult;
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
        TrainConfigInfo trainConfigInfo = buyInfo.getTrainConfigInfo();
        if (trainConfigInfo != null) {
            List<SpecificTrainInfo> selTrainInfo = trainConfigInfo.getTrains();
            for (SpecificTrainInfo specTrainInfo : selTrainInfo) {
                TrainInfo trainInfo = specTrainInfo.convertToTrainInfo();
                int index = all.indexOf(trainInfo);
                if (index != -1) {
                    perfers.add(all.get(index));
                }
            }
        }
        else {
            for (TrainInfo train : all) {
                if (!TrainInfo.isSellOut(train.getZe_num())) {//二等座
                    perfers.add(train);
                }
                else if (!TrainInfo.isSellOut(train.getYz_num())) {//硬座
                    perfers.add(train);
                }
                else if (!TrainInfo.isSellOut(train.getYw_num())) {//硬卧
                    perfers.add(train);
                }
                else if (!TrainInfo.isSellOut(train.getWz_num())) {//无座
                    perfers.add(train);
                }
                
            }
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
        for (int i = trains.size() - 1; i >= 0; i--) {//最有希望买到的票最后打印
            TicketMainFrame.trace(trains.get(i).getStation_train_code() + ":" + trains.get(i).getLeftTicketInfo());
            
        }
        TicketMainFrame.trace("------------------------------------");
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
        cookies.put("_jc_save_showZtkyts", "true");
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
