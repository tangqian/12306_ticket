package com.free.app.ticket.util.constants;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * 
 * http请求头常量类
 * 
 */
public class HttpHeader {
    
    private static void setBaseHeader(HttpRequestBase http) {
        http.setHeader("Host", "kyfw.12306.cn");
        http.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:34.0) Gecko/20100101 Firefox/34.0");
        http.setHeader("Accept", "*/*");
        http.setHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        http.setHeader("Accept-Encoding", "gzip, deflate");
        http.setHeader("Referer", "https://kyfw.12306.cn/");
        http.setHeader("Cookie", "tmp");
        http.setHeader("Connection", "keep-alive");
        http.setHeader("Cache-Control", "no-cache");
    }
    
    public static void setCommonHeader(HttpRequestBase http){
        setBaseHeader(http);
    }
    
    public static void setLoginInitHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "text/html,application/xhtml+xml,application/xml,*/*");
    }
    
    public static void setLoginAuthCodeHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "*/*");
    }

    
    public static void setPostAjaxHeader(HttpRequestBase http){
        http.setHeader("Host", "kyfw.12306.cn");
        http.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:34.0) Gecko/20100101 Firefox/34.0");
        http.setHeader("Accept", "*/*");
        http.setHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        http.setHeader("Accept-Encoding", "gzip, deflate");
        http.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        http.setHeader("X-Requested-With", "XMLHttpRequest");
        http.setHeader("Referer", "https://kyfw.12306.cn/");
        //http.setHeader("Content-Length", "152");
        http.setHeader("Cookie", "tmp");
        http.setHeader("Connection", "keep-alive");
        http.setHeader("Pragma", "no-cache");
        http.setHeader("Cache-Control", "no-cache");
    }
    
    /**
     * 通用Get类型ajax请求header设置
     * 
    **/
    public static void setGetAjaxHeader(HttpRequestBase http) {
        http.setHeader("Host", "kyfw.12306.cn");
        http.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:34.0) Gecko/20100101 Firefox/34.0");
        http.setHeader("Accept", "*/*");
        http.setHeader("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        http.setHeader("Accept-Encoding", "gzip, deflate");
        //http.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        http.setHeader("If-Modified-Since", "0");
        http.setHeader("Cache-Control", "no-cache");
        http.setHeader("X-Requested-With", "XMLHttpRequest");
        http.setHeader("Referer", "https://kyfw.12306.cn/");
        http.setHeader("Cookie", "tmp");
        http.setHeader("Connection", "keep-alive");
        
    }
    
    public static void setLogoutHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "text/html,application/xhtml+xml,application/xml,*/*");
    }
    
    public static void setLoginHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "text/html,application/xhtml+xml,application/xml,*/*");
        http.setHeader("Cache-Control", "no-cache");
        http.setHeader("Content-Type", "application/x-www-form-urlencoded");
    }
    
    public static void setTiketInitHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "text/html,application/xhtml+xml,application/xml,*/*");
        http.setHeader("Cache-Control", "no-cache");
        http.setHeader("Content-Type", "application/x-www-form-urlencoded");
    }
    
    public static void setTiketSearchHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "*/*");
        http.setHeader("Cache-Control", "no-cache");
        http.setHeader("If-Modified-Since", "0");
        http.setHeader("X-Requested-With", "XMLHttpRequest");
    }
    
    public static void setSubmitOrderHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "*/*");
        http.setHeader("Cache-Control", "no-cache");
        http.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        http.setHeader("X-Requested-With", "XMLHttpRequest");
    }
    
    public static void setInitDcHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "text/html,application/xhtml+xml,application/xml,*/*");
        http.setHeader("Content-Type", "application/x-www-form-urlencoded");
    }
    
    /**
     * checkorder,confirmsingle,queryOrderWaitTime,getQueueCount 共用
     * 
     */
    public static void setCheckOrderHeader(HttpRequestBase http) {
        setBaseHeader(http);
        http.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        http.setHeader("Cache-Control", "no-cache");
        http.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        http.setHeader("X-Requested-With", "XMLHttpRequest");
    }
}
