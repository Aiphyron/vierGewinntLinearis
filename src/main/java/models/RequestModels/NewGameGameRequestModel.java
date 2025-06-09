package models.RequestModels;

import models.ClientModel;
import models.PlayerModel;

public class NewGameGameRequestModel extends GameRequestModel {
    private ClientModel client1;
    private PlayerModel player1;
    private ClientModel client2;
    private PlayerModel player2;

    public NewGameGameRequestModel(String gameId, ClientModel client1, PlayerModel player1, ClientModel client2, PlayerModel player2) {
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
    public void setClient1(ClientModel client1) {
        this.client1 = client1;
    }
    public void setPlayer1(PlayerModel player1) {
        this.player1 = player1;
    }
    public void setClient2(ClientModel client2) {
        this.client2 = client2;
    }
    public void setPlayer2(PlayerModel player2) {
        this.player2 = player2;
    }
}
