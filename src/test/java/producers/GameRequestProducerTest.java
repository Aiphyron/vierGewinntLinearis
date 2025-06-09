package producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.ClientModel;
import models.RequestModels.MoveGameRequestModel;
import models.RequestModels.NewGameGameRequestModel;
import models.PlayerModel;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GameRequestProducerTest {
    @Test
    void testMoveRequestJSONConversion() {
        ObjectMapper mapper = new ObjectMapper();
        MoveGameRequestModel request = new MoveGameRequestModel("game123", "player1", 5);
        String expectedJson = "{\"type\":\"move\",\"gameId\":\"game123\",\"player\":\"player1\",\"column\":5}";
        String actualJson = "";

        try {
            actualJson = mapper.writeValueAsString(request);
        } catch (Exception e) {
            System.err.println("Error serializing request: " + e.getMessage());
        }
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testNewGameRequestJSONConversion() {
        ObjectMapper mapper = new ObjectMapper();
        ClientModel client1 = new ClientModel("client1");
        ClientModel client2 = new ClientModel("client2");
        PlayerModel player1 = new PlayerModel("player1");
        PlayerModel player2 = new PlayerModel("player2");
        NewGameGameRequestModel request = new NewGameGameRequestModel("game123", client1, player1, client2, player2);
        String expectedJson = "{\"type\":\"newGame\",\"gameId\":\"game123\",\"client1\":{\"name\":\"client1\"},\"player1\":{\"name\":\"player1\"},\"client2\":{\"name\":\"client2\"},\"player2\":{\"name\":\"player2\"}}";
        String actualJson = "";

        try {
            actualJson = mapper.writeValueAsString(request);
        } catch (Exception e) {
            System.err.println("Error serializing request: " + e.getMessage());
        }
        assertEquals(expectedJson, actualJson);
    }

    // Test is not automatic => will always pass
    // Check in kafka instance for the two sent requests
    // I checked and it works (atm)
    @Test
    void testGameRequestProducer() {
        GameRequestProducer producer = new GameRequestProducer();
        MoveGameRequestModel moveRequest = new MoveGameRequestModel("game123", "player1", 5);
        NewGameGameRequestModel newGameRequest = new NewGameGameRequestModel("game123", new ClientModel("client1"), new PlayerModel("player1"), new ClientModel("client2"), new PlayerModel("player2"));

        // Test sending a move request
        producer.sendGameRequest(moveRequest);

        // Test sending a new game request
        producer.sendGameRequest(newGameRequest);

        // delay because kafka needs time to process the requests
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}