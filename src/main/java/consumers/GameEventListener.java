package consumers;

import models.GameEventModels.EventModel;
import models.GameEventModels.SyncGameModel;
import models.RequestModels.SyncGameTypes;

public interface GameEventListener {
    void onGameEvent(EventModel event);
    void onGameSync(SyncGameModel event, SyncGameTypes wantedType);
}
