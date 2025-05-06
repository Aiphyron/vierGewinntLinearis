package models;

public class MoveRequestModel extends RequestModel {
    private String player;
    private int column;

    public MoveRequestModel(String gameId, String player, int column) {
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
