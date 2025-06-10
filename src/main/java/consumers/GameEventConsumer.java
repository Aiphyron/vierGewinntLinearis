package consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.GameEventModels.EventModel;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;

import javax.swing.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

/**
 * GameEventConsumer is responsible for consuming game events from the game-events kafka topic.
 * It filters events based on the game ID and notifies a listener when relevant events are received.
 */
public class GameEventConsumer {
    private final KafkaConsumer<String, String> consumer = createConsumer();
    private static final String BOOTSTRAP_SERVERS = "10.50.15.52:9092";
    private static final String TOPIC_NAME = "game-events";
    private static final String CLIENT_ID = "Team 1xAm3xMa";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String gameId;

    private GameEventListener listener;

    /**
     * Indicates whether the consumer is currently running.
     * This flag is used to control the consumer loop.
     */
    private volatile boolean running = true;

    /**
     * GameEventConsumer constructor initializes the consumer with the specified game ID and listener.
     *
     * @param gameId   The ID of the game for which events are being consumed.
     * @param listener The listener that will be notified of game events.
     */
    public GameEventConsumer(String gameId, GameEventListener listener) {
        this.gameId = gameId;
        this.listener = listener;
        consumer.subscribe(Collections.singletonList(TOPIC_NAME));
    }

    /**
     * Creates a KafkaConsumer with the necessary configurations.
     *
     * @return A configured KafkaConsumer instance.
     */
    private KafkaConsumer<String, String> createConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group-" + UUID.randomUUID());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new KafkaConsumer<>(props);
    }

    /**
     * Consumes game events from the Kafka topic and notifies the listener of relevant events.
     * The method runs in a loop until stopped, polling for new records every 500 milliseconds.
     * Uses Jackson ObjectMapper to deserialize the event data.
     */
    public void consumeGameEvent() {
        try {
            while(running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, String> record : records) {
                    if (records.isEmpty()) break;
                    try {
                        EventModel event = objectMapper.readValue(record.value(), EventModel.class);

                        if (event.getGameId().equals(this.gameId) && listener != null
                                // Check if the event is within the last 24 hours
                            && Instant.ofEpochMilli(event.getTimeStamp()).isAfter(Instant.now().minusSeconds(60*60*24))) {
                            SwingUtilities.invokeLater(() -> listener.onGameEvent(event));
                        }
                    } catch (Exception e) {
                        System.err.println("Error during Object Mapping " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            consumer.close();
        }
    }

    /**
     * Stops the consumer by setting the running flag to false and interrupting the poll() call.
     */
    public void stop() {
        running = false;
        consumer.wakeup(); // Interrupt poll() call
    }


    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
