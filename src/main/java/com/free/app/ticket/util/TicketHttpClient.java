package com.free.app.ticket.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.model.ContacterInfo;
import com.free.app.ticket.model.JsonMsg4Authcode;
import com.free.app.ticket.model.JsonMsg4CheckOrder;
import com.free.app.ticket.model.JsonMsg4ConfirmQueue;
import com.free.app.ticket.model.JsonMsg4Contacter;
import com.free.app.ticket.model.JsonMsg4LeftTicket;
import com.free.app.ticket.model.JsonMsg4Login;
import com.free.app.ticket.model.JsonMsg4QueryWait;
import com.free.app.ticket.model.JsonMsg4QueueCount;
import com.free.app.ticket.model.JsonMsg4SubmitOrder;
import com.free.app.ticket.model.JsonMsgSuper;
import com.free.app.ticket.model.PassengerData;
import com.free.app.ticket.model.TicketConfigInfo;
import com.free.app.ticket.model.TrainInfo;
import com.free.app.ticket.model.JsonMsg4LeftTicket.TrainQueryInfo;
import com.free.app.ticket.model.PassengerData.SeatType;
import com.free.app.ticket.service.AutoBuyThreadService.OrderToken;
import com.free.app.ticket.util.DynamicJsUtil.Base32;
import com.free.app.ticket.util.constants.Constants;
import com.free.app.ticket.util.constants.HttpHeader;

public class TicketHttpClient {
    
    private static final Logger logger = LoggerFactory.getLogger(TicketHttpClient.class);
    
    private String JSESSIONID;
    
    private String BIGipServerotn;
    
    private String current_captcha_type;
    
    private String loginKey;
    
    private static final int DEBBUG_MAX_COUNT = 1000;
    
    private String query_url = "https://kyfw.12306.cn/otn/leftTicket/queryT";
    
    private static final Pattern PATTERN_DYNAMIC_JS = Pattern.compile("/otn/dynamicJs/(\\w+)");
    
    private static final Pattern PATTERN_LOGIN_KEY = Pattern.compile("var key='(\\w+)'");
    
    /*
     * private static final String loginAuthCodeFilePath =
     * System.getProperty("user.dir") + File.separator + "login_authcode.jpg";
     */

    /* loginUrl = path + "image" + File.separator + "passcode-login.jpg"; */

    private TicketHttpClient(String JSESSIONID, String BIGipServerotn) {
        this.JSESSIONID = JSESSIONID;
        this.BIGipServerotn = BIGipServerotn;
    }
    
