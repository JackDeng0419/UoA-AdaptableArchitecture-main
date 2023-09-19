import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class TrackRequestor {

    TrackRequests allTrackRequests;

    private static Logger log = LogManager.getLogger(TrackRequestor.class);
    private static final String CONTINUING_AFTER_EXCEPTION = "Exception: %s, continuing%n";

    private String bootstrapServer;
    private String KafkaSensorTopic = null;
    private KafkaProducer<String, byte[]> theProducer;

    public TrackRequestor(Configuration config) {
        KafkaSensorTopic = config.getKafkaSessionPrefix() + KafkaTopics.TRACK_REQUESTS;
        bootstrapServer = config.getKafkaHost() + ":" + config.getKafkaPort();
        theProducer = setUpKafkaProducer();

    }

    public void run() {

        allTrackRequests.getTrackRequests().stream().forEach(t -> writeRequest(t));
        allTrackRequests.getTrackRequests().clear();

    }

    private void writeRequest(TrackRequest theRequest) {
        try {

            byte byteArray[] = SerializationUtils.serialize(theRequest);

            theProducer.send(new ProducerRecord<>(
                    KafkaSensorTopic,
                    theRequest.getName(),
                    byteArray));

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
