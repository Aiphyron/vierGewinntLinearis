package models.RequestModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request model for a move action in a game.
 */
public class MoveGameRequestModel extends GameRequestModel {
    private final String player;
    private final int column;

    @JsonCreator
    public MoveGameRequestModel(
            @JsonProperty("gameId") String gameId,
            @JsonProperty("player") String player,
            @JsonProperty("column") int column) {
        super("move", gameId);
        this.player = player;
        this.column = column;
    }

    public String getPlayer() {
        return player;
    }

    public int getColumn() {
        return column;
    }
}
