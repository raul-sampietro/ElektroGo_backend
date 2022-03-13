package elektroGo.back.data.Gateways;

import java.math.BigDecimal;

public class GatewayChargingStations {
    private int id;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private int numberOfChargers;

    public GatewayChargingStations() {

    }

    public GatewayChargingStations(int id, BigDecimal latitude, BigDecimal longitude, int numberOfChargers) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numberOfChargers = numberOfChargers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public int getNumberOfChargers() {
        return numberOfChargers;
    }

    public void setNumberOfChargers(int numberOfChargers) {
        this.numberOfChargers = numberOfChargers;
    }
}
