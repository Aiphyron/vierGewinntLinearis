package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model representing a client with a name.
 */
public class ClientModel {
    private final String name;

    @JsonCreator
    public ClientModel(@JsonProperty("name") String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }
}
