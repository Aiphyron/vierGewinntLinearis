package models.GameEventModels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.ActionModels.Action;

import java.util.List;

public class EventModel {
    @JsonProperty("timeStamp")
    private long timeStamp;

    @JsonProperty("actions")
    private List<Action> actions;

    @JsonProperty("state")
    private GameState state;

    @JsonProperty("gameId")
    private String gameId;
    @JsonProperty("message")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    public long getTimeStamp() { return timeStamp; }
    public List<Action> getActions() { return actions; }
    public GameState getState() { return state; }
    public String getGameId() { return gameId; }

    public String getMessage() {
        return message;
    }
}
