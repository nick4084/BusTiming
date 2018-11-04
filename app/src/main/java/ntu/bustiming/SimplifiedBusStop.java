package ntu.bustiming;

public class SimplifiedBusStop implements Comparable<SimplifiedBusStop>{

        private String busStopCode;
        private String RoadName;
        private String Description;
        public SimplifiedBusStop() {
        }

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

        public int compareTo(SimplifiedBusStop s){
            return this.getDescription().compareTo(s.getDescription());
        }

}
