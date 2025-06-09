package models.RequestModels;

public abstract class GameRequestModel {
    private String type;
    private String gameId;

    public GameRequestModel(String type, String gameId) {
        this.type = type;
        this.gameId = gameId;
    }

    public String getType() {
        return type;
    }
    public String getGameId() {
        return gameId;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}