    /**
     * <初始获取cookie>
     * @return
     */
    public static TicketHttpClient getInstance() {
        TicketHttpClient instacne = null;
        HttpClient httpclient = buildHttpClient();
        HttpGet get = new HttpGet("https://kyfw.12306.cn/otn/login/init");
        HttpHeader.setLoginInitHeader(get);
        
        String JSESSIONID = null;
        String BIGipServerotn = null;
        InputStream is = null;
        String loginDynamicJsUrl = null;
        try {
            HttpResponse response = httpclient.execute(get);
            Header[] headers = response.getAllHeaders();
            
            boolean isGzip = false;
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].getName().equals("Set-Cookie")) {
                    String cookie[] = headers[i].getValue().split("=");
                    String cookieName = cookie[0];
                    String cookieValue = cookie[1].split(";")[0];
                    if (cookieName.equals("JSESSIONID")) {
                        JSESSIONID = cookieValue;
                    }
                    if (cookieName.equals("BIGipServerotn")) {
                        BIGipServerotn = cookieValue;
                    }
                }
                else if ("Content-Encoding".equals(headers[i].getName()) && "gzip".equals(headers[i].getValue())) {
                    isGzip = true;
                }
            }
            
            if (JSESSIONID != null && BIGipServerotn != null) {
                is = response.getEntity().getContent();
                String responseBody;
                if (isGzip) {
                    responseBody = zipInputStream(is);
                }
                else {
                    responseBody = readInputStream(is);
                }
                
                Matcher m_token = PATTERN_DYNAMIC_JS.matcher(responseBody);
                if (m_token.find()) {
                    loginDynamicJsUrl = m_token.group(1);
                }
                else {
                    logger.error("httpClient init get loginDynamicJsUrl  fail for unknow reason, check it!");
                }
            }
            logger.info("JSESSIONID = " + JSESSIONID + ";BIGipServerotn = " + BIGipServerotn);
        }
        catch (Exception e) {
            logger.error("getInstance error : ", e);
        }
        finally {
            httpclient.getConnectionManager().shutdown();
        }
        
        if (JSESSIONID != null && BIGipServerotn != null) {
            instacne = new TicketHttpClient(JSESSIONID, BIGipServerotn);
            if (loginDynamicJsUrl != null) {
                instacne.buildLoginKey(loginDynamicJsUrl);
            }
        }
        return instacne;
    }
    
    private void buildLoginKey(String jsFileName) {
        if (logger.isDebugEnabled()) {
            logger.debug("---ajax get 获取登录页dynamicJs的内容，从中提取出登录KEY---");
        }
        
        HttpGet get = new HttpGet("https://kyfw.12306.cn/otn/dynamicJs/" + jsFileName);
        HttpHeader.setCommonHeader(get);
        setCookie(get, null);
        
        try {
            String result = doGetRequest(get);
            
            Matcher m_token = PATTERN_LOGIN_KEY.matcher(result);
            if (m_token.find()) {
                loginKey = m_token.group(1);
                System.out.println(loginKey);
            }
        }
        catch (Exception e) {
            logger.error("获取登录KEY出错", e);
            TicketMainFrame.remind("获取登录KEY出错");
        }
    }
    
    public File buildLoginCodeImage() {
        HttpClient httpclient = buildHttpClient();
        HttpGet get = new HttpGet("https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=login&rand=sjrand");
        HttpHeader.setLoginAuthCodeHeader(get);
        setCookie(get, null);
        
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + JSESSIONID + ".login.jpg");
        OutputStream out = null;
        InputStream is = null;
        try {
            HttpResponse response = httpclient.execute(get);
            Header[] headers = response.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].getName().equals("Set-Cookie")) {
                    String cookie[] = headers[i].getValue().split("=");
                    String cookieName = cookie[0];
                    String cookieValue = cookie[1].split(";")[0];
                    if (cookieName.equals("current_captcha_type")) {
                        current_captcha_type = cookieValue;
                    }
                }
            }
            
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                is = entity.getContent();
                out = new FileOutputStream(file);
                int readByteCount = -1;
                byte[] buffer = new byte[256];
                while ((readByteCount = is.read(buffer)) != -1) {
                    out.write(buffer, 0, readByteCount);
                }
            }
        }
        catch (Exception e) {
            file = null;
            logger.error("buildLoginCodeImage error : ", e);
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                }
                catch (IOException e) {
                }
            }
            httpclient.getConnectionManager().shutdown();
        }
        return file;
    }
    
    public boolean checkLoginAuthcode(String authCode) {
        if (logger.isDebugEnabled()) {
            logger.debug("---ajax post 检查验证码---");
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(Constants.CHECKCODE_PARAMS_RANDCODE, authCode));
        params.add(new BasicNameValuePair(Constants.CHECKCODE_PARAMS_RAND, "sjrand"));
        params.add(new BasicNameValuePair(Constants.RANDCODE_VALIDATE, ""));
        
        HttpPost post = new HttpPost("https://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn");
        HttpHeader.setPostAjaxHeader(post);
        setCookie(post, null);
        
        boolean result = false;
        try {
            String checkResult = doPostRequest(post, params);
            JsonMsg4Authcode msg = JSONObject.parseObject(checkResult, JsonMsg4Authcode.class);
            if ("1".equals(msg.getData().getResult())) {
                result = true;
            }
        }
        catch (Exception e) {
            logger.error("验证码检查发生异常", e);
            TicketMainFrame.remind("验证码检查发生异常,请联系管理员");
        }
        
        return result;
    }
    
    /**
     * <登录检查>
     * 
     * @param username
     * @param password
     * @param authcode
     * @return null代表成功，否则返回登录失败原因
     */
    public String checkLogin(String username, String password, String authcode) {
        if (logger.isDebugEnabled()) {
            logger.debug("---ajax post 登录检查---");
        }
        boolean checkCodeResult = checkLoginAuthcode(authcode);// 先检查验证码
        if (!checkCodeResult) {
            return "验证码不正确！";
        }
        
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("loginUserDTO.user_name", username));
        params.add(new BasicNameValuePair("userDTO.password", password));
        params.add(new BasicNameValuePair("randCode", authcode));
        params.add(new BasicNameValuePair("randCode_validate", ""));
        if (loginKey != null)
            params.add(new BasicNameValuePair(loginKey,
                DynamicJsUtil.encode32(DynamicJsUtil.bin216(Base32.encrypt("1111", loginKey)))));
        params.add(new BasicNameValuePair("myversion", "undefined"));
        
        /*HttpPost post = getHttpPost("https://kyfw.12306.cn/otn/login/loginAysnSuggest", null);
        HttpHeader.setPostAjaxHeader(post);*/
        HttpPost post = new HttpPost("https://kyfw.12306.cn/otn/login/loginAysnSuggest");
        HttpHeader.setPostAjaxHeader(post);
        setCookie(post, null);
        
        String result = null;
        try {
            String checkResult = doPostRequest(post, params);
            JsonMsg4Login msg = JSONObject.parseObject(checkResult, JsonMsg4Login.class);
            
            if (!"Y".equals(msg.getData().getString("loginCheck"))) {// 登录不成功
                result = msg.getMessages()[0];
            }
        }
        catch (Exception e) {
            result = "登录异常!";
            logger.error("登录发生异常", e);
            TicketMainFrame.remind("登录发生异常");
        }
        
        return result;
    }
    
    public ContacterInfo[] getPassengers() {
        if (logger.isDebugEnabled()) {
            logger.debug("---ajax post 获取联系人---");
        }
        HttpPost post = new HttpPost("https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs");
        HttpHeader.setPostAjaxHeader(post);
        setCookie(post, null);
        
        ContacterInfo[] result = null;
        try {
            String checkResult = doPostRequest(post, null);
            JsonMsg4Contacter msg = JSONObject.parseObject(checkResult, JsonMsg4Contacter.class);
            if (msg.getStatus()) {
                if (!msg.getData().isExist() && msg.getData().getExMsg() != null) {
                    TicketMainFrame.remind(msg.getData().getExMsg());
                }
                else {
                    result = msg.getData().getNormal_passengers();
                }
            }
        }
        catch (Exception e) {
            logger.error("获取联系人异常", e);
            TicketMainFrame.remind("获取联系人异常,请稍后再试");
        }
        
        return result;
    }
    
    /**
     * <查询余票信息>
     * @param configInfo
     * @param cookieMap
     * @return
     */
    public List<TrainInfo> queryLeftTicket(TicketConfigInfo configInfo, Map<String, String> cookieMap) {
        if (logger.isDebugEnabled()) {
            logger.debug("---ajax get 查询余票信息---");
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("leftTicketDTO.train_date", configInfo.getTrain_date()));
        params.add(new BasicNameValuePair("leftTicketDTO.from_station", configInfo.getFrom_station()));
        params.add(new BasicNameValuePair("leftTicketDTO.to_station", configInfo.getTo_station()));
        params.add(new BasicNameValuePair("purpose_codes", configInfo.getPurpose_codes()));
        String paramsUrl = URLEncodedUtils.format(params, "UTF-8");
        
        if ("A".equals(current_captcha_type) || "C".equals(current_captcha_type)) {//验证码为A类型时，查询前先调用LOG请示
            queryLog(paramsUrl, cookieMap);
        }
        HttpGet get = new HttpGet(query_url + "?" + paramsUrl);
        HttpHeader.setGetAjaxHeader(get);
        setCookie(get, cookieMap);
        
        List<TrainInfo> result = null;
        try {
            String checkResult = doGetRequest(get);
            JsonMsg4LeftTicket msg = JSONObject.parseObject(checkResult, JsonMsg4LeftTicket.class);
            if (msg.getStatus()) {
                List<TrainQueryInfo> infos = msg.getData();
                if (infos != null) {
                    result = new ArrayList<TrainInfo>();
                    TrainInfo trainInfo;
                    for (TrainQueryInfo info : infos) {
                        trainInfo = info.getQueryLeftNewDTO();
                        trainInfo.setSecretStr(info.getSecretStr());
                        result.add(trainInfo);
                    }
                }
                else {
                    String[] tips = msg.getMessages();
                    if (tips != null && tips.length > 0) {
                        if (tips[0].contains("非法")) {
                            //query_url = "https://kyfw.12306.cn/otn/leftTicket/query";
                            logger.error("查询余票时出现错误：" + tips[0]);
                        }
                    }
                }
            }
            else {
                if (StringUtils.isNotEmpty(msg.getC_url())) {//切换地址了
                    query_url = "https://kyfw.12306.cn/otn/" + msg.getC_url();
                }
            }
        }
        catch (JSONException e) {
            query_url = "https://kyfw.12306.cn/otn/leftTicket/query";
            logger.error("查询余票异常，内容异常", e);
            TicketMainFrame.remind("查询余票异常，内容异常");
        }
        catch (Exception e) {
            logger.error("查询余票异常", e);
            TicketMainFrame.remind("查询余票异常");
        }
        
        return result;
    }
    
    /**
     * <查询余票前先进行LOG调用>
     * @param paramsUrl
     * @param cookieMap
     */
    public void queryLog(String paramsUrl, Map<String, String> cookieMap) {
        if (logger.isDebugEnabled()) {
            logger.debug("---ajax get 查询余票前先LOG---");
        }
        
        HttpGet get = new HttpGet("https://kyfw.12306.cn/otn/leftTicket/log?" + paramsUrl);
        HttpHeader.setGetAjaxHeader(get);
        setCookie(get, cookieMap);
        
        try {
            String checkResult = doGetRequest(get);
            JsonMsgSuper msg = JSONObject.parseObject(checkResult, JsonMsgSuper.class);
            if (msg.getStatus()) {
                
            }
        }
        catch (Exception e) {
            logger.error("查询余票前LOG异常", e);
        }
        
    }
    
    /**
     * <选择某个车次进入预订页前检验>
     * @param configInfo
     * @param cookieMap
     * @param train
     * @return
     */
    public String submitOrderRequest(TicketConfigInfo configInfo, Map<String, String> cookieMap, TrainInfo train) {
        if (logger.isDebugEnabled()) {
            logger.debug("---ajax post 进入预订页面前检验---");
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("NTQ3NjE2", "OWY2YmExNzM5OWM4YjJkNQ=="));
        params.add(new BasicNameValuePair("myversion", "undefiend"));
        try {
            params.add(new BasicNameValuePair("secretStr", URLDecoder.decode(train.getSecretStr(), "UTF-8")));
        }
        catch (UnsupportedEncodingException e1) {
            logger.error("进入预订页参数编码错误", e1);
            TicketMainFrame.remind("预订页参数编码异常，请通知管理员!");
            return "预订页参数编码异常，请通知管理员!";
        }
        params.add(new BasicNameValuePair("train_date", configInfo.getTrain_date()));
        params.add(new BasicNameValuePair("back_train_date", DateUtils.formatDate(new Date())));
        params.add(new BasicNameValuePair("tour_flag", "dc"));//单程票标识
        params.add(new BasicNameValuePair("purpose_codes", configInfo.getPurpose_codes()));
        params.add(new BasicNameValuePair("query_from_station_name", configInfo.getFrom_station_name()));
        params.add(new BasicNameValuePair("query_to_station_name", configInfo.getTo_station_name()));
        
        HttpPost post = new HttpPost("https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest");
        HttpHeader.setPostAjaxHeader(post);
        setCookie(post, cookieMap);
        
        String result = null;
        try {
            String checkResult = doPostRequest(post, params);
            
            int reConnCount = 0;
            while (reConnCount < 3 && StringUtils.isEmpty(checkResult)) {
                System.out.println("----reconn-----");
                try {
                    Thread.sleep(100L);
                }
                catch (InterruptedException e) {
                }
                reConnCount++;
                checkResult = doPostRequest(post, params);
            }
            JsonMsg4SubmitOrder msg = JSONObject.parseObject(checkResult, JsonMsg4SubmitOrder.class);
            if (msg != null) {
                if (msg.getHttpstatus() == 200 && msg.getStatus()) {
                    
                }
                else {
                    if (msg.getMessages() != null) {
                        TicketMainFrame.remind(msg.getMessages()[0]);
                        result = msg.getMessages()[0];
                    }
                }
            }
        }
        catch (Exception e) {
            logger.error("进入预订页面校验失败", e);
            TicketMainFrame.remind("进入预订页面校验失败");
            result = "进入预订页面校验失败";
        }
        
        return result;
    }
    
    /**
     * <选择某个车次进入预订页>
     * @param cookieMap
     * @return
     */
    public String getInitDcPage(Map<String, String> cookieMap) {
        if (logger.isDebugEnabled()) {
            logger.debug("---ajax post 进入单程票预订页---");
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("_json_att", ""));
        
        HttpPost post = new HttpPost("https://kyfw.12306.cn/otn/confirmPassenger/initDc");
        HttpHeader.setInitDcHeader(post);
        setCookie(post, cookieMap);
        
        String result = null;
        try {
            result = doPostRequest(post, params);
        }
        catch (Exception e) {
            logger.error("进入单程票预订页", e);
            TicketMainFrame.remind("进入单程票预订页");
        }
        
        return result;
    }
    
    public File buildOrderCodeImage(Map<String, String> cookies) {
        if (logger.isDebugEnabled()) {
            logger.debug("---ajax get 获取提交订单验证码---");
        }
        HttpClient httpclient = buildHttpClient();
        HttpGet get = new HttpGet("https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=passenger&rand=randp");
        HttpHeader.setLoginAuthCodeHeader(get);
        setCookie(get, cookies);
        
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + JSESSIONID + ".order.jpg");
        OutputStream out = null;
        InputStream is = null;
        try {
            HttpResponse response = httpclient.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                is = entity.getContent();
                out = new FileOutputStream(file);
                int readByteCount = -1;
                byte[] buffer = new byte[256];
                while ((readByteCount = is.read(buffer)) != -1) {
                    out.write(buffer, 0, readByteCount);
                }
            }
        }
        catch (Exception e) {
            file = null;
            logger.error("buildOrderCodeImage error : ", e);
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                }
                catch (IOException e) {
                }
            }
            httpclient.getConnectionManager().shutdown();
        }
        return file;
    }
    
    public JsonMsg4CheckOrder checkOrderAuthcode(String authCode, String token, List<PassengerData> passengers,
        Map<String, String> cookieMap) {
        if (logger.isDebugEnabled()) {
            logger.debug("---ajax post 检查下单验证码---");
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("bed_level_order_num", "000000000000000000000000000000"));
        params.add(new BasicNameValuePair("cancel_flag", "2"));
        params.add(new BasicNameValuePair("passengerTicketStr", getPassengerTicketStr(passengers)));
        params.add(new BasicNameValuePair("oldPassengerStr", getOldPassengerStr(passengers)));
        params.add(new BasicNameValuePair("randCode", authCode));
        params.add(new BasicNameValuePair("REPEAT_SUBMIT_TOKEN", token));
        params.add(new BasicNameValuePair("tour_flag", "dc"));
        params.add(new BasicNameValuePair("_json_att", ""));
        
        HttpPost post = new HttpPost("https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo");
        HttpHeader.setPostAjaxHeader(post);
        setCookie(post, cookieMap);
        
        JsonMsg4CheckOrder result = null;
        try {
            String checkResult = doPostRequest(post, params);
            int reConnCount = 0;
            while (reConnCount < 3 && StringUtils.isEmpty(checkResult)) {
                System.out.println("----reconn-----");
                try {
                    Thread.sleep(100L);
                }
                catch (InterruptedException e) {
                }
                reConnCount++;
                checkResult = doPostRequest(post, params);
            }
            result = JSONObject.parseObject(checkResult, JsonMsg4CheckOrder.class);
        }
        catch (Exception e) {
            logger.error("检查下单验证码发生异常", e);
            TicketMainFrame.remind("检查下单验证码发生异常,请联系管理员");
        }
        
        return result;
    }
    
    public JsonMsg4QueueCount getQueueCount(TrainInfo train, Date trianDate, String seatType, String token,
        Map<String, String> cookieMap) {
        if (logger.isDebugEnabled()) {
            logger.debug("---ajax post 获取排队人数---");
        }
        DateFormat df = new SimpleDateFormat("EEE MMM dd yyyy", Locale.US);
        
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("purpose_codes", "00"));
        params.add(new BasicNameValuePair("seatType", seatType));
        params.add(new BasicNameValuePair("train_no", train.getTrain_no()));
        params.add(new BasicNameValuePair("fromStationTelecode", train.getFrom_station_telecode()));
        params.add(new BasicNameValuePair("stationTrainCode", train.getStation_train_code()));
        params.add(new BasicNameValuePair("toStationTelecode", train.getTo_station_telecode()));
        params.add(new BasicNameValuePair("leftTicket", train.getYp_info()));
        params.add(new BasicNameValuePair("REPEAT_SUBMIT_TOKEN", token));
        params.add(new BasicNameValuePair("train_date", df.format(trianDate) + " 00:00:00 GMT+0800"));//Sun Feb 01 2015 00:00:00 GMT+0800
        params.add(new BasicNameValuePair("_json_att", ""));
        
        HttpPost post = new HttpPost("https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount");
        HttpHeader.setPostAjaxHeader(post);
        setCookie(post, cookieMap);
        
        JsonMsg4QueueCount result = null;
        try {
            String checkResult = doPostRequest(post, params);
            int reConnCount = 0;
            while (reConnCount < 3 && StringUtils.isEmpty(checkResult)) {
                System.out.println("----reconn-----");
                try {
                    Thread.sleep(100L);
                }
                catch (InterruptedException e) {
                }
                reConnCount++;
                checkResult = doPostRequest(post, params);
            }
            result = JSONObject.parseObject(checkResult, JsonMsg4QueueCount.class);
        }
        catch (Exception e) {
            logger.error("获取排队人数发生异常", e);
            TicketMainFrame.remind("获取排队人数发生异常");
        }
        return result;
    }
    
    public JsonMsg4ConfirmQueue confirmQueue(TrainInfo train, OrderToken token, List<PassengerData> passengers,
        String authcode, Map<String, String> cookieMap) {
        if (logger.isDebugEnabled()) {
            logger.debug("---ajax post 确认排队购买---");
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("purpose_codes", "00"));
        params.add(new BasicNameValuePair("key_check_isChange", token.getKey_check_isChange()));
        params.add(new BasicNameValuePair("passengerTicketStr", getPassengerTicketStr(passengers)));
        params.add(new BasicNameValuePair("oldPassengerStr", getOldPassengerStr(passengers)));
        params.add(new BasicNameValuePair("randCode", authcode));
        params.add(new BasicNameValuePair("leftTicketStr", train.getYp_info()));
        params.add(new BasicNameValuePair("REPEAT_SUBMIT_TOKEN", token.getToken()));
        params.add(new BasicNameValuePair("train_location", train.getLocation_code()));
        params.add(new BasicNameValuePair("_json_att", ""));
        
        HttpPost post = new HttpPost("https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue");
        HttpHeader.setPostAjaxHeader(post);
        setCookie(post, cookieMap);
        
        JsonMsg4ConfirmQueue result = null;
        try {
            String checkResult = doPostRequest(post, params);
            int reConnCount = 0;
            while (reConnCount < 3 && StringUtils.isEmpty(checkResult)) {
                System.out.println("----reconn-----");
                try {
                    Thread.sleep(100L);
                }
                catch (InterruptedException e) {
                }
                reConnCount++;
                checkResult = doPostRequest(post, params);
            }
            result = JSONObject.parseObject(checkResult, JsonMsg4ConfirmQueue.class);
        }
        catch (Exception e) {
            logger.error("确认排队购买发生异常", e);
            TicketMainFrame.remind("确认排队购买发生异常");
        }
        return result;
    }
    
    /**
     * <查询余票信息>
     * @param configInfo
     * @param cookieMap
     * @return
     */
    public JsonMsg4QueryWait queryOrderWaitTime(String token, Map<String, String> cookieMap) {
        if (logger.isDebugEnabled()) {
            logger.debug("---ajax get 查询排队等待信息---");
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("random", String.valueOf(System.currentTimeMillis())));
        params.add(new BasicNameValuePair("tourFlag", "dc"));
        params.add(new BasicNameValuePair("_json_att", ""));
        params.add(new BasicNameValuePair("REPEAT_SUBMIT_TOKEN", token));
        String paramsUrl = URLEncodedUtils.format(params, "UTF-8");
        
        HttpGet get = new HttpGet("https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime" + "?" + paramsUrl);
        HttpHeader.setGetAjaxHeader(get);
        setCookie(get, cookieMap);
        
        JsonMsg4QueryWait result = null;
        try {
            String checkResult = doGetRequest(get);
            result = JSONObject.parseObject(checkResult, JsonMsg4QueryWait.class);
        }
        catch (Exception e) {
            logger.error("查询排队等待信息异常", e);
            TicketMainFrame.remind("查询排队等待信息异常");
        }
        return result;
    }
    
    /**
     * <退出>
     *
     */
    public void loginOut() {
        if (logger.isDebugEnabled()) {
            logger.debug("---post 登录退出---");
        }
        HttpPost post = new HttpPost("https://kyfw.12306.cn/otn/login/loginOut");
        HttpHeader.setLogoutHeader(post);
        setCookie(post, null);
        
        try {
            doPostRequest(post, null);
        }
        catch (Exception e) {
            logger.error("退出发生异常", e);
        }
        TicketMainFrame.trace("退出成功!");
    }
    
    private static X509TrustManager tm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }
        
        public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }
        
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };
    
    private static HttpClient buildHttpClient() {
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContext.getInstance("TLS");
        }
        catch (NoSuchAlgorithmException e) {
            logger.error("getHttpClient error for NoSuchAlgorithmException", e);
        }
        
        try {
            sslcontext.init(null, new TrustManager[]
            {tm}, null);
        }
        catch (KeyManagementException e) {
            logger.error("getHttpClient error for KeyManagementException", e);
        }
        SSLSocketFactory ssf = new SSLSocketFactory(sslcontext);
        ClientConnectionManager ccm = new DefaultHttpClient().getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", 443, ssf));
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        HttpClient httpclient = new DefaultHttpClient(ccm, params);
        return httpclient;
    }
    
    /**
     * <公用POST请求方法>
     * 
     * @param http
     * @param params
     * @return
     */
    private String doPostRequest(HttpPost request, List<NameValuePair> params) {
        if (logger.isDebugEnabled()) {
            logger.debug("[post url] {}", request.getURI().toString());
        }
        HttpClient httpclient = buildHttpClient();
        String responseBody = null;
        
        InputStream is = null;
        try {
            if (params != null) {
                UrlEncodedFormEntity uef = new UrlEncodedFormEntity(params, "UTF-8");
                request.setEntity(uef);
            }
            if (logger.isDebugEnabled()) {
                if (params == null)
                    logger.debug("[post params] null");
                else {
                    logger.debug("[post params] {}", URLEncodedUtils.format(params, "UTF-8"));
                    /*if (!URLEncodedUtils.format(params, "UTF-8").contains("password"))//不打印密码参数
                        logger.debug("[post params] {}", URLEncodedUtils.format(params, "UTF-8"));*/
                }
            }
            HttpResponse response = httpclient.execute(request);
            Header[] headers = response.getAllHeaders();
            boolean isGzip = false;
            for (int i = 0; i < headers.length; i++) {
                if ("Content-Encoding".equals(headers[i].getName()) && "gzip".equals(headers[i].getValue())) {
                    isGzip = true;
                    break;
                }
            }
            is = response.getEntity().getContent();
            if (isGzip) {
                responseBody = zipInputStream(is);
            }
            else {
                responseBody = readInputStream(is);
            }
            
        }
        catch (Exception e) {
            logger.error("doPostRequest error:", e);
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                }
            }
            httpclient.getConnectionManager().shutdown();
        }
        
        if (logger.isDebugEnabled()) {
            if (responseBody != null && responseBody.length() < DEBBUG_MAX_COUNT)
                logger.debug("[responseBody] {}", responseBody);
        }
        return responseBody;
    }
    
    /**
     * <公用get请求>
     * @param request
     * @return
     */
    private String doGetRequest(HttpGet request) {
        if (logger.isDebugEnabled()) {
            logger.debug("[get url] {}", request.getURI().toString());
        }
        HttpClient httpclient = buildHttpClient();
        String responseBody = null;
        
        InputStream is = null;
        try {
            HttpResponse response = httpclient.execute(request);
            
            Header[] headers = response.getAllHeaders();
            boolean isGzip = false;
            for (int i = 0; i < headers.length; i++) {
                if ("Content-Encoding".equals(headers[i].getName()) && "gzip".equals(headers[i].getValue())) {
                    isGzip = true;
                    break;
                }
            }
            is = response.getEntity().getContent();
            if (isGzip) {
                responseBody = zipInputStream(is);
            }
            else {
                responseBody = readInputStream(is);
            }
            
        }
        catch (Exception e) {
            logger.error("doGetRequest error:", e);
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                }
            }
            httpclient.getConnectionManager().shutdown();
        }
        if (logger.isDebugEnabled()) {
            if (responseBody != null && responseBody.length() < DEBBUG_MAX_COUNT)
                logger.debug("[responseBody] {}", responseBody);
        }
        return responseBody;
    }
    
    private void setCookie(HttpRequestBase request, Map<String, String> cookieMap) {
        request.setHeader("Cookie", getCookieStr(cookieMap));
    }
    
    private String getCookieStr(Map<String, String> cookieMap) {
        String cookie;
        if (cookieMap != null && cookieMap.size() > 0) {
            cookie = "JSESSIONID=" + JSESSIONID;
            for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
                cookie += "; " + entry.getKey() + "=" + entry.getValue();
            }
            cookie += ";BIGipServerotn=" + BIGipServerotn;
            cookie += ";current_captcha_type=" + current_captcha_type;
        }
        else {
            cookie =
                "JSESSIONID=" + JSESSIONID + ";BIGipServerotn=" + BIGipServerotn + ";current_captcha_type="
                    + current_captcha_type;
        }
        logger.debug("[request cooikes] {}", cookie);
        return cookie;
    }
    
    /**
     * 获取参数oldPassengerStr值
     * 
     * @param passengers
     * @return
     */
    private String getOldPassengerStr(List<PassengerData> passengers) {
        StringBuilder sb = new StringBuilder();
        for (PassengerData p : passengers) {
            sb.append(p.getName())
                .append(',')
                .append(p.getCardTypeValue())
                .append(",")
                .append(p.getCardNo())
                .append(',')
                .append(p.getTicketTypeValue())
                .append('_');
        }
        return sb.toString();
    }
    
    /**
     * 获取oldPassengerStr
     * 
     * @param userInfo
     * @return
     */
    private String getPassengerTicketStr(List<PassengerData> passengers) {
        StringBuilder sb = new StringBuilder();
        for (PassengerData p : passengers) {
            if (p.getSeatType() != SeatType.NONE_SEAT) {
                sb.append(p.getSeatTypeValue());
            }
            sb.append(",0,")
                .append(p.getTicketTypeValue())
                .append(',')
                .append(p.getName())
                .append(',')
                .append(p.getCardTypeValue())
                .append(",")
                .append(p.getCardNo())
                .append(',')
                .append(p.getMobileNotNull())
                .append(",N_");
        }
        
        return sb.substring(0, sb.length() - 1);
    }
    
    /**
     * 处理返回文件流
     * 
     * @param is
     * @return
     * @throws IOException
     */
    private static String readInputStream(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null)
            buffer.append(line + "\n");
        return buffer.toString();
    }
    
    /**
     * 处理gzip,deflate返回流
     * 
     * @param is
     * @return
     * @throws IOException
     */
    private static String zipInputStream(InputStream is) throws IOException {
        GZIPInputStream gzip = new GZIPInputStream(is);
        BufferedReader in = new BufferedReader(new InputStreamReader(gzip, "UTF-8"));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null)
            buffer.append(line + "\n");
        return buffer.toString();
    }
}
