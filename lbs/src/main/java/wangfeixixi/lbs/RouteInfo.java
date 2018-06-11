package wangfeixixi.lbs;

public class RouteInfo {
    public float distance;
    public float taxiCost;
    public int duration;

    public RouteInfo(float distance, float taxiCost, int duration) {
        this.distance = distance;
        this.taxiCost = taxiCost;
        this.duration = duration;
    }

    public RouteInfo() {

    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getTaxiCost() {
        return taxiCost;
    }

    public void setTaxiCost(float taxiCost) {
        this.taxiCost = taxiCost;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
