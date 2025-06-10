package ui.model;

/**
 * Enum representing the two players in the game.
 * It provides methods to convert between string representations and enum values.
 */
public enum PlayerEnum {
    ONE,
    TWO;

    /**
     * Converts a string representation of a player to the corresponding PlayerEnum.
     *
     * @param player the string representation of the player (e.g., "PLAYER1" or "PLAYER2")
     * @return the corresponding PlayerEnum value
     * @throws IllegalArgumentException if the string does not match any player
     */
    public static PlayerEnum fromString(String player) {
        if (player.equalsIgnoreCase("PLAYER1")) {
            return PlayerEnum.ONE;
        } else if (player.equalsIgnoreCase("PLAYER2")) {
            return PlayerEnum.TWO;
        } else {
            throw new IllegalArgumentException("Invalid player string: " + player);
        }
    }

    @Override
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
