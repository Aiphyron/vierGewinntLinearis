package models.RequestModels;

public class MoveGameRequestModel extends GameRequestModel {
    private String player;
    private int column;

    public MoveGameRequestModel(String gameId, String player, int column) {
        super("move", gameId);
        this.player = player;
        this.column = column;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
