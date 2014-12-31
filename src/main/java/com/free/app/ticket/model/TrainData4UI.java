package com.free.app.ticket.model;

import java.io.Serializable;
import java.util.List;

import javax.swing.JComboBox;

import com.free.app.ticket.model.PassengerData.SeatType;

public class TrainData4UI implements Serializable {
    
    private BuyModel model;
    
    private UserTrainInfo[] userTrains;
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -6942330093086661308L;
    
    public enum BuyModel {
        Precise("精确买票"), Fuzzy("范围买票");
        
        public String getLabel() {
            return label;
        }
        
        private String label;
        
        private BuyModel(String label) {
            this.label = label;
        }
    }
    
    public enum SeatOptionType {
        GAOTIE("高铁席别") {
            @Override
            public void initItem(JComboBox comboBox, String itemSelected) {
                comboBox.removeAllItems();
                for (String s : GAOTIEITEMS) {
                    comboBox.addItem(s);
                }
                if (itemSelected != null)
                    comboBox.setSelectedItem(itemSelected);
                else
                    comboBox.setSelectedItem(GAOTIEITEMS[0]);
            }
            
            @Override
            public SeatType[] caculateSeatTypeInSort(String bestItem, String worstItem) {
                SeatType[] result = null;
                SeatType bestType = getSeatType(bestItem);
                SeatType worstType = getSeatType(worstItem);
                if (bestType != null && worstType != null) {
                    result = bestType.ordinal() > worstType.ordinal() ? SeatType.getSortValues(SeatType.TWO_SEAT, worstType) : null;
                    if (bestType.ordinal() > worstType.ordinal()) {
                        result = new SeatType[bestType.ordinal() - worstType.ordinal() + 1];
                        //SeatType.
                    }
                }
                return null;
            }
            
            @Override
            public SeatType getSeatType(String itemName) {
                SeatType type = null;
                if ("二等座".equals(itemName)) {
                    type = SeatType.TWO_SEAT;
                }
                else if ("一等座".equals(itemName)) {
                    type = SeatType.ONE_SEAT;
                }
                else if ("商务座/特等座".equals(itemName)) {
                    type = SeatType.BUSS_SEAT;
                }
                return type;
            }
            
        },
        OTHER("其它席别") {
            @Override
            public void initItem(JComboBox comboBox, String itemSelected) {
                comboBox.removeAllItems();
                for (String s : OTHERITEMS) {
                    comboBox.addItem(s);
                }
                if (itemSelected != null)
                    comboBox.setSelectedItem(itemSelected);
                else
                    comboBox.setSelectedItem(GAOTIEITEMS[0]);
            }
            
            @Override
            public SeatType[] caculateSeatTypeInSort(String bestItem, String worstItem) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public SeatType getSeatType(String itemName) {
                SeatType type = null;
                if ("硬座".equals(itemName)) {
                    type = SeatType.HARD_SEAT;
                }
                else if ("软座".equals(itemName)) {
                    type = SeatType.SOFT_SEAT;
                }
                else if ("硬卧".equals(itemName)) {
                    type = SeatType.HARD_SLEEPER;
                }
                else if ("软卧".equals(itemName)) {
                    type = SeatType.SOFT_SLEEPER;
                }
                else if ("高级软卧".equals(itemName)) {
                    type = SeatType.VAG_SLEEPER;
                }
                return type;
            }
        };
        
        private String label;
        
        private static String[] GAOTIEITEMS =
        {"二等座", "一等座", "商务座/特等座"};
        
        private static String[] OTHERITEMS =
        {"硬座", "软座", "硬卧", "软卧", "高级软卧"};
        
        private SeatOptionType(String label) {
            this.label = label;
        }
        
        public static SeatOptionType getType(char firstChar) {
            SeatOptionType type = null;
            switch (firstChar) {
                case 'D':
                case 'C':
                case 'G':
                    type = SeatOptionType.GAOTIE;
                    break;
                case 'Z':
                case 'T':
                case 'K':
                case 'L':
                    type = SeatOptionType.OTHER;
                    break;
                default:
                    break;
            }
            return type;
        }
        
        public abstract void initItem(JComboBox box, String itemSelected);
        
        public abstract SeatType[] caculateSeatTypeInSort(String bestItem, String worstItem);
        
        public abstract SeatType getSeatType(String itemName);
        
        public String getLabel() {
            return label;
        }
    }
    
    public static class UserTrainInfo implements Serializable {
        
        /**
         * 注释内容
         */
        private static final long serialVersionUID = -2434913538729720962L;
        
        public String getTrainCode() {
            return trainCode;
        }
        
        public String getBestSeatType() {
            return bestSeatType;
        }
        
        public String getWorstSeatType() {
            return worstSeatType;
        }
        
        private String trainCode;
        
        private String bestSeatType;
        
        private String worstSeatType;
        
        public UserTrainInfo(String trainCode, String bestSeatType, String worstSeatType) {
            super();
            this.trainCode = trainCode;
            this.bestSeatType = bestSeatType;
            this.worstSeatType = worstSeatType;
        }
        
    }
    
    public void setModel(BuyModel model) {
        this.model = model;
    }
    
    public BuyModel getModel() {
        return model;
    }
    
    public void setUserTrains(UserTrainInfo[] userTrains) {
        this.userTrains = userTrains;
    }
    
    public UserTrainInfo[] getUserTrains() {
        return userTrains;
    }
}
