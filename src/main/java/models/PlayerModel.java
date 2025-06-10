package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model representing a client with a name.
 */
public class PlayerModel {
    private final String name;

    @JsonCreator
    public PlayerModel(@JsonProperty("name") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
