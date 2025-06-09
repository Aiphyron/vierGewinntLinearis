package models.RequestModels;

public class SyncGameRequestModel {
    private long timestamp;
    private String gameId;
    private SyncGameTypes type;
    private String clientName;
    private String playerName;
    // Only for future use, currently nothing implemented and every request sends 8x8
    private int rows;
    private int cols;

    public SyncGameRequestModel() {
        // Default constructor for deserialization
    }

    public SyncGameRequestModel(long timestamp, String gameId, SyncGameTypes type, String clientName, String playerName, int rows, int cols) {
        this.timestamp = timestamp;
        this.gameId = gameId;
        this.type = type;
        this.clientName = clientName;
        this.playerName = playerName;
        this.rows = rows;
        this.cols = cols;
    }

    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getGameId() {
        return gameId;
    }
    public SyncGameTypes getType() {
        return type;
    }
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
    public void setType(SyncGameTypes type) {
        this.type = type;
    }
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public int getRows() {
        return rows;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
    public int getCols() {
        return cols;
    }
    public void setCols(int cols) {
        this.cols = cols;
    }
}
