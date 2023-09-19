import org.apache.kafka.common.utils.Time;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class SensorTracks implements Serializable {

    private static List<SensorTrack> trackMap = new ArrayList<>();

    private SensorTracks() {
        // Avoid default constructor - by setting this private
    }

    private static final double METRES_PER_DEGREE = 111139.0D;

    public static List<SensorTrack> getTrackMap() {
        return trackMap;
    }

    public static void createTracks() {

        for (long i=0; i<1000; i++) {

            SensorTrack st1 = new SensorTrack();
            st1.setTrackId(trackMap.size());
            st1.setTimestamp(Time.SYSTEM.milliseconds());
            st1.setLatitude(-35.72496);
            st1.setLongitude(138.11785);
            st1.setAltitude(0.0);
            trackMap.add(st1);

            SensorTrack st2 = new SensorTrack();
            st2.setTrackId(trackMap.size());
            st2.setTimestamp(Time.SYSTEM.milliseconds());
            st2.setLatitude(-36.17368);
            st2.setLongitude(138.35938);
            st2.setAltitude(0.0);
            trackMap.add(st2);

            SensorTrack st3 = new SensorTrack();
            st3.setTrackId(trackMap.size());
            st3.setTimestamp(Time.SYSTEM.milliseconds());
            st3.setLatitude(-36.55892);
            st3.setLongitude(136.48365);
            st3.setAltitude(0.0);
            trackMap.add(st3);

            SensorTrack st4 = new SensorTrack();
            st4.setTrackId(trackMap.size());
            st4.setTimestamp(Time.SYSTEM.milliseconds());
            st4.setLatitude(-35.58436);
            st4.setLongitude(136.35503);
            st4.setAltitude(0.0);
            trackMap.add(st4);

            SensorTrack st5 = new SensorTrack();
            st5.setTrackId(trackMap.size());
            st5.setTimestamp(Time.SYSTEM.milliseconds());
            st5.setLatitude(-35.35978);
            st5.setLongitude(137.74308);
            st5.setAltitude(0.0);
            trackMap.add(st5);

        }

        System.out.print("SensorTracks created ");
        System.out.print(trackMap.size());
        System.out.println(" new tracks.");
    }

}
