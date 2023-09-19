import java.io.Serializable;

public class TrackRequest extends SensorTrack implements Serializable {

    public TrackRequest() {
        this.requestType = "Unknown";
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    private String requestType;

}
