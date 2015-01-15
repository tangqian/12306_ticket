package com.free.app.ticket.model;

import java.util.ArrayList;
import java.util.List;

import com.free.app.ticket.TicketMainFrame;
import com.free.app.ticket.model.PassengerData.SeatType;
import com.free.app.ticket.model.TrainData4UI.BuyModel;
import com.free.app.ticket.model.TrainData4UI.SeatOptionType;
import com.free.app.ticket.model.TrainData4UI.UserTrainInfo;


public class TrainConfigInfo {
    
    private BuyModel model;
    
    private List<SpecificTrainInfo> trains;
    
    public TrainConfigInfo(List<UserTrainInfo> userTrains) {
        trains = new ArrayList<SpecificTrainInfo>();
        for (UserTrainInfo trainInfo : userTrains) {
            SeatOptionType optionType = SeatOptionType.getType(trainInfo.getTrainCode().charAt(0));
            if(optionType == null){
                TicketMainFrame.remind("精确购买车次[" + trainInfo.getTrainCode() + "]不正确，请检查。车次开头只能是D、C、G、Z、T、K、L");
            }else{
                SpecificTrainInfo train = new SpecificTrainInfo(trainInfo.getTrainCode(), optionType, trainInfo.getBestSeatType(), trainInfo.getWorstSeatType());
                trains.add(train);
            }
        }
    }
    
    public BuyModel getModel() {
        return model;
    }

    public List<SpecificTrainInfo> getTrains() {
        return trains;
    }

    public static class SpecificTrainInfo{

        private String trainCode;
        
        private List<SeatType> seatPerfers;

        public SpecificTrainInfo(String trainCode, List<SeatType> seatPerfers) {
            super();
            this.trainCode = trainCode;
            this.seatPerfers = seatPerfers;
        }
        
        public SpecificTrainInfo(String trainCode, SeatOptionType optionType, String bestSeat, String worstSeat) {
            super();
            this.trainCode = trainCode;
            if(optionType == SeatOptionType.GAOTIE){
                
            }
            else {
                
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

        public TrainInfo convertToTrainInfo() {
            TrainInfo trainInfo = new TrainInfo();
            trainInfo.setStation_train_code(this.trainCode);
            
            return trainInfo;
        }
        
        public SpecificTrainInfo convertFromTrainInfo(TrainInfo trainInfo) {
            List<SeatType> theSeatPerfers = new ArrayList<SeatType>();
            SeatOptionType optionType = SeatOptionType.getType(trainInfo.getStation_train_code().charAt(0));
            if (optionType == SeatOptionType.GAOTIE) {
                theSeatPerfers.add(SeatType.TWO_SEAT);
                theSeatPerfers.add(SeatType.ONE_SEAT);
                theSeatPerfers.add(SeatType.NONE_SEAT);
            }
            else {
                theSeatPerfers.add(SeatType.HARD_SEAT);
                theSeatPerfers.add(SeatType.HARD_SLEEPER);
                theSeatPerfers.add(SeatType.NONE_SEAT);
            }
            return new SpecificTrainInfo(trainInfo.getStation_train_code(), theSeatPerfers);
        }
        
    }
    
    
}
