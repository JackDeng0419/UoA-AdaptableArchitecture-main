import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SensorTrackProducer {

    private static Logger log = LogManager.getLogger(SensorTrackProducer.class);
    private static final String CONTINUING_AFTER_EXCEPTION = "Exception: %s, continuing%n";

    private String bootstrapServer;
    private String KafkaSensorTopic = null;
    private KafkaProducer<String, byte[]> theProducer;

    public SensorTrackProducer(Configuration config) {
        KafkaSensorTopic = config.getKafkaSessionPrefix() + KafkaTopics.SENSOR_TRACKS;
        bootstrapServer = config.getKafkaHost() + ":" + config.getKafkaPort();
        theProducer = setUpKafkaProducer();

        SensorTracks.createTracks();

    }

    public void run() {
        try {
            Thread.sleep(1L * 1000L); //1 sec delay
        } catch (Exception ex) {
            //System.out.println("Something went wrong in SensorTrack::run()");
            if (log.isDebugEnabled()) {
                log.debug(CONTINUING_AFTER_EXCEPTION, ex);
           }
        }

        SensorTracks.getTrackMap().stream().forEach(t -> writeTrack(t));
    }

    private void writeTrack(SensorTrack theTrack) {
        try {
            if (!theTrack.isReported()) {

                byte byteArray[] = SerializationUtils.serialize(theTrack);

                theProducer.send(new ProducerRecord<>(
                        KafkaSensorTopic,
                        theTrack.getName(),
                        byteArray));
            }
        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.debug(CONTINUING_AFTER_EXCEPTION, ex);
            }
        }
    }

    private KafkaProducer<String, byte[]> setUpKafkaProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServer);
        props.put("acks", "all");
        props.put("retries", "0");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", ByteArraySerializer.class.getName());
        return new KafkaProducer<>(props);
    }


}
