package models.RequestModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.ClientModel;
import models.PlayerModel;

/**
 * Represents a request to create a new game with two players and their associated clients.
 */
public class NewGameGameRequestModel extends GameRequestModel {
    private final ClientModel client1;
    private final PlayerModel player1;
    private final ClientModel client2;
    private final PlayerModel player2;

    @JsonCreator
    public NewGameGameRequestModel(
            @JsonProperty("gameId") String gameId,
            @JsonProperty("client1") ClientModel client1,
            @JsonProperty("player1") PlayerModel player1,
            @JsonProperty("client2") ClientModel client2,
            @JsonProperty("player2") PlayerModel player2) {
        super("newGame", gameId);
        this.client1 = client1;
        this.player1 = player1;
        this.client2 = client2;
        this.player2 = player2;
    }
    public ClientModel getClient1() {
        return client1;
    }
    public PlayerModel getPlayer1() {
        return player1;
    }
    public ClientModel getClient2() {
        return client2;
    }
    public PlayerModel getPlayer2() {
        return player2;
    }
}
