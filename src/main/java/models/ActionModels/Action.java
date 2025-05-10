package models.ActionModels;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MoveAction.class, name = "move"),
        @JsonSubTypes.Type(value = DeleteBottomRowAction.class, name = "deleteBottomRow"),
        @JsonSubTypes.Type(value = WinAction.class, name = "winAction"),
        @JsonSubTypes.Type(value = NewGameAction.class, name = "newGame")
})
public abstract class Action {
    private final String type;

    public Action(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

}
