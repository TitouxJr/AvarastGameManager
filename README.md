# AvarastGameManager

AvarastGameManager is a Minecraft Spigot plugin (1.20.1) that implements a mini-game system, specifically designed for the "Extraction" game mode. This plugin provides a complete framework for managing mini-games with customizable features.

## Features

### Extraction Game Mode
- **Time-Limited Gameplay**: Configurable duration (default: 30 minutes)
- **Dynamic Block Breaking System**: Only specific blocks can be broken during the game
- **Visual Feedback**:
  - Boss bar showing remaining time
  - Action bar notifications for block breaks
  - Particle effects and sounds when breaking blocks
  - Center screen countdown at game start

### Game Management
- **State System**: Handles different game states (LOBBY, STARTING, ACTIVE, ENDING)
- **Block Management**: 
  - Configurable list of breakable blocks
  - Automatic block respawning after game ends
  - Prevention of block placement during games

### Commands
- `/extraction` - Start the extraction mini-game
- `/extraction stop` - Force stop the current game

## Installation

1. Download the latest release from the releases page
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. The plugin will generate a default configuration file

## Configuration

The plugin uses a `config.yml` file with the following options:

```yaml
# Game Settings
game-duration-minutes: 30    # Duration of each game in minutes
countdown-seconds: 10        # Countdown duration before game starts

# List of blocks that can be broken during games
breakable-blocks:
  - DIAMOND_ORE
  - IRON_ORE
  - GOLD_ORE
  - EMERALD_ORE
  - COAL_ORE
  - STONE
```

## Permissions

- `avarastgamemanager.extraction` - Allows players to use the extraction commands (default: true)

## Game Flow

1. **Starting the Game**
   - Use `/extraction` to start the game
   - A countdown will begin
   - Players will see a "Get ready!" message

2. **During the Game**
   - Players can only break configured blocks
   - Block breaking shows visual feedback
   - Time remaining is displayed via boss bar
   - Blocks are tracked for respawning

3. **Ending the Game**
   - Game ends when time runs out or `/extraction stop` is used
   - All broken blocks respawn
   - Players return to normal gameplay

## Development

This plugin is built using:
- Java
- Spigot API 1.20.1
- Maven for dependency management

### Building from Source

1. Clone the repository
```bash
git clone https://github.com/YourUsername/AvarastGameManager.git
```

2. Build using Maven
```bash
cd AvarastGameManager
mvn clean package
```

The compiled JAR will be in the `target` directory.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

If you encounter any issues or have questions, please open an issue on the GitHub repository. 