package consumers;

import models.GameEventModels.SyncGameModel;
import models.RequestModels.SyncGameTypes;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.GameEventModels.EventModel;
import ui.model.GameModel;

import javax.swing.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class GameSyncConsumer {
    private final KafkaConsumer<String, String> consumer = createConsumer();
    private static final String BOOTSTRAP_SERVERS = "10.50.15.52:9092";
    private static final String SYNC_GAMES_TOPIC = "sync-games-3xMa1xAm";
    private static final String CLIENT_ID = "Team 1xAm3xMa";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private GameEventListener listener;
    private GameModel gameModel;

    public GameSyncConsumer(GameEventListener listener, GameModel gameModel) {
        this.gameModel = gameModel;
        this.listener = listener;
        consumer.subscribe(Collections.singletonList(SYNC_GAMES_TOPIC));
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

    public void consumeGameSync(SyncGameTypes syncGameType) {
        boolean found = false;
        try {
            while (!found) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));

                ArrayList<SyncGameModel> events = new ArrayList<>();
                for (ConsumerRecord<String, String> record : records) {
                    if (records.isEmpty()) break;
                    try {
                        SyncGameModel event = objectMapper.readValue(record.value(), SyncGameModel.class);
                        events.add(event);
                    } catch (Exception e) {
                        System.err.println("Error during Object Mapping: " + e.getMessage());
                    }
                }

                // Remove any events older than 1 hour
                Instant oneHourAgo = Instant.now().minusSeconds(3600);
                events.removeIf(event -> Instant.ofEpochMilli(event.getTimestamp()).isBefore(oneHourAgo));

                if (events.isEmpty()) {
                    continue; // Skip if no events are available
                }
                if (syncGameType == SyncGameTypes.PLAYER_JOINED) {
                    // Filter events for PLAYER_JOINED
                    events.removeIf(event -> event.getType() != SyncGameTypes.PLAYER_JOINED);

                    if (listener != null && !events.isEmpty()) {
                        for (SyncGameModel event : events) {
                            if (event.getGameId().equals(gameModel.getGameId())) {
                                SwingUtilities.invokeLater(() -> listener.onGameSync(event, syncGameType));
                                found = true;
                                break;
                            }
                        }
                    }
                } else if (syncGameType == SyncGameTypes.SEARCH_GAME) {
                    // Check for available gameIds in SEARCH_GAME events
                    // Adds all SEARCH_GAME events and removes them if their gameId is found in a PLAYER_JOINED event
                    ArrayList<SyncGameModel> availableGameIds = new ArrayList<>();
                    for (SyncGameModel event : events) {
                        if (event.getType() == SyncGameTypes.SEARCH_GAME) {
                            availableGameIds.add(event);
                        } else if (event.getType() == SyncGameTypes.PLAYER_JOINED) {
                            availableGameIds.removeIf(e -> e.getGameId().equals(event.getGameId()));
                        }
                    }

                    // Only the newest available SEARCH_GAME event is paired, not optimal but fine for now. TODO: Optimize this later
                    SyncGameModel newestSearchGame = availableGameIds.stream()
                            .filter(e -> e.getType() == SyncGameTypes.SEARCH_GAME)
                            .max(Comparator.comparingLong(SyncGameModel::getTimestamp))
                            .orElse(null);

                    if (listener != null && newestSearchGame != null) {
                        SwingUtilities.invokeLater(() -> listener.onGameSync(newestSearchGame, syncGameType));
                        found = true;
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
        consumer.close();
    }
}
