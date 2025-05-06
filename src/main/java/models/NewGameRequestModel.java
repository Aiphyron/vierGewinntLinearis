package models;

public class NewGameRequestModel extends RequestModel {
    private ClientModel client1;
    private PlayerModel player1;
    private ClientModel client2;
    private PlayerModel player2;

    public NewGameRequestModel(String gameId, ClientModel client1, PlayerModel player1, ClientModel client2, PlayerModel player2) {
        super("newGame", gameId);
        this.client1 = client1;
        this.player1 = player1;
        this.client2 = client2;
        this.player2 = player2;
    }
}
