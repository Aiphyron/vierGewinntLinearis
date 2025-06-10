package producers;


import com.fasterxml.jackson.databind.ObjectMapper;
import models.RequestModels.GameRequestModel;
import models.RequestModels.SyncGameRequestModel;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
/**
 * GameRequestProducer is responsible for sending game requests to the Kafka topic.
 * It serializes the request models to JSON and sends them to the game-requests topic.
 * Also handles Game synchronization requests to the sync-games-3xMa1xAm topic.
 */
public class GameRequestProducer {
    private static final String BOOTSTRAP_SERVERS = "10.50.15.52:9092";
    private static final String GAME_REQUEST_TOPIC = "game-requests";
    private static final String SYNC_GAMES_TOPIC = "sync-games-3xMa1xAm";
    private static final String CLIENT_ID = "Team 1xAm3xMa";

    private final KafkaProducer<String, String> producer = createProducer();

    /**
     *  Creates a KafkaProducer with the necessary configurations.
     *  @return A configured KafkaProducer instance.
     */
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
     * IMPORTANT: If the application is closed immediately after calling this method, the request will not be sent, 'cause delay
     * @param request The request to send.
     */
    public void sendGameRequest(GameRequestModel request) {
        ObjectMapper mapper = new ObjectMapper();
        String mappedRequest = null;

        try {
            mappedRequest = mapper.writeValueAsString(request);
            System.out.println("Serialized request: " + mappedRequest);
        } catch (Exception e) {
            System.err.println("Error serializing request: " + e.getMessage());
        }

        producer.send(new ProducerRecord<>(GAME_REQUEST_TOPIC, mappedRequest),
                (recordMetadata, e) -> {
                    if (e != null) {
                        System.err.println("Error sending message: " + e.getMessage());
                    } else {
                        System.out.println("Message sent successfully: " + recordMetadata.toString());
                    }
                });
    }

    /**
     * Sends a synchronization game request to the Kafka topic.
     * @param request The synchronization game request to send.
     */
    public void sendSyncGameRequest(SyncGameRequestModel request) {
        ObjectMapper mapper = new ObjectMapper();
        String mappedRequest = null;

        try {
            mappedRequest = mapper.writeValueAsString(request);
            System.out.println("Serialized sync request: " + mappedRequest);
        } catch (Exception e) {
            System.err.println("Error serializing sync request: " + e.getMessage());
        }

        producer.send(new ProducerRecord<>(SYNC_GAMES_TOPIC, mappedRequest),
                (recordMetadata, e) -> {
                    if (e != null) {
                        System.err.println("Error sending sync message: " + e.getMessage());
                    } else {
                        System.out.println("Sync message sent successfully: " + recordMetadata.toString());
                    }
                });
    }

    /**
     * Closes the producer.
     */
    public void stop() {
        producer.close();
        System.out.println("Producer closed successfully.");
    }
}
