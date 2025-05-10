package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerModel {
    private String name;

    @JsonCreator
    public PlayerModel(@JsonProperty("") String name) {
        this.name = name;
    }

    public PlayerModel() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
