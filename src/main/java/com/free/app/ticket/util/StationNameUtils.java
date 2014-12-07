package com.free.app.ticket.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StationNameUtils {

	/**
	 * 获取站点名称对应code地址
	 */
	//private static String url = "https://kyfw.12306.cn/otn/resources/js/framework/station_name.js";

	private static Map<String, String> cityName2Code = new HashMap<String, String>();

	private static final Logger logger = LoggerFactory
			.getLogger(TicketHttpClient.class);

	static {
		InputStream is = StationNameUtils.class
				.getResourceAsStream("/station_name.txt");
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			String line = null;
			while ((line = in.readLine()) != null) {
				String temp[] = line.split("\\|");
				cityName2Code.put(temp[1], temp[2]);
			}
			logger.debug("初始化站点名称对应code成功!");
		} catch (Exception e) {
			logger.error("初始化站点名称对应code发现异常", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	/**
	 * 基于车站名称获取对应的车站代码 注意填写精确的乘车车站名称，如"北京西"和"北京"是两个不同代码
	 * 
	 * @param cityName
	 * @return
	 */
	public static String getCityCode(String cityName) {
		return cityName2Code.get(cityName);
	}
}
