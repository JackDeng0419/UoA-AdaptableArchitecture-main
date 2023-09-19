import java.io.Serializable;

public class SensorTrack  extends Position implements Serializable {

    private String name;
    private long timestamp;
    private long trackId;
    private long sensorId;
    private double speed;
    private double course;
    private boolean reported;

    public boolean isReported() {
        return reported;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }

    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getCourse() {
        return course;
    }

    public void setCourse(double course) {
        this.course = course;
    }

    public SensorTrack() {
        this.name = "Txxxx";
        this.timestamp = 0L;
        this.trackId = 0L;
        this.sensorId = 0L;
        this.reported = false;
    }
}

