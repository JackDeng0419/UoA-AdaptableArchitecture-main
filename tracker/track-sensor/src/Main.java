import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Main {

    private static Logger log = LogManager.getLogger(Main.class);

    private static boolean operational;

    private static void start() {

        operational = true;

        System.out.println("Starting Up");
        log.info("Starting Up");

        System.out.println("Creating config");
        Configuration config = new Configuration();
        System.out.println("Creating sensorTrackProducer");
        SensorTrackProducer sensorTrackProducer = new SensorTrackProducer(config);

        System.out.println("Starting Loop");

        while(operational) {
            sensorTrackProducer.run();
        }

    }

    public static void main(String[] args) {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                operational = false;

                log.info("Shutting down");
            }));

            start();
        } catch (Exception e) {
            log.debug("context", e);
        }
    }
}

