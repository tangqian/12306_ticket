package com.free.app.ticket.model;

public enum JsonSeatTypeEnum {
    GR("高级软卧"), QT("其它"), RW("软卧"), RZ("软座"), TZ("特等座"), WZ("无座"), YB("未知座"), YW("硬卧"), YZ("硬座"), ZE("二等座"), ZY("一等座"), SWZ(
        "商务座");
    
    private String label;
    
    private JsonSeatTypeEnum(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
}
