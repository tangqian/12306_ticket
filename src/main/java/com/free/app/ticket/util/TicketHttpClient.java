package com.free.app.ticket.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.model.ContacterInfo;
import com.free.app.ticket.model.JsonMsg4Authcode;
import com.free.app.ticket.model.JsonMsg4Contacter;
import com.free.app.ticket.model.JsonMsg4LeftTicket;
import com.free.app.ticket.model.JsonMsg4Login;
import com.free.app.ticket.model.TicketConfigInfo;
import com.free.app.ticket.model.JsonMsg4LeftTicket.TrainQueryInfo;
import com.free.app.ticket.util.constants.Constants;
import com.free.app.ticket.util.constants.HttpHeader;
import com.free.app.ticket.util.constants.UrlConstants;

public class TicketHttpClient {

	private static final Logger logger = LoggerFactory
			.getLogger(TicketHttpClient.class);

	private String JSESSIONID;

	private String BIGipServerotn;

	/*
	 * private static final String loginAuthCodeFilePath =
	 * System.getProperty("user.dir") + File.separator + "login_authcode.jpg";
	 */

	/* loginUrl = path + "image" + File.separator + "passcode-login.jpg"; */

	private TicketHttpClient(String JSESSIONID, String BIGipServerotn) {
		this.JSESSIONID = JSESSIONID;
		this.BIGipServerotn = BIGipServerotn;
	}

	public static TicketHttpClient getInstance() {
		TicketHttpClient instacne = null;
		HttpClient httpclient = buildHttpClient();
		HttpGet get = new HttpGet(UrlConstants.REQ_LOGININIT_URL);
		HttpHeader.setLoginInitHeader(get);

		String JSESSIONID = null;
		String BIGipServerotn = null;
		try {
			HttpResponse response = httpclient.execute(get);
			Header[] headers = response.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				if (headers[i].getName().equals("Set-Cookie")) {
					String cookie[] = headers[i].getValue().split("=");
					String cookieName = cookie[0];
					String cookieValue = cookie[1].split(";")[0];
					if (cookieName.equals(Constants.JSESSIONID)) {
						JSESSIONID = cookieValue;
					}
					if (cookieName.equals(Constants.BIGIPSERVEROTN)) {
						BIGipServerotn = cookieValue;
					}
				}
			}
			// String responseBody =
			// zipInputStream(response.getEntity().getContent());
			// 获取消息头的信息
			// logger.debug("return msg : \n" + responseBody);
			logger.info("JSESSIONID = " + JSESSIONID + ";BIGipServerotn = "
					+ BIGipServerotn);
		} catch (Exception e) {
			logger.error("getInstance error : ", e);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

		if (JSESSIONID != null && BIGipServerotn != null) {
			instacne = new TicketHttpClient(JSESSIONID, BIGipServerotn);
		}
		return instacne;
	}

