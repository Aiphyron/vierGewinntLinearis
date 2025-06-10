package models.GameEventModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.RequestModels.SyncGameTypes;

/**
 * Represents a game synchronization event model.
 * This model is used to connect to other players
 */
public class SyncGameModel {
    private final long timestamp;
    private final String gameId;
    private final SyncGameTypes type;
    private final String clientName;
    private final String playerName;
    // Only for future use, currently nothing implemented and every request sends 8x8
    private final int rows;
    private final int cols;

    @JsonCreator
    public SyncGameModel(
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

    public long getTimestamp() {return timestamp;}
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
