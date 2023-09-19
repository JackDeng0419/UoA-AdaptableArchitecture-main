import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Consumer {

    private Consumer() {}

    public static KafkaConsumer<String, byte[]>

    setUpKafkaConsumerAndSubscribe(String bootstrapServer, String topic) {
        Properties props  = new Properties();
        props.put("bootstrap.servers", bootstrapServer);
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", ByteArrayDeserializer.class.getName());

        KafkaConsumer<String, byte[]> theConsumer = new KafkaConsumer<>(props);
        String [] topics = {topic};
        List<String> topicList = new ArrayList<>(Arrays.asList(topics));
        theConsumer.subscribe(topicList);
        return theConsumer;
    }

}
