import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import org.apache.commons.lang3.SerializationUtils;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class DatabaseActions {

    private static Logger log = LogManager.getLogger(DatabaseActions.class);
    private static final String CONTINUING_AFTER_EXCEPTION = "Exception: %s, continuing%n";
    private String bootstrapServer = null;
    private String kafkaSensorTracksTopic = null;

    private MongoCollection<Document> tracksCollection;
    private KafkaConsumer<String, byte[]> consumer;

    public DatabaseActions(String databaseHost,
                           String databasePort,
                           String databaseName,
                           String kafkaSessionPrefix,
                           String kafkaHost,
                           String kafkaPort) {
        this.tracksCollection = setUpMongoCollection(databaseHost,databasePort, databaseName, "tracks");

        kafkaSensorTracksTopic = kafkaSessionPrefix + KafkaTopics.TRACK_REQUESTS;
        bootstrapServer = kafkaHost + ":" + kafkaPort;
        consumer = Consumer.setUpKafkaConsumerAndSubscribe(bootstrapServer, kafkaSensorTracksTopic);
    }

    public void run() {

        try {

            // Process messages
            ConsumerRecords<String, byte[]> trackRequests = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, byte[]> trackRequest : trackRequests) {
                try {

                    System.out.println("Bingo! a request");
                    TrackRequest myTrackRequest =  (TrackRequest)SerializationUtils.deserialize(trackRequest.value());

                    if (myTrackRequest.getRequestType().equals("Create")) {
                        insertTrack (myTrackRequest);
                    }
                    if (myTrackRequest.getRequestType().equals("Update")) {
                        updateTrack (myTrackRequest);
                    }

                } catch (Exception ex) {
                    if (log.isDebugEnabled()) {
                        log.debug(CONTINUING_AFTER_EXCEPTION, ex);
                    }
                }
            }

        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.debug(CONTINUING_AFTER_EXCEPTION, ex);
            }
        }
    }

    private void insertTrack(TrackRequest myTrackRequest) {
        try {
            Document trackDocument = new Document();
            trackDocument.put("TrackId",Long.toString(myTrackRequest.getTrackId()));
            trackDocument.put("Timestamp",myTrackRequest.getTimestamp());
            trackDocument.put("Name",myTrackRequest.getName());
            trackDocument.put("RequestType",myTrackRequest.getRequestType());
            trackDocument.put("SensorId",myTrackRequest.getSensorId());
            trackDocument.put("Latitude",myTrackRequest.getLatitude());
            trackDocument.put("Longitude",myTrackRequest.getLongitude());
            trackDocument.put("Altitude",myTrackRequest.getAltitude());
            trackDocument.put("Speed",myTrackRequest.getSpeed());
            trackDocument.put("Course",myTrackRequest.getCourse());
            tracksCollection.insertOne(trackDocument);
        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.debug(CONTINUING_AFTER_EXCEPTION, ex);
            }
        }
    }

    private void updateTrack(TrackRequest myTrackRequest) {
        try {
            Bson query  = Filters.eq("TrackId",Long.toString(myTrackRequest.getTrackId()));
            Bson updates  = Updates.combine(
                    Updates.set("TrackId",Long.toString(myTrackRequest.getTrackId())),
                    Updates.set("Timestamp",myTrackRequest.getTimestamp()),
                    Updates.set("Name",myTrackRequest.getName()),
                    Updates.set("RequestType",myTrackRequest.getRequestType()),
                    Updates.set("SensorId",myTrackRequest.getSensorId()),
                    Updates.set("Latitude",myTrackRequest.getLatitude()),
                    Updates.set("Longitude",myTrackRequest.getLongitude()),
                    Updates.inc("Altitude",100),
                    //Updates.set("Altitude",myTrackRequest.getAltitude()),
                    Updates.set("Speed",myTrackRequest.getSpeed()),
                    Updates.set("Course",myTrackRequest.getCourse()));
            UpdateResult upResult = tracksCollection.updateOne(query, updates);
            System.out.println(upResult.toString());

        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.debug(CONTINUING_AFTER_EXCEPTION, ex);
            }
        }
    }

    private MongoClient setUpMongoClient (String databaseHost, String databasePort) {
        return new MongoClient(databaseHost + ":" + databasePort);
    }

    private MongoCollection<Document> setUpMongoCollection (String databaseHost,
                                                            String databasePort,
                                                            String databaseName,
                                                            String collectionName) {
        MongoClient client = setUpMongoClient(databaseHost, databasePort);
        MongoDatabase db = client.getDatabase(databaseName);
        return db.getCollection(collectionName);
    }

    private KafkaConsumer<String, byte[]> setUpKafkaConsumerAndSubscribe( String topic,
                                                                          String host,
                                                                          String port,
                                                                          String sessionPrefix) {
        Properties props = new Properties();
        props.put("bootstrap.servers", host + ":" + port);
        props.put("group.id", "TrackManagerGroup");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", ByteArrayDeserializer.class.getName());

        //Set up the kafka consumer
        KafkaConsumer<String, byte[]> theConsumer;
        theConsumer = new KafkaConsumer<>(props);

        //Subscribe to the topic
        theConsumer.subscribe(Collections.singletonList((sessionPrefix + topic)));
        return theConsumer;
    }
}
