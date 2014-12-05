package com.free.app.ticket.model;

/**
 * 
 * 联系人信息实体类
 * 
 */
public class ContacterInfo {

    private String passenger_name;
    
    private String passenger_id_type_code;//1
    
    private String passenger_id_type_name;//二代身份证
    
    private String passenger_id_no;//身份证号
    
    private String passenger_type;//1
    
    private String passenger_type_name;//成人
    
    private String mobile_no;
    
    public ContacterInfo() {
    }
    
    public ContacterInfo(String passengerName, String passengerIdNo, String mobileNo) {
        super();
        passenger_name = passengerName;
        passenger_id_no = passengerIdNo;
        mobile_no = mobileNo;
    }

    public void setPassenger_name(String passenger_name) {
        this.passenger_name = passenger_name;
    }
    
    public String getPassenger_name() {
        return passenger_name;
    }
    
    public void setPassenger_id_no(String passenger_id_no) {
        this.passenger_id_no = passenger_id_no;
    }
    
    public String getPassenger_id_no() {
        return passenger_id_no;
    }
    
    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }
    
    public String getMobile_no() {
        return mobile_no;
    }
    
    public void setPassenger_id_type_code(String passenger_id_type_code) {
        this.passenger_id_type_code = passenger_id_type_code;
    }
    
    public String getPassenger_id_type_code() {
        return passenger_id_type_code;
    }
    
    public void setPassenger_id_type_name(String passenger_id_type_name) {
        this.passenger_id_type_name = passenger_id_type_name;
    }
    
    public String getPassenger_id_type_name() {
        return passenger_id_type_name;
    }
    
    public void setPassenger_type(String passenger_type) {
        this.passenger_type = passenger_type;
    }
    
    public String getPassenger_type() {
        return passenger_type;
    }
    
    public void setPassenger_type_name(String passenger_type_name) {
        this.passenger_type_name = passenger_type_name;
    }
    
    public String getPassenger_type_name() {
        return passenger_type_name;
    }
    
    
    @Override
    public String toString() {
        return "ContacterInfo [passenger_name=" + passenger_name + ", passenger_type_name=" + passenger_type_name
            + ", passenger_id_no=" + passenger_id_no + ", mobile_no=" + mobile_no + "]";
    }

    
}
