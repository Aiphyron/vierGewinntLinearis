package models;

public class MoveRequestModel {
    private String type = "move";
    private String gameId;
    private String player;
    private int column;

    public MoveRequestModel(String gameId, String player, int column) {
        this.gameId = gameId;
        this.player = player;
        this.column = column;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
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
