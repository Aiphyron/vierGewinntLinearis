package producers;


import com.fasterxml.jackson.databind.ObjectMapper;
import models.RequestModel;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class GameRequestProducer {
    private static final String BOOTSTRAP_SERVERS = "10.50.15.52:9092";
    private static final String TOPIC_NAME = "game-requests";
    private static final String CLIENT_ID = "Team 1xAm3xMa";

    private final KafkaProducer<String, String> producer = createProducer();


    private KafkaProducer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new KafkaProducer<>(props);
    }

    /**
     * Sends a request to the Kafka topic.
     * IMPORTANT: If the application is closed immediately after calling this method, the request will not be sent.
     * @param request The request to send.
     */
    public void sentRequest(RequestModel request) {
        ObjectMapper mapper = new ObjectMapper();
        String mappedRequest = null;

        try {
            mappedRequest = mapper.writeValueAsString(request);
            System.out.println("Serialized request: " + mappedRequest);
        } catch (Exception e) {
            System.err.println("Error serializing request: " + e.getMessage());
        }

        producer.send(new ProducerRecord<>(TOPIC_NAME, mappedRequest),
                (recordMetadata, e) -> {
                    if (e != null) {
                        System.err.println("Error sending message: " + e.getMessage());
                    } else {
                        System.out.println("Message sent successfully: " + recordMetadata.toString());
                    }
                });
    }
}
