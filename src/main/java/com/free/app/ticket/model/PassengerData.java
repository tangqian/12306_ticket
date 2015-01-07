/*
 * 12306-hunter: Java Swing C/S版本12306订票助手
 * 本程序完全开放源代码，仅作为技术学习交流之用，不得用于任何商业用途
 * 个人可自由免费使用或二次开发，自行承担任何相关责任
 */
package com.free.app.ticket.model;

import java.io.Serializable;

/**
 * 乘客数据对象
 */
public class PassengerData implements Serializable {
    
    private static final long serialVersionUID = -4047393628292653857L;
    
    private String cardNo;
    
    private CardType cardType = CardType.IDENTITY;
    
    private String name;
    
    private String mobile;
    
    private TicketType ticketType = TicketType.ADULT;
    
    private SeatType seatType = SeatType.TWO_SEAT;
    
    public enum TicketType {
        ADULT("成人票", "1"), CHILD("儿童票", "2"), STUDENT("学生票", "3"), SOLDIER("残军票", "4");
        
        private String label;
        
        private String value;
        
        private TicketType(String label, String value) {
            this.label = label;
            this.value = value;
        }
        
        public String getValue() {
            return this.value;
        }
        
        public String toString() {
            return label;
        }
    }
    
    public enum CardType {
        IDENTITY("二代身份证", "1"), GAPASSPORT("港澳通行证", "C"), TPASSPORT("台湾通行证", "G"), PASSPORT("护照", "B");
        
        private String label;
        
        private String value;
        
        private CardType(String label, String value) {
            this.label = label;
            this.value = value;
        }
        
        public String getValue() {
            return this.value;
        }
        
        public String toString() {
            return label;
        }
    }
    
    public enum SeatType {
        BUSS_SEAT("商务座", "9"), BEST_SEAT("特等座", "P"), ONE_SEAT("一等座", "M"), TWO_SEAT("二等座", "O"), VAG_SLEEPER("高级软卧",
            "6"), SOFT_SLEEPER("软卧", "4"), HARD_SLEEPER("硬卧", "3"), SOFT_SEAT("软座", "2"), HARD_SEAT("硬座", "1"), NONE_SEAT(
            "无座", "-1"), OTH_SEAT("其他", "0");
        
        private String label;
        
        private String value;
        
        private SeatType(String label, String value) {
            this.label = label;
            this.value = value;
        }
        
        public String getValue() {
            return this.value;
        }
        
        public String toString() {
            return label;
        }
        
        public String getLabel() {
            return label;
        }
        
        /**
         * <根据value获取enum>
         * @param value
         * @return
         */
        public static SeatType getTypeByValue(String value) {
            SeatType ret = null;
            for (SeatType type : SeatType.values()) {
                if (type.getValue().equals(value)) {
                    ret = type;
                    break;
                }
            }
            return ret;
        }
        
        /**
         * <获取有序的席别数组>
         * @param startOrdinal
         * @param endOrdinal
         * @return
         */
        public static SeatType[] getSortValues(SeatType startType, SeatType endType) {
            SeatType[] ret = null;
            SeatType[] values = SeatType.values();
            int startOrdinal = startType.ordinal();
            int endOrdinal = endType.ordinal();
            if (startOrdinal > endOrdinal) {
                ret = new SeatType[startOrdinal - endOrdinal + 1];
                int pos = 0;
                for (int i = startOrdinal; i >= endOrdinal; i--) {
                    ret[pos++] = values[i];
                }
            }
            else {
                ret = new SeatType[endOrdinal - startOrdinal + 1];
                int pos = 0;
                for (int i = startOrdinal; i <= endOrdinal; i++) {
                    ret[pos++] = values[i];
                }
            }
            
            return ret;
        }
    }
    
    public PassengerData(String name, String cardNo, String mobile) {
        this.name = name;
        this.cardNo = cardNo;
        this.mobile = mobile;
    }
    
    public String getCardNo() {
        return cardNo;
    }
    
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    
    public CardType getCardType() {
        return cardType;
    }
    
    public String getCardTypeValue() {
        return cardType.getValue();
    }
    
    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getMobile() {
        return mobile;
    }
    
    public String getMobileNotNull() {
        if (mobile == null)
            return "";
        return mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    public TicketType getTicketType() {
        return ticketType;
    }
    
    public String getTicketTypeValue() {
        return ticketType.getValue();
    }
    
    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }
    
    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }
    
    public SeatType getSeatType() {
        return seatType;
    }
    
    public String getSeatTypeValue() {
        return seatType.getValue();
    }

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		PassengerData passenger = (PassengerData)obj;
		if (passenger.cardNo.equals(this.cardNo)) {
			return true;
		}
		return false;
	}
    
}
