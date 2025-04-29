- [x] random AI
- [x] refactor
- [x] smart AI
- [x] unit test
- [x] impr UI
- [] refactor 
- [x] CI/CD (create build on commits to master; if tests pass aussi)
- [x] local builds
- [] smart AI 2.0
- [x] CI/CD (create release for all platforms)



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

## How to Run

### Running in an IDE (IntelliJ IDEA)
1. Open the project in IntelliJ IDEA
2. Wait for Maven to download dependencies
3. Find `HelloApplication.java` in the project explorer
4. Right-click and select "Run 'HelloApplication.main()'"

### Running from Command Line
1. Build the project:
   ```bash
   mvn clean package -DskipTests
   ```
2. Run the JAR:
   ```bash
   java -jar target/batlleship-1.0-SNAPSHOT.jar
   ```

### Generating Runtime Image (macOS)
1. Build the runtime image:
   ```bash
   mvn clean javafx:jlink
   ```
2. The runtime image will be created in `target/battleship.zip`
3. Extract the zip file
4. Run the application:
   ```bash
   ./target/battleship/bin/battleship
   ```

## Project Structure
```
BattleshipGame/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/sj/batlleship/batlleship/
│       │       ├── HelloApplication.java
│       │       ├── controllers/
│       │       │   ├── GameController.java
│       │       │   └── WelcomeController.java
│       │       ├── models/
│       │       │   ├── GameBoard.java
│       │       │   ├── Cell.java
│       │       │   ├── Ship.java
│       │       │   ├── Player.java
│       │       │   ├── HumanPlayer.java
│       │       │   └── ComputerPlayer.java
│       │       ├── strategies/
│       │       │   ├── RandomStrategy.java
│       │       │   └── SmartStrategy.java
│       │       ├── enums/
│       │       │   ├── Orientation.java
│       │       │   ├── CellState.java
│       │       │   └── GameState.java
│       │       └── constants/
│       │           └── GameConstants.java
│       └── resources/
│           └── com/sj/batlleship/batlleship/
│               ├── views/
│               │   ├── game-scene.fxml
│               │   └── welcome.fxml
│               ├── images/
│               │   ├── carrier.png
│               │   ├── battleship.png
│               │   ├── cruiser.png
│               │   ├── submarine.png
│               │   └── destroyer.png
│               └── icon/
│                   └── ship.png
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
2. TestFX for ui and int testing
3. Creating Native Installers
4. Animations
