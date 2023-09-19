import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static Logger log = LogManager.getLogger(Main.class);

    private static boolean operational;

    private static void start() {

        operational = true;

        Configuration config = new Configuration();
        TrackRequests allTrackRequests;
        SensorTrackConsumer sensorTrackConsumer = new SensorTrackConsumer(config);
        TrackRequestor trackRequestor = new TrackRequestor(config);

        while(operational) {
            sensorTrackConsumer.run();
            trackRequestor.run();
        }
    }

    public static void main(String[] args) {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                operational = false;
                //System.out.println("Shutting down");
                log.info("Shutting down");
            }));

            start();
        } catch (Exception e) {
            //System.out.println(e);
            log.debug("context", e);
        }
    }
}