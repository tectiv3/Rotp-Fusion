# Game Initialization Flow in Rotp-Fusion

## Overview
The game initialization follows this flow: UI Setup → Options Finalization → Galaxy Generation → Game Start

## 1. Main Entry Points

### SetupGalaxyUI.startGame() (src/rotp/ui/game/SetupGalaxyUI.java:1930-1951)
- User clicks "Start" button in setup screen
- Calls `Empire.resetPlayerId()`
- Saves current options to file: `opts.saveOptionsToFile(LIVE_OPTIONS_FILE)`
- Sets `starting = true`
- Invokes async Runnable that:
  - Calls `GameSession.instance().startGame(opts)`
  - Triggers intro panel selection
  - Measures and logs game startup time
  - Sets `GameUI.gameName` from options

### SetupGalaxyUI.restartGame() (src/rotp/ui/game/SetupGalaxyUI.java:1916-1929)
- User presses CTRL when starting (game-within-game restart)
- Saves current options
- Creates `GalaxyCopy` of the old galaxy with new options
- Calls `RotPUI.instance().selectRestartGamePanel(oldGalaxy)`
- Allows selection of which empire to play

## 2. GameSession.startGame() (src/rotp/model/game/GameSession.java:297-319)

**Purpose**: Create a fresh new game with new options

**Flow**:
1. `stopCurrentGame()` - cleanup any previous game
2. `options(newGameOptions.copyAllOptions())` - copy all game options to session
3. `rulesetManager().setAsGameMode()`
4. `instance().getGovernorOptions().gameStarted()`
5. `startExecutors()` - start game processing threads
6. Generate new game ID and achievement ID
7. **`GalaxyFactory.current().newGalaxy()`** - generate fresh galaxy
8. Initialize galaxy systems and status
9. Save session for recovery/autosave

**Key call**: `GalaxyFactory.current().newGalaxy()` (no parameters)

## 3. GameSession.restartGame() (src/rotp/model/game/GameSession.java:320-343)

**Purpose**: Restart with new options but using old galaxy structure

**Flow**: Similar to startGame() but:
- Takes two parameters: `newGameOptions` and `GalaxyCopy src`
- Options: uses `src.options().copyAllOptions()` (preserves galaxy structure)
- **`GalaxyFactory.current().newGalaxy(src)`** - regenerate galaxy from copy
- Supports player swapping within the same galaxy

**Key call**: `GalaxyFactory.current().newGalaxy(GalaxyCopy src)`

## 4. GalaxyFactory.newGalaxy() Methods (src/rotp/model/galaxy/GalaxyFactory.java)

### newGalaxy() - Fresh Galaxy (line 78-129)
- Takes NO parameters
- Gets options from `GameSession.instance().options()`
- Generates galaxy shape if not already done
- Creates all nebulas, species, and systems from scratch
- Saves options to GAME_OPTIONS_FILE

### newGalaxy(GalaxyCopy src) - Restart Galaxy (line 51-77)
- Takes GalaxyCopy parameter with old galaxy data
- Checks `opts.selectedRestartAppliesSettings()`:
  - TRUE: Uses new options (ignores old galaxy structure)
  - FALSE: Uses `src.options()` to preserve old settings
- Creates galaxy from `GalaxyBaseData` copy
- Marks galaxy as `g.restartedGame = true`
- Regenerates species and systems based on old structure

## 5. GalaxyCopy Class (src/rotp/model/galaxy/GalaxyFactory.java:630-706)

**Fields**:
- `newOptions`: IGameOptions passed to constructor
- `oldOptions`: IGameOptions from previous session
- `galSrc`: GalaxyBaseData (structure of old galaxy)
- `nebulaSizeMult`: nebula configuration
- `alienRaces`: List of alien race keys in use
- `nearbyStarSystemNumber`: nearby system count from old game
- `swappedPositions`: whether player position was swapped

**Constructor**: `GalaxyCopy(IGameOptions newOpts)`

**copy(GameSession oldS) method**:
- Copies old galaxy structure to galSrc
- Saves old options for comparison
- **Calls `newOptions.copyForRestart(oldOptions)`** to merge settings

**selectEmpire(int index) method**:
- Allows selecting which empire becomes player
- Handles player race, homeworld, leader name based on settings
- Can apply new AI or keep old settings based on options

## 6. IGameOptions.copyAllOptions() (src/rotp/model/game/IGameOptions.java:785)

**Interface method** - creates a full deep copy of all game settings

## 7. MOO1GameOptions.copyAllOptions() (src/rotp/model/game/MOO1GameOptions.java:1323-1332)

