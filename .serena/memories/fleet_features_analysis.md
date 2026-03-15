# Fleet Design Tint Outlines and Route Drawing Analysis

## 1. FLEET DESIGN TINT OUTLINES (Commit e9dda09b)

### Overview
Fleet icons on the galaxy map now display with distinct shapes based on mission type and use the ship design's tint color for the icon outline.

### Architecture
**Key Classes:**
- `ShipFleet.java` — Fleet entity with rendering logic
- `ShipDesign.java` — Individual ship design with mission inference
- `Empire.java` — Caches mission-specific ship images with custom outline colors
- `ShipLibrary.java` — Generates mission-specific icon shapes

### How Fleet Colors Are Determined

**Dominant Mission Detection (ShipFleet)**
- Method: `dominantMission()` (line 788-802)
- Iterates through all ship designs in fleet
- Returns the mission type if all ships share same mission, else -1 (mixed fleet)
- Mission types:
  - SCOUT (dart shape)
  - COLONY (dome/ark shape)
  - FIGHTER (swept-wing shape)
  - BOMBER (heavy wide-body shape)
  - DESTROYER (angular warship shape)

**Mission Inference (ShipDesign)**
- Method: `inferredMission()` (line 228-255)
- Detects mission from ship loadout:
  - COLONY: has colony special module
  - SCOUT: has scanner + ≤1 weapon
  - BOMBER: ground-attack weapons > ship weapons
  - DESTROYER: ship weapons + hull size ≥ LARGE
  - FIGHTER: ship weapons + hull size < LARGE
  - Default: SCOUT

**Dominant Ship Color (ShipFleet)**
- Method: `dominantShipColor()` (line 804-818)
- Iterates through all ship designs in fleet
- Returns the `shipColor()` value if all ships share same color, else -1 (mixed colors)
- Field: `ShipDesign.shipColor` (line 79)
- Type: integer ID mapping to colors via `ImageColorizer.color(id)`

**Outline Color Mapping**
- File: `rotp/util/ImageColorizer.java` (line 67-83)
- Static method: `color(int id)`
- Maps IDs to Java Color objects:
  - WHITE (5), GRAY (6), BLACK (7), RED (8), GREEN (9), BLUE (10)
  - YELLOW (1), ORANGE (2), PURPLE (3), AQUA (4), LIGHT_BLUE (11), DARK_GREEN (12)
  - NO_COLOR (-1), TRANSPARENT (0)

### Fleet Icon Rendering (ShipFleet.draw)
- File: `rotp/model/galaxy/ShipFleet.java` (line 1095-1169)
- Method: `draw(GalaxyMapPanel map, Graphics2D g2)`

**Flow:**
1. Determine `imgSize` (1=small, 2=large, 3=huge) based on fleet hull points
2. Get dominant mission via `dominantMission()`
3. If mission > 0 (pure mission fleet):
   - Get dominant ship color via `dominantShipColor()`
   - Use `outlineColorId = (shipColor >= 0) ? shipColor : 0`
   - Call `empire().missionShipImage(mission, outlineColorId)`
   - Sizes: missionShipImage / missionShipImageLarge / missionShipImageHuge
4. If mission ≤ 0 (mixed fleet):
   - Fall back to default behavior: standard shipImage or scoutImage

**Caching in Empire**
- File: `rotp/model/empires/Empire.java` (line 465-501)
- Three caches: missionShipImageCache, missionShipImageLargeCache, missionShipImageHugeCache
- Cache key: `mission * 1000 + colorId * 100 + outlineColorId`
- Methods: `missionShipImage()`, `missionShipImageLarge()`, `missionShipImageHuge()`

### Mission Icon Rendering (ShipLibrary)
- File: `rotp/model/ships/ShipLibrary.java` (line 202-590)
- Pattern for each mission type (scout, colony, fighter, bomber, destroyer):
  - Methods: `<mission>ShipImage()`, `<mission>ShipImageLarge()`, `<mission>ShipImageHuge()`

**Example: Scout Icon Rendering**
```java
g.setColor(options().color(colorId));  // Fleet's empire color
g.fillPolygon(pX, pY, 6);              // Fill dart shape
g.setColor(Color.yellow);
g.fillRect(...);                       // Yellow scanner highlight
g.setColor(outlineColor);              // Design's tint color
g.drawPolygon(pX, pY, 6);              // Draw outline in tint color
```

**Icon Features:**
- Fill: Empire's faction color
- Details: Yellow (scanner), Orange (engines), Cyan (forward lights), Red (weapons)
- Outline: Ship design's tint color (passed as `outlineColor` parameter)

---

