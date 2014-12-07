package com.free.app.ticket.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.model.TicketBuyInfo;
import com.free.app.ticket.model.TicketConfigInfo;
import com.free.app.ticket.model.TrainInfo;
import com.free.app.ticket.model.JsonMsg4LeftTicket.TrainQueryInfo;
import com.free.app.ticket.util.DateUtils;
import com.free.app.ticket.util.TicketHttpClient;
import com.free.app.ticket.util.constants.Constants;

public class AutoBuyThreadService extends Thread {

	private static final Logger logger = LoggerFactory
			.getLogger(AutoBuyThreadService.class);

	private TicketBuyInfo buyInfo;

	private boolean isSuccess = false;

	private Map<String, String> cookies;

	public AutoBuyThreadService(TicketBuyInfo ticketBuyInfo) {
        this.buyInfo = ticketBuyInfo;
        cookies = getCookie(ticketBuyInfo.getConfigInfo());
    }

	@Override
	public void run() {
		while (!isSuccess) {
			TicketHttpClient client = HttpClientThreadService.getHttpClient();
			List<TrainInfo> trainInfos = client.queryLeftTicket(buyInfo.getConfigInfo(), cookies);
			if(trainInfos == null)
				continue;
			
			List<TrainInfo> canBuyTrain = new ArrayList<TrainInfo>();
			List<TrainInfo> notBuyTrain = new ArrayList<TrainInfo>();
			for (TrainInfo train : trainInfos) {
				if(train.getCanWebBuy().equals("Y")){
					canBuyTrain.add(train);
				}else{
					notBuyTrain.add(train);
				}
			}
			
			showNotBuyTrains(notBuyTrain);
			
			break;
		}

	}

	private void showNotBuyTrains(List<TrainInfo> notBuyTrain) {
		if(notBuyTrain.isEmpty())
			return;
		
		String msg = "";
		for (TrainInfo trainInfo : notBuyTrain) {
			msg += trainInfo.getStation_train_code() + ",";
		}
		msg = msg.substring(0, msg.length() -1);
		TicketMainFrame.trace("已售完车次有【" + msg + "】");
	}

	private Map<String, String> getCookie(TicketConfigInfo config) {
		Map<String, String> cookies = new HashMap<String, String>();
		cookies.put(Constants._jc_save_fromStation, getUnicode4Cookie(config.getFrom_station_name(),config.getFrom_station()));
		cookies.put(Constants._jc_save_toStation,getUnicode4Cookie(config.getTo_station_name(),config.getTo_station()));
		cookies.put(Constants._jc_save_fromDate, config.getTrain_date());
		cookies.put(Constants._jc_save_toDate, DateUtils.formatDate(new Date()));
		cookies.put(Constants._jc_save_wfdc_flag, "dc");
		return cookies;
	}
	
	public static String getUnicode4Cookie(String cityName, String cityCode) {
		String result = "";
		for (int i = 0; i < cityName.length(); i++) {
			int chr1 = (char) cityName.charAt(i);
			if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
				result += "%u" + Integer.toHexString(chr1).toUpperCase();
			} else {
				result += cityName.charAt(i);
			}
		}
		result += "%2C"+cityCode;
		return result;
	}
}
