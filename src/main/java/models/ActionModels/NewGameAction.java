package models.ActionModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.ClientModel;
import models.PlayerModel;



public class NewGameAction extends Action {
    private final ClientModel client1;
    private final PlayerModel player1;
    private final ClientModel client2;
    private final PlayerModel player2;
    private final int rows;
    private final int cols;

    @JsonCreator
    public NewGameAction(
            @JsonProperty("client1") ClientModel client1,
            @JsonProperty("player1") PlayerModel player1,
            @JsonProperty("client2") ClientModel client2,
            @JsonProperty("player2") PlayerModel player2,
            @JsonProperty("rows") int rows,
            @JsonProperty("cols") int cols
    ) {
        super("newGame");
        this.client1 = client1;
        this.player1 = player1;
        this.client2 = client2;
        this.player2 = player2;
        this.rows = rows;
        this.cols = cols;
    }

    public ClientModel getClient1() { return client1; }
    public PlayerModel getPlayer1() { return player1; }
    public ClientModel getClient2() { return client2; }
    public PlayerModel getPlayer2() { return player2; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }
}
