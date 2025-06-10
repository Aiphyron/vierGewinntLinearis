package consumers;

import models.GameEventModels.EventModel;
import models.GameEventModels.SyncGameModel;
import models.RequestModels.SyncGameTypes;

/**
 * Interface for listening to game events.
 */
public interface GameEventListener {
    /**
     * Called when a game event occurs.
     *
     * @param event the game event that occurred
     */
    void onGameEvent(EventModel event);
    /**
     * Called when a game sync event occurs.
     *
     * @param event the sync game event that occurred
     * @param wantedType the type of sync game that is wanted
     */
    void onGameSync(SyncGameModel event, SyncGameTypes wantedType);
}
