package models.RequestModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Abstract class representing a game request model.
 */
public abstract class GameRequestModel {
    private final String type;
    private final String gameId;

    @JsonCreator
    public GameRequestModel(
            @JsonProperty("type") String type,
            @JsonProperty("gameId") String gameId) {
        this.type = type;
        this.gameId = gameId;
    }

    public String getType() {
        return type;
    }
    public String getGameId() {
        return gameId;
    }
}