	private static X509TrustManager tm = new X509TrustManager() {
		public void checkClientTrusted(X509Certificate[] xcs, String string)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] xcs, String string)
				throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};

	private static HttpClient buildHttpClient() {
		SSLContext sslcontext = null;
		try {
			sslcontext = SSLContext.getInstance("TLS");
		} catch (NoSuchAlgorithmException e) {
			logger.error("getHttpClient error for NoSuchAlgorithmException", e);
		}

		try {
			sslcontext.init(null, new TrustManager[] { tm }, null);
		} catch (KeyManagementException e) {
			logger.error("getHttpClient error for KeyManagementException", e);
		}
		SSLSocketFactory ssf = new SSLSocketFactory(sslcontext);
		ClientConnectionManager ccm = new DefaultHttpClient()
				.getConnectionManager();
		SchemeRegistry sr = ccm.getSchemeRegistry();
		sr.register(new Scheme("https", 443, ssf));
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
		HttpClient httpclient = new DefaultHttpClient(ccm, params);
		return httpclient;
	}

	public File buildLoginCodeImage() {
		HttpClient httpclient = buildHttpClient();
		HttpGet get = getHttpGet(UrlConstants.REQ_GETLOGINPASSCODE_URL, null);
		HttpHeader.setLoginAuthCodeHeader(get);

		File file = new File(System.getProperty("java.io.tmpdir")
				+ File.separator + JSESSIONID + ".login.jpg");
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
		} catch (Exception e) {
			file = null;
			logger.error("buildLoginCodeImage error : ", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
			httpclient.getConnectionManager().shutdown();
		}
		return file;
	}

	public boolean checkLoginAuthcode(String authCode) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Constants.CHECKCODE_PARAMS_RANDCODE,
				authCode));
		params.add(new BasicNameValuePair(Constants.CHECKCODE_PARAMS_RAND,
				"sjrand"));
		params.add(new BasicNameValuePair(Constants.RANDCODE_VALIDATE, ""));

		HttpPost post = getHttpPost(UrlConstants.REQ_CHECKCODE_URL, null);
		HttpHeader.setCommonAjaxHeader(post);

		boolean result = false;
		try {
			String checkResult = doPostRequest(post, params);
			JsonMsg4Authcode msg = JSONObject.parseObject(checkResult,
					JsonMsg4Authcode.class);
			logger.info(checkResult);
			if ("1".equals(msg.getData().getResult())) {
				result = true;
			}
		} catch (Exception e) {
			logger.error("验证码检查发生异常", e);
			TicketMainFrame.remind("验证码检查发生异常,请联系管理员");
		}

		return result;
	}

	public ContacterInfo[] getPassengers() {
		HttpPost post = getHttpPost(UrlConstants.REQ_GETPASSENGER_URL, null);
		HttpHeader.setCommonAjaxHeader(post);

		ContacterInfo[] result = null;
		try {
			String checkResult = doPostRequest(post, null);
			logger.info(checkResult);
			JsonMsg4Contacter msg = JSONObject.parseObject(checkResult,
					JsonMsg4Contacter.class);
			System.out.println(msg);
			if (msg.getStatus()) {
				result = msg.getData().getNormal_passengers();
			}
		} catch (Exception e) {
			logger.error("获取联系人异常", e);
			TicketMainFrame.remind("获取联系人异常,请稍后再试");
		}

		return result;
	}

	public List<TrainQueryInfo> queryLeftTicket(TicketConfigInfo configInfo,
			Map<String, String> cookieMap) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Constants.TICKETINIT_TRAIN_DATE,
				configInfo.getTrain_date()));
		params.add(new BasicNameValuePair(Constants.TICKETINIT_FROM_STATION,
				configInfo.getFrom_station()));
		params.add(new BasicNameValuePair(Constants.TICKETINIT_TO_STATION,
				configInfo.getTo_station()));
		params.add(new BasicNameValuePair(Constants.TICKETINIT_PURPOSE_CODES,
				configInfo.getPurpose_codes()));
		String paramsUrl = URLEncodedUtils.format(params, "UTF-8");
		HttpGet get = getHttpGet(UrlConstants.REQ_TIKETSEARCH_URL + "?"
				+ paramsUrl, cookieMap);
		HttpHeader.setCommonAjaxHeader(get);

		List<TrainQueryInfo> result = null;
		try {
			String checkResult = doGetRequest(get);
			logger.info(checkResult);
			JsonMsg4LeftTicket msg = JSONObject.parseObject(checkResult,
					JsonMsg4LeftTicket.class);
			if (msg.getStatus()) {
				result = msg.getData();
			}
		} catch (Exception e) {
			logger.error("查询余票异常", e);
			TicketMainFrame.remind("查询余票异常，继续查询");
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
		boolean checkCodeResult = checkLoginAuthcode(authcode);// 先检查验证码
		if (!checkCodeResult) {
			return "验证码不正确！";
		}

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Constants.LOGIN_PARAMS_USERNAME,
				username));
		params.add(new BasicNameValuePair(Constants.LOGIN_PARAMS_PASSWORD,
				password));
		params.add(new BasicNameValuePair(Constants.LOGIN_PARAMS_RANDCODE,
				authcode));
		params.add(new BasicNameValuePair(Constants.RANDCODE_VALIDATE, ""));
		params.add(new BasicNameValuePair("NDM1OTA5",
				"MGE4MTI4ZGJkODM4YjEwYQ=="));
		params.add(new BasicNameValuePair("myversion", "undefined"));

		HttpPost post = getHttpPost(UrlConstants.REQ_LOGINAYSNSUGGEST_URL, null);
		HttpHeader.setCommonAjaxHeader(post);

		String result = null;
		try {
			String checkResult = doPostRequest(post, params);
			JsonMsg4Login msg = JSONObject.parseObject(checkResult,
					JsonMsg4Login.class);
			logger.info(checkResult);

			if (!"Y".equals(msg.getData().getString("loginCheck"))) {// 登录不成功
				result = msg.getMessages()[0];
			}
		} catch (Exception e) {
			result = "登录异常!";
			logger.error("登录发生异常", e);
			TicketMainFrame.remind("登录发生异常,请联系管理员");
		}

		return result;
	}

	/**
	 * <退出>
	 * 
	 * @param username
	 * @param password
	 * @param authcode
	 * @return null代表成功，否则返回登录失败原因
	 */
	public void loginOut() {
		HttpPost post = getHttpPost(UrlConstants.REQ_LOGOUT_URL, null);
		HttpHeader.setLogoutHeader(post);

		try {
			String checkResult = doPostRequest(post, null);
			logger.info(checkResult);
		} catch (Exception e) {
			logger.error("退出发生异常", e);
		}
		TicketMainFrame.trace("退出成功!");
	}

	/**
	 * <公用POST请求方法>
	 * 
	 * @param http
	 * @param params
	 * @param isGzip
	 * @return
	 */
	private String doPostRequest(HttpPost request, List<NameValuePair> params) {
		// 获取请求参数
		HttpClient httpclient = buildHttpClient();
		String responseBody = null;

		InputStream is = null;
		try {
			if (params != null) {
				UrlEncodedFormEntity uef = new UrlEncodedFormEntity(params,
						"UTF-8");
				request.setEntity(uef);
				logger.info(URLEncodedUtils.format(params, "UTF-8"));
			}
			logger.debug("post request url={}", request.getURI().getRawQuery());
			HttpResponse response = httpclient.execute(request);
			Header[] headers = response.getAllHeaders();
			boolean isGzip = false;
			for (int i = 0; i < headers.length; i++) {
				if ("Content-Encoding".equals(headers[i].getName())
						&& "gzip".equals(headers[i].getValue())) {
					isGzip = true;
					break;
				}
			}
			is = response.getEntity().getContent();
			if (isGzip) {
				responseBody = zipInputStream(is);
			} else {
				responseBody = readInputStream(is);
			}

		} catch (Exception e) {
			logger.error("doPostRequest error:", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			httpclient.getConnectionManager().shutdown();
		}
		return responseBody;
	}

	private String doGetRequest(HttpGet request) {
		// 获取请求参数
		HttpClient httpclient = buildHttpClient();
		String responseBody = null;

		InputStream is = null;
		try {
			HttpResponse response = httpclient.execute(request);
			logger.debug("get request url={}", request.getURI().getRawQuery());
			Header[] headers = response.getAllHeaders();
			boolean isGzip = false;
			for (int i = 0; i < headers.length; i++) {
				if ("Content-Encoding".equals(headers[i].getName())
						&& "gzip".equals(headers[i].getValue())) {
					isGzip = true;
					break;
				}
			}
			is = response.getEntity().getContent();
			if (isGzip) {
				responseBody = zipInputStream(is);
			} else {
				responseBody = readInputStream(is);
			}

		} catch (Exception e) {
			logger.error("doPostRequest error:", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			httpclient.getConnectionManager().shutdown();
		}
		return responseBody;
	}

	/**
	 * 创建get请求
	 * 
	 */
	private HttpGet getHttpGet(String url, Map<String, String> cookieMap) {
		HttpGet get = new HttpGet(url);
		setCookie(get, cookieMap);
		return get;
	}

	/**
	 * 创建post请求
	 * 
	 */
	private HttpPost getHttpPost(String url, Map<String, String> cookieMap) {
		HttpPost post = new HttpPost(url);
		setCookie(post, cookieMap);
		return post;
	}

	private void setCookie(HttpRequestBase request,
			Map<String, String> cookieMap) {
		if (cookieMap != null && cookieMap.size() > 0) {
			String cookie = "JSESSIONID=" + JSESSIONID;
			for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
				cookie += "; " + entry.getKey() + "=" + entry.getValue();
			}
			cookie += "; BIGipServerotn=" + BIGipServerotn;
			logger.debug("request cooike={}", cookie);
			request.addHeader("Cookie", cookie);
		} else {
			request.addHeader("Cookie", "JSESSIONID=" + JSESSIONID
					+ ";BIGipServerotn=" + BIGipServerotn);
		}
	}

	/**
	 * 处理返回文件流
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private String readInputStream(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is,
				"UTF-8"));
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
	private String zipInputStream(InputStream is) throws IOException {
		GZIPInputStream gzip = new GZIPInputStream(is);
		BufferedReader in = new BufferedReader(new InputStreamReader(gzip,
				"UTF-8"));
		StringBuffer buffer = new StringBuffer();
		String line;
		while ((line = in.readLine()) != null)
			buffer.append(line + "\n");
		return buffer.toString();
	}
}
