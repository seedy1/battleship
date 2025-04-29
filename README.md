- [x] random AI
- [x] refactor
- [x] smart AI
- [x] unit test
- [x] impr UI
- [] refactor 
- [] CI/CD (create build on commits to master; if tests pass aussi)
- [] local builds
- [] smart AI 2.0
- [] CI/CD (create release for all platforms)



create .jar
`mvn clean package -DskipTests` then `java -jar battleshipv.jar`

> `mvn javafx:jlink` to get zip
> 
mvn clean javafx:jlink jpackage:jpackage
creates 
> target/dist/Battleship-1.0.0.dmg (macOS)
> target/dist/Battleship-1.0.0.msi (Windows)
> target/dist/Battleship-1.0.0.deb (Linux)

TL;DR
Run and develop → javafx:run

Package self-contained runtime → jlink:jlink

Create native installer → jpackage:jpackage

# Javafx Battleship Game (MVC)

```
BattleshipGame/
├── src/
│   └── battleship/
│       ├── Main.java
│       ├── GameController.java
│       ├── GameBoard.java
│       ├── Cell.java
│       ├── Ship.java
│       ├── Player.java
│       ├── HumanPlayer.java
│       ├── ComputerPlayer.java
│       └── enums/
│           ├── Orientation.java
│           └── CellState.java
├── resources/
│   └── battleship/
│       ├── game-scene.fxml
│       ├── welcome.fxml
│       └── assets/
│           ├── carrier.png
│           ├── battleship.png
│           ├── cruiser.png
│           ├── submarine.png
│           └── destroyer.png
└── README.md
```

start() − The entry point method where the JavaFX graphics code is to be written.

stop() − An empty method which can be overridden, here you can write the logic to stop the application.

init() − An empty method which can be overridden, but you cannot create a stage or scene in this method.

An instance of the application class is created.

Init() method is called.

The start() method is called.

The launcher waits for the application to finish and calls the stop() method.

javafx vs swing
game lib vs coding
https://www.wikihow.com/Win-at-Battleship
https://www.thesprucecrafts.com/how-to-win-at-battleship-411068
https://docs.oracle.com/javase/9/docs/api/java/awt/Taskbar.html
## future features and improvemnts

1. Multiplayer Lan
2. Animations