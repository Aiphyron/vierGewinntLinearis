package models.ActionModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MoveAction extends Action {
    private final int column;
    private final String player;

    @JsonCreator
    public MoveAction(@JsonProperty("player") String player,
                      @JsonProperty("column") int column) {
        super("move");
        this.player = player;
        this.column = column;
    }

    public String getPlayer() { return player; }
    public int getColumn() { return column; }
}
