package models.RequestModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a request to synchronize a game state.
 * This model is used to send synchronization requests between clients and the server.
 */
public class SyncGameRequestModel {
    private final long timestamp;
    private final String gameId;
    private final SyncGameTypes type;
    private final String clientName;
    private final String playerName;
    // Only for future use, currently nothing implemented and every request sends 8x8
    private final int rows;
    private final int cols;

    @JsonCreator
    public SyncGameRequestModel(
            @JsonProperty("timestamp") long timestamp,
            @JsonProperty("gameId") String gameId,
            @JsonProperty("type") SyncGameTypes type,
            @JsonProperty("clientName") String clientName,
            @JsonProperty("playerName") String playerName,
            @JsonProperty("rows") int rows,
            @JsonProperty("cols") int cols) {
        this.timestamp = timestamp;
        this.gameId = gameId;
        this.type = type;
        this.clientName = clientName;
        this.playerName = playerName;
        this.rows = rows;
        this.cols = cols;
    }

    public long getTimestamp() {
        return timestamp;
    }
    public String getGameId() {
        return gameId;
    }
    public SyncGameTypes getType() {
        return type;
    }
    public String getClientName() {
        return clientName;
    }
    public String getPlayerName() {
        return playerName;
    }
    public int getRows() {
        return rows;
    }
    public int getCols() {
        return cols;
    }
}
