package com.free.app.ticket.model;

import java.util.List;

public class TicketBuyInfo {

	private TicketConfigInfo configInfo;

	private List<PassengerData> passengers;
	
	private String currentBuySeat;

	public TicketBuyInfo(TicketConfigInfo configInfo, List<PassengerData> passengers) {
		super();
		this.configInfo = configInfo;
		this.passengers = passengers;
	}

	public List<PassengerData> getPassengers() {
		return passengers;
	}
	
	public TicketConfigInfo getConfigInfo() {
		return configInfo;
	}

    public void setCurrentBuySeat(String currentBuySeat) {
        this.currentBuySeat = currentBuySeat;
    }

    public String getCurrentBuySeat() {
        return currentBuySeat;
    }

	
	
}
