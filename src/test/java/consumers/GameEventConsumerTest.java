package consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.ActionModels.*;
import models.GameEventModels.EventModel;
import models.GameEventModels.GameState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameEventConsumerTest {
    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testEventDeserialization() throws Exception {
        String json = """
            {
              "timeStamp": 123456789,
              "gameId": "game-1",
              "state": "OK",
              "actions": [
                {
                  "type": "move",
                  "player": "Player1",
                  "column": 32
                },
                {
                    "type": "deleteBottomRow",
                    "row": 5
                }
              ]
           }
        """;
        EventModel event = objectMapper.readValue(json, EventModel.class);
        assertEquals(123456789, event.getTimeStamp());
        assertEquals("game-1", event.getGameId());
        assertEquals(GameState.OK, event.getState());
        assertEquals(2, event.getActions().size());
    }

    @Test
    public void testMoveActionDeserialization() throws Exception {
        String json = """
            {
              "timeStamp": 123456789,
              "gameId": "game-1",
              "state": "OK",
              "actions": [
                {
                  "type": "move",
                  "player": "Player1",
                  "column": 3
                }
              ]
            }
        """;
        EventModel event = objectMapper.readValue(json, EventModel.class);
        assertEquals(1, event.getActions().size());
        assertInstanceOf(MoveAction.class, event.getActions().getFirst());

        MoveAction action = (MoveAction) event.getActions().getFirst();
        assertEquals("Player1", action.getPlayer());
        assertEquals(3, action.getColumn());
    }

    @Test
    public void testDeleteBottomRowActionDeserialization() throws Exception {
        String json = """
            {
              "timeStamp": 123456789,
              "gameId": "game-2",
              "state": "OK",
              "actions": [
                {
                  "type": "deleteBottomRow",
                  "row": 5
                }
              ]
            }
        """;

        EventModel event = objectMapper.readValue(json, EventModel.class);
        assertInstanceOf(DeleteBottomRowAction.class, event.getActions().getFirst());
        DeleteBottomRowAction action = (DeleteBottomRowAction) event.getActions().getFirst();
        assertEquals(5, action.getRow());
    }

    @Test
    public void testWinActionDeserialization() throws Exception {
        String json = """
            {
              "timeStamp": 123456789,
              "gameId": "game-3",
              "state": "OK",
              "actions": [
                {
                  "type": "winAction",
                  "player": "Player2"
                }
              ]
            }
        """;

        EventModel event = objectMapper.readValue(json, EventModel.class);
        assertInstanceOf(WinAction.class, event.getActions().getFirst());
        WinAction action = (WinAction) event.getActions().getFirst();
        assertEquals("Player2", action.getPlayer());
    }

    @Test
    public void testNewGameActionDeserialization() throws Exception {
        String json = """
            {
              "timeStamp": 123456789,
              "gameId": "game-4",
              "state": "OK",
              "actions": [
                {
                  "type": "newGame",
                  "client1": { "name": "Client1" },
                  "player1": { "name": "Player1" },
                  "client2": { "name": "Client2" },
                  "player2": { "name": "Player2" },
                  "rows": 6,
                  "cols": 7
                }
              ]
            }
        """;

        EventModel event = objectMapper.readValue(json, EventModel.class);
        assertInstanceOf(NewGameAction.class, event.getActions().getFirst());
        NewGameAction action = (NewGameAction) event.getActions().getFirst();
        assertEquals("Client1", action.getClient1().getName());
        assertEquals("Player1", action.getPlayer1().getName());
        assertEquals("Client2", action.getClient2().getName());
        assertEquals("Player2", action.getPlayer2().getName());
        assertEquals(6, action.getRows());
        assertEquals(7, action.getCols());
    }

    @Test
    public void testStateError() throws Exception{
        String json = """
            {
                    "timeStamp": 1746130691239,
                    "actions":[
                    {"type":"move"
                    ,
                    "player":"PLAYER1"
                    ,
                    "column":11}
                    ],
                    "state": "ERROR"
                    ,
                    "message":"Illegal column number: 11, max=8"
                    ,
                    "gameId":"7921f007-668d-4612-a660-7729104886fd" }
        """;
        EventModel event = objectMapper.readValue(json, EventModel.class);
        assertEquals(GameState.ERROR, event.getState());
        assertEquals("Illegal column number: 11, max=8", event.getMessage());
    }

    @Test
    public void testInvalidJsonThrowsException() {
        String invalidJson = """
            {
              "timeStamp": 123456789,
              "gameId": "game-1",
              "state": "OK",
              "actions": [
                {
                  "type": "winning",
                  "player": "Player2",
                  "looser": "Player1"
                }
              ]
            }
        """;
        assertThrows(Exception.class, () -> {
            objectMapper.readValue(invalidJson, EventModel.class);
        });
    }
}
