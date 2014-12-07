/*
 * 12306-hunter: Java Swing C/S版本12306订票助手
 * 本程序完全开放源代码，仅作为技术学习交流之用，不得用于任何商业用途
 */
package com.free.app.ticket.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 保存主窗口UI初始数据对象
 */
public class MixData4UI implements Serializable {

	private static final long serialVersionUID = 5912310187954784584L;

	private String trainFrom;
	
	private String trainTo;
	
	private String userName;

	// 乘车人数据集合
	private List<PassengerData> passengerDatas = new ArrayList<PassengerData>();

	public String getTrainFrom() {
		return trainFrom;
	}

	public void setTrainFrom(String trainFrom) {
		this.trainFrom = trainFrom;
	}

	public String getTrainTo() {
		return trainTo;
	}

	public void setTrainTo(String trainTo) {
		this.trainTo = trainTo;
	}

	public List<PassengerData> getPassengerDatas() {
		return passengerDatas;
	}

	public void setPassengerDatas(List<PassengerData> passengerDatas) {
		this.passengerDatas = passengerDatas;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}
