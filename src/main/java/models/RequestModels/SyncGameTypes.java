package models.RequestModels;

/**
 * Enum representing the types of synchronization game requests.
 * Each type has a string representation used in JSON serialization.
 */
public enum SyncGameTypes {
    /**
     * Represents a player joining a game.
     */
    PLAYER_JOINED("player_joined"),
    /**
     * Represents a player searching for a game.
     */
    SEARCH_GAME("search_game"),
    ;

    /**
     * The string representation of the game type.
     */
    private final String stringRepresentation;

    /**
     * Constructor for SyncGameTypes.
     *
     * @param searchGame the string representation of the game type
     */
    SyncGameTypes(String searchGame) {
        this.stringRepresentation = searchGame;
    }

    /**
     * Gets the string representation of the game type.
     *
     * @return the string representation
     */
    public String getStringRepresentation() {
        return stringRepresentation;
    }

    /**
     * Returns the string representation of the game type.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return stringRepresentation;
    }

    /**
     * Converts a string representation to the corresponding SyncGameTypes enum value.
     *
     * @param stringRepresentation the string representation of the game type
     * @return the corresponding SyncGameTypes enum value
     * @throws IllegalArgumentException if the string does not match any enum value
     */
    public static SyncGameTypes fromString(String stringRepresentation) {
        for (SyncGameTypes type : values()) {
            if (type.stringRepresentation.equals(stringRepresentation)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + stringRepresentation);
    }
}
