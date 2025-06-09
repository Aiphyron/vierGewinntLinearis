package ui.model;

public enum PlayerEnum {
    ONE,
    TWO;

    public static PlayerEnum fromString(String player) {
        if (player.equalsIgnoreCase("PLAYER1")) {
            return PlayerEnum.ONE;
        } else if (player.equalsIgnoreCase("PLAYER2")) {
            return PlayerEnum.TWO;
        } else {
            throw new IllegalArgumentException("Invalid player string: " + player);
        }
    }

    public String toString() {
        switch (this) {
            case ONE:
                return "PLAYER1";
            case TWO:
                return "PLAYER2";
            default:
                throw new IllegalArgumentException("Unknown player enum: " + this);
        }
    }
}
