import java.time.Duration;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SensorTrackConsumer {

    private static Logger log = LogManager.getLogger(SensorTrackConsumer.class);
    private static final String CONTINUING_AFTER_EXCEPTION = "Exception: %s, continuing%n";
    private String bootstrapServer = null;
    private String kafkaSensorTracksTopic = null;
    private KafkaConsumer<String, byte[]> consumer;

    private static KnownTracks knownTracks;

    TrackRequests allTrackRequests;

    public SensorTrackConsumer(Configuration config) {
        kafkaSensorTracksTopic = config.getKafkaSessionPrefix() + KafkaTopics.SENSOR_TRACKS;
        bootstrapServer = config.getKafkaHost() + ":" + config.getKafkaPort();
        consumer = Consumer.setUpKafkaConsumerAndSubscribe(bootstrapServer, kafkaSensorTracksTopic);
        KnownTracks.getInstance();
    }

    public void run() {
        // Process messages
        ConsumerRecords<String, byte[]> sensorTracks = consumer.poll(Duration.ofMillis(500));
        for (ConsumerRecord<String, byte[]> sensorTrack : sensorTracks) {
            try {
                SensorTrack mySensorTrack =  (SensorTrack)SerializationUtils.deserialize(sensorTrack.value());
                if (KnownTracks.hasId(mySensorTrack.getTrackId())) {
                    allTrackRequests.add("Update", mySensorTrack);
                } else {

                    allTrackRequests.add("Create", mySensorTrack);
                    KnownTracks.add(mySensorTrack.getTrackId());
                }
            } catch (Exception e) {
                log.debug(CONTINUING_AFTER_EXCEPTION,e);
            }
        }
    }
}
