package models;

public class MoveRequestModel extends RequestModel {
    private String player;
    private int column;

    public MoveRequestModel(String gameId, String player, int column) {
        super("move", gameId);
        this.player = player;
        this.column = column;
    }
}
