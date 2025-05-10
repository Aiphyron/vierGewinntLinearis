package models.ActionModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteBottomRowAction extends Action {
    private final int row;

    @JsonCreator
    public DeleteBottomRowAction(@JsonProperty("row") int row) {
        super("deleteBottomRow");
        this.row = row;
    }

    public int getRow() {
        return row;
    }
}
