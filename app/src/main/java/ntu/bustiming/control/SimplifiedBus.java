package ntu.bustiming.control;


public class SimplifiedBus {

    private String serviceNo;
    private String busStopCode;
    private String RoadName;
    private String Direction;

    public SimplifiedBus() {
    }

    public SimplifiedBus(String serviceNo, String busStopCode, String roadName, String direction) {
        this.serviceNo = serviceNo;
        this.busStopCode = busStopCode;
        RoadName = roadName;
        Direction = direction;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
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


}
