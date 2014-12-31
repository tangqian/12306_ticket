package com.free.app.ticket;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.free.app.ticket.model.PassengerData.SeatType;
import com.free.app.ticket.util.TicketHttpClient;

public class Test {
    
    private static final Pattern PATTERN_DYNAMIC_JS = Pattern.compile("/otn/dynamicJs/(\\w+)");
    
    /** <一句话功能简述>
     * @param args
     */
    public static void main(String[] args) {
        SeatType a = SeatType.TWO_SEAT;
        System.out.println(a.ordinal());
        System.out.println(SeatType.BUSS_SEAT.ordinal());
        System.out.println(SeatType.BEST_SEAT.ordinal());
        /*System.out.println(SeatType.NONE_SEAT > SeatType.BEST_SEAT);*/
        
        Matcher m_token = PATTERN_DYNAMIC_JS.matcher("fdsafds/otn/dynamicJs/fdsafdff\"");
        if (m_token.find()) {
            String s = m_token.group(1);
            System.out.println(s);
        }
        
        TicketHttpClient httpClient = TicketHttpClient.getInstance();
        httpClient.testRequest();
    }
    
}