## 2. FLEET ROUTE DRAWING

### Overview
Fleet routes (travel lines) from current position to destination are drawn on the galaxy map using the FlightPathSprite system.

### Key Classes
- `FlightPathSprite.java` — Renders flight paths/routes
- `GalaxyMapPanel.java` — Manages map drawing including route sprites

### Route Line Drawing Architecture

**FlightPathSprite Drawing**
- File: `rotp/ui/sprites/FlightPathSprite.java`
- Method: `draw(GalaxyMapPanel map, Graphics2D g2)` (line 198-200)
  - Delegates to `drawShipPath()` or `drawPlanetPath()` based on type
- Core drawing: `draw(Graphics2D g2, int animationCount, float scale, boolean hovering, int x1, int y1, int x2, int y2, Color c0)` (line 229-271)

**Route Line Colors**
- Method: `lineColor(GalaxyMapPanel map, StarSystem sys)` (line 105-131)
- Determined by conditions:

For Player Fleets:
- **Yellow**: Invalid destination OR transport to different empire
- **Magenta**: Working path + passes through nebula
- **Green**: Valid destination (default)

For AI/Monster Fleets:
- **Red**: Aggressive to player
- **Yellow**: Neutral/allied

For Colony Relocation:
- **Purple** (rallyColor = new Color(96, 0, 128))

**Route Line Rendering Details**
- Line animation: 6-frame animation cycle (animationSpeed = 5)
- Line styles determined by state:
  - Working path: thicker animated dashed line
  - Hovering: medium animated dashed line
  - Default: standard animated dashed line
- Dash pattern: `new float[] {f12, f6}` with phase animation
- Strokes: `BasicStroke.CAP_ROUND`, `BasicStroke.JOIN_ROUND`

**Stroke Creation (initStrokes)**
- File: `FlightPathSprite.java` (line 293-315)
- Two stroke arrays: `lines[6][6]` for normal paths, `rallyStroke[2][9]` for relocation

```java
// Normal path strokes: 6 scale levels × 6 animation frames
for (int i=0;i<6;i++)
    for (int j=0;j<6;j++)
        lines[i][j] = new BasicStroke(width, CAP_ROUND, JOIN_ROUND, 10, {12,6}, phase)

// Rally strokes: 2 levels × 9 frames  
rallyStroke[0][i] = new BasicStroke(BasePanel.s2, ...) // s2 width
rallyStroke[1][i] = new BasicStroke(BasePanel.s3, ...) // s3 width
```

**Line Drawing**
- File: `FlightPathSprite.java` (line 263-268)
```java
g2.setColor(c0);                    // Set route color
g2.drawLine(x2, y2, x1, y1);        // Draw from source to destination
g2.drawOval(x2-s9, y2-s9, s18, s18); // Draw circle at destination
```

### Route Rendering in GalaxyMapPanel
- File: `rotp/ui/main/GalaxyMapPanel.java`
- Method: `drawWorkingFlightPaths()` (line 1109-1117)
  - Draws all active "working paths" (paths being edited/previewed)
- Standard paths drawn via FleetPanel or ship selection

**Integration:**
- `FlightPathSprite.workingPaths()` — Static list of active route sprites
- `Ship.pathSprite()` — Each ship has an associated FlightPathSprite
- Routes selected via `isSelectableAt()` (line 194-196) — clickable polygon around line

### Current Route Styling Summary

| Attribute | Value |
|-----------|-------|
| **Line Colors** | Green (valid), Yellow (invalid/transport), Magenta (nebula), Red (hostile), Purple (relocation) |
| **Line Style** | Dashed animated (12px dash, 6px gap) |
| **Animation** | 6-frame cycle, 5 ticks per frame |
| **Line Cap** | ROUND |
| **Line Join** | ROUND |
| **Destination Marker** | Circle (diameter = 18px) |
| **Default Width** | Scales based on zoom level (1-6 pixels) |

---

## Access Patterns

### Getting Design Tint Color from Fleet
```
Fleet.dominantShipColor() → ShipDesign.shipColor() 
→ ImageColorizer.color(colorId) → java.awt.Color
```

### Getting Design Mission from Fleet
```
Fleet.dominantMission() → ShipDesign.inferredMission() → int (SCOUT/COLONY/etc)
```

### Rendering Fleet with Tint Outline
```
ShipFleet.draw() → Empire.missionShipImage(mission, outlineColorId)
→ ShipLibrary.<mission>ShipImage() → BufferedImage with colored outline
```

### Rendering Fleet Routes
```
FlightPathSprite.draw() → lineColor() → g2.drawLine() with animated dashed stroke
```
