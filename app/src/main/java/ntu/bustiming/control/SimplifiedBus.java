package ntu.bustiming.control;


public class SimplifiedBus {

    private String serviceNo;
    private String busStopCode;
    private String RoadName;
    private String Direction;
    private String Discription;
    private String Stopsequence;

    public SimplifiedBus() {
    }

    public SimplifiedBus(String serviceNo, String busStopCode, String roadName, String direction,String Discription,String Stopsequence) {
        this.serviceNo = serviceNo;
        this.busStopCode = busStopCode;
        RoadName = roadName;
        Direction = direction;
        this.Discription = Discription;
        this.Stopsequence = Stopsequence;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getStopsequence() {
        return Stopsequence;
    }

    public void setStopsequence(String stopsequence) {
        Stopsequence = stopsequence;
    }

    public String getBusStopCode() {
        return busStopCode;
    }

    public void setBusStopCode(String busStopCode) {
        this.busStopCode = busStopCode;
    }

    public String getRoadName() {
        return RoadName;
    }

    public void setRoadName(String roadName) {
        RoadName = roadName;
    }

    public String getDirection() {
        return Direction;
    }

    public void setDirection(String direction) {
        Direction = direction;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }
}
