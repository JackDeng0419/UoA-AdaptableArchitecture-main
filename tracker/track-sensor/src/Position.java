import java.io.Serializable;

public class Position implements Serializable {

    private double latitude;
    private double longitude;
    private double altitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public Position() {
        this.latitude = 0.0D;
        this.longitude = 0.0D;
        this.altitude = 0.0D;
    }
}
