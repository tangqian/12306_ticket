package com.free.app.ticket.model;

import java.util.List;

public class TicketBuyInfo {

	private TicketConfigInfo ticketConfigInfo;
	
	private TrainConfigInfo trainConfigInfo = null;

	private List<PassengerData> passengers;
	
	private String currentBuySeat;

	public TicketBuyInfo(TicketConfigInfo ticketConfigInfo, List<PassengerData> passengers) {
	    super();
        this.ticketConfigInfo = ticketConfigInfo;
        this.passengers = passengers;
    }
	
	public TicketBuyInfo(TicketConfigInfo ticketConfigInfo, List<PassengerData> passengers, TrainConfigInfo trainConfigInfo) {
		super();
		this.ticketConfigInfo = ticketConfigInfo;
		this.passengers = passengers;
		this.trainConfigInfo = trainConfigInfo;
	}

	public List<PassengerData> getPassengers() {
		return passengers;
	}
	
	public TicketConfigInfo getTicketConfigInfo() {
		return ticketConfigInfo;
	}

    public void setCurrentBuySeat(String currentBuySeat) {
        this.currentBuySeat = currentBuySeat;
    }

    public String getCurrentBuySeat() {
        return currentBuySeat;
    }

    public TrainConfigInfo getTrainConfigInfo() {
        return trainConfigInfo;
    }

	
	
}
