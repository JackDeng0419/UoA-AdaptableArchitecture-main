import java.util.ArrayList;
import java.util.List;

public final class TrackRequests {

    private static List<TrackRequest> trackRequests = new ArrayList<>();


    private TrackRequests() {
        // Avoid default constructor - by setting this private

    }

    public static List<TrackRequest> getTrackRequests() {
        return trackRequests;
    }

    static void add(String theRequest, SensorTrack theTrack) {
        TrackRequest newTrackRequest= new TrackRequest();
        newTrackRequest.setRequestType(theRequest);
        newTrackRequest.setTrackId(theTrack.getTrackId());
        newTrackRequest.setLatitude(theTrack.getLatitude());
        newTrackRequest.setLongitude(theTrack.getLongitude());
        newTrackRequest.setAltitude(theTrack.getAltitude());
        newTrackRequest.setSpeed(theTrack.getSpeed());
        newTrackRequest.setCourse(theTrack.getCourse());
        trackRequests.add(newTrackRequest);
    }

}