**Implementation**: 
```java
try {
    MOO1GameOptions opts = copyObjectData();
    return opts;
}
```
- Uses `copyObjectData()` for serialization-based deep copy
- Returns null on exception

## 8. MOO1GameOptions.copyForRestart() (src/rotp/model/game/MOO1GameOptions.java:327-349)

**Purpose**: Merge new game options with old galaxy configuration

**Copies from oldOpt to this**:
- `selectedGalaxySize`
- `selectedGalaxyShape`
- `selectedNebulaeOption`
- `selectedNumberOpponents`
- All system-specific params from UI lists
- First/Second ring system numbers

**This is called by**:
- `GalaxyCopy.copy()` when creating restart galaxy
- Updates the newOptions with old galaxy settings

## 9. Key Game Settings (MOO1GameOptions.java)

### Galaxy Configuration
- `selectedGalaxySize`: Small/Medium/Large/Huge
- `selectedGalaxyShape`: Text/Bitmap shape selection
- `selectedGameDifficulty`: Easiest to Hardest
- `selectedNumberOpponents`: 1-10 rivals
- `selectedStarDensityOption`: Star density multiplier

### Advanced Options
- `selectedGalaxyAge`: Normal/Young/Old (affects race stats)
- `selectedResearchRate`: Research speed multiplier
- `selectedTechTradeOption`: Tech trading rules
- `selectedRandomEventOption`: Random events on/off/types
- `selectedWarpSpeedOption`: Warp speed multiplier
- `selectedNebulaeOption`: Nebula frequency
- `selectedCouncilWinOption`: Council victory condition
- `selectedPlanetQualityOption`: Planet quality distribution
- `selectedTerraformingOption`: Terraforming availability
- `selectedColonizingOption`: Colonization restrictions
- `selectedFuelRangeOption`: Fleet fuel range
- `selectedRandomizeAIOption`: AI randomization
- `selectedAIHostilityOption`: AI hostility level
- `selectedAutoplayOption`: Autoplay mode

### Player/Race Settings
- `player`: NewPlayer object with race/leader/homeworld
- `opponentRaces[]`: Alien race selections
- `specificOpponentAIOption[]`: Per-AI preferences
- `specificOpponentCROption[]`: Per-opponent abilities

## 10. Flow Summary: Starting Fresh Game

```
User clicks Start in SetupGalaxyUI
    ↓
SetupGalaxyUI.startGame()
    - Save options to file
    - Call GameSession.instance().startGame(opts)
    ↓
GameSession.startGame(IGameOptions newGameOptions)
    - Copy all options: newGameOptions.copyAllOptions()
    - GalaxyFactory.current().newGalaxy() [NO PARAMETER]
    ↓
GalaxyFactory.newGalaxy()
    - Generate fresh GalaxyShape from options
    - Create new Galaxy object
    - SpeciesFactory generates player + aliens
    - Place all systems, nebulas, planets
    - Call init(galaxy) to finalize
    - Save options to GAME_OPTIONS_FILE
    ↓
Galaxy.startGame()
    - Process initial notifications
    - Show advisor
    ↓
Game begins
```

## 11. Flow Summary: Restarting Game

```
User clicks Start with CTRL (or RestartGame option)
    ↓
SetupGalaxyUI.restartGame()
    - Save options
    - Create GalaxyCopy(opts)
    - Call RotPUI.selectRestartGamePanel(oldGalaxy)
    ↓
Player selects empire to play
    ↓
GalaxyCopy.selectEmpire(index)
    - Swap player position if needed
    - Merge options based on restart settings
    ↓
GameSession.restartGame(newGameOptions, GalaxyCopy src)
    - Use src.options() or newOptions based on settings
    - GalaxyFactory.current().newGalaxy(src) [WITH PARAMETER]
    ↓
GalaxyFactory.newGalaxy(GalaxyCopy src)
    - Create Galaxy from GalaxyBaseData
    - Check selectedRestartAppliesSettings():
      - TRUE: regenerate with new options
      - FALSE: keep old galaxy structure
    - SpeciesFactory with src parameter
    - Regenerate species using old alien races
    - Place systems respecting old structure
    ↓
Game begins with old galaxy, selected empire
```

## 12. Method to Call for Starting Fresh Game

**Answer**: `GameSession.instance().startGame(options)`

Where `options` is an IGameOptions object with desired settings. The flow is:
1. Get or create IGameOptions with desired settings
2. Call `GameSession.instance().startGame(options)`
3. GameSession will internally call `copyAllOptions()` to duplicate settings
4. Generate fresh galaxy with those settings

If you need to preserve some old options while changing others:
1. Get previous options from GameSession
2. Modify specific settings
3. Call `GameSession.instance().startGame(modifiedOptions)`
