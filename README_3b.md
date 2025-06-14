# Aufgabe 3b
Dieses Projekt umfasst die Implementierung von Connect Four: Linetris über Kafka mit einer Swing GUI.
Die SpielID wird über ein eigenes Kafka-Topic übertragen, um die Spielteilnehmer zu synchronisieren. Wenn ein Spieler ein Spiel sucht, wird ein SEARCH_GAME event ausgelöst
und der Spieler wartet auf ein PLAYER-JOINED Event mit der selben SpielID. Will ein anderer Spieler beitreten, wird nach der ältesten SpielID gesucht, die noch kein passendes PLAYER_JOINED Event
hat. Mit dieser SpielID wird dann ein PLAYER_JOINED Event ausgelöst.
Wichtige Klassen sind:
  - Die Kafka Consumer: GameEventConsumer und GameSyncConsumer, die Events von Kafka lesen/konsumieren
  - Der (Linetris)GameEventListener: Jedes (vorgefilterte) Event, das von einem Consumer gelesen wird, wird von einem Listener verarbeitet
  - Main: Hier wird die GUI und damit das Programm gestartet
  - Die verschiedenen Models:
    - GameEventModels: Models für die allgemeinen Game Events (Events vom game master und Sync Events auf unserem eigenen Topic)
    - ActionModels: Models für die vom game master herausgegebenen Aktionen
    - RequestModels: Models für die an den game master zu sendenden SpielRequests
  - Producer: der Producer ist dafür zuständig, die GameRequests an Kafka zu schicken.
  - UI Komponenten:
    - BoardPanel: Hier wird das eigentliche Board je nach Spielstatus aufgezeichnet
    - GameModel: Ein Model zum Verwalten des aktuellen Spielstatusses
    - ConnectFourFrame: Das gesamte Programmfenster