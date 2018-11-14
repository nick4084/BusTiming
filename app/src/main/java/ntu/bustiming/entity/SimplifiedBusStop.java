package ntu.bustiming.entity;

/**
 * This is the simplified bus stop class
 */
public class SimplifiedBusStop implements Comparable<SimplifiedBusStop>{

        private String busStopCode;
        private String RoadName;
        private String Description;
        public SimplifiedBusStop() {
        }

    /**
     * This is the constructor for the simplified bus stop class
     * @param busStopCode The bus stop code
     * @param roadName The road name
     * @param description The description of the bus stop
     */
        public SimplifiedBusStop(String busStopCode, String roadName, String description) {
            this.busStopCode = busStopCode;
            RoadName = roadName;
            Description = description;
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

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

    /**
     * This method will compare both bus stops by description
     * @param s The other bus stop for comparison
     * @return -1 if lower, 1 if higher and 0 if equal
     */
        public int compareTo(SimplifiedBusStop s){
            return this.getDescription().compareTo(s.getDescription());
        }

}
