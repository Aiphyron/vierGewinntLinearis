package models.ActionModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WinAction extends Action {
    private final String player;

    @JsonCreator
    public WinAction(@JsonProperty("player") String player) {
        super("winAction");
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }
}
