package consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.GameEventModels.EventModel;

import javax.swing.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

public class GameEventConsumer {
    private final KafkaConsumer<String, String> consumer = createConsumer();
    private static final String BOOTSTRAP_SERVERS = "10.50.15.52:9092";
    private static final String TOPIC_NAME = "game-events";
    private static final String CLIENT_ID = "Team 1xAm3xMa";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String gameId;

    private GameEventListener listener;

    private volatile boolean running = true;

    public GameEventConsumer(String gameId, GameEventListener listener) {
        this.gameId = gameId;
        this.listener = listener;
        consumer.subscribe(Collections.singletonList(TOPIC_NAME));
    }

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
