package com.free.app.ticket.util.constants;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * 
 * http请求头常量类
 * 
 */
public class HttpHeader {
    
    private static String REFERER = "Referer";
    
    private static String CACHECONTROL = "Cache-Control";
    
    private static String XREQUESTEDWITH = "x-requested-with";
    
    private static String CONTENTTYPE = "Content-Type";
    
    private static String CACHECONTROL_VALUE = "no-cache";
    
    private static String CONTENTTYPE_UTF8_VALUE = "application/x-www-form-urlencoded; charset=UTF-8";
    
    private static String CONTENTTYPE_VALUE = "application/x-www-form-urlencoded";
    
    private static String XREQUESTEDWITH_VALUE = "XMLHttpRequest";
    
    private static void setBaseHeader(HttpRequestBase http) {
        http.setHeader("Accept-Encoding", "gzip, deflate");
        http.setHeader("Accept-Language", "zh-cn");
        http.setHeader("Connection", "keep-alive");
        http.setHeader("Host", "kyfw.12306.cn");
        http.setHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
    }
    
    public static void setLoginInitHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "text/html,application/xhtml+xml,application/xml,*/*");
        http.setHeader("Referer", UrlConstants.REF_WEBSITEROOT_URL);
    }
    
    public static void setLoginAuthCodeHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "*/*");
        http.setHeader("Referer", UrlConstants.REF_WEBSITEROOT_URL);
    }

    /**
     * 通用Post类型ajax请求header设置
     * 
    **/
    public static void setPostAjaxHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "*/*");
        http.setHeader(CACHECONTROL, CACHECONTROL_VALUE);
        http.setHeader(CONTENTTYPE, CONTENTTYPE_UTF8_VALUE);
        http.setHeader(REFERER, UrlConstants.REF_WEBSITEROOT_URL);
        http.setHeader(XREQUESTEDWITH, XREQUESTEDWITH_VALUE);
    }
    
    /**
     * 通用Get类型ajax请求header设置
     * 
    **/
    public static void setGetAjaxHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "*/*");
        http.setHeader(CACHECONTROL, CACHECONTROL_VALUE);
        http.setHeader(CONTENTTYPE, CONTENTTYPE_UTF8_VALUE);
        http.setHeader("If-Modified-Since", "0");
        http.setHeader(REFERER, UrlConstants.REF_WEBSITEROOT_URL);
        http.setHeader(XREQUESTEDWITH, XREQUESTEDWITH_VALUE);
    }
    
    public static void setLogoutHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "text/html,application/xhtml+xml,application/xml,*/*");
        http.setHeader(REFERER, "https://kyfw.12306.cn/otn/index/init");
    }
    
    public static void setLoginHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "text/html,application/xhtml+xml,application/xml,*/*");
        http.setHeader(CACHECONTROL, CACHECONTROL_VALUE);
        http.setHeader(CONTENTTYPE, CONTENTTYPE_VALUE);
        http.setHeader(REFERER, UrlConstants.REF_WEBSITEROOT_URL);
    }
    
    public static void setTiketInitHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "text/html,application/xhtml+xml,application/xml,*/*");
        http.setHeader(CACHECONTROL, CACHECONTROL_VALUE);
        http.setHeader(CONTENTTYPE, CONTENTTYPE_VALUE);
        http.setHeader(REFERER, UrlConstants.REF_TICKET_URL);
    }
    
    public static void setTiketSearchHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "*/*");
        http.setHeader(CACHECONTROL, CACHECONTROL_VALUE);
        http.setHeader("If-Modified-Since", "0");
        http.setHeader(REFERER, UrlConstants.REF_TICKET_URL);
        http.setHeader(XREQUESTEDWITH, XREQUESTEDWITH_VALUE);
    }
    
    public static void setSubmitOrderHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "*/*");
        http.setHeader(CACHECONTROL, CACHECONTROL_VALUE);
        http.setHeader(CONTENTTYPE, CONTENTTYPE_UTF8_VALUE);
        http.setHeader(REFERER, UrlConstants.REF_TICKET_URL);
        http.setHeader(XREQUESTEDWITH, XREQUESTEDWITH_VALUE);
    }
    
    public static void setInitDcHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "text/html,application/xhtml+xml,application/xml,*/*");
        http.setHeader(CONTENTTYPE, CONTENTTYPE_VALUE);
        http.setHeader(REFERER, UrlConstants.REF_TICKET_URL);
    }
    
    /**
     * checkorder,confirmsingle,queryOrderWaitTime,getQueueCount 共用
     * 
     */
    public static void setCheckOrderHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        http.setHeader(CACHECONTROL, CACHECONTROL_VALUE);
        http.setHeader(CONTENTTYPE, CONTENTTYPE_UTF8_VALUE);
        http.setHeader(REFERER, UrlConstants.REQ_INITDC_URL);
        http.setHeader(XREQUESTEDWITH, XREQUESTEDWITH_VALUE);
    }
}
