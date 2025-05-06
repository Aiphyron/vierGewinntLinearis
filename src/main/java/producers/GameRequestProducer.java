package producers;


import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import models.MoveRequestModel;
import models.NewGameRequestModel;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class GameRequestProducer {
    private static final String BOOTSTRAP_SERVERS = "10.50.15.52:9092";
    private static final String TOPIC_NAME = "game-requests";
    private static final String CLIENT_ID = "Team 1xAm3xMa";

    private final KafkaProducer<String, Double> producer = createProducer();


    private KafkaProducer<String, Double> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonValueSerializer.class);
        return new KafkaProducer<>(props);
    }

    public void sentMoveRequest(MoveRequestModel request) {
        producer.send(new ProducerRecord<>(TOPIC_NAME, 1.0),
                (recordMetadata, e) -> {
                    if (e != null) {
                        System.err.println("Error sending message: " + e.getMessage());
                    } else {
                        System.out.println("Message sent successfully: " + recordMetadata.toString());
                    }
                });

    }

    public void sentNewGameRequest(NewGameRequestModel request) {

    }
}
