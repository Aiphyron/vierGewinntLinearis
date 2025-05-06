package models;

public abstract class RequestModel {
    private String type;
    private String gameId;

    public RequestModel(String type, String gameId) {
        this.type = type;
        this.gameId = gameId;
    }
}