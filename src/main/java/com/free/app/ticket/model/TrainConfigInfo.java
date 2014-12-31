package com.free.app.ticket.model;

import java.util.List;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.model.PassengerData.SeatType;
import com.free.app.ticket.model.TrainData4UI.BuyModel;
import com.free.app.ticket.model.TrainData4UI.SeatOptionType;
import com.free.app.ticket.model.TrainData4UI.UserTrainInfo;


public class TrainConfigInfo {
    
    private BuyModel model;
    
    private List<SpecificTrainInfo> trains;
    
    public TrainConfigInfo(UserTrainInfo[] userTrains) {
        SpecificTrainInfo train;
        for (UserTrainInfo trainInfo : userTrains) {
            SeatOptionType optionType = SeatOptionType.getType(trainInfo.getTrainCode().charAt(0));
            if(optionType == null){
                TicketMainFrame.remind("精确购买车次[" + trainInfo.getTrainCode() + "]不正确，请检查。车次开头只能是D、C、G、Z、T、K、L");
            }else{
                train = new SpecificTrainInfo(trainInfo.getTrainCode(), optionType, trainInfo.getBestSeatType(), trainInfo.getWorstSeatType());
            }
        }
    }
    
    public static class SpecificTrainInfo{

        private String trainCode;
        
        private List<SeatType> seatPerfers;

        public SpecificTrainInfo(String trainCode, SeatOptionType optionType, String bestSeat, String worstSeat) {
            super();
            this.trainCode = trainCode;
            if(optionType == SeatOptionType.GAOTIE){
                
            }
        }
        
        public String getTrainCode() {
            return trainCode;
        }

        public void setSeatPerfers(List<SeatType> seatPerfers) {
            this.seatPerfers = seatPerfers;
        }

        public List<SeatType> getSeatPerfers() {
            return seatPerfers;
        }

    }
    
    
}
