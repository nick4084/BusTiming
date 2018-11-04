package ntu.bustiming.entity;

import java.sql.Time;
import java.util.BitSet;

/**
 * This class is an entity class for notification
 */
public class Notification {
    private int id;
    private String name;
    private int busstop_code;
    private int bus_code;
    private int ntf_hour;
    private int ntf_minute;
    private boolean isActivated=false;
    private BitSet ntf_days; //bitmap
    private String days[] = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};


    public Notification() {

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBusstop_code() {
        return busstop_code;
    }

    public void setBusstop_code(int busstop_code) {
        this.busstop_code = busstop_code;
    }

    public int getBus_code() {
        return bus_code;
    }

    public void setBus_code(int bus_code) {
        this.bus_code = bus_code;
    }

    public int getNtf_hour() {
        return ntf_hour;
    }

    public void setNtf_hour(int ntf_hour) {
        this.ntf_hour = ntf_hour;
    }

    public int getNtf_minute() {
        return ntf_minute;
    }

    public void setNtf_minute(int ntf_minute) {
        this.ntf_minute = ntf_minute;
    }

    public BitSet getNtf_days() {
        return ntf_days;
    }

    public void setNtf_days(BitSet ntf_days) {
        this.ntf_days = ntf_days;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public String displayTime(){
        return ""+ntf_hour+":"+ntf_minute;
    }


    /**
     * This method converts the bitset into a string that contains the days in a week
     * @return The string that contains the days in a week
     */
    public String displayDays(){
        String result="";
        for(int i=0;i<7;i++){
            if(ntf_days.get(i)){
                if(result.equals("")){
                    result+=days[i];
                }else{
                    result+=", "+days[i];
                }
            }
        }
        if(result.equals("")){
            return "None";
        }
        return result;
    }

}
